package maze;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.table.DefaultTableModel;

import static java.lang.Integer.parseInt;

public class databaseGUI extends JFrame implements ActionListener, Runnable {

    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private final String[] columnsNames = {"Maze Title", "Author Name", "Date Created", "Last Edit", "Hidden"};
    private String[][] info = databaseStorage.retrieveMaze();


    public String selectedMaze;




    public void createGUI() {
        DefaultTableModel model = new DefaultTableModel(info, columnsNames){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        setSize(WIDTH, HEIGHT);
        JTable savedMazes = new JTable(model);
        savedMazes.setCellSelectionEnabled(false);
        savedMazes.setRowSelectionAllowed(true);
        savedMazes.getColumnModel().getColumn(4).setPreferredWidth(0);
        savedMazes.getColumnModel().getColumn(4).setMinWidth(0);
        savedMazes.getColumnModel().getColumn(4).setMaxWidth(0);
        ListSelectionModel select = savedMazes.getSelectionModel();
        select.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        select.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int[] row = savedMazes.getSelectedRows();
                TableModel tm = savedMazes.getModel();
                selectedMaze = (String) tm.getValueAt(row[0], 4);
            }
        });
        JPanel pnlDisplay = createPanel(Color.WHITE);
        pnlDisplay.setLayout(new BorderLayout());
        JScrollPane pane = new JScrollPane(savedMazes);
        pnlDisplay.add(pane);
        JMenuBar top = new JMenuBar();
        JButton open = new JButton("Open Selected");
        open.addActionListener(this);
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
        ArrayList<Integer> retrievedDirections = new ArrayList<>();
        ArrayList<Point> convertedDirections = new ArrayList<>();
        String[] res = selectedMaze.split("[,]", 0);
        for(String myStr: res) {
            String[] idk = myStr.split("[-]", 0);
            for (String myNextStr: idk){
                retrievedDirections.add(Integer.valueOf(myNextStr));
            }
        }
        for(int n = 0; n < retrievedDirections.size(); n = n+2) {
            Point temp = new Point(retrievedDirections.get(n), retrievedDirections.get(n + 1));
            convertedDirections.add(temp);
        }
        UserGUI.isFromDB = true;
        UserGUI.retrievedPoints = convertedDirections;
        UserGUI.loadMaze();
    }

    @Override
    public void run() {

    }





    public databaseGUI(String title) throws HeadlessException{
        super(title);
        createGUI();
    }
}
