package FinalProject.Domain.Validator;

import FinalProject.Domain.Entity.QuizTemplate;
import FinalProject.Exception.DataValidationException;
import org.jetbrains.annotations.NotNull;

public class QuizValidator implements ValidatorInterface<QuizTemplate> {
    public final Integer MIN_NAME_LENGTH = 5;
    public final Integer MIN_DESCRIPTION_LENGTH = 10;

    @Override
    public void validate(@NotNull QuizTemplate entity) {
        if (entity.getCategoryId() == null) {
            throw new DataValidationException("The quiz must be assigned to a category!");
        }

        if (entity.getDescription().length() < MIN_DESCRIPTION_LENGTH) {
            throw new DataValidationException("The description must be longer than " + MIN_DESCRIPTION_LENGTH + " characters!");
        }

        if (entity.getName().length() < MIN_NAME_LENGTH) {
            throw new DataValidationException("The description must be longer than " + MIN_DESCRIPTION_LENGTH + " characters!");
        }
    }
}
