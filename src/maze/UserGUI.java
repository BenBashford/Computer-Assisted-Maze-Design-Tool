package maze;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Objects;



public class UserGUI extends JFrame implements ActionListener, Runnable{

    private static final int WIDTH = 300;
    private static final int HEIGHT = 200;

    private JPanel pnlDisplay;
    private JTextPane areDisplay;
    private JMenu manual;
    private JMenu auto;
    private JMenu imgSelect;
    private JMenu saveOpen;
    private JMenuItem autoGen;
    private JMenuItem manGen;
    private JMenuItem iconAdd;
    private JMenuItem mazeAdd;
    private JMenuItem save;
    private JMenuItem open;
    private static int size;

    public UserGUI(String title) throws HeadlessException{
        super(title);
        createGUI();
    }

    public void createGUI(){

        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        pnlDisplay = createPanel(Color.WHITE);
        areDisplay = txtArea();
        pnlDisplay.setLayout(new BorderLayout());
//        pnlDisplay.add(areDisplay);

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
        iconAdd = createJMenuItem("Insert Icon");
        mazeAdd = createJMenuItem("Insert Maze Image");
        save = createJMenuItem("Save");
        open = createJMenuItem("Open");
        manual.add(manGen);
        auto.add(autoGen);
        imgSelect.add(iconAdd);
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


    private JTextPane txtArea(){
        JTextPane display = new JTextPane();
        display.setEditable(false);
        display.setFont(new Font("Arial",Font.BOLD,20));
        display.setBorder(BorderFactory.createEtchedBorder());
        return display;
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
            areDisplay.setEditable(true);
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
            if (Objects.equals(s, "Small")){
                size = 10;
            }
            else if (Objects.equals(s, "Medium")){
                size = 8;
            }
            else if (Objects.equals(s, "Large")){
                size = 5;
            }

            if ((s != null)){
                final Rectangle[] r = {pnlDisplay.getBounds()};
                final Maze[] g = {new Maze(r[0].width, r[0].height)};
                pnlDisplay.add(g[0]);
                setVisible(true);
                this.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        int code = e.getKeyCode();
                        if (code == KeyEvent.VK_SPACE) {
                            r[0] = pnlDisplay.getBounds(); g[0] = new Maze(r[0].width, r[0].height); }
                        repaint();
                    }
                });

            }
        }
        else if (src == iconAdd){
            areDisplay.insertIcon(imageInsert.addImage(0));
        }
        else if (src == mazeAdd){
            imageInsert.addImage(1);
        }
        else if (src == open){
            new databaseGUI("Database");
        }
        else if (src == save){
            JPanel myPanel = new JPanel();
            myPanel.add(new JLabel("Save Current Maze?"));

            int result = JOptionPane.showConfirmDialog(
                    null, myPanel, "Save Current Maze?", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION){
                // Link to databaseStorage here
            }
        }
    }

    public static int returnSize(){
        return size;
    }

    public static void main (String[] args){
        new UserGUI("MazeCo Computer Assisted Maze Design Tool");
    }


}
