package FinalProject.Controller;

import FinalProject.Application.ResourcePaths;
import FinalProject.Application.SceneProperties;
import FinalProject.Auth.UserSession;
import FinalProject.Di.Di;
import FinalProject.Domain.DTO.UserDTO;
import FinalProject.Service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;

public class LoginController extends AbstractController {
    @FXML
    public TextField email;

    @FXML
    public PasswordField password;

    @FXML
    public VBox form;

    @FXML
    public Text message;

    /**
     * Attempts to authenticate the user with the provided credentials.
     */
    public void handleLogin(ActionEvent actionEvent) {
        UserService userService = (UserService) Di.getInstance(UserService.class);

        try {
            UserDTO user = userService.findOneByEmailWithPassword(email.getText(), password.getText());
            UserSession.startSession(user);
            redirectToHomePage(user);
        } catch (Exception e) {
            message.setText("Cannot login. Please try again or contact an admin.");
        }
    }

    /**
     * Redirects the user to his/her homepage.
     * <p>
     * The admin homepage is the admin dashboard, the user homepage is the quiz listing.
     */
    public void redirectToHomePage(@NotNull UserDTO user) {
        String path = user.isAdmin() ? ResourcePaths.ADMIN_DASHBOARD : ResourcePaths.CANDIDATE_QUIZ_LISTING;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(path));

        try {
            Parent root = fxmlLoader.load();
            stage.setTitle(SceneProperties.APP_TITLE);
            stage.setScene(new Scene(root, SceneProperties.MAIN_DISPLAY_WIDTH, SceneProperties.MAIN_DISPLAY_HEIGHT));

            stage.show();
        } catch (Exception e) {
            message.setText("Cannot redirect you to your homepage.");
        }
    }

    @Override
    protected void init() {
        form.requestFocus();
    }
}
