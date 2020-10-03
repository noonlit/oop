package FinalProject.Controller.Admin.Quiz;

import FinalProject.Application.ResourcePaths;
import FinalProject.Controller.AbstractController;
import FinalProject.Di.Di;
import FinalProject.Domain.DTO.QuizTemplateDTO;
import FinalProject.Service.QuizTemplateService;
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
    public TableView<QuizTemplateDTO> listing;

    @FXML
    public TableColumn<QuizTemplateDTO, Integer> id;

    @FXML
    public Text message;

    private QuizTemplateService service;

    @Override
    protected void init() {
        service = (QuizTemplateService) Di.getInstance(QuizTemplateService.class);
        List<QuizTemplateDTO> quizzes = service.getQuizzesDataForAdminListing();

        for (QuizTemplateDTO quiz : quizzes) {
            listing.getItems().add(quiz);
        }

        listing.getSortOrder().add(id);
        listing.sort();
    }

    /**
     * Displays a popup with a form for adding a new quiz.
     */
    public void showNewQuizForm(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(ResourcePaths.NEW_QUIZ));
        NewController controller = new NewController(listing);
        loader.setController(controller);
        showPopup(loader);
    }

    /**
     * Displays a popup with a form prefilled with the selected quiz details.
     */
    public void showQuizDetails(ActionEvent actionEvent) {
        QuizTemplateDTO selected = listing.getSelectionModel().getSelectedItem();

        if (selected == null) {
            message.setText("Please select a quiz.");
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource(ResourcePaths.EDIT_QUIZ));
        EditController controller = new EditController(listing);
        loader.setController(controller);
        showPopup(loader);
    }

    /**
     * Deletes the selected quiz.
     */
    public void deleteQuiz(ActionEvent actionEvent) {
        QuizTemplateDTO selected = listing.getSelectionModel().getSelectedItem();

        if (selected == null) {
            message.setText("Please select a quiz.");
            return;
        }

        service.delete(selected);
        listing.getItems().remove(selected);
    }
}
