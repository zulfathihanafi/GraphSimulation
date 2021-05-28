package Controllers;


import BackEnd.GraphComponent.MapVertex;
import BackEnd.Simulation.Greedy.BestFirst;
import BackEnd.Simulation.Greedy.Dijkstra;
import BackEnd.map.MapGraph;
import Classes.CurrentLayout;
import Classes.Graph;
import Classes.Layout;
import Classes.Model;
import Tools.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Simulation implements Initializable {

    public ComboBox<String> AlgorithmChooserBox;
    public Text AnswerText;
    public StackPane paneStack1;
    public Button start_button;
    public Text back_text;
    private static MapGraph map = new MapGraph();

    private String textFileDirectory = FirstChooseFile.textFileDirectory;


    Graph graph = new Graph();
    private static int N, C;

    private Map<String,Integer> algorithmInteger;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //comboBox initializer
        String[] algorithm = {"Dijkstra","Best Search","A* Search","Basic Simulation"};
        algorithmInteger = new HashMap<>();
        for(int i=0;i<algorithm.length;i++){
            algorithmInteger.put(algorithm[i],i);
        }
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

            double[] x = new double[N];
            double[] y = new double[N];
            for (int i = 0; i < N; i++) {
                String[] line_i = inText.nextLine().split(" ");
                MapVertex temp = new MapVertex(Double.parseDouble(line_i[0]), Double.parseDouble(line_i[1]), Integer.parseInt(line_i[2]), i); //add Vertex in Map
                map.addVertex(temp);
                x[i] = Integer.parseInt(line_i[0]);
                y[i] = Integer.parseInt(line_i[1]);
                //model.addCell(i,Integer.parseInt(line_i[0])/8, Integer.parseInt(line_i[1])/8);
            }
            map.completeConnect();
            
            //scaling the graph in GUI
            int MaxRange = 600;
            x = Normalizer.minMax(x,MaxRange);
            y = Normalizer.minMax(y,MaxRange);
            for(int i=0;i<N;i++){
                model.addCell(i,x[i],y[i]);
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

    //this method will back to prev page
    public void backTextPressed(MouseEvent event) {
        try {

            map.clear();
            Parent parent = FXMLLoader.load(getClass().getResource("../FXMLFiles/FirstChooseFile.fxml"));
            Scene scene = new Scene(parent);

            //This line gets stage information
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

            window.setScene(scene);
            window.show();

        }catch (IOException e){
            System.out.println("Error"+ e);
        }
    }

    public void startButtonPressed(ActionEvent event) {
        String algorithm = AlgorithmChooserBox.getValue();
        System.out.println(algorithm);
        String[] answer = new String[2];
        int getAlgorithm = algorithmInteger.get(algorithm);
        switch (getAlgorithm){
            case 0 :
                answer = Dijkstra.run(map,C);
                break;

        }
        String text = "";
        for(int i=0;i<answer.length;i++){
            text+=answer[i]+"\n";
        }

        AnswerText.setText(text);

    }
    //File textFile = new File(FirstChooseFile.textFileDirectory);




}
