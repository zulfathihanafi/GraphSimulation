package Controllers;


import Classes.CurrentLayout;
import Classes.Graph;
import Classes.Layout;
import Classes.Model;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Simulation implements Initializable {

    public ComboBox<String> AlgorithmChooserBox;
    public Text AnswerText;
    public StackPane paneStack1;


    private String textFileDirectory = FirstChooseFile.textFileDirectory;


    Graph graph = new Graph();
    private static int N, C;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //comboBox initializer
        String[] algorithm = {"Dijkstra","Best Search","A* Search","Basic Simulation"};
        AlgorithmChooserBox.getItems().setAll(algorithm);



        //for the graph below
        BorderPane BorderRoot = new BorderPane();

        graph = new Graph();

        BorderRoot.setCenter(graph.getScrollPane());

        Scene scene = new Scene(BorderRoot);
        scene.getStylesheets().add(getClass().getResource("../Classes/whitebg.css").toExternalForm());



        addGraphComponents();

        Layout layout = new CurrentLayout(graph);
        layout.execute();

        paneStack1.getChildren().add(BorderRoot);




    }



    private void addGraphComponents() {
        Model model = graph.getModel();

        graph.beginUpdate();
        //addcell (id,x,y)
        //model.addCell(1,50,50);





        try {
            Scanner inText = new Scanner(new FileInputStream(textFileDirectory));

            String[] firstLine = inText.nextLine().split(" ");
            N = Integer.parseInt(firstLine[0]); //N represents the number of locations in the text file
            C = Integer.parseInt(firstLine[1]);

            for (int i = 0; i < N; i++) {
                String[] line_i = inText.nextLine().split(" ");
                model.addCell(i,Integer.parseInt(line_i[0])/8, Integer.parseInt(line_i[1])/8);
            }
            for(int i=0;i<N;i++){
                for(int j=0;j<N;j++){
                    if(i!=j){
                        model.addEdge(i,j);
                    }
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        }




        graph.endUpdate();
    }
    //File textFile = new File(FirstChooseFile.textFileDirectory);


}
