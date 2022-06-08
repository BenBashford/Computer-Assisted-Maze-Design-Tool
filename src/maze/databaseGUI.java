package maze;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.table.DefaultTableModel;

import static java.lang.Integer.parseInt;

public class databaseGUI extends JFrame implements ActionListener, Runnable {

    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private final String[] columnsNames = {"Maze Title", "Author Name", "Date Created", "Last Edit", "Maze", "Size", "LogoPos", "LogoSize"};
    private String[][] info = databaseStorage.retrieveMaze();


    public String selectedMaze;
    private static int selectedSize;
    private static int selectedLogoX;
    private static int selectedLogoY;
    private static int selectedLogoSize;
    private static String selectedTitle;
    public static boolean hasLogo;

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
        savedMazes.getColumnModel().getColumn(5).setPreferredWidth(0);
        savedMazes.getColumnModel().getColumn(5).setMinWidth(0);
        savedMazes.getColumnModel().getColumn(5).setMaxWidth(0);
        savedMazes.getColumnModel().getColumn(6).setPreferredWidth(0);
        savedMazes.getColumnModel().getColumn(6).setMinWidth(0);
        savedMazes.getColumnModel().getColumn(6).setMaxWidth(0);
        savedMazes.getColumnModel().getColumn(7).setPreferredWidth(0);
        savedMazes.getColumnModel().getColumn(7).setMinWidth(0);
        savedMazes.getColumnModel().getColumn(7).setMaxWidth(0);
        ListSelectionModel select = savedMazes.getSelectionModel();
        select.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        select.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int[] row = savedMazes.getSelectedRows();
                TableModel tm = savedMazes.getModel();
                selectedMaze = (String) tm.getValueAt(row[0], 4);
                String tempSize = (String) tm.getValueAt(row[0], 5);
                selectedSize = parseInt (tempSize);

                String tempLogoPos = (String) tm.getValueAt(row[0], 6); // Really lazy way of error checking but should function
                if (tempLogoPos != null) {
                    hasLogo = true;
                    String[] idk = tempLogoPos.split("[-]", 0);
                    ArrayList<Integer> retrievedPos = new ArrayList<>();
                    for (String myNextStr : idk) {
                        retrievedPos.add(Integer.valueOf(myNextStr));
                    }
                    selectedLogoX = retrievedPos.get(0);
                    selectedLogoY = retrievedPos.get(1);

                    String tempLogoSize = (String) tm.getValueAt(row[0], 7);
                    selectedLogoSize = parseInt (tempLogoSize);

                    selectedTitle = (String) tm.getValueAt(row[0], 0);
                }
                else{
                    hasLogo = false;
                }



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
        UserGUI.retrievedDirections.clear();
        String[] res = selectedMaze.split("[,]", 0);
        Collections.addAll(UserGUI.retrievedDirections, res);
        UserGUI.isFromDB = true;
        UserGUI.isManual = false;
        UserGUI.loadMaze(true);
    }

    @Override
    public void run() {

    }

    public static int returnSize(){
        return selectedSize;
    }

    public static int returnLogoPos(Integer n){
        if (n==0){
            return selectedLogoX;
        }
        else{
            return selectedLogoY;
        }
    }

    public static int returnLogoSize(){
        return selectedLogoSize;
    }

    public static String returnTitle(){
        return selectedTitle;
    }



    public databaseGUI(String title) throws HeadlessException{
        super(title);
        createGUI();
    }
}
