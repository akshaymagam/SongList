package Song.app;


import javafx.application.Application;
import Song.view.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SongLib extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Song/view/song.fxml"));
        SplitPane root = (SplitPane) loader.load();

        Controller controller = loader.getController();
        controller.start(primaryStage);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();


    }

    public static void main(String[] args) {
        launch(args);

    }

}