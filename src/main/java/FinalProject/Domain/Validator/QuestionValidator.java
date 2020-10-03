package FinalProject.Domain.Validator;

import FinalProject.Domain.Entity.QuestionTemplate;
import FinalProject.Exception.DataValidationException;
import org.jetbrains.annotations.NotNull;

public class QuestionValidator implements ValidatorInterface<QuestionTemplate> {
    public final Integer MIN_ANSWER_LENGTH = 1;
    public final Integer MIN_TEXT_LENGTH = 5;

    @Override
    public void validate(@NotNull QuestionTemplate entity) {
        if (entity.getText().length() < MIN_TEXT_LENGTH) {
            throw new DataValidationException("The question text must be longer than " + MIN_TEXT_LENGTH + " characters!");
        }

        if (entity.getExpectedAnswer().length() < MIN_ANSWER_LENGTH) {
            throw new DataValidationException("The expected answer must be longer than " + MIN_ANSWER_LENGTH + " characters!");
        }
    }
}
