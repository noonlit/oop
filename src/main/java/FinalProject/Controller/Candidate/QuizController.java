package FinalProject.Controller.Candidate;

import FinalProject.Auth.UserSession;
import FinalProject.Controller.AbstractController;
import FinalProject.Di.Di;
import FinalProject.Domain.DTO.QuizInstanceDTO;
import FinalProject.Domain.DTO.QuizTemplateDTO;
import FinalProject.Domain.Entity.QuestionInstance;
import FinalProject.Domain.Entity.QuestionTemplate;
import FinalProject.Domain.Entity.QuizInstance;
import FinalProject.Service.QuizInstanceService;
import FinalProject.Service.QuizTemplateService;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Collection;

public class QuizController extends AbstractController {
    @FXML
    public VBox questions;

    @FXML
    public Button submit;

    @FXML
    public Text message;

    private QuizTemplateDTO selected;
    private TableView<QuizTemplateDTO> listing;

    /**
     * Initializes the controller with the selected quiz.
     */
    public QuizController(TableView<QuizTemplateDTO> listing) {
        this.listing = listing;
        this.selected = listing.getSelectionModel().getSelectedItem();
    }

    @Override
    protected void init() {
        QuizTemplateService service = (QuizTemplateService) Di.getInstance(QuizTemplateService.class);
        Collection<QuestionTemplate> questionsData = service.getAssociatedQuestions(selected.getQuiz());

        for (QuestionTemplate question : questionsData) {
            Label id = new Label();
            id.setText(question.getId().toString());
            id.setId("question_id");
            id.setVisible(false);

            Label questionText = new Label();
            questionText.setId("question_text");
            questionText.setText(question.getText());

            TextArea answerField = new TextArea();
            answerField.setId("answer");
            answerField.setPromptText(question.getHint());
            answerField.setWrapText(true);

            Label expectedAnswer = new Label();
            expectedAnswer.setId("expected_answer");
            expectedAnswer.setText(question.getExpectedAnswer());
            expectedAnswer.setVisible(false);

            Separator separator = new Separator();

            VBox wrapper = new VBox();
            wrapper.setId("questions_wrapper");
            wrapper.getChildren().addAll(id, questionText, answerField, expectedAnswer, separator);
            wrapper.setSpacing(10);
            questions.getChildren().add(wrapper);
        }

        submit.setOnAction(this::submitQuiz);
    }

    /**
     * Gather quiz data and save it.
     */
    private void submitQuiz(ActionEvent actionEvent) {
        try {
            // Build quiz instance DTO from quiz data.
            QuizInstanceDTO quizInstanceDTO = new QuizInstanceDTO();
            QuizInstance quizInstance = new QuizInstance();
            quizInstance.setName(selected.getName());
            quizInstance.setDescription(selected.getDescription());
            quizInstance.setCategoryLabel(selected.getCategory().toString());
            quizInstance.setQuizTemplateId(selected.getId());
            quizInstance.setUserId(UserSession.getUser().getId());
            quizInstanceDTO.setQuiz(quizInstance);

            ObservableList<Node> elements = questions.getChildren();
            for (Node element : elements) {
                if (!element.getId().equals("questions_wrapper")) {
                    continue;
                }

                QuestionInstance questionInstance = new QuestionInstance();
                for (Node child : ((VBox) element).getChildren()) {
                    if (child.getId() == null) {
                        continue;
                    }

                    if (child.getId().equals("question_id")) {
                        String id = ((Label) child).getText();
                        questionInstance.setQuestionTemplateId(Integer.parseInt(id));
                        continue;
                    }

                    if (child.getId().equals("question_text")) {
                        questionInstance.setText(((Label) child).getText());
                        continue;
                    }

                    if (child.getId().equals("answer")) {
                        questionInstance.setAnswer(((TextArea) child).getText());
                        continue;
                    }

                    if (child.getId().equals("expected_answer")) {
                        questionInstance.setExpectedAnswer(((Label) child).getText());
                    }
                }

                quizInstanceDTO.addQuestion(questionInstance);
            }

            QuizInstanceService quizInstanceService = (QuizInstanceService) Di.getInstance(QuizInstanceService.class);
            quizInstanceService.saveSolvedQuiz(quizInstanceDTO);

            listing.getItems().remove(selected);

            Stage stage = (Stage) submit.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            message.setText("Cannot submit quiz. Please try again or contact an admin.");
        }
    }
}
