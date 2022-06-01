package maze;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;



public class UserGUI extends JFrame implements ActionListener, Runnable{

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    public static boolean isLogo = false;

    public Maze currentMaze;

    private JPanel pnlDisplay;
    private JMenu manual;
    private JMenu auto;
    private JMenu imgSelect;
    private JMenu saveOpen;
    private JMenuItem autoGen;
    private JMenuItem manGen;
    private JMenuItem logoAdd;
    private JMenuItem mazeAdd;
    private JMenuItem save;
    private JMenuItem open;
    private static int size;

    public UserGUI(String title) throws HeadlessException, IOException {
        super(title);
        createGUI();
    }

    public void createGUI() throws IOException {
        databaseStorage.create();
        setSize(WIDTH, HEIGHT);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        pnlDisplay = createPanel(Color.WHITE);
        pnlDisplay.setLayout(new BorderLayout());

        JMenuBar top = new JMenuBar();
        manual = createJMenu("Manual");
        auto = createJMenu("Auto");
        imgSelect = createJMenu("Select Image/s");
        saveOpen = createJMenu("Save/Open");
        top.add (manual);
        top.add (auto);
        top.add (imgSelect);
        top.add(saveOpen);
        autoGen = createJMenuItem("Generate Maze");
        manGen = createJMenuItem("Begin Manual Maze Design");
        logoAdd = createJMenuItem("Insert Logo");
        mazeAdd = createJMenuItem("Insert Maze Image");
        save = createJMenuItem("Save");
        open = createJMenuItem("Open");
        manual.add(manGen);
        auto.add(autoGen);
        imgSelect.add(logoAdd);
        imgSelect.add(mazeAdd);
        saveOpen.add(save);
        saveOpen.add(open);

        getContentPane().add(pnlDisplay, BorderLayout.CENTER);
        getContentPane().add(top, BorderLayout.NORTH);
        repaint();
        setLocationRelativeTo(null);
        setVisible(true);


    }
    private JPanel createPanel(Color c){
        JPanel temp = new JPanel();
        temp.setBackground(c);
        return temp;
    }


    private JMenuItem createJMenuItem(String str){
        JMenuItem temp = new JMenuItem();
        temp.setText(str);
        temp.addActionListener(this);
        return temp;
    }

    private JMenu createJMenu(String str){
        JMenu temp = new JMenu();
        temp.setText(str);
        return temp;
    }


    @Override
    public void run() {

    }

    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == manGen){
            // Insert Manual Generation Code Here
        }
        else if (src == autoGen){
            JPanel myPanel = new JPanel();
            myPanel.add(new JLabel("Size:"));


            Object[] possibilities = {"Small", "Medium", "Large"};
            String s = (String)JOptionPane.showInputDialog(
                    myPanel,
                    "Please Select a Maze Size:\n",
                    "Size Selector",
                    JOptionPane.PLAIN_MESSAGE, null,
                    possibilities,
                    "ham");

            // These sizes are arbitrary, if you find a way to make it so the overall maze dimensions don't change when changing maze size please do so.
            if (Objects.equals(s, "Small")){
                size = 40;
            }
            else if (Objects.equals(s, "Medium")){
                size = 30;
            }
            else if (Objects.equals(s, "Large")){
                size = 20;
            }

            if ((s != null)){
                final Rectangle[] r = {pnlDisplay.getBounds()};
                final Maze[] g = {new Maze(r[0].width, r[0].height)};
                pnlDisplay.add(g[0], BorderLayout.CENTER);
                setVisible(true);
                currentMaze = g[0];
                this.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        int code = e.getKeyCode();
                        if (code == KeyEvent.VK_SPACE) {
                            r[0] = pnlDisplay.getBounds();
                            g[0] = new Maze(r[0].width, r[0].height);
                            currentMaze = g[0];
                        }
                        repaint();
                    }
                });

            }
        }
        else if (src == logoAdd){
            isLogo = true;
           genAndSolve.logo = (imageInsert.addImage(0));
        }
        else if (src == mazeAdd){
            imageInsert.addImage(1);
        }
        else if (src == open){
            new databaseGUI("Database");
        }
        else if (src == save){
            JPanel myPanel = new JPanel();
            JTextField title = new JTextField(8);
            JTextField author = new JTextField(8);

            myPanel.add(new JLabel("Maze Title"));
            myPanel.add(title, BorderLayout.NORTH);
            myPanel.add(new JLabel("Author Name"));
            myPanel.add(author, BorderLayout.SOUTH);
            int result = JOptionPane.showConfirmDialog(
                    null, myPanel, "Save Current Maze?", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION){
                databaseStorage.insertMaze(currentMaze, title.getText(), author.getText());

            }
        }
    }

    public static int returnSize(){
        return size;
    }

    public static void main (String[] args) throws IOException {
        new UserGUI("MazeCo Computer Assisted Maze Design Tool");
    }


}
