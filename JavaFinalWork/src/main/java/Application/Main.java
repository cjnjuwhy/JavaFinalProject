package Application;
	
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    final Controller controller = new Controller();
    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setScene(new Scene(controller.initializeGUI()));
        controller.initializeGame();
        
        primaryStage.setResizable(false);
        primaryStage.setTitle("葫芦娃大战妖精 @151271022 Huanyu Wang");
        primaryStage.show();
        controller.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
