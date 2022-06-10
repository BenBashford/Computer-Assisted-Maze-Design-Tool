package maze;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;


public class UserGUI extends JFrame implements ActionListener, Runnable {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;


    public static boolean isLogo = false;

    public static Maze currentMaze;

    public static JPanel pnlDisplay;
    private JMenu manual;
    private JMenu auto;
    private JMenu imgSelect;
    private JMenu saveOpen;
    private JMenuItem autoGen;
    private JMenuItem manGen;
    private JMenuItem logoAdd;
    private JMenuItem mazeAdd;
    private JMenuItem imageRemove;
    private JMenuItem save;
    private JMenuItem open;
    private JToggleButton mazeSolutions;
    private JToggleButton setEditable;
    private JButton difficulty;
    public static int size;
    public static boolean isManual;
    public static MouseListener ml = null;


    public static boolean isFromDB = false;
    public static boolean editable = false;

    public static boolean isReloaded = false;

    public static ArrayList<String> retrievedDirections = new ArrayList<>();

    public static ArrayList<String> reloadStorage = new ArrayList<>();

    public static int reloadImageX;
    public static int reloadImageY;


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
        setResizable(false);


        pnlDisplay = createPanel(Color.WHITE);
        pnlDisplay.setLayout(new BorderLayout());

        JMenuBar top = new JMenuBar();
        manual = createJMenu("Manual");
        auto = createJMenu("Auto");
        imgSelect = createJMenu("Select Image/s");
        saveOpen = createJMenu("Save/Open");
        mazeSolutions = createToggleButton("Generate With Solution");
        setEditable = createToggleButton("Enable Manual Maze Editing");
        difficulty = createButton("Difficulty");
        top.add(manual);
        top.add(auto);
        top.add(imgSelect);
        top.add(saveOpen);
        top.add(mazeSolutions);
        top.add(setEditable);
        top.add(difficulty);
        autoGen = createJMenuItem("Generate Maze");
        manGen = createJMenuItem("Begin Manual Maze Design");
        logoAdd = createJMenuItem("Insert Logo");
        mazeAdd = createJMenuItem("Insert Maze Image");
        imageRemove = createJMenuItem("Remove Current Image/s");
        save = createJMenuItem("Save");
        open = createJMenuItem("Open");
        manual.add(manGen);
        auto.add(autoGen);
        imgSelect.add(logoAdd);
        imgSelect.add(mazeAdd);
        imgSelect.add(imageRemove);
        saveOpen.add(save);
        saveOpen.add(open);

        getContentPane().add(pnlDisplay, BorderLayout.CENTER);
        getContentPane().add(top, BorderLayout.NORTH);
        repaint();
        setLocationRelativeTo(null);
        setVisible(true);


    }

    private JPanel createPanel(Color c) {
        JPanel temp = new JPanel();
        temp.setBackground(c);
        return temp;
    }

    private JButton createButton(String str){
        JButton temp = new JButton();
        temp.setFocusable(false);
        temp.setText(str);
        temp.addActionListener(this);
        return temp;
    }

    private JToggleButton createToggleButton(String str) {
        JToggleButton temp = new JToggleButton();
        temp.setFocusable(false);
        temp.setText(str);
        temp.addActionListener(this);
        return temp;
    }

    private JMenuItem createJMenuItem(String str) {
        JMenuItem temp = new JMenuItem();
        temp.setText(str);
        temp.addActionListener(this);
        return temp;
    }

    private JMenu createJMenu(String str) {
        JMenu temp = new JMenu();
        temp.setText(str);
        return temp;
    }


    @Override
    public void run() {

    }

    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == manGen) { //manual generation
            isFromDB = false; //maze is not pulled from database
            //create a new panel to determine the cell size
            JPanel myPanel = new JPanel();
            myPanel.add(new JLabel("Size:"));

            Object[] possibilities = {"Small", "Medium", "Large"};
            String s = (String) JOptionPane.showInputDialog(
                    myPanel,
                    "Please Select a Maze Size:\n",
                    "Size Selector",
                    JOptionPane.PLAIN_MESSAGE, null,
                    possibilities,
                    "ham");

            // These sizes are arbitrary, if you find a way to make it so the overall maze dimensions don't change when changing maze size please do so.
            if (Objects.equals(s, "Small")) {
                size = 40;
            } else if (Objects.equals(s, "Medium")) {
                size = 30;
            } else if (Objects.equals(s, "Large")) {
                size = 20;
            }
            //
            if ((s != null)) {
                isManual = true;
                final Rectangle[] r = {pnlDisplay.getBounds()}; // In case window needs to become resizable, this code will accommodate
                Maze[] g = {new Maze(r[0].width, r[0].height)};
                int maxWidthCoord = genAndSolve.maze.length;
                int maxHeightCoord = genAndSolve.maze[0].length;

                pnlDisplay.removeAll();
                pnlDisplay.add(g[0], BorderLayout.CENTER);
                pnlDisplay.setVisible(true);
                currentMaze = g[0];
                //double for loop deleting the whole maze
                for (int i = 0; i < maxWidthCoord; i++) { //x coordinate
                    for (int j = 0; j < maxHeightCoord; j++) { //y coordinate
                        if ((i * j == 0) || (i == maxWidthCoord - 1 || j == maxHeightCoord - 1)) {
                            genAndSolve.maze[i][j] = genAndSolve.state.WALL; //place walls around the border
                        } else {
                            genAndSolve.maze[i][j] = genAndSolve.state.PATH; //fill the center with paths
                        }

                    }
                }

                pnlDisplay.revalidate();
                pnlDisplay.repaint();
                if (pnlDisplay.getMouseListeners().length == 1) {
                    pnlDisplay.removeMouseListener(ml);
                }
                if (pnlDisplay.getMouseListeners().length == 0) {
                    ml = (new MouseAdapter() {
                        public void mouseClicked(MouseEvent c) {
                            if (editable) { //check if the maze is editable
                                pnlDisplay.removeAll();
                                int coordX = c.getX() / size; //get the x coordinate of the mouse
                                int coordY = c.getY() / size; //get the y coordinate of the mouse

                                if (String.valueOf(genAndSolve.maze[coordX][coordY]) == "WALL" && coordX != 0 && coordY != 0 && coordX < maxWidthCoord - 1 && coordY < maxHeightCoord - 1) {
                                    genAndSolve.maze[coordX][coordY] = genAndSolve.state.PATH;
                                }
                                else if (String.valueOf(genAndSolve.maze[coordX][coordY]) == "PATH" && coordX < maxWidthCoord - 1 && coordY < maxHeightCoord - 1) {
                                    genAndSolve.maze[coordX][coordY] = genAndSolve.state.WALL;
                                }
                                pnlDisplay.removeAll();
                                pnlDisplay.add(g[0], BorderLayout.CENTER);
                                pnlDisplay.revalidate();
                                pnlDisplay.repaint();
                            }
                        }
                    });
                    pnlDisplay.addMouseListener(ml);
                }
                pnlDisplay.revalidate();
                pnlDisplay.repaint();


            }

        } else if (src == autoGen) {
            isFromDB = false;
            isManual = false;
            JPanel myPanel = new JPanel();
            myPanel.add(new JLabel("Size:"));


            Object[] possibilities = {"Small", "Medium", "Large"};
            String s = (String) JOptionPane.showInputDialog(
                    myPanel,
                    "Please Select a Maze Size:\n",
                    "Size Selector",
                    JOptionPane.PLAIN_MESSAGE, null,
                    possibilities,
                    "ham");

            if (Objects.equals(s, "Small")) {
                size = 40;
            } else if (Objects.equals(s, "Medium")) {
                size = 30;
            } else if (Objects.equals(s, "Large")) {
                size = 20;
            }

            if ((s != null)) {
                final Rectangle[] r = {pnlDisplay.getBounds()}; // In case window needs to become resizable, this code will accommodate
                final Maze[] g = {new Maze(r[0].width, r[0].height)};
                pnlDisplay.removeAll();
                pnlDisplay.add(g[0], BorderLayout.CENTER);
                pnlDisplay.setVisible(true);
                currentMaze = g[0];
                addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        int code = e.getKeyCode();
                        if (code == KeyEvent.VK_SPACE) {
                            loadMaze(false);
                        }
                    }
                });

                pnlDisplay.revalidate();
                pnlDisplay.repaint();
                int maxWidthCoord = genAndSolve.maze.length;
                int maxHeightCoord = genAndSolve.maze[0].length;
                if (pnlDisplay.getMouseListeners().length == 1) {
                    pnlDisplay.removeMouseListener(ml);
                }
                if (pnlDisplay.getMouseListeners().length == 0) {
                    ml = (new MouseAdapter() {
                        public void mouseClicked(MouseEvent c) {
                            if (editable) { //check if the maze is editable
                                pnlDisplay.removeAll();
                                int coordX = c.getX() / size; //get the x coordinate of the mouse
                                int coordY = c.getY() / size; //get the y coordinate of the mouse

                                if (String.valueOf(genAndSolve.maze[coordX][coordY]) == "WALL" && coordX != 0 && coordY != 0 && coordX < maxWidthCoord - 1 && coordY < maxHeightCoord - 1) {
                                    genAndSolve.maze[coordX][coordY] = genAndSolve.state.PATH;
                                }
                                else if (String.valueOf(genAndSolve.maze[coordX][coordY]) == "PATH" && coordX < maxWidthCoord - 1 && coordY < maxHeightCoord - 1) {
                                    genAndSolve.maze[coordX][coordY] = genAndSolve.state.WALL;
                                }
                                pnlDisplay.removeAll();
                                pnlDisplay.add(g[0], BorderLayout.CENTER);
                                pnlDisplay.revalidate();
                                pnlDisplay.repaint();
                            }
                        }
                    });
                    pnlDisplay.addMouseListener(ml);
                }
                pnlDisplay.revalidate();
                pnlDisplay.repaint();
            }
        } else if (src == logoAdd) {
            isLogo = true;
            genAndSolve.logo = (imageInsert.addImage(0));

            if (UserGUI.size != 0 && !isFromDB) {
                if (!isManual){
                    loadMaze(false);
                }
                else {
                    reload(Save());
                }
            }
        } else if (src == mazeAdd) {
            imageInsert.addImage(1);
            if (UserGUI.size != 0 && !isFromDB) {
                if (!isManual){
                    loadMaze(false);
                }
                else {
                    reload(Save());
                }
            }
        } else if (src == imageRemove) {
            imageInsert.removeImage();
            if (UserGUI.size != 0 && !isFromDB) {
                if (!isManual){
                    loadMaze(false);
                }
                else {
                    reload(Save());
                }
            }

        } else if (src == open) {
            new databaseGUI("Database");

        } else if (src == save) {
            JPanel myPanel = new JPanel();
            JTextField title = new JTextField(8);
            JTextField author = new JTextField(8);
            myPanel.add(new JLabel("Maze Title"));
            myPanel.add(title, BorderLayout.NORTH);
            myPanel.add(new JLabel("Author Name"));
            myPanel.add(author, BorderLayout.SOUTH);
            int result = JOptionPane.showConfirmDialog(
                    null, myPanel, "Save Current Maze?", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                if (title.getText().length() > 0 && author.getText().length() > 0 && validateName(title.getText())) {
                    createScreenshot(title.getText());
                    try {
                        if (databaseStorage.retrieveTitles().contains(title.getText())) {
                            databaseStorage.updateMaze(title.getText(), Save());
                        }
                        else {
                            databaseStorage.insertMaze(title.getText(), author.getText(), Save());
                        }
                    } catch (SQLException | FileNotFoundException ex) {
                        ex.printStackTrace();
                    }
                } else if (currentMaze == null) {
                    JOptionPane.showMessageDialog(null, "No currently generated maze", "Error", JOptionPane.INFORMATION_MESSAGE);
                } else if (!validateName(title.getText())) {
                    JOptionPane.showMessageDialog(null, "Invalid Title", "Error", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    String errMsg;
                    if (title.getText().length() == 0 && author.getText().length() > 0) {
                        errMsg = "Title";
                    } else if (author.getText().length() == 0 && title.getText().length() > 0) {
                        errMsg = "Author";
                    } else {
                        errMsg = "Title & Author";
                    }
                    JOptionPane.showMessageDialog(null, "Missing " + errMsg, "Error", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } else if (src == mazeSolutions) {
            boolean savedSolution = false;
            if ((isFromDB && !genAndSolve.genSolutions)) {
                int maxWidthCoord = genAndSolve.maze.length;
                int maxHeightCoord = genAndSolve.maze[0].length;
                for (int i = 0; i < maxWidthCoord; i++) { //x coordinate
                    for (int j = 0; j < maxHeightCoord; j++) { //y coordinate
                        if (Objects.equals(String.valueOf(genAndSolve.maze[i][j]), "SOLUTION")) {
                            savedSolution = true;
                        }
                    }
                }
            }
            genAndSolve.genSolutions = !genAndSolve.genSolutions;
            if (size != 0 && !savedSolution) {
                reload(Save());
            }
        } else if (src == setEditable) {
            editable = !editable;
        } else if (src == difficulty) {
            int w = genAndSolve.maze.length;
            int h = genAndSolve.maze[0].length;
            int counterPath = 0;
            int counterSolution = 0;
            int counterCorner = 0;
            for (int i = 0; i < w; i++) { // x
                for (int j = 0; j < h; j++) { // y
                    if (String.valueOf(genAndSolve.maze[i][j]) == "SOLUTION") {
                        counterSolution++;
                    }
                    if (String.valueOf(genAndSolve.maze[i][j]) == "PATH" || String.valueOf(genAndSolve.maze[i][j]) == "PLACEHOLDER") {
                        counterPath++;
                    }
                    // defined by if
                    // above or below and
                    // left or right
                    // == wall
//                    if ((1<i && i<h) && (1<j && j<w)) {
                    if ((i != 0 && i < w - 1) && (j != 0 && j < h - 1)) {
                        if (String.valueOf(genAndSolve.maze[i][j]) != "WALL" && !(i==1 && j==1)) {
                            if (
                                    ((String.valueOf(genAndSolve.maze[i][j - 1]) == "WALL") && (String.valueOf(genAndSolve.maze[i + 1][j]) == "WALL") && (String.valueOf(genAndSolve.maze[i - 1][j]) == "WALL"))
                                            || ((String.valueOf(genAndSolve.maze[i][j - 1]) == "WALL") && (String.valueOf(genAndSolve.maze[i + 1][j]) == "WALL") && (String.valueOf(genAndSolve.maze[i][j + 1]) == "WALL"))
                                            || ((String.valueOf(genAndSolve.maze[i - 1][j]) == "WALL") && (String.valueOf(genAndSolve.maze[i + 1][j]) == "WALL") && (String.valueOf(genAndSolve.maze[i][j + 1]) == "WALL"))
                                            || ((String.valueOf(genAndSolve.maze[i - 1][j]) == "WALL") && (String.valueOf(genAndSolve.maze[i][j - 1]) == "WALL") && (String.valueOf(genAndSolve.maze[i][j + 1]) == "WALL"))
                            ) {
                                counterCorner++;
                            }
                        }
                    }
                }
            }

            double percentCells = (counterSolution*100/(counterPath+counterSolution));

            String string = String.format("There are %d dead ends \n The solution is %.1f percent of the maze", counterCorner, percentCells);
            //String string = String.format("There a %d corner cells  The solution is %f % of the maze  The solution difficulty is %s", counterCorner, percentCells);

            //JOptionPane.showMessageDialog(null, string);
            //System.out.print(string);
            
            JOptionPane.showMessageDialog(null, string, "Maze Difficulty", JOptionPane.PLAIN_MESSAGE);

            //JOptionPane.showMessageDialog(null, string, "Maze Difficulty", JOptionPane.PLAIN_MESSAGE);
        }
    }

    private boolean validateName(String title) {
        File file = new File(title);
        boolean created = false;
        try {
            created = file.createNewFile();
        } catch (IOException e) {
            //Empty catch, there is intended to be an error here if a non-file friendly title is given
        } finally {
            if (created) {
                file.delete();
            }
        }
        return created;
    }

    public String Save() {
        int width = genAndSolve.maze.length;
        int height = genAndSolve.maze[0].length;
        String totalStates = "";
        String k;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                k = String.valueOf(Maze.maze.maze[i][j]);
                totalStates = totalStates.concat(k + ",");
            }
        }
        return totalStates;
    }

    public static void reload(String reloadedMaze) {
        isReloaded = true;
        reloadStorage.clear();
        String temp = genAndSolve.returnLogoCoordinates();
        String[] idk = temp.split("[-]", 0);
        ArrayList<Integer> retrievedPos = new ArrayList<>();
        for (String myNextStr : idk) {
            retrievedPos.add(Integer.valueOf(myNextStr));
        }
        reloadImageX = retrievedPos.get(0);
        reloadImageY = retrievedPos.get(1);
        String[] res = reloadedMaze.split("[,]", 0);
        Collections.addAll(reloadStorage, res);
        final Rectangle[] r = {pnlDisplay.getBounds()};
        final Maze[] g = {new Maze(r[0].width, r[0].height)};
        if (!genAndSolve.genSolutions){
            int maxWidthCoord = genAndSolve.maze.length;
            int maxHeightCoord = genAndSolve.maze[0].length;
            for (int i = 0; i < maxWidthCoord; i++) { //x coordinate
                for (int j = 0; j < maxHeightCoord; j++) { //y coordinate
                    if (Objects.equals(String.valueOf(genAndSolve.maze[i][j]), "SOLUTION") || (Objects.equals(String.valueOf(genAndSolve.maze[i][j]), "PLACEHOLDER"))) {
                        genAndSolve.maze[i][j] = genAndSolve.state.PATH;
                    }
                }
            }
        }
        pnlDisplay.removeAll();
        pnlDisplay.add(g[0], BorderLayout.CENTER);
        pnlDisplay.setVisible(true);
        currentMaze = g[0];
        pnlDisplay.revalidate();
        pnlDisplay.repaint();
        isReloaded = false;
    }



    public static void loadMaze(Boolean fromDB){
        if (fromDB) {
            size = databaseGUI.returnSize();

        }
        final Rectangle[] r = {pnlDisplay.getBounds()};
        final Maze[] g = {new Maze(r[0].width, r[0].height)};
        if (fromDB) {
            pnlDisplay.removeAll();
            pnlDisplay.add(g[0], BorderLayout.CENTER);
            pnlDisplay.setVisible(true);
            currentMaze = g[0];
            Integer n = 0;
            int maxWidthCoord = genAndSolve.maze.length;
            int maxHeightCoord = genAndSolve.maze[0].length;
            for (int i = 0; i < maxWidthCoord; i++) { //x coordinate
                for (int j = 0; j < maxHeightCoord; j++) { //y coordinate
                    genAndSolve.maze[i][j] = genAndSolve.state.valueOf(retrievedDirections.get(n));
                    n++;
                }
            }
        }
        else{
            r[0] = pnlDisplay.getBounds();
            g[0] = new Maze(r[0].width, r[0].height);
            currentMaze = g[0];
            pnlDisplay.add(g[0], BorderLayout.CENTER);
        }
        pnlDisplay.revalidate();
        pnlDisplay.repaint();
        int maxWidthCoord = genAndSolve.maze.length;
        int maxHeightCoord = genAndSolve.maze[0].length;
        if (pnlDisplay.getMouseListeners().length == 1) {
            pnlDisplay.removeMouseListener(ml);
        }
        if (pnlDisplay.getMouseListeners().length == 0) {
            ml = (new MouseAdapter() {
                public void mouseClicked(MouseEvent c) {
                    if (editable) { //check if the maze is editable

                        pnlDisplay.removeAll();
                        int coordX = c.getX() / size; //get the x coordinate of the mouse
                        int coordY = c.getY() / size; //get the y coordinate of the mouse

                        if (String.valueOf(genAndSolve.maze[coordX][coordY]) == "WALL" && coordX != 0 && coordY != 0 && coordX < maxWidthCoord - 1 && coordY < maxHeightCoord - 1) {
                            genAndSolve.maze[coordX][coordY] = genAndSolve.state.PATH;
                        } else if (String.valueOf(genAndSolve.maze[coordX][coordY]) == "PATH" && coordX < maxWidthCoord - 1 && coordY < maxHeightCoord - 1) {
                            genAndSolve.maze[coordX][coordY] = genAndSolve.state.WALL;
                        }
                        pnlDisplay.removeAll();
                        pnlDisplay.add(g[0], BorderLayout.CENTER);
                        pnlDisplay.revalidate();
                        pnlDisplay.repaint();
                    }

                }

            });
            pnlDisplay.addMouseListener(ml);
        }
        pnlDisplay.revalidate();
        pnlDisplay.repaint();
    }

    public void createScreenshot(String title){
        BufferedImage screenShot = new BufferedImage(pnlDisplay.getWidth(), pnlDisplay.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = screenShot.createGraphics();
        currentMaze.paintAll(g2d);
        g2d.dispose();

        try
        {
            ImageIO.write ( screenShot, "png", new File ( "src/images/MazeImages/"+title+"-Screenshot.png" ) );
        }
        catch ( IOException e )
        {
            e.printStackTrace ();
        }
    }


    public static int returnSize(){
        return size;
    }


    public static void main (String[] args) throws IOException {
        new UserGUI("MazeCo Computer Assisted Maze Design Tool");
    }


}
