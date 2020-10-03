package Hotel.Domain;

import Hotel.Exception.InvalidCheckinException;
import Hotel.Exception.InvalidCheckoutException;
import org.jetbrains.annotations.NotNull;

public class Validator {
    /**
     * Validates that the days number for the checkin is positive.
     *
     * @param checkin The checkin to validate.
     */
    public void validateCheckin(@NotNull Checkin checkin) {
        String message = "";

        if (checkin.getDaysNumber() <= 0) {
            message += "Days number must be positive!\n";
        }

        if (message.length() > 0) {
            throw new InvalidCheckinException(message);
        }
    }

    /**
     * Validates that the given checkout is complete.
     *
     * @param checkout The checkout to validate.
     */
    public void validateCheckout(Checkout checkout) {
        String message = "";

        if (checkout.getFeedback().length() == 0) {
            message += "You must provide feedback!";
        }

        if (checkout.getRating() < 1 || checkout.getRating() > 5) {
            message += "The rating must be a number between 1 and 5!";
        }

        if (message.length() > 0) {
            throw new InvalidCheckoutException(message);
        }
    }
}
