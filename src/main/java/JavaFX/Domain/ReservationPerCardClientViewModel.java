package JavaFX.Domain;

import java.util.Objects;

public class ReservationPerCardClientViewModel {
    int cardClient;
    int numberOfReservation;

    public ReservationPerCardClientViewModel(int cardClient, int numberOfReservation) {
        this.cardClient = cardClient;
        this.numberOfReservation = numberOfReservation;
    }

    public int getCardClient() {
        return cardClient;
    }

    public void setCardClient(int cardClient) {
        this.cardClient = cardClient;
    }

    public int getNumberOfReservation() {
        return numberOfReservation;
    }

    public void setNumberOfReservation(int numberOfReservation) {
        this.numberOfReservation = numberOfReservation;
    }

    @Override
    public String toString() {
        return "ReservationPerCardClientViewModel{" +
                "cardClient=" + cardClient +
                ", numberOfReservation=" + numberOfReservation +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReservationPerCardClientViewModel)) return false;
        ReservationPerCardClientViewModel that = (ReservationPerCardClientViewModel) o;
        return getCardClient() == that.getCardClient() &&
                getNumberOfReservation() == that.getNumberOfReservation();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCardClient(), getNumberOfReservation());
    }
}
