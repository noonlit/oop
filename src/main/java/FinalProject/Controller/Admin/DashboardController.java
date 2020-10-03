package FinalProject.Controller.Admin;

import FinalProject.Application.ResourcePaths;
import FinalProject.Auth.UserSession;
import FinalProject.Controller.AbstractController;
import FinalProject.Domain.DTO.UserDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * The controller for the admin dashboard screen.
 */
public class DashboardController extends AbstractController {
    /**
     * The greeting displayed for the admin user.
     */
    @FXML
    public Label greeting;

    @FXML
    public Button logout;

    @Override
    protected void init() {
        UserDTO user = UserSession.getUser();
        greeting.setText("Welcome, " + user.getEmail());
    }

    /**
     * Displays all the quizzes found in the application.
     */
    public void showQuizListing(ActionEvent actionEvent) {
        reloadView(ResourcePaths.QUIZ_LISTING);
    }

    /**
     * Displays all the questions found in the application.
     */
    public void showQuestionListing(ActionEvent actionEvent) {
        reloadView(ResourcePaths.QUESTION_LISTING);
    }

    /**
     * Displays all the users found in the application.
     */
    public void showUserListing(ActionEvent actionEvent) {
        reloadView(ResourcePaths.USER_LISTING);
    }

    /**
     * Displays all the quiz results for quizzes submitted so far.
     */
    public void showCandidateResults(ActionEvent actionEvent) {
        reloadView(ResourcePaths.CANDIDATE_RESULTS);
    }

    public void logout(ActionEvent actionEvent) {
        UserSession.destroySession();

        stage = (Stage) logout.getScene().getWindow();
        stage.close();

        reloadLogin();
    }
}
