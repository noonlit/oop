package Hotel.UserInterface;

import java.util.Collection;

import Hotel.Domain.Checkin;
import Hotel.Domain.Checkout;
import Hotel.Service.HotelService;
import Hotel.Util.InputOutput;
import Hotel.ViewModel.CheckoutDetailsViewModel;

public class Console {
    private boolean devMode = false;

    private InputOutput inputOutputService;
    private HotelService hotelService;

    /**
     * Constants representing the menu option IDs.
     */
    private static final int QUIT_OPTION = 0;
    private static final int CHECKIN_OPTION = 1;
    private static final int UPDATE_CHECKIN_OPTION = 2;
    private static final int CHECKOUT_OPTION = 3;
    private static final int DISPLAY_ALL_OPTION = 4;

    public Console(HotelService hotelService, InputOutput inputOutputService) {
        this.hotelService = hotelService;
        this.inputOutputService = inputOutputService;
    }

    /**
     * Displays the application menu.
     */
    private void showMenu() {
        System.out.format("%d. New check in \n", CHECKIN_OPTION);
        System.out.format("%d. Update check in \n", UPDATE_CHECKIN_OPTION);
        System.out.format("%d. Check out \n", CHECKOUT_OPTION);
        System.out.format("%d. Show checkout details \n", DISPLAY_ALL_OPTION);
        System.out.println("0. Exit.");
        System.out.println("Your option: ");
    }

    /**
     * Displays the menu, reads the client option, executes the option.
     */
    public void run(boolean devMode) {
        this.devMode = devMode;

        while (true) {
            showMenu();

            int option = inputOutputService.readInt();
            if (option == CHECKIN_OPTION) {
                handleNewCheckin();
                continue;
            }

            if (option == UPDATE_CHECKIN_OPTION) {
                handleExistingCheckin();
                continue;
            }

            if (option == CHECKOUT_OPTION) {
                handleCheckout();
                continue;
            }
            if (option == DISPLAY_ALL_OPTION) {
                handleDisplayAll();
                continue;
            }

            if (option == QUIT_OPTION) {
                return;
            }

            System.err.println("Not implemented or invalid option!");
        }
    }

    /**
     * Registers a new checkin in the application based on user input data.
     */
    private void handleNewCheckin() {
        System.out.println("How many people?");
        int personsNumber = inputOutputService.readInt();

        System.out.println("How many days?");
        int days = inputOutputService.readInt();

        System.out.println("Which room?");
        int room = inputOutputService.readInt();

        try {
            Checkin checkin = hotelService.createCheckin(personsNumber, days, room);
            System.out.println("New checkin was successful! " + checkin);
        } catch (Exception ex) {
            System.err.println("We have errors:");
            System.err.println(ex.getMessage());

            if (devMode) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Updates an existing checkin in the application based on user input data.
     */
    private void handleExistingCheckin() {
        System.out.println("What is your checkin id?");
        int id = inputOutputService.readInt();;

        System.out.println("How many people?");
        int personsNumber = inputOutputService.readInt();;

        System.out.println("How many days?");
        int days = inputOutputService.readInt();;

        System.out.println("Which room?");
        int room = inputOutputService.readInt();;

        try {
            Checkin checkin = hotelService.updateCheckin(id, personsNumber, days, room);
            System.out.println("Checkin was successfully updated! " + checkin);
        } catch (Exception ex) {
            System.err.println("We have errors:");
            System.err.println(ex.getMessage());

            if (devMode) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Creates a checkout based on user input data.
     */
    private void handleCheckout() {
        System.out.println("Which room?");
        int roomNumber = inputOutputService.readInt();

        System.out.println("What is your feedback?");
        String feedback = inputOutputService.readLine();;

        System.out.println("What is your rating?");
        int rating = inputOutputService.readInt();

        try {
            Checkout checkout = hotelService.createCheckout(roomNumber, feedback, rating);
            System.out.println("Checkout was created! " + checkout);
        } catch (Exception ex) {
            System.err.println("We have errors:");
            System.err.println(ex.getMessage());

            if (devMode) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Displays existing room ratings from checkout data.
     */
    private void handleDisplayAll() {
        try {
            Collection<CheckoutDetailsViewModel> roomRatings = hotelService.getRoomRatingAveragesSortedAscendingly();

            for (CheckoutDetailsViewModel roomRating : roomRatings) {
                System.out.println(roomRating);
            }
        } catch (Exception ex) {
            System.err.println("We have errors:");
            System.err.println(ex.getMessage());

            if (devMode) {
                ex.printStackTrace();
            }
        }
    }
}
