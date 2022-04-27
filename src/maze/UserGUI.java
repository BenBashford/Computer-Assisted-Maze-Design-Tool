package maze;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;


public class UserGUI extends JFrame implements ActionListener, Runnable{

    private static final int WIDTH = 300;
    private static final int HEIGHT = 200;

    private JPanel pnlDisplay;
    private JTextPane areDisplay;
    private JMenu manual;
    private JMenu auto;
    private JMenu imgSelect;
    private JMenuItem autoGen;
    private JMenuItem manGen;
    private JMenuItem imgAdd;

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
        pnlDisplay.add(areDisplay);

        JMenuBar top = new JMenuBar();
        manual = createJMenu("Manual");
        auto = createJMenu("Auto");
        imgSelect = createJMenu("Select Image/s");
        top.add (manual);
        top.add (auto);
        top.add (imgSelect);
        autoGen = createJMenuItem("Generate Maze");
        manGen = createJMenuItem("Begin Manual Maze Design");
        imgAdd = createJMenuItem("Insert Image");
        manual.add(manGen);
        auto.add(autoGen);
        imgSelect.add(imgAdd);

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
                MazeGUI.open();
            }
        }
        else if (src == imgAdd){
            ImageIcon temp = new ImageIcon("src/images/Test Image.png"); // Create image icon
            Image image = temp.getImage(); // Change into normal image
            Image newTemp = image.getScaledInstance(120,120, java.awt.Image.SCALE_SMOOTH); // Rescale
            temp = new ImageIcon(newTemp); // Change back into image icon
            areDisplay.insertIcon(temp);
        }
    }

    public static int returnSize(){
        return size;
    }

    public static void main (String[] args){
        new UserGUI("UserGUIS");
    }
}
