package FinalProject.Service;

import FinalProject.Domain.DTO.UserDTO;
import FinalProject.Domain.Entity.User;
import FinalProject.Domain.Entity.UserRole;
import FinalProject.Domain.Validator.UserValidator;
import FinalProject.Exception.CannotAuthenticateException;
import FinalProject.Repository.Db.UserRepository;
import FinalProject.Repository.Db.UserRoleRepository;
import FinalProject.Repository.RepositoryManager;
import com.google.inject.Inject;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class UserService {
    private final RepositoryManager repositoryManager;
    private final Encryptor encryptor;
    private final UserValidator userValidator;

    @Inject
    public UserService(RepositoryManager repositoryManager, Encryptor encryptor, UserValidator userValidator) {
        this.repositoryManager = repositoryManager;
        this.encryptor = encryptor;
        this.userValidator = userValidator;
    }

    /**
     * Retrieves a user by email and validates that his/her password matches the one given.
     */
    public UserDTO findOneByEmailWithPassword(String email, String password) {
        UserDTO user = findOneByEmail(email);

        if (!encryptor.verify(password, user.getPassword())) {
            throw new CannotAuthenticateException("User and password do not match!");
        }

        return user;
    }

    /**
     * Retrieves a user by email.
     */
    public UserDTO findOneByEmail(String email) {
        UserRepository repository = (UserRepository) repositoryManager.getRepository(UserRepository.class);
        User user = repository.findOneByEmail(email);

        UserRoleRepository userRoleRepository = (UserRoleRepository) repositoryManager.getRepository(UserRoleRepository.class);
        UserRole role = userRoleRepository.findOne(UserRole.class, user.getRoleId());
        UserDTO dto = new UserDTO();
        dto.setUser(user);
        dto.setRole(role);

        return dto;
    }

    /**
     * Retrieves all available user roles.
     */
    public Collection<UserRole> getUserRoles() {
        UserRoleRepository roleRepository = (UserRoleRepository) repositoryManager.getRepository(UserRoleRepository.class);
        return roleRepository.getAll(UserRole.class).values();
    }

    /**
     * Retrieves the role associated with the given user.
     */
    public UserRole getRoleForUser(@NotNull User user) {
        UserRoleRepository roleRepository = (UserRoleRepository) repositoryManager.getRepository(UserRoleRepository.class);
        return roleRepository.findOne(UserRole.class, user.getRoleId());
    }

    /**
     * Saves the user.
     */
    public boolean save(@NotNull UserDTO user, String password) {
        return save(user.getUser(), password);
    }

    /**
     * Saves an existing user.
     */
    public boolean save(@NotNull User user, String password) {
        // the password field is blank by default in the user edit form, so the user might not have a password yet
        boolean passwordIsBlank = password == null || password.isEmpty();
        if (!passwordIsBlank) {
            user.setPassword(password);
        }

        userValidator.validate(user);

        if (!passwordIsBlank) {
            user.setPassword(encryptor.encrypt(password));
        }

        return getDefaultRepository().save(user);
    }

    /**
     * Creates a new user with the given password.
     */
    public boolean create(@NotNull User user, String password) {
        user.setPassword(password);
        userValidator.validateNew(user);
        user.setPassword(encryptor.encrypt(password));

        return getDefaultRepository().save(user);
    }

    /**
     * Retrieves users + their roles data for display in the admin users listing.
     */
    public List<UserDTO> getUsersDataForListing() {
        UserRepository repository = (UserRepository) repositoryManager.getRepository(UserRepository.class);
        HashMap<Integer, User> users = repository.getAll(User.class);

        List<UserDTO> userList = new ArrayList<>();
        for (Map.Entry<Integer, User> entry : users.entrySet()) {
            User user = entry.getValue();
            UserDTO userDTO = new UserDTO();
            userDTO.setUser(user);
            userDTO.setRole(getRoleForUser(user));
            userList.add(userDTO);
        }

        return userList;
    }

    /**
     * Deletes the user.
     */
    public boolean delete(@NotNull UserDTO user) {
        UserRepository repository = (UserRepository) repositoryManager.getRepository(UserRepository.class);
        return repository.delete(user.getUser());
    }

    /**
     * Returns this service's default repository, which is the user template repository.
     */
    private  UserRepository getDefaultRepository() {
        return (UserRepository) repositoryManager.getRepository(UserRepository.class);
    }
}
