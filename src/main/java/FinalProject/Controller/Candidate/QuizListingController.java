package FinalProject.Controller.Candidate;

import FinalProject.Application.ResourcePaths;
import FinalProject.Auth.UserSession;
import FinalProject.Controller.AbstractController;
import FinalProject.Di.Di;
import FinalProject.Domain.DTO.QuizTemplateDTO;
import FinalProject.Domain.DTO.UserDTO;
import FinalProject.Service.QuizTemplateService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;

public class QuizListingController extends AbstractController {
    @FXML
    public TableView<QuizTemplateDTO> listing;

    @FXML
    public TableColumn<QuizTemplateDTO, String> name;

    @FXML
    public TableColumn<QuizTemplateDTO, String> category;

    @FXML
    public TableColumn<QuizTemplateDTO, Integer> id;

    @FXML
    public Button logout;

    @FXML
    public Text message;

    @FXML
    public Label pageTitle;

    @FXML
    public TableColumn<QuizTemplateDTO, String> description;

    @Override
    protected void init() {
        UserDTO currentUser = UserSession.getUser();
        QuizTemplateService service = (QuizTemplateService) Di.getInstance(QuizTemplateService.class);
        List<QuizTemplateDTO> quizzesData = service.getQuizzesDataForCandidateListing(currentUser);

        listing.getItems().addAll(quizzesData);

        listing.getSortOrder().add(id);
        listing.sort();
    }

    /**
     * Displays the quiz page.
     */
    public void startQuiz(ActionEvent actionEvent) {
        QuizTemplateDTO selected = listing.getSelectionModel().getSelectedItem();

        if (selected == null) {
            message.setText("Please select a quiz.");
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource(ResourcePaths.CANDIDATE_QUIZ));
        QuizController controller = new QuizController(listing);
        loader.setController(controller);
        showPopup(loader);
    }

    /**
     * Displays the candidate's quiz scores.
     */
    public void viewQuizScores(ActionEvent actionEvent) {
        showPopup(ResourcePaths.CANDIDATE_SCORES);
    }

    /**
     * Logs out the candidate.
     */
    public void logout(ActionEvent actionEvent) {
        UserSession.destroySession();

        stage = (Stage) logout.getScene().getWindow();
        stage.close();

        reloadLogin();
    }
}
