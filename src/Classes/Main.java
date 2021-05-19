package Classes;

import javafx.application.Application;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main extends Application {
    private static int N, C;
    private static final String path = "Sample\\Sample3.txt";
    Graph graph = new Graph();

    @Override
    public void start(Stage primaryStage) throws Exception{
        BorderPane root = new BorderPane();

        graph = new Graph();

        root.setCenter(graph.getScrollPane());

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("whitebg.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();

        addGraphComponents();

        Layout layout = new CurrentLayout(graph);
        layout.execute();
    }

    private void addGraphComponents() {
        Model model = graph.getModel();

        graph.beginUpdate();
        //addcell (id,x,y)
        //model.addCell(1,50,50);





        try {
            Scanner inText = new Scanner(new FileInputStream(path));

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


    public static void main(String[] args) {
        launch(args);
    }
}
