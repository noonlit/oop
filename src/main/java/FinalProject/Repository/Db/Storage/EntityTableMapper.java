package FinalProject.Repository.Db.Storage;

import FinalProject.Domain.Entity.*;

import java.util.Map;

/**
 * Maps an entity class to a database table.
 */
public class EntityTableMapper {
    private final Map<Class<? extends AbstractEntity>, String> map = Map.of(
            User.class, "user",
            UserRole.class, "user_role",
            QuizTemplate.class, "quiz_template",
            Category.class, "category",
            QuestionTemplate.class, "question_template",
            QuizInstance.class, "quiz_instance",
            QuestionInstance.class, "question_instance"
    );

    public String getTableName(Class<? extends AbstractEntity> entityClass) {
        return map.get(entityClass);
    }
}
