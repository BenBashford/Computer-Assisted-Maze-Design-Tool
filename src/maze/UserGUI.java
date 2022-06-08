package maze;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;


public class UserGUI extends JFrame implements ActionListener, Runnable{

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
    public static int size;
    public static boolean isManual;



    public static boolean isReprint;

    public static boolean isFromDB = false;
    public static boolean editable = false;



    public static ArrayList<Point> savedDirections = new ArrayList<>();

    public static ArrayList<Point> retrievedPoints = new ArrayList<>();

    public static ArrayList<Integer> storedXY = new ArrayList<>();

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
        editable = false;


        pnlDisplay = createPanel(Color.WHITE);
        pnlDisplay.setLayout(new BorderLayout());

        JMenuBar top = new JMenuBar();
        manual = createJMenu("Manual");
        auto = createJMenu("Auto");
        imgSelect = createJMenu("Select Image/s");
        saveOpen = createJMenu("Save/Open");
        mazeSolutions = createToggleButton("Generate With Solution");
        top.add (manual);
        top.add (auto);
        top.add (imgSelect);
        top.add(saveOpen);
        top.add(mazeSolutions);
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
    private JPanel createPanel(Color c){
        JPanel temp = new JPanel();
        temp.setBackground(c);
        return temp;
    }

    private JToggleButton createToggleButton(String str){
        JToggleButton temp = new JToggleButton();
        temp.setFocusable(false);
        temp.setText(str);
        temp.addActionListener(this);
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
        if (src == manGen){ //manual generation
            isFromDB = false; //maze is not pulled from database
            editable = true;
            //create a new panel to determine the cell size
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
            else if (Objects.equals(s, "Large")) {
                size = 20;
            }
            //
            if ((s != null)) {
                isManual = true;
                savedDirections.clear();
                final Rectangle[] r = {pnlDisplay.getBounds()}; // In case window needs to become resizable, this code will accommodate
                Maze[] g = {new Maze(r[0].width, r[0].height)};
                int maxWidthCoord = g[0].maze.maze.length;
                int maxHeightCoord = g[0].maze.maze[0].length;

                pnlDisplay.removeAll();
                pnlDisplay.add(g[0], BorderLayout.CENTER);
                pnlDisplay.setVisible(true);
                currentMaze = g[0];
                Point initial = new Point(1,1);
                savedDirections.add(initial);
                //double for loop deleting the whole maze
                if (pnlDisplay.getMouseListeners().length == 0) {
                    pnlDisplay.addMouseListener(new MouseAdapter() {
                        int n = 0;
                        public void mouseClicked(MouseEvent c) {
                            if (editable) { //check if the maze is editable
                                pnlDisplay.removeAll();
                                int coordX = c.getX() / size; //get the x coordinate of the mouse
                                int coordY = c.getY() / size; //get the y coordinate of the mouse
                                System.out.println(coordX + ", " + coordY + " " + String.valueOf(g[0].maze.maze[coordX][coordY]));
                                System.out.println(pnlDisplay.getMouseListeners().length);
                                boolean genTest = (coordX % 2 == 1 && coordY % 2 == 1);

                                ArrayList<Point>  options =  new ArrayList<>();
                                options.add(new Point(savedDirections.get(n).x, savedDirections.get(n).y + 2));
                                options.add(new Point(savedDirections.get(n).x, savedDirections.get(n).y - 2));
                                options.add(new Point(savedDirections.get(n).x + 2, savedDirections.get(n).y));
                                options.add(new Point(savedDirections.get(n).x - 2, savedDirections.get(n).y));

                                if (String.valueOf(g[0].maze.maze[coordX][coordY]) == "PLACEHOLDER" && coordX != 0 && coordY != 0 && coordX < maxWidthCoord - 1 && coordY < maxHeightCoord - 1 && genTest) {
                                    if (options.contains(new Point (coordX, coordY))) {
                                        System.out.println("L");
                                        if (coordY > savedDirections.get(n).y) {
                                            g[0].maze.maze[coordX][coordY-1] = genAndSolve.state.PATH;
                                            Point temp = new Point(coordX, coordY);
                                            savedDirections.add(temp);
                                        } else if (coordY < savedDirections.get(n).y){
                                            g[0].maze.maze[coordX][coordY +1] = genAndSolve.state.PATH;
                                            Point temp = new Point(coordX, coordY);
                                            savedDirections.add(temp);
                                        }
                                        else if (coordX > savedDirections.get(n).x){
                                            g[0].maze.maze[coordX-1][coordY] = genAndSolve.state.PATH;
                                            Point temp = new Point(coordX, coordY);
                                            savedDirections.add(temp);
                                        }
                                        else if (coordX < savedDirections.get(n).y){
                                            g[0].maze.maze[coordX+1][coordY] = genAndSolve.state.PATH;
                                            Point temp = new Point(coordX, coordY);
                                            savedDirections.add(temp);
                                        }
                                        n++;
                                    }
                                   

                                }
//                                else if (String.valueOf(g[0].maze.maze[coordX][coordY]) == "PATH" && coordX < maxWidthCoord - 1 && coordY < maxHeightCoord - 1  && genTest) {
//                                    g[0].maze.maze[coordX][coordY] = genAndSolve.state.PATH;
//                                }
                                pnlDisplay.removeAll();
                                pnlDisplay.add(g[0], BorderLayout.CENTER);
                                pnlDisplay.revalidate();
                                pnlDisplay.repaint();
                            }
                        }

                    });
                }

                pnlDisplay.revalidate();
                pnlDisplay.repaint();

                //on space - reset
            }

        }
        else if (src == autoGen){
            isFromDB = false;
            isManual = false;
            isReprint = false;
            savedDirections.clear();
            storedXY.clear();
            editable = false;
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
                size = 40;
            }
            else if (Objects.equals(s, "Medium")){
                size = 30;
            }
            else if (Objects.equals(s, "Large")){
                size = 20;
            }

            if ((s != null)){
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
                            savedDirections.clear();
                            storedXY.clear();
                            isReprint = false;
                            loadMaze(false);
                        }

                    }
                });
                pnlDisplay.revalidate();
                pnlDisplay.repaint();
            }
        }
        else if (src == logoAdd){
            isLogo = true;
            isReprint = false;
           genAndSolve.logo = (imageInsert.addImage(0));
            if (UserGUI.size != 0) {
                loadMaze(false);
            }
        }
        else if (src == mazeAdd){
            imageInsert.addImage(1);
        }
        else if (src == imageRemove){
            imageInsert.removeImage();
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
                if (title.getText().length() > 0 && author.getText().length() > 0 && !databaseStorage.retrieveTitles().contains(title.getText()) && validateName(title.getText())) {
                        createScreenshot(title.getText());
                    try {
                        databaseStorage.insertMaze(title.getText(), author.getText(), convert());
                    } catch (SQLException | FileNotFoundException ex) {
                        ex.printStackTrace();
                    }
                }
                else if (currentMaze == null){
                    JOptionPane.showMessageDialog(null, "No currently generated maze", "Error", JOptionPane.INFORMATION_MESSAGE);
                }
                else if (databaseStorage.retrieveTitles().contains(title.getText())){
                    JOptionPane.showMessageDialog(null, "Title already used in database", "Error", JOptionPane.INFORMATION_MESSAGE);
                }
                else if (!validateName(title.getText())) {
                    JOptionPane.showMessageDialog(null, "Invalid Title", "Error", JOptionPane.INFORMATION_MESSAGE);
                    }
                else{
                    String errMsg;
                    if (title.getText().length() == 0 && author.getText().length() > 0){
                        errMsg = "Title";
                    }
                    else if (author.getText().length() == 0 && title.getText().length() > 0){
                        errMsg = "Author";
                    }
                    else{
                        errMsg = "Title & Author";
                    }
                    JOptionPane.showMessageDialog(null, "Missing " + errMsg, "Error", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
        else if (src == mazeSolutions){
            genAndSolve.genSolutions = !genAndSolve.genSolutions;
            if (isFromDB) {
                loadMaze(true);
            }
            else if (size != 0){
                isReprint = true;
                loadMaze(false);
            }
        }
    }

    private boolean validateName(String title){
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



    public String convert(){
        String totalxy = "";
        for (int i = 0; i < savedDirections.size(); i++){
            Point p = savedDirections.get(i);
            String x = String.valueOf(p.x);
            String y = String.valueOf(p.y);
            String xy = x.concat("-" + y);
            totalxy = totalxy.concat(xy +",");
        }
        return totalxy;
    }

    public static void loadMaze(Boolean fromDB){
        if (fromDB){
            size = databaseGUI.returnSize();
        }
        final Rectangle[] r = {pnlDisplay.getBounds()};
        final Maze[] g = {new Maze(r[0].width, r[0].height)};
        if (!isReprint) {
            savedDirections.clear();
            storedXY.clear();
        }
        r[0] = pnlDisplay.getBounds();
        g[0] = new Maze(r[0].width, r[0].height);
        currentMaze = g[0];
        pnlDisplay.add(g[0], BorderLayout.CENTER);
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


    public static boolean returnIsReprint(){
        return isReprint;
    }


    public static int returnSize(){
        return size;
    }


    public static void main (String[] args) throws IOException {
        new UserGUI("MazeCo Computer Assisted Maze Design Tool");
    }


}
