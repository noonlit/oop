package FinalProject.Controller.Candidate;

import FinalProject.Auth.UserSession;
import FinalProject.Controller.AbstractController;
import FinalProject.Di.Di;
import FinalProject.Domain.DTO.QuizInstanceDTO;
import FinalProject.Domain.DTO.UserDTO;
import FinalProject.Service.QuizInstanceService;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.Collection;

public class QuizScoresController extends AbstractController {
    @FXML
    public TableView<QuizInstanceDTO> listing;

    @FXML
    public TableColumn<QuizInstanceDTO, Integer> id;

    @FXML
    public TableColumn<QuizInstanceDTO, String> name;

    @FXML
    public TableColumn<QuizInstanceDTO, String> categoryLabel;

    @FXML
    public TableColumn<QuizInstanceDTO, Integer> score;

    @Override
    public void init() {
        UserDTO currentUser = UserSession.getUser();
        QuizInstanceService service = (QuizInstanceService) Di.getInstance(QuizInstanceService.class);

        // Display all candidate scores.
        Collection<QuizInstanceDTO> quizzesData = service.getQuizzesDataForUserScoresListing(currentUser);
        listing.getItems().addAll(quizzesData);
    }
}
