package maze;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
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
                    return (new ImageIcon(path));
                }
                logoAdd();
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
