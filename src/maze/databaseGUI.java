package maze;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.table.DefaultTableModel;

public class databaseGUI extends JFrame implements ActionListener, Runnable {

    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private final String[] columnsNames = {"Maze Title", "Author Name", "Date Created", "Last Edit", "Maze"};
    private String[][] info = databaseStorage.retrieveMaze();


    public Object selectedMaze;




    protected static final String searchBarString = "Searchbar";


    public void createGUI() {
        DefaultTableModel model = new DefaultTableModel(info, columnsNames){
            @Override
            public boolean isCellEditable(int row, int column) {
                //Stops table rows from being editable
                return false;
            }
        };
        setSize(WIDTH, HEIGHT);
        JTable savedMazes = new JTable(model);
        savedMazes.getColumnModel().getColumn(4).setMinWidth(0);
        savedMazes.getColumnModel().getColumn(4).setMaxWidth(0);
        savedMazes.getColumnModel().getColumn(4).setWidth(0);
        savedMazes.setCellSelectionEnabled(false);
        savedMazes.setRowSelectionAllowed(true);
        ListSelectionModel select = savedMazes.getSelectionModel();
        select.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        select.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int[] row = savedMazes.getSelectedRows();
                TableModel tm = savedMazes.getModel();
                selectedMaze = tm.getValueAt(row[0], 4);
            }
        });
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
        System.out.println(selectedMaze);

    }

    @Override
    public void run() {

    }


    public databaseGUI(String title) throws HeadlessException{
        super(title);
        createGUI();
    }
}
