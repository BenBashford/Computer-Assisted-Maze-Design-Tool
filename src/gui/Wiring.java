package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Wiring extends JFrame implements ActionListener, Runnable{

    public static final int WIDTH = 300;
    public static final int HEIGHT = 200;

    private JPanel pnlDisplay;
    private JPanel pnl2;
    private JPanel pnl3;
    private JPanel pnlBtn;
    private JPanel pnl5;
    private JButton btnLoad;
    private JButton btnUnload;
    private JButton btnFind;
    private JButton btnSwitch;
    private JTextArea areDisplay;


    public Wiring(String title) throws HeadlessException{
        super(title);
        createGUI();
    }



    public void createGUI(){

        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());


        pnlDisplay = createPanel(Color.WHITE);
        pnl2 = createPanel(Color.LIGHT_GRAY);
        pnl3 = createPanel(Color.LIGHT_GRAY);
        pnlBtn = createPanel(Color.LIGHT_GRAY);
        pnl5 = createPanel(Color.LIGHT_GRAY);
        btnLoad = createButton("Load");
        btnUnload = createButton("Unload");
        btnFind = createButton("Find");
        btnSwitch = createButton("Switch");
        areDisplay = ayyLmao();
        pnlDisplay.setLayout(new BorderLayout());
        pnlDisplay.add(areDisplay);

        getContentPane().add(pnlDisplay, BorderLayout.CENTER);
        getContentPane().add(pnl2, BorderLayout.NORTH);
        getContentPane().add(pnl3, BorderLayout.EAST);
        getContentPane().add(pnlBtn, BorderLayout.SOUTH);
        getContentPane().add(pnl5, BorderLayout.WEST);


        repaint();
        setVisible(true);

        layoutButtonPanel();

    }
    private JPanel createPanel(Color c){
        JPanel temp = new JPanel();
        temp.setBackground(c);
        return temp;
    }

    private JTextArea ayyLmao(){
        JTextArea display = new JTextArea();
        display.setEditable(false);
        display.setLineWrap(true);
        display.setFont(new Font("Arial",Font.BOLD,24));
        display.setBorder(BorderFactory.createEtchedBorder());
        return display;
    }

    private void addToPanel(JPanel jp,Component c, GridBagConstraints
            constraints,int x, int y, int w, int h) {
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.gridwidth = w;
        constraints.gridheight = h;
        jp.add(c, constraints);
    }


    private void layoutButtonPanel() {
        GridBagLayout layout = new GridBagLayout();
        pnlBtn.setLayout(layout);
        //add components to grid
        GridBagConstraints constraints = new GridBagConstraints();
        //Defaults
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 100;
        constraints.weighty = 100;
        addToPanel(pnlBtn, btnLoad,constraints,0,0,1,1);
        addToPanel(pnlBtn, btnUnload,constraints,3,0,2,1);
        addToPanel(pnlBtn, btnFind,constraints,0,2,2,1);
        addToPanel(pnlBtn, btnSwitch,constraints,3,2,2,1);
    }

    private JButton createButton(String str) {
        JButton temp = new JButton();
        temp.setText(str);
        temp.addActionListener(this);
        return temp;


    }
    public void actionPerformed(ActionEvent e) {
        //Get event source
        Object src = e.getSource();
//Consider the alternatives - not all active at once.
        if (src==btnLoad) {
            JButton btn = ((JButton) src);
            areDisplay.setText(btn.getText().trim());
        }
    }

    @Override
    public void run() {

    }
}
