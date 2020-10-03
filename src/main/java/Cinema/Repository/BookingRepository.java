package Cinema.Repository;

import Cinema.Domain.AbstractEntity;
import Cinema.Domain.Booking;
import Cinema.Repository.History.History;
import Cinema.Repository.Storage.StorageInterface;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BookingRepository extends AbstractRepository {
    /**
     * Constructor.
     *
     * @param undoLog keeps track of commands (create, insert, update) to undo.
     * @param redoLog keeps track of commands (create, insert, update) to redo.
     * @param storage where we keep The data (e.g. in memory, db, files)
     */
    public BookingRepository(History undoLog, History redoLog, StorageInterface storage) {
        super(undoLog, redoLog, storage);
    }

    /**
     * Searches for bookings. Matches are performed against the customer card number and movie title fields.
     *
     * @param query The query.
     * @return A list of bookings for that customer.
     */
    public List<AbstractEntity> find(String query) {
        Collection<AbstractEntity> bookings = storage.findAll();
        List<AbstractEntity> result = new ArrayList<>();

        for (AbstractEntity element : bookings) {
            Booking bookingElement = (Booking) element;

            if (bookingElement.getMovieTitle().equals(query)) {
                result.add(bookingElement);
            }

            int customerCardNumber = bookingElement.getCustomerCardNumber();
            try {
                int customerCardNumberQuery = Integer.parseInt(query);
                if (customerCardNumber == customerCardNumberQuery) {
                    result.add(bookingElement);
                }
            } catch (NumberFormatException e) {
                // do nothing, we simply don't add this result.
            }
        }

        return result;
    }

    /**
     * Searches for a booking. Matches are performed against the customer card number and movie title fields.
     *
     * @param query The query.
     * @return The first booking found.
     */
    public AbstractEntity findOne(String query) {
        Collection<AbstractEntity> bookings = findAll();

        for (AbstractEntity element : bookings) {
            Booking bookingElement = (Booking) element;

            if (bookingElement.getMovieTitle().equals(query)) {
                return bookingElement.clone();
            }

            int customerCardNumber = bookingElement.getCustomerCardNumber();

            try {
                int customerCardNumberQuery = Integer.parseInt(query);
                if (customerCardNumber == customerCardNumberQuery) {
                    return bookingElement.clone();
                }
            } catch (NumberFormatException e) {
                // do nothing, we simply don't return this result.
            }
        }

        return null;
    }

    /**
     * Returns a list of bookings that match the given intervals' dates and times.
     *
     * @param startInterval The start date and time.
     * @param endInterval The end date and time.
     * @return The bookings that match the interval.
     */
    public List<Booking> find(LocalDateTime startInterval, LocalDateTime endInterval) {
        Collection<AbstractEntity> bookings = findAll();
        List<Booking> result = new ArrayList<>();

        for (AbstractEntity element : bookings) {
            Booking bookingElement = (Booking) element;
            startInterval = startInterval.withYear(bookingElement.getYear()).withMonth(bookingElement.getMonth()).withDayOfMonth(bookingElement.getDay());
            endInterval = endInterval.withYear(bookingElement.getYear()).withMonth(bookingElement.getMonth()).withDayOfMonth(bookingElement.getDay());

            if (bookingElement.getDateTime().isAfter(startInterval) && bookingElement.getDateTime().isBefore(endInterval)) {
                result.add(bookingElement.clone());
            }
        }

        return result;
    }

    /**
     * Deletes entries between the given intervals' dates and times.
     *
     * @param startInterval The start date and time.
     * @param endInterval The end date and time.
     * @return The number of deleted entries.
     */
    public int delete(LocalDateTime startInterval, LocalDateTime endInterval) {
        Collection<AbstractEntity> bookings = findAll();

        int deleted = 0;
        for (AbstractEntity element : bookings) {
            Booking bookingElement = (Booking) element;
            if (bookingElement.getDateTime().isAfter(startInterval) && bookingElement.getDateTime().isBefore(endInterval)) {
                delete(bookingElement.getId());
                deleted++;
            }
        }

        return deleted;
    }

    /**
     * Deletes a list of bookings for the movie with the given ID.
     * @param id
     * @return boolean
     */
    public boolean deleteByMovieId(int id) {
        List<AbstractEntity> bookings = findByMovieId(id);
        return deleteAll(bookings);
    }

    public List<Booking> getBookingsSortedByCustomerCreditCardNumber() {
        // cast abstract entities to bookings and sort them by customer card number
        return storage.findAll().stream()
                .map(element -> (Booking) element).sorted(Comparator.comparingInt(Booking::getCustomerCardNumber))
                .collect(Collectors.toList());
    }

    public List<Booking> getBookingsSortedByMovieId() {
        // cast abstract entities to bookings and sort them by movie ID
        return storage.findAll().stream()
                .map(element -> (Booking) element).sorted(Comparator.comparingInt(booking -> booking.getMovie().getId()))
                .collect(Collectors.toList());
    }

    /**
     * Returns a list of bookings for the movie with the given ID.
     *
     * @param id
     * @return
     */
    public List<AbstractEntity> findByMovieId(int id) {
        Collection<AbstractEntity> bookings = findAll();
        List<AbstractEntity> result = new ArrayList<>();

        for (AbstractEntity element : bookings) {
            Booking bookingElement = (Booking) element;

            if (bookingElement.getMovie().getId().equals(id)) {
                result.add(bookingElement.clone());
            }
        }

        return result;
    }
}
