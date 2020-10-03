package Cinema.ViewModel;

/**
 * View model for displaying customer card numbers + corresponding bookings count.
 */
public class CustomerCardNumberAndBookingCount {
    public int cardNumber;
    public int bookings;

    public CustomerCardNumberAndBookingCount(int cardNumber, int bookings) {
        this.cardNumber = cardNumber;
        this.bookings = bookings;
    }

    @Override
    public String toString() {
        return String.format("Customer with card number %d has %d bookings", cardNumber, bookings);
    }
}
