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

public class NewController extends AbstractController {
    @FXML
    public TextField text;

    @FXML
    public TextArea expectedAnswer;

    @FXML
    public Button submit;

    @FXML
    public Text message;

    @FXML
    public TextField hint;

    @FXML
    public VBox form;

    private QuestionTemplateService service;
    private final TableView<QuestionTemplateDTO> listing;

    public NewController(@NotNull TableView<QuestionTemplateDTO> listing) {
        this.listing = listing;
    }

    @Override
    protected void init() {
        service = (QuestionTemplateService) Di.getInstance(QuestionTemplateService.class);
        form.requestFocus();
        submit.setOnAction(this::createQuestion);
    }

    /**
     * Creates a new question based on the values in the form.
     */
    public void createQuestion(ActionEvent actionEvent) {
        try {
            // Get input values.
            String questionText = text.getText();
            String hintText = hint.getText();
            String expectedAnswerText = expectedAnswer.getText();

            // Create question.
            QuestionTemplate question = new QuestionTemplate();
            question.setText(questionText).setHint(hintText).setExpectedAnswer(expectedAnswerText);
            service.save(question);

            QuestionTemplateDTO questionTemplateDTO = new QuestionTemplateDTO();
            questionTemplateDTO.setQuestion(question);
            listing.getItems().add(questionTemplateDTO);

            // Close popup.
            Stage stage = (Stage) submit.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            message.setText(e.getMessage());
        }
    }
}
