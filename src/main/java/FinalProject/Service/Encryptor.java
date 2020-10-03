package FinalProject.Service;

import org.apache.commons.codec.digest.DigestUtils;
import org.jetbrains.annotations.NotNull;

public class Encryptor {
    @NotNull
    public String encrypt(String string) {
        return DigestUtils.md5Hex(string);
    }

    public boolean verify(String string, String hash) {
        return encrypt(string).equals(hash);
    }
}
