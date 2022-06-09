package maze;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Objects;


public class imageInsert {

    public static String logoPath = null;
    public static String startPath = null;
    public static String endPath = null;

    public static int logoSize;

    public static ImageIcon addImage(int imgType) {
        if (!UserGUI.isFromDB && UserGUI.size != 0) {
            try {
                JFileChooser file = new JFileChooser();
                file.setCurrentDirectory(new File(System.getProperty("user.home")));
                //filtering files
                FileNameExtensionFilter filter = new FileNameExtensionFilter("*.Images", "jpg", "png");
                file.addChoosableFileFilter(filter);
                int res = file.showSaveDialog(null);
                //if the user clicks on save in Jfilechooser
                if (res == JFileChooser.APPROVE_OPTION) {

                    File selFile = file.getSelectedFile();
                    String path = selFile.getAbsolutePath();

                    BufferedImage inputImage = ImageIO.read(new File(path));
                    if (imgType == 0) {
                        JPanel myPanel = new JPanel();
                        myPanel.add(new JLabel("Size:"));
                        Object[] possibilities = {"1x1", "3x3", "5x5"};
                        String s = (String) JOptionPane.showInputDialog(
                                myPanel,
                                "Please Select Image Size:\n",
                                "Size Selector",
                                JOptionPane.PLAIN_MESSAGE, null,
                                possibilities,
                                "ham");
                        if (Objects.equals(s, "1x1")) {
                            logoSize = 1;
                        } else if (Objects.equals(s, "3x3")) {
                            logoSize = 3;
                        } else if (Objects.equals(s, "5x5")) {
                            logoSize = 5;
                        }

                        logoPath = path;
                        return (new ImageIcon(inputImage));
                    } else if (imgType == 1) {
                        JPanel myPanel = new JPanel();
                        myPanel.add(new JLabel("Size:"));


                        Object[] possibilities = {"Start", "End"};
                        String s = (String) JOptionPane.showInputDialog(
                                myPanel,
                                "Add Image as Start or End Icon:\n",
                                "Start / End",
                                JOptionPane.PLAIN_MESSAGE, null,
                                possibilities,
                                "ham");
                        if (Objects.equals(s, "Start")) {
                            genAndSolve.mazeStartImage = (new ImageIcon(inputImage));
                            startPath = path;
                        } else if (Objects.equals(s, "End")) {
                            endPath = path;
                            genAndSolve.mazeEndImage = (new ImageIcon(inputImage));
                        }

                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (UserGUI.isFromDB){
            JOptionPane.showMessageDialog(null, "Adding Image/s to Stored Maze is Not Currently Supported", "Error", JOptionPane.INFORMATION_MESSAGE);
        }
        else if (UserGUI.size == 0){
            JOptionPane.showMessageDialog(null, "Please Generate a Maze First", "Error", JOptionPane.INFORMATION_MESSAGE);
        }
        return null;
    }

    public static void removeImage() {
        if (!UserGUI.isFromDB && UserGUI.size != 0) {
            JPanel myPanel = new JPanel();
            myPanel.add(new JLabel("Image Remover:"));


            Object[] possibilities = {"Start", "End", "Logo"};
            String s = (String) JOptionPane.showInputDialog(
                    myPanel,
                    "Please Select Image to Remove:\n",
                    "Image Remover",
                    JOptionPane.PLAIN_MESSAGE, null,
                    possibilities,
                    "ham");

            if (Objects.equals(s, "Start")) {
                if (genAndSolve.mazeStartImage == null) {
                    JOptionPane.showMessageDialog(null, "No Start Image To Remove", "Error", JOptionPane.INFORMATION_MESSAGE);
                }
                else{
                    genAndSolve.mazeStartImage = null;
                    startPath = null;
                }
            } else if (Objects.equals(s, "End")) {
                if (genAndSolve.mazeEndImage == null) {
                    JOptionPane.showMessageDialog(null, "No End Image To Remove", "Error", JOptionPane.INFORMATION_MESSAGE);
                }
                else{
                    genAndSolve.mazeEndImage = null;
                    endPath = null;
                }
            } else if (Objects.equals(s, "Logo")) {
                if (genAndSolve.logo == null) {
                    JOptionPane.showMessageDialog(null, "No Logo To Remove", "Error", JOptionPane.INFORMATION_MESSAGE);
                }
                else{
                    genAndSolve.logo = null;
                    UserGUI.isLogo = false;
                    logoPath = null;
                }

            }
        }
        else if (UserGUI.isFromDB){
            JOptionPane.showMessageDialog(null, "Removing Image/s from Stored Maze is Not Currently Supported", "Error", JOptionPane.INFORMATION_MESSAGE);
        }
        else if (UserGUI.size == 0){
            JOptionPane.showMessageDialog(null, "Please Generate a Maze First", "Error", JOptionPane.INFORMATION_MESSAGE);
        }

    }

    public static String returnPath(Integer n) {
        if (n == 0) {
            return logoPath;
        }
        else if (n == 1) {
            return startPath;
        }
        else{
            return endPath;
        }

    }
}
