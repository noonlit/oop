package FinalProject.Domain.Validator;

import FinalProject.Domain.Entity.User;
import FinalProject.Exception.DataValidationException;
import FinalProject.Exception.EntityNotFoundException;
import FinalProject.Repository.Db.UserRepository;
import FinalProject.Repository.RepositoryManager;
import com.google.inject.Inject;
import org.jetbrains.annotations.NotNull;

public class UserValidator implements ValidatorInterface<User> {
    public final Integer MIN_PASSWORD_LENGTH = 5;

    private final UserRepository repository;

    @Inject
    public UserValidator(RepositoryManager repositoryManager) {
        this.repository = (UserRepository) repositoryManager.getRepository(UserRepository.class);
    }

    @Override
    public void validate(@NotNull User entity) {
        if (entity.getEmail().isEmpty()) {
            throw new DataValidationException("A user must have an email!");
        }

        if (entity.getRoleId() == null) {
            throw new DataValidationException("A user must have a role!");
        }

        String password = entity.getPassword();
        boolean passwordIsBlank = password == null || password.isEmpty();
        if (!passwordIsBlank && password.length() < MIN_PASSWORD_LENGTH) {
            throw new DataValidationException("The password must be at least " + MIN_PASSWORD_LENGTH + " characters long!");
        }
    }

    public void validateNew(@NotNull User entity) {
        validate(entity);

        // validate that the user doesn't already exist.
        boolean found = true;
        try {
            repository.findOneByEmail(entity.getEmail());
        } catch (EntityNotFoundException e) {
            found = false;
        }

        if (found) {
            throw new DataValidationException("User already exists!");
        }

        // validate that the user has a password.
        String password = entity.getPassword();
        boolean passwordIsBlank = password == null || password.isEmpty();
        if (passwordIsBlank) {
            throw new DataValidationException("The user must have a password!");
        }
    }
}
