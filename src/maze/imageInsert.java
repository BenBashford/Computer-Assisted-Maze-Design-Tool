package maze;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

// THIS CLASS IS TO BE ADDED: EXISTING CODE IS ONLY A ROUGH FRAMEWORK
public class imageInsert {
    private static String imgLogo;

    public static ImageIcon addImage(int imgType) {
        imgLogo = imgSelect();
        try {
            if (imgType == 0) {
                JFileChooser file = new JFileChooser();
                file.setCurrentDirectory(new File(System.getProperty("user.home")));
                //filtering files
                FileNameExtensionFilter filter = new FileNameExtensionFilter("*.Images","jpg","png");
                file.addChoosableFileFilter(filter);
                int res = file.showSaveDialog(null);
                //if the user clicks on save in Jfilechooser
                if(res == JFileChooser.APPROVE_OPTION){
                    File selFile = file.getSelectedFile();
                    String path = selFile.getAbsolutePath();
                    BufferedImage inputImage = ImageIO.read(new File(path));
                    int scaledWidth = 100;
                    int scaledHeight = 100;
                    BufferedImage outputImage = new BufferedImage(scaledWidth,
                            scaledHeight, inputImage.getType());

                    // scales the input image to the output image
                    Graphics2D g2d = outputImage.createGraphics();
                    g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
                    g2d.dispose();

                    // This needs to add to the Maze rather than just displaying in a JTextArea, which is temporary and for testing. I don't know how to exclude a section of cells from the maze generation code, but that would need to be done before the image can be added.
                    return (new ImageIcon(outputImage));
                }

            }
            else if (imgType == 1) {
                // Add image as maze element
                mazeAdd();
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
