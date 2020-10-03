package FinalProject;

import FinalProject.Application.ResourcePaths;
import FinalProject.Application.SceneProperties;
import FinalProject.Controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(ResourcePaths.LOGIN));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle(SceneProperties.APP_TITLE);
        primaryStage.setScene(new Scene(root));

        LoginController controller = fxmlLoader.getController();
        controller.setStage(primaryStage);

        primaryStage.show();
    }
}