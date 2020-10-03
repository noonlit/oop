package Cinema.Domain;

/**
 * Base class for concrete entities.
 */
abstract public class AbstractEntity {
    protected Integer id;

    /**
     * The ID can only be set once.
     *
     * @param id The entity id
     */
    public void setId(int id) {
        if (this.id == null) {
            this.id = id;
        }
    }

    /**
     * @return Integer
     */
    public Integer getId() {
        return id;
    }

    /**
     * @return AbstractEntity A copy of the entity
     */
    public abstract AbstractEntity clone();

    /**
     * @return String a string representation of the entity
     */
    public abstract String toString();
}
