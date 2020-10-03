package FinalProject.Controller;

import FinalProject.Application.ResourcePaths;
import FinalProject.Application.SceneProperties;
import FinalProject.Exception.CannotRenderException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public abstract class AbstractController {
    /**
     * The right-side pane where the listings/forms are displayed.
     */
    @FXML
    public Pane view;

    /**
     * The stage corresponding to the current controller.
     */
    protected Stage stage;

    /**
     * The current view (pane) loader.
     */
    protected FXMLLoader viewLoader;

    /**
     * Sets the stage.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initialize() {
        Platform.runLater(this::init);
    }

    /**
     * Replaces the right-side pane with the contents of the FXML file found at the provided location.
     *
     * @param path The file location.
     */
    protected void reloadView(String path) {
        if (view == null) {
            throw new CannotRenderException("Cannot load the view.");
        }

        viewLoader = new FXMLLoader(getClass().getResource(path));

        Parent root;
        try {
            root = viewLoader.load();
        } catch (Exception ex) {
            throw new CannotRenderException("Cannot load the view.");
        }

        view.getChildren().clear();
        view.getChildren().add(root);
    }

    /**
     * Reloads the login page.
     * This is called after logout.
     */
    protected void reloadLogin() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(ResourcePaths.LOGIN));
        Parent root;
        try {
            root = fxmlLoader.load();
        } catch (Exception ex) {
            throw new CannotRenderException("Cannot load the view.");
        }

        stage = new Stage();
        stage.setTitle(SceneProperties.APP_TITLE);
        stage.setScene(new Scene(root));

        LoginController controller = fxmlLoader.getController();
        controller.setStage(stage);

        stage.show();
    }

    /**
     * Displays a popup based on the given path.
     */
    protected void showPopup(String path) {
        FXMLLoader popupLoader = new FXMLLoader(getClass().getResource(path));

        Parent root;
        try {
            root = popupLoader.load();
        } catch (Exception ex) {
            throw new CannotRenderException("Cannot load the view.");
        }

        Stage stage = new Stage();
        stage.setScene(new Scene(root, SceneProperties.POPUP_DISPLAY_WIDTH, SceneProperties.POPUP_DISPLAY_HEIGHT));
        stage.show();
    }

    /**
     * Displays a popup based on the given loader.
     */
    protected void showPopup(FXMLLoader loader) {
        Parent root;
        try {
            root = loader.load();
        } catch (Exception e) {
            throw new CannotRenderException("Cannot load the view.");
        }

        Stage stage = new Stage();
        stage.setScene(new Scene(root, SceneProperties.POPUP_DISPLAY_WIDTH, SceneProperties.POPUP_DISPLAY_HEIGHT));
        stage.show();
    }

    /**
     * Initializes the view with data.
     */
    protected abstract void init();
}
