package index;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.border.*;
import java.io.File;
import java.util.HashMap;
import java.io.*;
import java.util.*;
import java.util.List;


public class GUIFileSearch extends JFrame {
    public static File file;
    public static boolean fileChoosen;
    public static boolean processed;
    public static FileIndex fi;
    public static HashMap<String, List<Integer>> searchResult;

    public GUIFileSearch() {
        fi = new FileIndex();
        file = new File("src/");
        fileChoosen = false;
        processed = false;
        searchResult = new HashMap<>();

        // The only data the OS needs to give you a window is the text to
        // display at the top.
        JFrame frame = new JFrame("File Search");

        JPanel leftPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension(300, 800));
        JPanel rightPanel = new JPanel();
        rightPanel.setPreferredSize(new Dimension(500, 800));
        leftPanel.setBorder(BorderFactory.createEmptyBorder());
        rightPanel.setBorder(BorderFactory.createEmptyBorder());

        JLabel fileLabel = new JLabel("no file selected");
        fileLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        fileLabel.setBorder(BorderFactory.createEmptyBorder());

        JButton openButton = new JButton("open");
        openButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel parseLabel = new JLabel("no file parsed");
        parseLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        parseLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        parseLabel.setVisible(fileChoosen);

        JButton parseButton = new JButton("parse file");
        parseButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        parseButton.setVisible(fileChoosen);

        JLabel wordsLabel = new JLabel(
                "<html>please type in words seperated by spaces to search:</html>");
        wordsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        wordsLabel.setVisible(processed);
        JTextArea wordsInput = new JTextArea();
        wordsInput.setAlignmentX(Component.CENTER_ALIGNMENT);
        wordsInput.setVisible(processed);

        JButton searchButton = new JButton("search words");
        searchButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        searchButton.setVisible(processed);

        openButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(file);
                fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                int result = fileChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    file = fileChooser.getSelectedFile();
                    fileLabel.setText(file.getName());
                    fileChoosen = true;
                    parseLabel.setVisible(fileChoosen);
                    parseButton.setVisible(fileChoosen);
                }
            }
        });

        parseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                parseLabel.setText("file parsing......");
                try {
                    parseLabel.setText(file.getPath());
                    fi = FileIndex.createIndexFrom(file.getPath());
                } catch (IOException e) {
                    System.out.println("Fail to create index");
                    return;
                }

                parseLabel.setText("file parsed successfully");
                processed = true;
                wordsLabel.setVisible(processed);
                wordsInput.setVisible(processed);
                searchButton.setVisible(processed);
            }
        });

        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        // right panel (search result panel)
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                String[] words = wordsInput.getText().split("\\s+");
                // lower case
                for (int i = 0; i < words.length; i++) {
                    words[i] = words[i].toLowerCase();
                }
                searchResult = searchWords(words);
                rightPanel.removeAll();
                rightPanel.repaint();
                if (searchResult.size() == 0) {
                    JOptionPane.showMessageDialog(frame,
                            "No search result");
                }
                for (String filepath : searchResult.keySet()) {
                    List<Integer> l = searchResult.get(filepath);
                    rightPanel.add(new AccordionTitle(filepath, l.size(), l).get());
                }
                rightPanel.revalidate();
                validate();
            }
        });

        JScrollPane scrollableTextArea = new JScrollPane(rightPanel);
        scrollableTextArea.setBorder(BorderFactory.createEmptyBorder());
        scrollableTextArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollableTextArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        frame.add(leftPanel, BorderLayout.WEST);
        frame.add(scrollableTextArea, BorderLayout.EAST);

        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.add(openButton);
        leftPanel.add(fileLabel);
        leftPanel.add(parseButton);
        leftPanel.add(parseLabel);
        leftPanel.add(wordsLabel);
        leftPanel.add(wordsInput);
        leftPanel.add(searchButton);

        // In general, remember to do all the following!
        frame.pack();

        // This tells the Java application to terminate when the user closes the
        // window; this is typically what you want, unless you create multiple
        // windows.
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Give the window some initial size (the user is always free to resize
        // the window manually).
        frame.setSize(800, 800);

        // The window doesn't appear until you tell it to.
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new GUIFileSearch();
    }

    /*
    Search the given list of words.
    Return a map from file names to indices
     */
    private HashMap<String, List<Integer>> searchWords(String[] words) {
        HashMap<String, List<Integer>> result = new HashMap<>();

        // search the first word of the input list
        Set<String> files = fi.listAll(words[0]);
        for (String filepath : files) {
            List<Integer> indices = fi.getLocations(filepath, words[0]);
            result.put(filepath, indices);
        }

        // search the rest of the words if exist
        for (int i = 1; i < words.length; i++) {
            if (result.size() == 0) {
                break;
            }

            HashMap<String, List<Integer>> temp = new HashMap<>();
            files = fi.listAll(words[i]);
            for (String filepath : files) {
                temp.put(filepath, fi.getLocations(filepath, words[i]));
            }

            // add the search result from the previous word
            for (String filepath : temp.keySet()) {
                temp.get(filepath).addAll(result.get(filepath));
            }

            // replace the previous search result map
            result = temp;
        }

        // sort indices
        for (String filepath : result.keySet()) {
            Collections.sort(result.get(filepath));
        }
        return result;
    }

    private class AccordionTitle extends JComponent {
        public boolean collapse;
        public JPanel panel;
        public AccordionTitle(String fn, int rank, List<Integer> list) {
            collapse = true;
            panel = new JPanel();
            File f = new File(fn);
            JButton title = new JButton(
                    f.getName() + " (" + searchResult.get(fn).size() + ")");
            title.setPreferredSize(new Dimension(500, 40));
            IndexPreview content = new IndexPreview(fn, list);
            JPanel contentPanel = content.get();
            contentPanel.setVisible(!collapse);

            title.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    collapse = !collapse;
                    contentPanel.setVisible(!collapse);
                }
            });

            panel.add(title, BorderLayout.NORTH);
            panel.add(contentPanel, BorderLayout.SOUTH);
        }

        public JPanel get() {
            return panel;
        }
    }

    private  class IndexPreview extends JComponent {
        public int index;
        public List<Integer> locations;
        public File f;
        public RandomAccessFile raf;
        public JPanel wrapper;

        public IndexPreview(String fn, List<Integer> list) {
            index = 0;
            locations = list;
            f = new File(fn);
            try {
                raf = new RandomAccessFile(f, "r");
            } catch (FileNotFoundException e) {
                System.out.println("File not found");
                return;
            }


            wrapper = new JPanel();
            wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
            JLabel indexLable = new JLabel("Occurrence: " + index);
            indexLable.setAlignmentX(Component.CENTER_ALIGNMENT);
            JTextArea content = new JTextArea(getChars(index));
            content.setPreferredSize(new Dimension(450, 150));
            content.setLineWrap(true);
            content.setWrapStyleWord(true);
            content.setOpaque(false);
            content.setEditable(false);
            content.setAlignmentX(Component.CENTER_ALIGNMENT);


            JPanel btns = new JPanel();
            JButton prevButton = new JButton("prev");
            JButton nextButton = new JButton("next");
            btns.add(prevButton, BorderLayout.WEST);
            btns.add(nextButton, BorderLayout.EAST);

            wrapper.add(indexLable);
            wrapper.add(content);
            wrapper.add(btns);

            prevButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    if (index > 0) {
                        index--;
                    }
                    indexLable.setText("Occurrence: " + index);
                    content.setText(getChars(index));
                }
            });

            nextButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    if (index < locations.size() - 1) {
                        index++;
                    }
                    indexLable.setText("Occurrence: " + index);
                    content.setText(getChars(index));
                }
            });
        }

        public JPanel get() {
            return wrapper;
        }

        private String getChars(int pos) {
            long fileLength = f.length();
            byte[] data = new byte[200];
            // If the file is less than 100 chacters, print the while file
            if (locations.get(pos)-100 < 0) {
                try {
                    raf.seek(0);
                } catch (IOException e) {
                    System.out.println("seek failed");
                    return "";
                }

            } else {
                try {
                    raf.seek(locations.get(pos)-100);
                } catch (IOException e) {
                    System.out.println("seek failed");
                    return "";
                }
            }

            try {
                raf.read(data);
            } catch (IOException e) {
                System.out.println("read failed");
                return "";
            }

            String output = new String(data);
            return output;
        }
    }
}