package FinalProject.Controller.Admin.User;

import FinalProject.Controller.AbstractController;
import FinalProject.Di.Di;
import FinalProject.Domain.DTO.UserDTO;
import FinalProject.Domain.Entity.User;
import FinalProject.Domain.Entity.UserRole;
import FinalProject.Service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Collection;

public class NewController extends AbstractController {
    @FXML
    public VBox form;

    @FXML
    public TextField email;

    @FXML
    public PasswordField password;

    @FXML
    public ComboBox<UserRole> role;

    @FXML
    public Button submit;

    @FXML
    public Text message;

    private UserService service;
    private TableView<UserDTO> listing;

    /**
     * Initializes the controller with the selected user.
     */
    public NewController(TableView<UserDTO> listing) {
        this.listing = listing;
        service = (UserService) Di.getInstance(UserService.class);
    }

    @Override
    protected void init() {
        Collection<UserRole> roles = service.getUserRoles();
        role.getItems().setAll(roles);
        role.getSelectionModel().select(1);

        form.requestFocus();

        submit.setOnAction(this::createUser);
    }

    /**
     * Creates a new user based on the values in the form.
     */
    public void createUser(ActionEvent actionEvent) {
        try {
            // Get input values.
            String emailText = email.getText();
            String passwordText = password.getText();
            Integer roleId = role.getSelectionModel().getSelectedItem().getId();

            // Create user.
            User user = new User();
            user.setEmail(emailText).setRoleId(roleId);
            service.create(user, passwordText);

            UserDTO userDTO = new UserDTO();
            userDTO.setUser(user);
            userDTO.setRole(service.getRoleForUser(user));

            listing.getItems().add(userDTO);

            // Close popup.
            Stage stage = (Stage) submit.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            message.setText(e.getMessage());
        }
    }
}
