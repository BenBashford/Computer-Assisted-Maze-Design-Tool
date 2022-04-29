package maze;

import javax.swing.*;

// THIS CLASS IS TO BE ADDED: EXISTING CODE IS ONLY A ROUGH FRAMEWORK
public class imageInsert {
    private static String imgLogo;

    public static void addImage(int imgType) {
        imgLogo = imgSelect();
        try {
            if (imgType == 0) {
                // Add image as logo
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
