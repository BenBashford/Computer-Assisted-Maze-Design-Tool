package maze;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class databaseGUI extends JFrame implements ActionListener, Runnable {

    private static final int WIDTH = 300;
    private static final int HEIGHT = 200;
    private String[] columnsNames = {"Maze Title", "Author Name", "Date Created", "Last Edit"};
    private Object[][] data = {
            {"Example Maze", "Ben", "29/04/2022", "29/04/2022"}
    };
    private JTable savedMazes;

    private JPanel pnlDisplay;
    private JTextField searchBar;
    private JLabel searchBarLabel;
    private JButton open;
    protected static final String searchBarString = "Searchbar";


    public void createGUI() {

        setSize(WIDTH, HEIGHT);
        savedMazes = new JTable(data, columnsNames);
        pnlDisplay = createPanel(Color.WHITE);
        pnlDisplay.setLayout(new BorderLayout());
        pnlDisplay.add(savedMazes);
        JMenuBar top = new JMenuBar();
        open = new JButton("Open Selected");
        searchBar = new JTextField(10);
        searchBar.setActionCommand(searchBarString);
        searchBarLabel = new JLabel(searchBarString + ":");
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

    }

    @Override
    public void run() {

    }


    public databaseGUI(String title) throws HeadlessException{
        super(title);
        createGUI();
    }
}
