package JavaFX.Domain;

import java.util.Objects;

public class Movie extends Entity {

    private String title;
    private int yearOfRelease;
    private int price;
    private boolean inTheatre;

    public Movie( String title, int yearOfRelease, int price) {

        this.title = title;
        this.yearOfRelease = yearOfRelease;
        this.price = price;
        this.inTheatre = true;
    }

    public Movie(Movie movie){
        this(movie.getTitle(),movie.getYearOfRelease(),movie.getPrice());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYearOfRelease() {
        return yearOfRelease;
    }

    public void setYearOfRelease(int yearOfRelease) {
        this.yearOfRelease = yearOfRelease;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isInTheatre() {
        return inTheatre;
    }

    public void setInTheatre(boolean inTheatre) {
        this.inTheatre = inTheatre;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" +this.getId() +
                " title='" + title + '\'' +
                ", yearOfRelease=" + yearOfRelease +
                ", price=" + price +
                ", inTheatre=" + inTheatre +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movie)) return false;
        Movie movie = (Movie) o;
        return getYearOfRelease() == movie.getYearOfRelease() &&
                getPrice() == movie.getPrice() &&
                isInTheatre() == movie.isInTheatre() &&
                Objects.equals(getTitle(), movie.getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getYearOfRelease(), getPrice(), isInTheatre());
    }
}
