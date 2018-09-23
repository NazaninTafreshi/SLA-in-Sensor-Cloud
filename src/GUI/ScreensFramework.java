/*
 * To change this license header, choose License Headers in ScreensFramework Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/*
* author Nazanin Tafreshi
 */
public class ScreensFramework extends Application {


    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("ChooseJsonScFXML.fxml"));

        Scene scene = new Scene(root,900, 600);
        stage.setTitle("Sensor Cloud_nMT");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        launch(args);
    }


}
