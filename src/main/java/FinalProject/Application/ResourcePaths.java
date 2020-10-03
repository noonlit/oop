package FinalProject.Application;

/**
 * Contains paths to FXML files representing screens in the application.
 */
public class ResourcePaths {
    public static final String BP = "/final_project";
    public static final String ADMIN = "/admin";
    public static final String LOGIN = BP + "/login.fxml";

    /**
     * Admin paths.
     */
    public static final String ADMIN_DASHBOARD = BP + ADMIN + "/dashboard.fxml";
    public static final String QUESTION_LISTING = BP + ADMIN + "/question_listing.fxml";
    public static final String USER_LISTING = BP + ADMIN + "/user_listing.fxml";
    public static final String QUIZ_LISTING = BP + ADMIN + "/quiz_listing.fxml";
    public static final String CANDIDATE_RESULTS = BP + ADMIN + "/result_listing.fxml";
    public static final String EDIT_RESULT = BP + ADMIN + "/edit_result.fxml";
    public static final String NEW_QUESTION = BP + ADMIN + "/new_question.fxml";
    public static final String EDIT_QUESTION = BP + ADMIN + "/edit_question.fxml";
    public static final String NEW_QUIZ = BP + ADMIN + "/new_quiz.fxml";
    public static final String EDIT_QUIZ = BP + ADMIN + "/edit_quiz.fxml";
    public static final String NEW_USER = BP + ADMIN + "/new_user.fxml";
    public static final String EDIT_USER = BP + ADMIN + "/edit_user.fxml";

    /**
     * Candidate paths
     */
    public static final String CANDIDATE = "/candidate";
    public static final String CANDIDATE_QUIZ_LISTING = BP + CANDIDATE + "/quiz_listing.fxml";
    public static final String CANDIDATE_QUIZ = BP + CANDIDATE + "/quiz.fxml";
    public static final String CANDIDATE_SCORES = BP + CANDIDATE + "/scores.fxml";
}
