package Cinema.Repository;

import Cinema.Domain.AbstractEntity;
import Cinema.Domain.Movie;
import Cinema.Repository.Command.CompositeCommand;
import Cinema.Repository.Command.DeleteBatchCommand;
import Cinema.Repository.Command.DeleteEntityCommand;
import Cinema.Repository.Command.ReversibleCommandInterface;
import Cinema.Repository.History.History;
import Cinema.Repository.Storage.StorageInterface;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MovieRepository extends AbstractRepository {
    /**
     * Constructor.
     *
     * @param undoLog keeps track of commands (create, insert, update) to undo.
     * @param redoLog keeps track of commands (create, insert, update) to redo.
     * @param storage where we keep The data (e.g. in memory, db, files)
     */
    public MovieRepository(History undoLog, History redoLog, StorageInterface storage) {
        super(undoLog, redoLog, storage);
    }

    /**
     * Searches for movies. Matches are performed against the title and year fields.
     *
     * @param query The query.
     * @return A list of bookings for that customer.
     */
    public List<AbstractEntity> find(String query) {
        query = query.toLowerCase();
        Collection<AbstractEntity> movies = findAll();
        List<AbstractEntity> result = new ArrayList<>();

        for (AbstractEntity element : movies) {
            Movie movieElement = (Movie) element;
            String title = movieElement.getTitle().toLowerCase();
            if (title.contains(query)) {
                result.add(movieElement.clone());
                continue;
            }

            if (Integer.toString(movieElement.getYear()).equals(query)) {
                result.add(movieElement.clone());
            }
        }

        return result;
    }

    /**
     * Searches for a movie. Matches are performed against the title and year fields.
     *
     * @param query The query.
     * @return The first movie found.
     */
    public AbstractEntity findOne(String query) {
        query = query.toLowerCase();
        Collection<AbstractEntity> movies = findAll();

        for (AbstractEntity element : movies) {
            Movie movieElement = (Movie) element;
            String title = movieElement.getTitle().toLowerCase();
            if (title.contains(query)) {
                return movieElement.clone();
            }

            if (Integer.toString(movieElement.getYear()).equals(query)) {
                return movieElement.clone();
            }
        }

        return null;
    }

    public int updatePrices(int minOldPrice, int newPrice) {
        Collection<AbstractEntity> movies = findAll();

        int updated = 0;
        for (AbstractEntity element : movies) {
            Movie movieElement = (Movie) element;
            if (movieElement.getPrice() < minOldPrice) {
                movieElement.setPrice(newPrice);
                save(movieElement);
                updated++;
            }
        }

        return updated;
    }

    /**
     * Deleting a movie should delete the associated bookings.
     *
     * Therefore, this method will use a composite command to delete both.
     *
     * @param id The entity id
     * @return
     */
    public boolean deleteAndClearBookings(int id, BookingRepository bookingRepository) {
        CompositeCommand composite = new CompositeCommand();

        // add command to delete movie
        composite.addCommand(new DeleteEntityCommand(storage, id));

        // find bookings associated to the movie
        List<AbstractEntity> entitiesToDelete = bookingRepository.findByMovieId(id);

        // add command to delete bookings associated to the movie
        ReversibleCommandInterface command = new DeleteBatchCommand(bookingRepository.getStorage(), entitiesToDelete);
        composite.addCommand(command);

        // record composite command so we can undo it
        undoLog.recordCommand(composite);

        return composite.execute();
    }
}
