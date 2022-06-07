package maze;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;


public class databaseStorage {
    private static String url;
    private static String username;
    private static String password;
    private static String schema;

    public static InputStream tempLogo;
    public static InputStream tempStart;
    public static InputStream tempEnd;

    public static ArrayList<Point> test = new ArrayList<>();

    public static void createNewDatabase(String fileName) throws IOException {
        Properties props = new Properties();
        FileInputStream in = null;
        in = new FileInputStream("src/db.props");
        props.load(in);
        url = props.getProperty("jdbc.url");
        username = props.getProperty("jdbc.username");
        password = props.getProperty("jdbc.password");
        schema = props.getProperty("jdbc.schema");

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void create() throws IOException {
        createNewDatabase(schema);
        String sql = "CREATE TABLE IF NOT EXISTS Mazes (\n"
                + "	Title text PRIMARY KEY,\n"
                + "	Author text NOT NULL,\n"
                + "	Created text NOT NULL,\n"
                + "	Edited text NOT NULL,\n"
                + "	Image blob NOT NULL,\n"
                + "	Maze text NOT NULL,\n"
                + " Size text NOT NULL, \n"
                + " Logo blob, \n"
                + " Start blob, \n"
                + " End blob, \n"
                + " LogoPos text, \n"
                + " LogoSize text, \n"
                + "	capacity real\n"
                + ");";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void insertMaze(String title, String author, String maze) throws SQLException, FileNotFoundException {
        File image = new File("src/images/MazeImages/"+title+"-Screenshot.png");
        FileInputStream inputStream = new FileInputStream(image);


        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        try {
            assert conn != null;
            try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Mazes (Title,Author,Created,Edited,Image,Maze,Size,Logo,Start,End,LogoPos,LogoSize) VALUES(?,?,datetime('now', 'localtime'),datetime('now', 'localtime'),?,?,?,?,?,?,?,?)")) {
                pstmt.setString(1, title);
                pstmt.setString(2, author);
                pstmt.setBinaryStream(3,  inputStream, (int)(image.length()));
                pstmt.setString(4, maze);
                pstmt.setString(5, String.valueOf(UserGUI.returnSize()));
                if (imageInsert.returnPath(0) != null) {
                    File logo = new File(imageInsert.returnPath(0));
                    FileInputStream logoStream = new FileInputStream(logo);
                    pstmt.setBinaryStream(6, logoStream, (int) (logo.length()));
                    pstmt.setString(9, genAndSolve.returnLogoCoordinates());
                    pstmt.setString(10, genAndSolve.returnLogoSize());
                }
                if (imageInsert.returnPath(1) != null){
                    File start = new File(imageInsert.returnPath(1));
                    FileInputStream startStream = new FileInputStream(start);
                    pstmt.setBinaryStream(7,  startStream, (int)(start.length()));
                }
                if (imageInsert.returnPath(2) != null){
                    File end = new File(imageInsert.returnPath(2));
                    FileInputStream endStream = new FileInputStream(end);
                    pstmt.setBinaryStream(8,  endStream, (int)(end.length()));
                }
                pstmt.executeUpdate();

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        // Insert maze into database
    }

    public static String[][] retrieveMaze(){
        Connection conn = null;
        ArrayList<ArrayList<String>> data = new ArrayList();
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        String sql = "SELECT Title,Author,Created,Edited,Image,Maze,Size,Logo,Start,End,LogoPos,LogoSize FROM Mazes";

        try (
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                InputStream tempScreenshot = rs.getBinaryStream("Image");
                BufferedImage newScreenshot = ImageIO.read(tempScreenshot);
                ImageIO.write(newScreenshot, "png", new File("src/images/retrieved/DBMazeImages/"+rs.getString("Title")+"-Screenshot.png"));

                tempLogo = rs.getBinaryStream("Logo");
                if (tempLogo != null){
                    BufferedImage newLogo = ImageIO.read(tempLogo);
                    ImageIO.write(newLogo, "png", new File("src/images/retrieved/logos/"+rs.getString("Title")+"Logo.png"));
                }

                tempStart = rs.getBinaryStream("Start");
                if (tempStart != null) {
                BufferedImage newStart = ImageIO.read(tempStart);
                ImageIO.write(newStart, "png", new File("src/images/retrieved/startImages/"+rs.getString("Title")+"Start.png"));
                }

                tempEnd = rs.getBinaryStream("End");
                if (tempEnd != null) {
                BufferedImage newEnd = ImageIO.read(tempEnd);
                ImageIO.write(newEnd, "png", new File("src/images/retrieved/endImages/"+rs.getString("Title")+"End.png"));
                }

                ArrayList<String> a = new ArrayList<>();
                a.add(rs.getString("Title"));
                a.add(rs.getString("Author"));
                a.add(rs.getString("Created"));
                a.add(rs.getString("Edited"));
                a.add(rs.getString("Maze"));
                a.add(rs.getString("Size"));
                a.add(rs.getString("LogoPos"));
                a.add(rs.getString("LogoSize"));
                data.add(a);
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[][] stringArray = data.stream().map(u -> u.toArray(new String[0])).toArray(String[][]::new);
        return stringArray;
        // Called from databaseGUI, retrieves maze
        // Calls MazeGUI to add selected maze to active screen
    }

    public static ArrayList<String> retrieveTitles() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        String sql = "SELECT Title FROM Mazes";
        ArrayList data = new ArrayList();
        try (
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                data.add(rs.getString("Title"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
}
