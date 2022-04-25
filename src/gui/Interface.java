package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Interface extends JFrame implements ActionListener, Runnable{

    public static final int WIDTH = 300;
    public static final int HEIGHT = 200;

    private JPanel pnlDisplay;
    private JTextArea areDisplay;
    private JMenu manual;
    private JMenu auto;
    private JMenu imgSelect;
    private JMenuItem autoGen;
    private JMenuItem manGen;



    public Interface(String title) throws HeadlessException{
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
        manGen.addActionListener(this);
        manual.add(manGen);
        auto.add(autoGen);

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


    private JTextArea txtArea(){
        JTextArea display = new JTextArea();
        display.setEditable(false);
        display.setLineWrap(true);
        display.setFont(new Font("Arial",Font.BOLD,20));
        display.setBorder(BorderFactory.createEtchedBorder());
        display.setText("Temp Home Screen");
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
            areDisplay.setEditable(false);
        }
    }
}
