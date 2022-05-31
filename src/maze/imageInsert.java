package maze;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Objects;

// THIS CLASS IS TO BE ADDED: EXISTING CODE IS ONLY A ROUGH FRAMEWORK
public class imageInsert {
    public static int logoSize;
    public static String position;
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
                // Some of the below code may be unnecessary, refactor !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                File selFile = file.getSelectedFile();
                String path = selFile.getAbsolutePath();
                BufferedImage inputImage = ImageIO.read(new File(path));
//                int scaledWidth = 100;
//                int scaledHeight = 100;
//                BufferedImage outputImage = new BufferedImage(scaledWidth,
//                        scaledHeight, inputImage.getType());
//
//                // scales the input image to the output image
//                Graphics2D g2d = outputImage.createGraphics();
//                g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
//                g2d.dispose();
                if (imgType == 0) {
                    // This needs to add to the Maze rather than just displaying in a JTextArea,
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

    public static void logoAdd(){
        // This calls genAndSolve
    }

    public static void mazeAdd(){
        // This calls genAndSolve
    }

    public static String imgSelect(){
        // This opens a gui element to select an image from files
        // str tempSrc = GUI output
        // ImageIcon temp = new ImageIcon(tempSrc);
        // return temp;
        return null;
    }

}
