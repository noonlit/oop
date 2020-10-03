package Cinema.Repository;

import Cinema.Domain.Movie;
import Cinema.Repository.History.History;
import Cinema.Repository.Storage.MemoryStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MovieRepositoryTest {
    private MovieRepository movieRepository;

    @BeforeEach
    void setUp() {
        this.movieRepository = new MovieRepository(new History(), new History(), new MemoryStorage());
    }

    @Test
    void undoSaveShouldDeleteMovie() {
        movieRepository.save(new Movie("Corpse Bride", 2005, 10, true));
        movieRepository.undo();

        assertNull(movieRepository.findOne("Corpse Bride"));
    }

    @Test
    void redoSaveShouldRestoreMovie() {
        movieRepository.save(new Movie("Corpse Bride", 2005, 10, true));
        movieRepository.undo();
        movieRepository.redo();

        assertNotNull(movieRepository.findOne("Corpse Bride"));
    }

    @Test
    void undoUpdateShouldRestoreOriginalMovieTitle() {
        movieRepository.save(new Movie(1,"Corpse Bride", 2005, 10, true));
        Movie movie = (Movie) movieRepository.get(1);

        // change the movie title and ensure the change was persisted
        movie.setTitle("Other title");
        movieRepository.save(movie);
        assertNotNull(movieRepository.findOne("Other title"));

        // undo the change
        movieRepository.undo();
        assertNotNull(movieRepository.findOne("Corpse Bride"));
        assertNull(movieRepository.findOne("Other title"));
    }

    @Test
    void entityExistsShouldReturnTrueIfEntityIsInStorage() {
        movieRepository.save(new Movie(1, "Corpse Bride", 2005, 10, true));
        assertTrue(movieRepository.entryExists(1));
    }
}