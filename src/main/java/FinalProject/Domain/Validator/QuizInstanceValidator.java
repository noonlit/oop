package FinalProject.Domain.Validator;

import FinalProject.Domain.Entity.QuizInstance;
import FinalProject.Exception.DataValidationException;

public class QuizInstanceValidator implements ValidatorInterface<QuizInstance> {
    public final Integer MIN_SCORE = 0;
    public final Integer MAX_SCORE = 10;

    @Override
    public void validate(QuizInstance entity) {
        Integer score = entity.getScore();
        if (score != null && (score < MIN_SCORE || score > MAX_SCORE)) {
            throw new DataValidationException("The score must be a value between " + MIN_SCORE + " and " + MAX_SCORE + "!");
        }
    }
}
