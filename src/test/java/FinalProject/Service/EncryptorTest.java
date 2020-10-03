package FinalProject.Service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EncryptorTest {
    private Encryptor encryptor;

    @BeforeEach
    void setUp() {
        this.encryptor = new Encryptor();
    }

    @Test
    void verifyShouldValidateMatchingString() {
        String original = "test";
        String encrypted = encryptor.encrypt(original);
        assertTrue(encryptor.verify(original, encrypted));
    }

    @Test
    void verifyShouldNotValidateNonMatchingString() {
        String original = "test";
        String encrypted = encryptor.encrypt(original);
        assertFalse(encryptor.verify("abc", encrypted));
    }
}