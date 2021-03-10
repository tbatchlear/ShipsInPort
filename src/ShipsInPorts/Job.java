// File: Job.java
// Date: 24 February 2019
// Author: 
// Purpose: Contains job information at each port.  Extends the Thing class
package ShipsInPorts;

import java.awt.Color;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

public class Job extends Thing implements Runnable {

    private final long jobDuration;
    private ArrayList<String> requirements = new ArrayList<>();//should be some of the skills of the persons
    JPanel jobPanel = new JPanel();
    Person worker = null;
    List<Person> workers = new ArrayList<>();
    String jobName = "";
    Ship parentShip;
    JProgressBar pm = new JProgressBar();
    boolean goFlag = true, noKillFlag = true;
    JButton jbGo = new JButton("Stop");
    JButton jbKill = new JButton("Cancel");
    Status status = Status.SUSPENDED;
    JLabel workerName = new JLabel("No Worker", SwingConstants.CENTER);
    JLabel lblJobName = new JLabel("", SwingConstants.CENTER);
    Thread ts;

    enum Status {
        RUNNING, SUSPENDED, WAITING, DONE
    };

    //Job Constructor
    public Job(String name, int index, int parent, double jobDuration, ArrayList<String> requirements, JPanel jp, Ship ship) {

        super(name, index, parent);
        this.parentShip = ship;
        this.jobPanel = jp;
        this.jobDuration = (long) jobDuration;
        this.requirements = requirements;
        this.jobName = name;

        lblJobName.setText(name);

        if (!this.requirements.isEmpty()) {
            this.workers = parentShip.requestWorkers(requirements);
        }

        startJobs();
    }

    public void startJobs() {
        pm = new JProgressBar();
        pm.setStringPainted(true);
        jobPanel.add(pm);
        jobPanel.add(workerName);
        jobPanel.add(new JLabel(jobName, SwingConstants.CENTER));

        jobPanel.add(jbGo);
        jobPanel.add(jbKill);

        jbGo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                toggleGoFlag();
            }
        });

        jbKill.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setKillFlag();
            }
        });

        ts = new Thread(this, name);
        ts.start();

    }

    public void setParentShip(Ship ship) {
        this.parentShip = ship;
    }

    public ArrayList<String> getRequirements() {
        return this.requirements;
    }

    public void toggleGoFlag() {
        goFlag = !goFlag;
    } // end method toggleRunFlag

    public void setKillFlag() {
        noKillFlag = false;
        jbKill.setBackground(Color.red);
    } // end setKillFlag

    void showStatus(Status st) {
        status = st;
        switch (status) {
            case RUNNING:
                jbGo.setBackground(Color.green);
                jbGo.setText("Running");
                break;
            case SUSPENDED:
                jbGo.setBackground(Color.yellow);
                jbGo.setText("Suspended");
                break;
            case WAITING:
                jbGo.setBackground(Color.orange);
                jbGo.setText("Waiting turn");
                break;
            case DONE:
                jbGo.setBackground(Color.red);
                jbGo.setText("Done");
                break;
        } // end switch on status
    } // end showStatus

    public void setWorkers(List<Person> workers) {
        this.workers = workers;
    }

    @Override
    public void run() {
        long time = System.currentTimeMillis();
        long startTime = time;
        long stopTime = time + 1000 * jobDuration;
        double duration = stopTime - time;

        if (requirements.isEmpty()) {
            workerName.setText("No Requirements");
            showStatus(Status.DONE);
            synchronized (this) {
                parentShip.getParentPort().cycleShips(parentShip);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Job.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            parentShip.getParentPort().updatePanel();
            return;
        }

        boolean skillsExists = true;
        for (String req : requirements) {
            if (!parentShip.skillExists(req)) {
                skillsExists = false;
                break;
            }
        }

        if (!skillsExists) {
            showStatus(Status.DONE);
            workerName.setText("No Skills Exist");
            for (int i = 0; i < workers.size(); i++) {
                this.workers.get(i).busyFlag = false;
            }
            return;
        }

        while (this.workers.size() < this.requirements.size()) {
            showStatus(Status.WAITING);
            workerName.setText("Waiting for available Worker");
            for (int i = 0; i < workers.size(); i++) {
                this.workers.get(i).busyFlag = false;
            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Job.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.workers = parentShip.requestWorkers(requirements);
        }

        for (int i = 0; i < workers.size(); i++) {
            workerName.setText(workers.get(i).name);// = new JLabel(, SwingConstants.CENTER);
            while (time < stopTime && noKillFlag) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
                if (goFlag) {
                    showStatus(Status.RUNNING);
                    time += 100;
                    pm.setValue((int) (((time - startTime) / duration) * 100));
                } else {
                    showStatus(Status.SUSPENDED);
                } // end if stepping
            } // end runninig
            this.workers.get(i).busyFlag = false;

            pm.setValue(100);
            showStatus(Status.DONE);

            synchronized (this) {
                
                try {
                    parentShip.getParentPort().cycleShips(parentShip);
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Job.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            parentShip.getParentPort().updatePanel();

        }

    }

    @Override
    public String toString() {
        String st = "Job: " + super.toString() + " " + jobDuration;
        for (String req : requirements) {
            st += " " + req;
        }
        return st;
    }

}
