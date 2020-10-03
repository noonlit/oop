package FinalProject.Controller.Admin.Quiz;

import FinalProject.Controller.AbstractController;
import FinalProject.Di.Di;
import FinalProject.Domain.DTO.QuizTemplateDTO;
import FinalProject.Domain.Entity.Category;
import FinalProject.Domain.Entity.QuestionTemplate;
import FinalProject.Domain.Entity.QuizTemplate;
import FinalProject.Service.QuizTemplateService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class NewController extends AbstractController {
    @FXML
    public TextField name;

    @FXML
    public TextArea description;

    @FXML
    public ComboBox<Category> category;

    @FXML
    public Button submit;

    @FXML
    public VBox form;

    @FXML
    public ListView<QuestionTemplate> questionsList;

    @FXML
    public Text message;

    private QuizTemplateService service;
    private final TableView<QuizTemplateDTO> listing;

    public NewController(@NotNull TableView<QuizTemplateDTO> listing) {
        this.listing = listing;
    }

    @Override
    protected void init() {
        service = (QuizTemplateService) Di.getInstance(QuizTemplateService.class);
        Collection<Category> categories = service.getQuizCategories();
        category.getItems().setAll(categories);
        category.getSelectionModel().select(0);

        // Populate questions list with available questions and preselect the ones associated with the quiz.
        Collection<QuestionTemplate> questions = service.getEligibleQuestions();
        questionsList.getItems().setAll(questions);
        questionsList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        form.requestFocus();

        submit.setOnAction(this::createQuiz);
    }

    /**
     * Creates a new quiz based on the values in the form.
     */
    public void createQuiz(ActionEvent actionEvent) {
        try {
            // Get input values.
            String nameText = name.getText();
            String descriptionText = description.getText();
            Category categoryValue = category.getSelectionModel().getSelectedItem();

            // Create quiz.
            QuizTemplate quiz = new QuizTemplate();
            quiz.setName(nameText).setDescription(descriptionText).setCategoryId(categoryValue.getId());

            QuizTemplateDTO quizTemplateDTO = new QuizTemplateDTO();
            quizTemplateDTO.setQuiz(quiz);
            quizTemplateDTO.setCategory(service.getCategoryForQuiz(quiz));

            // Set quiz questions.
            for (QuestionTemplate question : questionsList.getSelectionModel().getSelectedItems()) {
                quizTemplateDTO.addQuestion(question);
            }

            service.createQuizWithQuestions(quizTemplateDTO);

            listing.getItems().add(quizTemplateDTO);

            // Close popup.
            Stage stage = (Stage) submit.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            message.setText(e.getMessage());
        }
    }
}