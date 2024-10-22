package maze;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Stack;

public class genAndSolve {
    public static boolean genSolutions;
    private static int rows;
    private static int columns;
    private int cellSize;

    private static int randomX;
    private static int randomY;
    private int xLowerBound;
    private int xUpperBound;
    private int yLowerBound;
    private int yUpperBound;

    public enum state {
        WALL,
        PATH,
        PLACEHOLDER,
        SOLUTION,
        IMAGE,
    }


    public static ImageIcon mazeStartImage;
    public static ImageIcon mazeEndImage;
    public static ImageIcon logo;

    public static int imgSize;

    // main data structure
    public static state[][] maze;

    // start and end of maze
    private Point start;
    private Point end;

    private static int ranX;
    private static int ranY;


    public genAndSolve(int width, int height, int size) {
        imgSize = 0;
        size = Math.abs(size);
        columns = Math.abs(width) / size;
        rows = Math.abs(height) / size;
        columns = columns - 1 + columns % 2;
        rows = rows - 1 + rows % 2;
        cellSize = size;
        start = new Point(0, 1);
        end = new Point(columns - 1, rows - 2);

        xLowerBound = (3);
        xUpperBound = (columns-7);
        yLowerBound = (3);
        yUpperBound = (rows-7);
        if (UserGUI.isFromDB && databaseGUI.hasLogo){
            randomX = databaseGUI.returnLogoPos(0);
            randomY = databaseGUI.returnLogoPos(1);
        }
        else if(UserGUI.isReloaded && UserGUI.isLogo){
            randomX = UserGUI.reloadImageX;
            randomY = UserGUI.reloadImageY;
        }
        else{
            randomX = xLowerBound + 2*(int)(Math.random()*((xUpperBound-xLowerBound)/2+1));
            randomY = yLowerBound + 2*(int)(Math.random()*((yUpperBound-yLowerBound)/2+1));
        }


            configure();
        if (!UserGUI.isReloaded) {
            generate();
        }
        if (genSolutions) {
            solve();
        }
    }

    private static Point checkNext(Point current, state target, int dist) {

        final int n = 4; // number of neighbors

        // the options of cells
        Point[] options = { new Point(current.x, current.y + dist), new Point(current.x, current.y - dist),
                new Point(current.x + dist, current.y), new Point(current.x - dist, current.y) };
        Point[] testOptions = { new Point(current.x, current.y + dist/2), new Point(current.x, current.y - dist/2),
                new Point(current.x + dist/2, current.y), new Point(current.x - dist/2, current.y) };

        boolean[] goodIndices = new boolean[n]; // the options
        int nGood = 0; // number of good

        for (int i = 0; i < n; i++) {
            Point c = options[i];
            Point t = testOptions[i];

            boolean good = (c.x >= 0) && (c.x < columns) && (c.y >= 0) && (c.y < rows) && (maze[c.x][c.y] == target);
            boolean test = (c.x >= 0) && (c.x < columns) && (c.y >= 0) && (c.y < rows) && (maze[t.x][t.y] != state.IMAGE);
            if (test) {
                goodIndices[i] = good;
                if (good)
                    nGood++;
            }

        }
        if (nGood == 0)
            return null; // if there are no neighbors


        int rand = (int) (Math.random() * n);
        while (!goodIndices[rand]) {
            rand = (int) (Math.random() * n);
        }
        Point temp = options[rand];



        return temp; // return the random neighbor

    }

    private void configure() {
        maze = new state[columns][rows];

        int i, j;
        if (UserGUI.isReloaded) {
            int n = 0;
            int maxWidthCoord = genAndSolve.maze.length;
            int maxHeightCoord = genAndSolve.maze[0].length;
            for (i = 0; i < maxWidthCoord; i++) { //x coordinate
                for (j = 0; j < maxHeightCoord; j++) { //y coordinate
                    maze[i][j] = genAndSolve.state.valueOf(UserGUI.reloadStorage.get(n)); //place walls around the border
                    n++;
                }
            }
        }
        else{
            for (i = 0; i < columns; i++) {
                maze[i][0] = state.WALL;
                maze[i][rows - 1] = state.WALL;
            }

            for (j = 0; j < rows; j++) {
                maze[0][j] = state.WALL;
                maze[columns - 1][j] = state.WALL;
            }

            for (i = 1; i < columns - 1; i += 2) {
                for (j = 1; j < rows - 1; j += 2) {
                    maze[i][j] = state.PLACEHOLDER;
                    maze[i + 1][j] = state.WALL;
                    maze[i][j + 1] = state.WALL;
                }
            }

            for (i = 2; i < columns - 2; i += 2) {
                for (j = 2; j < rows - 2; j += 2) {
                    maze[i][j] = state.WALL;
                }
            }


            if (UserGUI.isLogo) {
                if (imageInsert.logoSize == 1) {
                    maze[randomX][randomY] = state.IMAGE;
                    imgSize = 1; // Number of PLACEHOLDER states replaced with IMAGE states
                } else if (imageInsert.logoSize == 3) {
                    int x, y;
                    for (x = 0; x < 3; x++) {
                        for (y = 0; y < 3; y++) {
                            maze[randomX + x][randomY + y] = state.IMAGE;
                        }
                    }
                    imgSize = 4;
                } else if (imageInsert.logoSize == 5) {
                    int x, y;
                    for (x = 0; x < 5; x++) {
                        for (y = 0; y < 5; y++) {
                            maze[randomX + x][randomY + y] = state.IMAGE;
                        }
                    }
                    imgSize = 9;
                }
            }
        }
    }

    private void generate() {
        Point current, next;
        Stack<Point> history = new Stack<Point>();

        int nToVisit;
        nToVisit = ((columns - 1) * (rows - 1) / 4) - imgSize ;
        int nVisited = 1;

        current = new Point(start.x + 1, start.y);
        maze[current.x][current.y] = state.PATH;

        while (nVisited < nToVisit) {
            next = checkNext(current, state.PLACEHOLDER, 2);
                if (next != null) {
                    int x = (current.x + next.x) / 2;
                    int y = (current.y + next.y) / 2;
                    maze[x][y] = state.PATH;

                    history.push(current);
                    current = next;
                    maze[current.x][current.y] = state.PATH;

                    nVisited++;
                } else if (!history.empty()) {
                    current = history.pop();
                }
        }
    }

    private void solve() {
        Point current, next;
        Stack<Point> history = new Stack<Point>();

        current = new Point(start.x + 1, start.y);
        maze[current.x][current.y] = state.SOLUTION;
        while (current.x != end.x -1 || current.y != end.y) {
            next = checkNext(current, state.PATH, 1);
            if (next != null) {
                history.push(current);
                current = next;
                maze[current.x][current.y] = state.SOLUTION;

            } else if (!history.empty()) {
                maze[current.x][current.y] = state.PLACEHOLDER;
                current = history.pop();
            }
            else{
                maze[start.x + 1][start.y] = state.PATH;
                JOptionPane.showMessageDialog(null, "Maze Has No Solution", "Error", JOptionPane.INFORMATION_MESSAGE);
                break;
            }

        }
    }



    public void draw(Graphics g) throws IOException {
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                state state_g = maze[i][j];
                Color colour;

                int x = i * cellSize;
                int y = j * cellSize;

                switch (state_g) {
                    case PLACEHOLDER:
                    case PATH:
                        colour = Color.WHITE;
                        break;
                    case SOLUTION:
                        colour = Color.BLUE;
                        break;
                    default:
                        colour = Color.BLACK;
                        break;
                }

                if (i == start.x && j == start.y) {
                    if (mazeStartImage == null){
                        colour = Color.GREEN;
                    }
                } else if (i == end.x && j == end.y) {
                    if (mazeEndImage == null){
                        colour = Color.RED;
                    }

                }
                g.setColor(colour);
                g.fillRect(x, y, cellSize, cellSize);

            }
        }
        if (logo != null && !UserGUI.isFromDB) {
            Image image;
            int n;
            image = logo.getImage();
            n = imageInsert.logoSize;
            ranX = randomX * cellSize;
            ranY = randomY * cellSize;
            g.drawImage(image, ranX, ranY, cellSize * n, cellSize * n, null);
        }

        if (UserGUI.isFromDB && databaseGUI.hasLogo) {
            Image image;
            int n;
            ImageIcon icon = new ImageIcon("src/images/retrieved/logos/"+databaseGUI.returnTitle()+"Logo.png");
            image = icon.getImage();
            n = databaseGUI.returnLogoSize();
            ranX = randomX * cellSize;
            ranY = randomY * cellSize;
            g.drawImage(image, ranX, ranY, cellSize * n, cellSize * n, null);
        }

        if (mazeStartImage != null) {
            Image image = mazeStartImage.getImage();
            g.drawImage(image, (start.x+1)*cellSize, start.y*cellSize, cellSize, cellSize, null );
        }
        if (mazeEndImage != null) {
            Image image = mazeEndImage.getImage();
            g.drawImage(image, (end.x+-1) * cellSize, end.y * cellSize, cellSize, cellSize, null);
        }
    }

    public static String returnLogoCoordinates(){
        String x = String.valueOf(randomX);
        String y = String.valueOf(randomY);
        return x.concat("-" + y);
    }

    public static String returnLogoSize(){
        return String.valueOf(imageInsert.logoSize);
    }
}
