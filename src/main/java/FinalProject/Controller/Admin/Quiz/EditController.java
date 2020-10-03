package FinalProject.Controller.Admin.Quiz;

import FinalProject.Controller.AbstractController;
import FinalProject.Di.Di;
import FinalProject.Domain.DTO.QuizTemplateDTO;
import FinalProject.Domain.Entity.Category;
import FinalProject.Domain.Entity.QuestionTemplate;
import FinalProject.Domain.Entity.QuizTemplate;
import FinalProject.Service.QuizTemplateService;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class EditController extends AbstractController {
    @FXML
    public VBox form;

    @FXML
    public TextField name;

    @FXML
    public TextArea description;

    @FXML
    public ComboBox<Category> category;

    @FXML
    public Button submit;

    @FXML
    public ListView<QuestionTemplate> questionsList;

    @FXML
    public Text message;

    private QuizTemplateService service;
    private final TableView<QuizTemplateDTO> listing;
    private final QuizTemplateDTO selected;

    /**
     * Initializes the controller with the selected quiz.
     */
    public EditController(@NotNull TableView<QuizTemplateDTO> listing) {
        this.selected = listing.getSelectionModel().getSelectedItem();
        this.listing = listing;
        service = (QuizTemplateService) Di.getInstance(QuizTemplateService.class);
    }

    @Override
    protected void init() {
        name.setText(selected.getName());
        description.setText(selected.getDescription());

        // Populate combo box with existing categories and preselect the one the quiz belongs to.
        Collection<Category> categories = service.getQuizCategories();
        category.getItems().setAll(categories);
        category.setValue(selected.getCategory());

        // Populate questions list with available questions and preselect the ones associated with the quiz.
        Collection<QuestionTemplate> questions = service.getEligibleQuestions(selected.getQuiz());
        questionsList.getItems().setAll(questions);
        questionsList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        for (QuestionTemplate question : questions) {
            Integer quizId = question.getQuizId();
            if (quizId != null && quizId.equals(selected.getId())) {
                questionsList.getSelectionModel().select(question);
            }
        }

        form.requestFocus();
        submit.setOnAction(this::updateQuiz);
    }

    /**
     * Updates selected quiz with current form data.
     */
    public void updateQuiz(ActionEvent actionEvent) {
        try {
            // Get input values.
            String nameText = name.getText();
            String descriptionText = description.getText();
            Category categoryValue = category.getSelectionModel().getSelectedItem();

            // Set quiz details.
            QuizTemplate quiz = selected.getQuiz();
            quiz.setName(nameText).setDescription(descriptionText).setCategoryId(categoryValue.getId());

            // Set quiz questions.
            for (QuestionTemplate question : questionsList.getItems()) {
                ObservableList<QuestionTemplate> list = questionsList.getSelectionModel().getSelectedItems();

                // Associate selected questions and unassociate deselected questions.
                if (list.contains(question)) {
                    question.setQuizId(selected.getQuiz().getId());
                } else {
                    question.setQuizId(null);
                }

                selected.addQuestion(question);
            }

            // Update question.
            service.updateQuizWithQuestions(selected);

            listing.refresh();

            // Close popup.
            Stage stage = (Stage) submit.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            message.setText(e.getMessage());
        }
    }
}
