package FinalProject.Service;

import FinalProject.Domain.DTO.UserDTO;
import FinalProject.Domain.Entity.User;
import FinalProject.Domain.Entity.UserRole;
import FinalProject.Domain.Validator.UserValidator;
import FinalProject.Exception.CannotAuthenticateException;
import FinalProject.Exception.EntityNotFoundException;
import FinalProject.Repository.Db.UserRepository;
import FinalProject.Repository.Db.UserRoleRepository;
import FinalProject.Repository.RepositoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class UserServiceTest {
    @InjectMocks  UserService service;

    @Mock
    UserRepository userRepository;

    @Mock
    UserRoleRepository userRoleRepository;

    @Mock
    Encryptor encryptor;

    @Mock
    UserValidator userValidator;

    @Mock
    RepositoryManager repositoryManager;

    @Mock
    User user;

    @Mock
    UserDTO userDTO;

    @Mock
    UserRole userRole;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        // stub repository manager methods to return mocks of actual repositories
        when(repositoryManager.getRepository(UserRepository.class)).thenReturn(userRepository);
        when(repositoryManager.getRepository(UserRoleRepository.class)).thenReturn(userRoleRepository);
    }

    @Test
    void saveWithoutPasswordShouldSaveUser() {
        // stub database save method to return true
        when(userRepository.save(any(User.class))).thenReturn(true);

        // check that the service has saved the user
        assertTrue(service.save(user, null));
        verify(userValidator, times(1)).validate(user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void saveWithPasswordShouldSaveUser() {
        when(userRepository.save(any(User.class))).thenReturn(true);
        when(user.getPassword()).thenReturn("abc");

        assertTrue(service.save(user, "abc"));

        verify(encryptor, times(1)).encrypt("abc");
        verify(userValidator, times(1)).validate(user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void saveWithDTOShouldSaveUser() {
        // stub database save method to return true
        when(userRepository.save(any(User.class))).thenReturn(true);
        when(userDTO.getUser()).thenReturn(user);

        assertTrue(service.save(userDTO, "abc"));

        verify(encryptor, times(1)).encrypt("abc");
        verify(userValidator, times(1)).validate(user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void createShouldSaveUser() {
        when(userRepository.save(any(User.class))).thenReturn(true);

        assertTrue(service.create(user, "abc"));

        verify(encryptor, times(1)).encrypt("abc");
        verify(userValidator, times(1)).validateNew(user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void findOneByEmailShouldFindOne() {
        when(userRepository.findOneByEmail("test@email.com")).thenReturn(user);

        UserDTO user = service.findOneByEmail("test@email.com");
        assertEquals(this.user, user.getUser());
    }

    @Test
    void findOneByEmailShouldThrowException() {
        when(userRepository.findOneByEmail("test@email.com")).thenThrow(new EntityNotFoundException("The entity was not found!"));

        assertThrows(EntityNotFoundException.class,
                () -> service.findOneByEmail("test@email.com")
        );
    }

    @Test
    void findOneByEmailWithPasswordShouldFindOne() {
        when(userRepository.findOneByEmail("test@email.com")).thenReturn(user);

        when(user.getPassword()).thenReturn("abc");
        when(encryptor.verify("abc", user.getPassword())).thenReturn(true);

        UserDTO user = service.findOneByEmailWithPassword("test@email.com", "abc");
        assertEquals(this.user, user.getUser());
    }

    @Test
    void findOneByEmailWithPasswordShouldFail() {
        when(userRepository.findOneByEmail("test@email.com")).thenReturn(user);

        // mock password verification
        when(user.getPassword()).thenReturn("abc");
        when(encryptor.verify("abc", user.getPassword())).thenReturn(false);

        assertThrows(CannotAuthenticateException.class,
                () -> service.findOneByEmailWithPassword("test@email.com", "abc")
        );
    }

    @Test
    void deleteShouldDeleteUser() {
        when(userRepository.delete(any(User.class))).thenReturn(true);
        when(userDTO.getUser()).thenReturn(user);
        assertTrue(service.delete(userDTO));
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void getUserRolesShouldReturnTwo() {
        HashMap<Integer, UserRole> map = new HashMap<>();
        map.put(1, userRole);
        map.put(2, userRole);
        when(userRoleRepository.getAll(UserRole.class)).thenReturn(map);

        assertEquals(service.getUserRoles().size(), 2);
    }

    @Test
    void getUserRoleByUserShouldReturnRole() {
        when(userRoleRepository.findOne(UserRole.class, user.getRoleId())).thenReturn(userRole);
        assertEquals(service.getRoleForUser(user), userRole);
    }

    @Test
    void getUsersDataForListingShouldReturnTwoUserDTOs() {
        HashMap<Integer, User> map = new HashMap<>();
        map.put(1, user);
        map.put(2, user);
        when(userRepository.getAll(User.class)).thenReturn(map);

        assertEquals(map.values().size(), service.getUsersDataForListing().size());
    }
}