package FinalProject.Controller.Admin.Result;

import FinalProject.Controller.AbstractController;
import FinalProject.Di.Di;
import FinalProject.Domain.DTO.QuizInstanceDTO;
import FinalProject.Domain.Entity.QuestionInstance;
import FinalProject.Service.QuizInstanceService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Collection;

public class EditController extends AbstractController {
    private final QuizInstanceDTO selected;
    @FXML
    public VBox form;

    @FXML
    public VBox questions;

    @FXML
    public TextField score;

    @FXML
    public Button submit;

    @FXML
    public Text message;

    private QuizInstanceService service;
    private TableView<QuizInstanceDTO> listing;

    /**
     * Initializes the controller with the selected result.
     */
    public EditController(TableView<QuizInstanceDTO> listing) {
        this.selected = listing.getSelectionModel().getSelectedItem();
        this.listing = listing;
        service = (QuizInstanceService) Di.getInstance(QuizInstanceService.class);
    }

    @Override
    protected void init() {
        Collection<QuestionInstance> questionsData = service.getAssociatedQuestions(selected.getQuiz());

        // Display result data as read-only. The only editable value here is the score.
        int index = 1;
        for (QuestionInstance question : questionsData) {
            Label questionText = new Label();
            questionText.setText(index + ". " + question.getText());
            index++;

            Label answerText = new Label();
            answerText.setText("Given answer: " + question.getAnswer());

            Label expectedAnswer = new Label();
            expectedAnswer.setText("Expected answer: " + question.getExpectedAnswer());

            Separator separator = new Separator();

            VBox wrapper = new VBox();
            wrapper.getChildren().addAll(questionText, answerText, expectedAnswer, separator);
            wrapper.setSpacing(10);
            questions.getChildren().add(wrapper);
        }

        if (selected.getScore() != null) {
            score.setText(selected.getScore().toString());
        }

        form.requestFocus();
        submit.setOnAction(this::submitQuiz);
    }

    /**
     * Updates selected quiz instance with the entered score.
     */
    private void submitQuiz(ActionEvent actionEvent) {
        try {
            // Get score.
            int scoreValue;
            try {
                scoreValue = Integer.parseInt(score.getText());
            } catch (Exception e) {
                message.setText("The score must be a value between 0 and 10!");
                return;
            }

            selected.getQuiz().setScore(scoreValue);

            // Save update.
            QuizInstanceService quizInstanceService = (QuizInstanceService) Di.getInstance(QuizInstanceService.class);
            quizInstanceService.save(selected);

            listing.refresh();

            // Close popup.
            Stage stage = (Stage) submit.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            message.setText(e.getMessage());
        }
    }
}