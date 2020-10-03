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
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class EditController extends AbstractController {
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
    private final UserDTO selected;
    private TableView<UserDTO> listing;

    /**
     * Initializes the controller with the selected user.
     */
    public EditController(@NotNull TableView<UserDTO> listing) {
        this.selected = listing.getSelectionModel().getSelectedItem();
        this.listing = listing;
        service = (UserService) Di.getInstance(UserService.class);
    }

    @Override
    protected void init() {
        email.setText(selected.getEmail());

        Collection<UserRole> roles = service.getUserRoles();
        role.getItems().setAll(roles);
        role.setValue(selected.getRole());

        form.requestFocus();

        submit.setOnAction(this::updateUser);
    }

    /**
     * Updates selected user with current form data.
     */
    private void updateUser(ActionEvent actionEvent) {
        try {
            // Get input details.
            String emailText = email.getText();
            String passwordText = password.getText();
            Integer roleId = role.getSelectionModel().getSelectedItem().getId();

            // Update user.
            User user = selected.getUser();
            user.setEmail(emailText).setRoleId(roleId);
            service.save(user, passwordText);

            selected.setRole(service.getRoleForUser(user));

            listing.refresh();

            // Close popup.
            Stage stage = (Stage) submit.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            message.setText(e.getMessage());
        }
    }
}
