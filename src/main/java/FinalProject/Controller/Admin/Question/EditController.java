package FinalProject.Controller.Admin.Question;

import FinalProject.Controller.AbstractController;
import FinalProject.Di.Di;
import FinalProject.Domain.DTO.QuestionTemplateDTO;
import FinalProject.Domain.Entity.QuestionTemplate;
import FinalProject.Service.QuestionTemplateService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

public class EditController extends AbstractController {
    @FXML
    public VBox form;

    @FXML
    public TextField text;

    @FXML
    public TextArea expectedAnswer;

    @FXML
    public TextField hint;

    @FXML
    public Button submit;

    @FXML
    public Text message;

    private final QuestionTemplateService service;
    private final TableView<QuestionTemplateDTO> listing;
    private final QuestionTemplateDTO selected;

    /**
     * Initializes the controller with the selected question.
     */
    public EditController(@NotNull TableView<QuestionTemplateDTO> listing) {
        this.selected = listing.getSelectionModel().getSelectedItem();
        this.listing = listing;
        service = (QuestionTemplateService) Di.getInstance(QuestionTemplateService.class);
    }

    @Override
    protected void init() {
        text.setText(selected.getText());
        hint.setText(selected.getHint());
        expectedAnswer.setText(selected.getExpectedAnswer());
        form.requestFocus();

        submit.setOnAction(this::updateQuestion);
    }

    /**
     * Updates selected question with current form data.
     */
    private void updateQuestion(ActionEvent actionEvent) {
        try {
            // Get input values.
            String questionText = text.getText();
            String hintText = hint.getText();
            String expectedAnswerText = expectedAnswer.getText();

            // Update question.
            QuestionTemplate question = selected.getQuestion();
            question.setText(questionText).setHint(hintText).setExpectedAnswer(expectedAnswerText);
            service.update(question);

            listing.refresh();

            // Close popup.
            Stage stage = (Stage) submit.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            message.setText(e.getMessage());
        }
    }
}
