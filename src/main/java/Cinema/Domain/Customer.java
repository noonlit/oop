package Cinema.Domain;

/**
 * Represents a customer in the cinema.
 */
public class Customer extends AbstractEntity {
    private int cardNumber;

    /**
     * Constructor.
     * A customer is solely represented by a card number.
     *
     * @param cardNumber The card number.
     */
    public Customer(int cardNumber) {
        this.cardNumber = cardNumber;
    }
    public Customer(Integer id, int cardNumber) {
        this.id = id;
        this.cardNumber = cardNumber;
    }

    /**
     * @return The card number
     */
    public int getCardNumber() {
        return cardNumber;
    }

    /**
     * @return A copy of the current instance.
     */
    public Customer clone() {
        return new Customer(getId(), getCardNumber());
    }

    /**
     * @return A string representation of the current instance
     */
    public String toString() {
        return String.format("Customer with card number %s.", getCardNumber());
    }
}
