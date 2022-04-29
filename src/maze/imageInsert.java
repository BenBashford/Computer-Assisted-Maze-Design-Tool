package maze;
// THIS CLASS IS TO BE ADDED: EXISTING CODE IS ONLY A ROUGH FRAMEWORK
public class imageInsert {


    public void addImage() {
        int imgType = UserGUI.returnImgType();
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

    public void logoAdd(){
        // This calls genAndSolve
    }

    public void mazeAdd(){
        // This calls genAndSolve
    }

}
