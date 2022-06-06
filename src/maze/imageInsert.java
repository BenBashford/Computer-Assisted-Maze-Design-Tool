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
    public static int logoSize;
    public static ImageIcon addImage(int imgType) {
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

                    return (new ImageIcon(inputImage));
                }
                else if (imgType == 1) {
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
                    } else if (Objects.equals(s, "End")) {
                        genAndSolve.mazeEndImage = (new ImageIcon(inputImage));
                    }

                }

            }

        }
        catch(Exception e){
            // Error message
        }
        return null;
    }

    public static void removeImage() {
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

        // These sizes are arbitrary, if you find a way to make it so the overall maze dimensions don't change when changing maze size please do so.
        if (Objects.equals(s, "Start")) {
            genAndSolve.mazeStartImage = null;
        } else if (Objects.equals(s, "End")) {
            genAndSolve.mazeEndImage = null;
        } else if (Objects.equals(s, "Logo")) {
            genAndSolve.logo = null;
            UserGUI.isLogo = false;
        }
    }
}
