package Cinema.Domain;

public class Movie extends AbstractEntity {
    private String title;
    private int year;
    private int price;
    private boolean isRunning;

    public Movie(Integer id, String title, int year, int price, boolean isRunning) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.price = price;
        this.isRunning = isRunning;
    }

    public Movie(String title, int year, int price, boolean isRunning) {
        this(null, title, year, price, isRunning);
    }

    /**
     * @param title The movie title.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @param year The movie release year.
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * @param price The movie price.
     */
    public void setPrice(int price) {
        this.price = price;
    }

    /**
     * @param isRunning Whether The movie is currently running.
     */
    public void setIsRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    /**
     * @return The movie id.
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * @return The movie title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return The year when the movie was released.
     */
    public int getYear() {
        return year;
    }

    /**
     * @return The movie's price.
     */
    public int getPrice() {
        return price;
    }

    /**
     * @return Whether the movie is running.
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * @return A string representation of the current instance
     */
    public String toString() {
        String message = String.format("ID %d, title %s, from year %s, with a ticket price of %d.", getId(), getTitle(), getYear(), getPrice());
        message += isRunning() ? " This movie is currently running." : " This movie is not currently running.";
        return message;
    }

    /**
     * @return A copy of the current instance
     */
    public Movie clone() {
        return new Movie(this.getId(), this.getTitle(), this.getYear(), this.getPrice(), this.isRunning());
    }
}
