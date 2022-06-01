package maze;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.table.DefaultTableModel;

public class databaseGUI extends JFrame implements ActionListener, Runnable {

    private static final int WIDTH = 300;
    private static final int HEIGHT = 200;
    private final String[] columnsNames = {"Maze Title", "Author Name", "Date Created", "Last Edit"};
    private String[][] info = databaseStorage.retrieveMaze();
    private String[][] data = {
            {"Example Maze", "Ben", "29/04/2022", "29/04/2022"},
            {"Example Maze", "Ben", "29/04/2022", "29/04/2022"},
    };




    protected static final String searchBarString = "Searchbar";


    public void createGUI() {
        DefaultTableModel model = new DefaultTableModel(info, columnsNames);
        setSize(WIDTH, HEIGHT);
        System.out.println(Arrays.deepToString(info));
        System.out.println(Arrays.deepToString(data));
        JTable savedMazes = new JTable(model);
        JPanel pnlDisplay = createPanel(Color.WHITE);
        pnlDisplay.setLayout(new BorderLayout());
        pnlDisplay.add(savedMazes);
        JMenuBar top = new JMenuBar();
        JButton open = new JButton("Open Selected");
        open.addActionListener(this);
        JTextField searchBar = new JTextField(10);
        searchBar.setActionCommand(searchBarString);
        JLabel searchBarLabel = new JLabel(searchBarString + ":");
        searchBarLabel.setLabelFor(searchBar);
        top.add(searchBarLabel);
        top.add(searchBar);
        top.add(open);
        getContentPane().add(pnlDisplay, BorderLayout.CENTER);
        getContentPane().add(top, BorderLayout.NORTH);

        repaint();
        setVisible(true);
    }


    private JPanel createPanel(Color c){
        JPanel temp = new JPanel();
        temp.setBackground(c);
        return temp;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
//        Object src = e.getSource();
//        if (src == open) {
//            databaseStorage.retrieveMaze("title", "author", "date", "edited");
//            // Replace static strings with readings from selected entry in database
//        }
    }

    @Override
    public void run() {

    }


    public databaseGUI(String title) throws HeadlessException{
        super(title);
        createGUI();
    }
}
