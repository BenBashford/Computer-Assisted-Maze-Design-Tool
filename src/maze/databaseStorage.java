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

    public static InputStream tempStorage;

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
                + "	Author text,\n"
                + "	Created text,\n"
                + "	Edited text,\n"
                + "	Image blob NOT NULL,\n"
                + "	Maze text NOT NULL,\n"
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
        File image = new File("src/images/onSave/"+title+".png");
        FileInputStream inputStream = new FileInputStream(image);
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        try {
            assert conn != null;
            try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Mazes (title,author,created,edited,image,maze) VALUES(?,?,datetime('now', 'localtime'),datetime('now', 'localtime'),?,?)")) {
                pstmt.setString(1, title);
                pstmt.setString(2, author);
                pstmt.setBinaryStream(3,  inputStream, (int)(image.length()));
                pstmt.setString(4, maze);
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
        String sql = "SELECT title, author, created, edited, image, maze FROM Mazes";

        try (
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                tempStorage = rs.getBinaryStream("image");
                BufferedImage newImage = ImageIO.read(tempStorage);
                ImageIO.write(newImage, "png", new File("src/images/retrieved/"+rs.getString("title")+".png"));
                ArrayList<String> a = new ArrayList<>();
                a.add(rs.getString("title"));
                a.add(rs.getString("author"));
                a.add(rs.getString("created"));
                a.add(rs.getString("edited"));
                a.add(rs.getString("maze"));
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
        String sql = "SELECT title FROM Mazes";
        ArrayList data = new ArrayList();
        try (
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                data.add(rs.getString("title"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
}
