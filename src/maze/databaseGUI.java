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

public class databaseGUI extends JFrame implements ActionListener, Runnable {

    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private final String[] columnsNames = {"Maze Title", "Author Name", "Date Created", "Last Edit"};
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
        ListSelectionModel select = savedMazes.getSelectionModel();
        select.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        select.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int[] row = savedMazes.getSelectedRows();
                TableModel tm = savedMazes.getModel();
                selectedMaze = (String) tm.getValueAt(row[0], 0);
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
        BufferedImage inputImage = null;
        try {
            inputImage = ImageIO.read(new File("src/images/retrieved/"+selectedMaze+".png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        UserGUI.pnlDisplay.removeAll();
        assert inputImage != null;
        UserGUI.pnlDisplay.add(new JLabel(new ImageIcon(inputImage)
        ));
        UserGUI.pnlDisplay.revalidate();
        UserGUI.pnlDisplay.repaint();
    }

    @Override
    public void run() {

    }





    public databaseGUI(String title) throws HeadlessException{
        super(title);
        createGUI();
    }
}
