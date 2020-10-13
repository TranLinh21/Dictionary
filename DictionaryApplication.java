import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class DictionaryApplication {
    private static DictionaryManagement dM = new DictionaryManagement();

    public DictionaryApplication() throws Exception {
        prepareGUI();
    }

    private void prepareGUI() throws Exception{
        dM.insertFromFile();
        final int frameHeight = 600;
        final int frameWidth = 800;
        final JFrame mainFrame = new JFrame("Dictionary");

        JLabel titleLabel = new JLabel("Dictionary",JLabel.CENTER);             // nhãn trên cùng
        JLabel searchTitle = new JLabel("Search here: ", JLabel.CENTER);        //nhãn cạnh ô search

        JTextField searchText = new JTextField();                                    //ô nhập từ
        JTextArea  resultText = new JTextArea();                                     //bảng in ra kết quả
        JButton searchButton = new JButton("Search");                           //nút search

        DefaultListModel<String> list = new DefaultListModel<>();
        JList<String> selectionList = new JList<>(list);                             //danh sách từ search

        JMenuBar menuBar = new JMenuBar();                                           //tool bar
        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");

        ImageIcon AddIcon = new ImageIcon("AddIcon.png");
        ImageIcon FixIcon = new ImageIcon("FixIcon.png");
        ImageIcon RemoveIcon = new ImageIcon("RemoveIcon.png");
        ImageIcon ImportFromFile = new ImageIcon("ImportFromFile.png");
        ImageIcon ExportToFile = new ImageIcon("ExportToFile.png");
        ImageIcon ExitIcon = new ImageIcon("ExitIcon.png");


        JMenuItem AddMenu = new JMenuItem("Add",AddIcon);
        JMenuItem FixMenu = new JMenuItem("Fix",FixIcon);
        JMenuItem RemoveMenu = new JMenuItem("Remove",RemoveIcon);
        JMenuItem importFile = new JMenuItem("ImportFromFile",ImportFromFile);
        JMenuItem exportFile = new JMenuItem("ExportToFile",ExportToFile);
        JMenuItem exitMenu = new JMenuItem("Exit",ExitIcon);
        /**
         * main.
         */
        mainFrame.setSize(frameWidth,frameHeight);

        titleLabel.setFont(new Font("Verdana",Font.PLAIN,36));
        titleLabel.setBounds(250,10,300,100);

        searchTitle.setBounds(0,150,80,30);

        resultText.setBounds(400,150,370,380);
        resultText.setEditable(false);

        searchText.setFont(new Font("Verdana",Font.PLAIN,18));
        searchText.setBounds(80,150,225,30);
        searchText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                String key = searchText.getText();
                list.clear();
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    resultText.setText(key + "\n");
                    resultText.append(dM.dictionaryLookup(key));
                } else {
                    String[] res = dM.dictionarySearcher(key);
                    for (String re : res) {
                        list.addElement(re);
                    }
                }
            }

        });

        selectionList.setForeground(Color.BLACK);
        JScrollPane listSP = new JScrollPane(selectionList);
        listSP.setBounds(0,190,380,340);
        MouseListener mouseListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() == 1) {
                    int index = selectionList.getSelectedIndex();
                    if (index>=0) {
                        Object o = list.getElementAt(index);
                        resultText.setText(o.toString() + "\n");
                        resultText.append(dM.dictionaryLookup(o.toString()));
                    }
                }
            }
        };
        selectionList.addMouseListener(mouseListener);

        searchButton.setBounds(305,150,75,30);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resultText.setText(dM.dictionaryLookup(searchText.getText()));
            }
        });
        String[] res = dM.dictionarySearcher("");
        for (String re : res) {
            list.addElement(re);
        }
        /**
         * tool bar
         */
        AddMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,ActionEvent.CTRL_MASK));
        FixMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK));
        RemoveMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
        importFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.CTRL_MASK));
        exportFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
        exitMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));

        AddMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField English = new JTextField(25);
                JTextArea Vietnamese = new JTextArea(10,25);

                Object[] msg = {"English:", English, "Vietnamese:",Vietnamese};

                int result = JOptionPane.showConfirmDialog(mainFrame, msg, "Add Word",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.YES_OPTION) {
                    //System.out.println("Add is used");
                    if (!dM.dictionaryLookup(English.getText()).equals("Word not found !!!")) {
                        JOptionPane.showMessageDialog(mainFrame, "The dictionaries already have this word!"
                                , "Message", JOptionPane.WARNING_MESSAGE);
                    } else {
                        if (Vietnamese.getText().length() == 0) {
                            JOptionPane.showMessageDialog(mainFrame, "Please write Vietnamese explain word!"
                                    , "Message", JOptionPane.WARNING_MESSAGE);
                        } else {
                            System.out.println(English.getText() + " " + Vietnamese.getText());
                            String fileName = "dictionaries.txt";
                            dM.dic.addWord(English.getText(), Vietnamese.getText() + "\n");
                            File file = new File("dictionaries.txt");
                            file.delete();
                            try {
                                dM.dictionaryExportToFile("dictionaries.txt");
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                            JOptionPane.showMessageDialog(mainFrame, "Add is completed!"
                                    , "message", JOptionPane.INFORMATION_MESSAGE);
                                System.out.println("Add is used");
                            }

                    };
                }
                else {
                    System.out.println("Add is not used");
                }
            }
        });
        FixMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField English = new JTextField(25);

                Object[] msg = {"English Word to Fix:", English};
                int result = JOptionPane.showConfirmDialog(mainFrame, msg, "Fix Word",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.YES_OPTION) {
                    if (dM.dictionaryLookup(English.getText()).equals("Word not found !!!")) {
                        JOptionPane.showMessageDialog(mainFrame, "Can not look up this word!"
                                , "Message", JOptionPane.WARNING_MESSAGE);
                    } else {
                    String word = English.getText();
                    System.out.println("Word is Fix: " + word);
                    JTextArea Vietnamese = new JTextArea(10,25);

                    Vietnamese.setText(dM.dictionaryLookup(word));
                    Object[] msg1 = {"English Word :" + word,
                            "Vietnamese :", Vietnamese};
                    int YN = JOptionPane.showConfirmDialog(mainFrame, msg1, "Fix Word",
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                    if (YN == JOptionPane.YES_OPTION) {
                            dM.modifyWord(word, Vietnamese.getText()+"\n");
                            File file = new File("dictionaries.txt");
                            file.delete();
                            try {
                                dM.dictionaryExportToFile("dictionaries.txt");
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            };
                            JOptionPane.showMessageDialog(mainFrame, "This word has been modified!"
                            , "message", JOptionPane.INFORMATION_MESSAGE);

                        }
                    }
                }
            }
        });
        RemoveMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField English = new JTextField(25);

                Object[] msg ={"English Word to Remove:", English};
                int result = JOptionPane.showConfirmDialog(mainFrame, msg, "Remove Word",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.YES_OPTION) {
                    if (dM.dictionaryLookup(English.getText()).equals("Word not found !!!")) {
                        JOptionPane.showMessageDialog(mainFrame, "Can not look up this word!"
                                , "Message", JOptionPane.WARNING_MESSAGE);
                    } else {
                        String word = English.getText();
                        System.out.println("Word is Removed: " + word);
                        Object[] msg1 = {"English Word :" + word,
                                "Vietnamese :" + dM.dictionaryLookup(word), "Do you want to remove this word?"};
                        int YN = JOptionPane.showConfirmDialog(mainFrame, msg1, "Question to Remove",
                                JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);

                        if (YN == JOptionPane.YES_OPTION) {
                            dM.removeWord(word);
                            File file = new File("dictionaries.txt");
                            file.delete();
                            try {
                                dM.dictionaryExportToFile("dictionaries.txt");
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            };
                            JOptionPane.showMessageDialog(mainFrame, "This word has been removed from dictionaries.txt"
                            , "message", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }
            }
        });

        importFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    dM.insertFromFile();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
        exportFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    dM.dictionaryExportToFile("newDictionary.txt");
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
        exitMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        editMenu.add(AddMenu);
        editMenu.add(FixMenu);
        editMenu.add(RemoveMenu);

        fileMenu.add(importFile);
        fileMenu.add(exportFile);
        fileMenu.add(exitMenu);

        fileMenu.addSeparator();
        editMenu.addSeparator();

        menuBar.add(fileMenu);
        menuBar.add(editMenu);

        mainFrame.setJMenuBar(menuBar);

        mainFrame.add(searchTitle);
        mainFrame.add(searchText);
        mainFrame.add(titleLabel);
        mainFrame.add(listSP);
        mainFrame.add(searchButton);
        mainFrame.add(resultText);
        mainFrame.setLayout(null);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        new DictionaryApplication();
    }
}