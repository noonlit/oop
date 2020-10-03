package FinalProject.Controller.Admin.User;

import FinalProject.Application.ResourcePaths;
import FinalProject.Auth.UserSession;
import FinalProject.Controller.AbstractController;
import FinalProject.Di.Di;
import FinalProject.Domain.DTO.UserDTO;
import FinalProject.Service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;

import java.util.List;

public class ListingController extends AbstractController {
    @FXML
    public Label pageTitle;

    @FXML
    public TableView<UserDTO> listing;

    @FXML
    public TableColumn<UserDTO, Integer> id;

    @FXML
    public Text message;

    private UserService service;

    @Override
    protected void init() {
        service = (UserService) Di.getInstance(UserService.class);
        List<UserDTO> users = service.getUsersDataForListing();

        for (UserDTO user : users) {
            listing.getItems().add(user);
        }

        listing.getSortOrder().add(id);
        listing.sort();
    }

    /**
     * Displays a popup with a form for adding a new user.
     */
    public void showNewUserForm(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(ResourcePaths.NEW_USER));
        NewController controller = new NewController(listing);
        loader.setController(controller);
        showPopup(loader);
    }

    /**
     * Deletes the selected user.
     */
    public void delete(ActionEvent actionEvent) {
        UserDTO selected = listing.getSelectionModel().getSelectedItem();

        if (selected == null) {
            message.setText("Please select a user.");
            return;
        }

        if (selected.getEmail().equals(UserSession.getUser().getEmail())) {
            message.setText("You can't delete your own user.");
            return;
        }

        service.delete(selected);
        listing.getItems().remove(selected);
    }

    /**
     * Displays a popup with a form prefilled with the selected user details.
     */
    public void showUserDetails(ActionEvent actionEvent) {
        UserDTO selected = listing.getSelectionModel().getSelectedItem();

        if (selected == null) {
            message.setText("Please select a user.");
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource(ResourcePaths.EDIT_USER));
        EditController controller = new EditController(listing);
        loader.setController(controller);
        showPopup(loader);
    }
}
