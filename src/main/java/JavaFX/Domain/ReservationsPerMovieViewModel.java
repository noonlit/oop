package JavaFX.Domain;

import java.util.Objects;

public class ReservationsPerMovieViewModel {
    String movieName;
    int numberOfReservation;

    public ReservationsPerMovieViewModel(String movieName, int numberOfReservation) {
        this.movieName = movieName;
        this.numberOfReservation = numberOfReservation;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public int getNumberOfReservation() {
        return numberOfReservation;
    }

    public void setNumberOfReservation(int numberOfReservation) {
        this.numberOfReservation = numberOfReservation;
    }

    @Override
    public String toString() {
        return "ReservationsPerMovieViewModel{" +
                "movieName='" + movieName + '\'' +
                ", numberOfReservation=" + numberOfReservation +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReservationsPerMovieViewModel)) return false;
        ReservationsPerMovieViewModel that = (ReservationsPerMovieViewModel) o;
        return getNumberOfReservation() == that.getNumberOfReservation() &&
                getMovieName().equals(that.getMovieName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMovieName(), getNumberOfReservation());
    }
}
