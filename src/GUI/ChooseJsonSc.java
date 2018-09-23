package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import project.BasicInformation;
import project.StaticObjects;

/**
 * @author Nazanin Tafreshi
 */
public class ChooseJsonSc implements Initializable {

    @FXML
    BorderPane borderPane;
    @FXML
    CheckBox appCb, sensorCb, cloudCb;
    @FXML
    Button okButton;
    @FXML
    static Button goToSc2;
    @FXML
    TextField countTxt;

    static Stage stage;
    StaticObjects staticObj=new StaticObjects();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void goToScreen2(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Json Files", "*.json"));
        File file = fileChooser.showOpenDialog(null);
        goToSc2 = (Button) event.getSource();
        
        if (file != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ChooseBoxFXML.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            stage = new Stage();
            stage.setTitle("Select Your Choice !");
            stage.setScene(new Scene(root));
            stage.show();

            BasicInformation.setJsonFile(file.toString());
            // new Manager();
        }
    }

    @FXML
    private void handleOkButtonAction(ActionEvent event) {
        stage.close();
        if (sensorCb.isSelected()) {
            StaticObjects.sensor = true;
        } else {
            StaticObjects.sensor = false;
        }
        if (cloudCb.isSelected()) {
            StaticObjects.cloud = true;
        } else {
            StaticObjects.cloud = false;
        }
        if (appCb.isSelected()) {
            StaticObjects.app = true;
        } else {
            StaticObjects.app = false;
        }
         staticObj.setCycleC(countTxt.getText());
        try {
            //  BasicInformation.setJsonFile(file.toString());
            Parent root = FXMLLoader.load(getClass().getResource("ShowEntityScFXML.fxml"));
            goToSc2.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
