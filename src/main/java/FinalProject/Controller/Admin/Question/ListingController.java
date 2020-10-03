package FinalProject.Controller.Admin.Question;

import FinalProject.Application.ResourcePaths;
import FinalProject.Controller.AbstractController;
import FinalProject.Di.Di;
import FinalProject.Domain.DTO.QuestionTemplateDTO;
import FinalProject.Service.QuestionTemplateService;
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
    public TableView<QuestionTemplateDTO> listing;

    @FXML
    public TableColumn<QuestionTemplateDTO, Integer> id;

    @FXML
    public Text message;

    private QuestionTemplateService service;

    @Override
    protected void init() {
        service = (QuestionTemplateService) Di.getInstance(QuestionTemplateService.class);
        List<QuestionTemplateDTO> questions = service.getQuestionsDataForListing();

        for (QuestionTemplateDTO question : questions) {
            listing.getItems().add(question);
        }

        listing.getSortOrder().add(id);
        listing.sort();
    }

    /**
     * Displays a popup with a form for adding a new question.
     */
    public void showNewQuestionForm(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(ResourcePaths.NEW_QUESTION));
        NewController controller = new NewController(listing);
        loader.setController(controller);
        showPopup(loader);
    }

    /**
     * Deletes the selected question.
     */
    public void deleteQuestion(ActionEvent actionEvent) {
        QuestionTemplateDTO selected = listing.getSelectionModel().getSelectedItem();

        if (selected == null) {
            message.setText("Please select a question.");
            return;
        }

        service.delete(selected);
        listing.getItems().remove(selected);
    }

    /**
     * Displays a popup with a form prefilled with the selected question details.
     */
    public void showQuestionDetails(ActionEvent actionEvent) {
        QuestionTemplateDTO selected = listing.getSelectionModel().getSelectedItem();

        if (selected == null) {
            message.setText("Please select a question.");
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource(ResourcePaths.EDIT_QUESTION));
        EditController controller = new EditController(listing);
        loader.setController(controller);
        showPopup(loader);
    }
}
