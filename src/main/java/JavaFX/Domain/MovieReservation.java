package JavaFX.Domain;

import java.time.LocalDate;
import java.util.Objects;

public class MovieReservation extends Entity {

    private Movie reservedMovie;
    private int clientCardNumber;
    private int hourOfStart;
    private LocalDate reservationDate;

    public MovieReservation( Movie reservedMovie, int clientCardNumber, int hourOfStart, LocalDate reservationDate) {

        this.reservedMovie = reservedMovie;
        this.clientCardNumber = clientCardNumber;
        this.hourOfStart = hourOfStart;
        this.reservationDate = reservationDate;
    }

    public Movie getReservedMovie() {
        return reservedMovie;
    }

    public void setReservedMovie(Movie reservedMovie) {
        this.reservedMovie = reservedMovie;
    }

    public int getClientCardNumber() {
        return clientCardNumber;
    }

    public void setClientCardNumber(int clientCardNumber) {
        this.clientCardNumber = clientCardNumber;
    }

    public int getHourOfStart() {
        return hourOfStart;
    }

    public void setHourOfStart(int hourOfStart) {
        this.hourOfStart = hourOfStart;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }

    @Override
    public String toString() {
        return "MovieReservation{" +
                "ID="+this.getId()+
                " reservedMovie=" + reservedMovie.getTitle() +
                ", clientCardNumber=" + clientCardNumber +
                ", hourOfStart=" + hourOfStart +
                ", reservationDate=" + reservationDate.toString() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MovieReservation)) return false;
        MovieReservation that = (MovieReservation) o;
        return getClientCardNumber() == that.getClientCardNumber() &&
                getHourOfStart() == that.getHourOfStart() &&
                getReservedMovie().equals(that.getReservedMovie()) &&
                getReservationDate().equals(that.getReservationDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getReservedMovie(), getClientCardNumber(), getHourOfStart(), getReservationDate());
    }
}
