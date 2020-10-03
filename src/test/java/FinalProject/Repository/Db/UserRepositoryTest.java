package FinalProject.Repository.Db;

import FinalProject.Domain.Entity.User;
import FinalProject.Repository.Db.Storage.Adapter;
import FinalProject.Repository.Db.Storage.QueryBuilderFactory;
import FinalProject.Repository.Db.Storage.StorageInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UserRepositoryTest {
    @InjectMocks
    UserRepository userRepository;

    @Mock
    Adapter adapter;

    @Mock
    QueryBuilderFactory queryBuilderFactory;

    @Mock
    StorageInterface<User> storageInterface;

    @Mock
    User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void saveShouldSaveUser() {
        when(storageInterface.save(any(User.class))).thenReturn(true);
        assertTrue(userRepository.save(user));
    }

    @Test
    void deleteShouldDeleteUser() {
        Class<User> userClass = (Class<User>) user.getClass();
        when(storageInterface.delete(userClass, user.getId())).thenReturn(true);
        assertTrue(userRepository.delete(user));
    }

    @Test
    void deleteByIdShouldDeleteUser() {
        Class<User> userClass = (Class<User>) user.getClass();
        when(storageInterface.delete(userClass, user.getId())).thenReturn(true);
        assertTrue(userRepository.deleteById(userClass, user.getId()));
    }

    @Test
    void findOneShouldFindUser() {
        Class<User> userClass = (Class<User>) user.getClass();
        when(storageInterface.fetchOne(userClass, 1)).thenReturn(user);

        assertEquals(user, userRepository.findOne(userClass, 1));
    }

    @Test
    void getAllShouldFindAllUsers() {
        Class<User> userClass = (Class<User>) user.getClass();
        HashMap<Integer, User> map = new HashMap<>();
        map.put(1, user);
        map.put(2, user);
        when(storageInterface.fetchAll(userClass)).thenReturn(map);

        assertEquals(userRepository.getAll(userClass), map);
    }
}