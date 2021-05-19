package Controllers;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class FirstChooseFile {

    public Text BrowseHyperlink;
    public Button SubmitButton;
    public TextField DirTextField;
    public static String textFileDirectory;
    @FXML
    void BrowseClicked(MouseEvent event) {
        FileChooser chooser = new FileChooser();
        File selectedFile = chooser.showOpenDialog(null);

            File existDir = new File("D:\\VSCode\\GraphSimulation\\Sample");
            chooser.setInitialDirectory(existDir);


        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt");
        chooser.getExtensionFilters().add(extensionFilter);


        if(selectedFile != null){
            DirTextField.setText(selectedFile.getAbsolutePath());
            textFileDirectory = selectedFile.getAbsolutePath();
        }else{

        }
    }

    public void SubmitButtonPressed(ActionEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("../FXMLFiles/Simulation.fxml"));
            Scene scene = new Scene(parent);

            //This line gets stage information
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

            window.setScene(scene);
            window.show();
        }catch (IOException e){
            System.out.println("Error"+ e);
        }
    }
}
