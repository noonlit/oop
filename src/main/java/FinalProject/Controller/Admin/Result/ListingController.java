package FinalProject.Controller.Admin.Result;

import FinalProject.Application.ResourcePaths;
import FinalProject.Controller.AbstractController;
import FinalProject.Di.Di;
import FinalProject.Domain.DTO.QuizInstanceDTO;
import FinalProject.Domain.Entity.User;
import FinalProject.Service.QuizInstanceService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;

import java.util.List;

public class ListingController extends AbstractController {
    @FXML
    public Label pageTitle;

    @FXML
    public TableView<QuizInstanceDTO> listing;

    @FXML
    public Text message;

    @FXML
    public TableColumn<QuizInstanceDTO, Integer> id;
    
    @FXML
    public TableColumn<QuizInstanceDTO, String> name;

    @FXML
    public TableColumn<QuizInstanceDTO, String> category;

    @FXML
    public TableColumn<QuizInstanceDTO, Integer> score;

    @FXML
    TableColumn<QuizInstanceDTO, User> user;

    @Override
    protected void init() {
        QuizInstanceService service = (QuizInstanceService) Di.getInstance(QuizInstanceService.class);
        List<QuizInstanceDTO> quizzes = service.getQuizzesDataForAdminListing();

        for (QuizInstanceDTO quiz : quizzes) {
            listing.getItems().add(quiz);
        }

        listing.getSortOrder().add(id);
        listing.sort();
    }

    /**
     * Displays a popup with a form prefilled with the selected result details.
     */
    public void showResultDetails(ActionEvent actionEvent) {
        QuizInstanceDTO selected = listing.getSelectionModel().getSelectedItem();

        if (selected == null) {
            message.setText("Please select a quiz.");
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource(ResourcePaths.EDIT_RESULT));
        EditController controller = new EditController(listing);
        loader.setController(controller);
        showPopup(loader);
    }
}