// File: ShipsInPorts.java
// Date: 24 February 2019
// Author: Tom Batchlear
// Purpose:  ShipsInPorts creates a GUI that prompts the user to select a file.  
// Once the file is selected, the user has the option to display everything or 
// search the file contents.  The SeaPortPogram calls the World class to handle 
//all functions
package ShipsInPorts;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;

public class ShipsInPorts extends JFrame {

    World world = new World();
    JTextArea jta = new JTextArea();
    JPanel jpResources = new JPanel();
    JPanel cardPanel = new JPanel();
    JSplitPane jSplit;
    JScrollPane jsp;
    JScrollPane jspThreads;
    JScrollPane jspResources;
    JPanel jpData = new JPanel();
    JPanel jpTree = new JPanel();
    JPanel allData = new JPanel();
    JPanel threadData = new JPanel();
    static ShipsInPorts sp;

    public ShipsInPorts() {
        setTitle("SeaPort Program");
        setSize(1400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        

        threadData.setLayout(new GridLayout(0, 5, 2, 5));
        jpResources.setLayout(new GridLayout(0, 6, 2, 5));

        jspResources = new JScrollPane(jpResources);
        jspResources.setBackground(Color.yellow);
        
        jspThreads = new JScrollPane(threadData);

        cardPanel.setLayout(new CardLayout());

        jta.setFont(new java.awt.Font("Monospaced", 0, 12));

        jsp = new JScrollPane(jta);

        JPanel jpSelection = new JPanel();
        JLabel selectAction = new JLabel("Select an action from the menu");
        jpSelection.add(selectAction);
        cardPanel.add(jpSelection);
        setJMenuBar(seaportProgramMenu());

        jSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jsp, dataTree());
        jSplit.setResizeWeight(0.5);
        jSplit.setDividerLocation(350);
        jSplit.setOneTouchExpandable(true);
        jSplit.setContinuousLayout(true);
        jSplit.setPreferredSize(new Dimension(600, 100));
        
        jpData.setLayout(new GridLayout(2, 1));
        jpData.add(jSplit);
        jpData.add(jspResources);
        

        allData.setLayout(new GridLayout(1, 2));
        allData.add(jpData);
        allData.add(jspThreads);
        
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(allData, BorderLayout.CENTER);
        getContentPane().add(cardPanel, BorderLayout.PAGE_START);
        setVisible(true);
    }// end ShipsInPorts Constructor

    public static void main(String[] args) {
        sp = new ShipsInPorts();
    }// end main
//
    public void selectFile() {
        JFileChooser jfc = new JFileChooser();
        File workingDirectory = new File(System.getProperty("user.dir"));
        jfc.setCurrentDirectory(workingDirectory);
        jfc.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
        jfc.addChoosableFileFilter(filter);
        int returnValue = jfc.showOpenDialog(null);
        try {
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = jfc.getSelectedFile();
                world.process(selectedFile, threadData, jpResources);
                allData.revalidate();
            }
        } catch (NullPointerException e) {

        }
    }//end selectFile()

    public void displayForm() {
        if (world.hmPorts.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please Select a File First",
                    "Error", JOptionPane.ERROR_MESSAGE);;
        } else {
            jta.setText(world.toString());
            jta.setCaretPosition(0);
            jSplit.setRightComponent(dataTree());
            jSplit.revalidate();
        }
    }//end displayForm()

    public void searchTree(String type, String target) {
        if (world.hmPorts.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please Select a File First",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            if (null != type) {
                switch (type) {
                    case "Name":
                        jta.setText(world.searchByName(target));
                        jta.setCaretPosition(0);
                        break;
                    case "Index":
                        jta.setText(world.searchByIndex(target));
                        jta.setCaretPosition(0);
                        break;
                    case "Skill":
                        jta.setText(world.searchBySkill(target));
                        jta.setCaretPosition(0);
                        break;
                    case "Type":
                        jta.setText(world.searchByType(target));
                        jta.setCaretPosition(0);
                        break;

                    default:
                        break;
                }
            }
        }
    }//end searchTree

    public void openHelpFile() {
        System.out.println("Help Button Pressed");
        if (Desktop.isDesktopSupported()) {
            try {
                File workingDirectory = new File(System.getProperty("user.dir"));
                File file = new File(workingDirectory + "\\Help.pdf");

                Desktop.getDesktop().open(file);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "File Not Found",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else {
            System.out.println("Desktop is not supported");
        }
    }

    //Methods to create GUI Components
    //Create GUI Menu Bar
    public final JMenuBar seaportProgramMenu() {
        JMenuBar menuBar;

        menuBar = new JMenuBar();

        JMenu menuFile = new JMenu("File");
        menuFile.setMnemonic(KeyEvent.VK_F);
        menuFile.getAccessibleContext().setAccessibleDescription(
                "Read and Display File Contents");
        menuBar.add(menuFile);

        JMenuItem menuItemOpen = new JMenuItem("Open File",
                KeyEvent.VK_O);
        menuItemOpen.addActionListener(e -> selectFile());
        menuFile.add(menuItemOpen);

        JMenuItem menuItemDisplay = new JMenuItem("Display Tree");
        menuItemDisplay.setMnemonic(KeyEvent.VK_D);
        menuItemDisplay.addActionListener(e -> displayForm());
        menuFile.add(menuItemDisplay);

        menuFile.addSeparator();
        ButtonGroup group = new ButtonGroup();

        JRadioButtonMenuItem rbSearchButton = new JRadioButtonMenuItem("Search Ports");
        JRadioButtonMenuItem rbSortButton = new JRadioButtonMenuItem("Sort Ships in Queue");
        rbSearchButton.setMnemonic(KeyEvent.VK_S);
        rbSearchButton.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent evt) {
                if (rbSearchButton.isSelected()) {
                    cardPanel.removeAll();
                    cardPanel.add(newSearchPanel());
                    cardPanel.repaint();
                    cardPanel.revalidate();
                }
            }
        });
        group.add(rbSearchButton);
        menuFile.add(rbSearchButton);

        rbSortButton.setMnemonic(KeyEvent.VK_Q);
        rbSortButton.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent evt) {
                if (rbSortButton.isSelected()) {
                    cardPanel.removeAll();
                    cardPanel.add(newSortPanel());
                    cardPanel.repaint();
                    cardPanel.revalidate();
                }
            }
        });
        group.add(rbSortButton);
        menuFile.add(rbSortButton);

        menuFile.addSeparator();

        JMenuItem menuItemExit = new JMenuItem("Exit",
                KeyEvent.VK_E);
        menuItemExit.addActionListener(e -> System.exit(0));
        menuFile.add(menuItemExit);

        //Build second menu in the menu bar.
        JMenu menuHelp = new JMenu("Help");
        menuHelp.setMnemonic(KeyEvent.VK_L);
        menuBar.add(menuHelp);

        JMenuItem menuItemHelp = new JMenuItem("Open Help",
                KeyEvent.VK_H);
        menuItemHelp.addActionListener(e -> openHelpFile());
        menuHelp.add(menuItemHelp);

        return menuBar;
    }

    //Create Search Panel to be added to the card layout
    public JPanel newSearchPanel() {
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new GridBagLayout());

        JButton jbs = new JButton("Search");
        JLabel jls = new JLabel("Search Target"),
                jlt = new JLabel("Search Topic");
        JTextField jtf = new JTextField(10);
        JComboBox<String> jcb = new JComboBox<>();
        GridBagConstraints gbc;

        jcb.addItem("Name");
        jcb.addItem("Index");
        jcb.addItem("Skill");
        jcb.addItem("Type");

        jls.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 0, 0, 0);
        searchPanel.add(jls, gbc);

        jtf.setMaximumSize(new Dimension(50, 50));
        jtf.setMinimumSize(new Dimension(10, 20));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.ipadx = 150;
        gbc.insets = new Insets(10, 20, 0, 0);
        searchPanel.add(jtf, gbc);

        jbs.setPreferredSize(new Dimension(150, 23));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 15, 15, 15);
        searchPanel.add(jbs, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 20, 0, 0);
        searchPanel.add(jcb, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(15, 0, 0, 0);
        searchPanel.add(jlt, gbc);

        jbs.addActionListener(e -> searchTree((String) (jcb.getSelectedItem()), jtf.getText()));

        return searchPanel;
    }

    //Create Sort Panel to be added to the card layout
    public JPanel newSortPanel() {

        JPanel sortPanel = new JPanel();

        sortPanel.setLayout(new GridBagLayout());

        JLabel lblSortText = new JLabel();
        JRadioButton rbWeight = new JRadioButton("Weight"),
                rbLength = new JRadioButton("Length"),
                rbWidth = new JRadioButton("Width"),
                rbDraft = new JRadioButton("Draft");
        GridBagConstraints gbc;
        JButton btnSort = new JButton("Sort");
        ButtonGroup group = new ButtonGroup();

        lblSortText.setText("Select a feature to sort ships in queue");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.insets = new Insets(15, 15, 15, 15);
        sortPanel.add(lblSortText, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        group.add(rbWeight);
        rbWeight.setSelected(true);
        sortPanel.add(rbWeight, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        group.add(rbLength);
        sortPanel.add(rbLength, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        group.add(rbWidth);
        sortPanel.add(rbWidth, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 1;
        group.add(rbDraft);
        sortPanel.add(rbDraft, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 15, 15, 15);
        sortPanel.add(btnSort, gbc);

        btnSort.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                if (rbWidth.isSelected()) {
                    jta.setText(world.sortByWidth());
                    jta.setCaretPosition(0);
                } else if (rbLength.isSelected()) {
                    jta.setText(world.sortByLength());
                    jta.setCaretPosition(0);
                } else if (rbWeight.isSelected()) {
                    jta.setText(world.sortByWeight());
                    jta.setCaretPosition(0);
                } else if (rbDraft.isSelected()) {
                    jta.setText(world.sortByDraft());
                    jta.setCaretPosition(0);
                } else {
                    JOptionPane.showMessageDialog(null, "Please Select a File First",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        });

        return sortPanel;
    }

    //Create JTree 
    public JScrollPane dataTree() {

        DefaultMutableTreeNode mainNode = new DefaultMutableTreeNode("World");

        JTree tree = new JTree(mainNode);

        buildTree(mainNode);

        JScrollPane treePane = new JScrollPane(tree);
        treePane.setPreferredSize(new Dimension(300, 400));

        return treePane;
    }

    //Build JTree by looping through data elements and adding
    public void buildTree(DefaultMutableTreeNode top) {
        DefaultMutableTreeNode portNode;

        for (Seaport seaPort : world.hmPorts.values()) {
            portNode = new DefaultMutableTreeNode(seaPort.name);

            DefaultMutableTreeNode node = new DefaultMutableTreeNode("Docks");

            for (Dock dock : seaPort.docks) {
                DefaultMutableTreeNode dockNode = new DefaultMutableTreeNode(dock.name);
                if (dock.getShip() != null) {
                    DefaultMutableTreeNode shipNode = new DefaultMutableTreeNode(dock.getShip().name);
                    for (Job job : dock.getShip().jobs) {
                        shipNode.add(new DefaultMutableTreeNode(job.name));
                    }
                    dockNode.add(shipNode);
                }
                node.add(dockNode);
                portNode.add(node);
            }

            node = new DefaultMutableTreeNode("Ships in Que");

            for (Ship ship : seaPort.que) {
                DefaultMutableTreeNode shipNode = new DefaultMutableTreeNode(ship.name);
                for (Job job : ship.jobs) {
                    shipNode.add(new DefaultMutableTreeNode(job.name));
                }
                node.add(shipNode);
            }
            portNode.add(node);

            node = new DefaultMutableTreeNode("All Ships");

            for (Ship ship : seaPort.ships) {
                DefaultMutableTreeNode shipNode = new DefaultMutableTreeNode(ship.name);
                for (Job job : ship.jobs) {
                    shipNode.add(new DefaultMutableTreeNode(job.name));
                }
                node.add(shipNode);
            }
            portNode.add(node);

            node = new DefaultMutableTreeNode("People");

            for (Person person : seaPort.persons) {
                DefaultMutableTreeNode personNode = new DefaultMutableTreeNode(person.getName());
                node.add(personNode);
            }
            portNode.add(node);
            top.add(portNode);
        }
    }


}//end class ShipsInPorts
