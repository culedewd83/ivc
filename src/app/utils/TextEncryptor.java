package app.utils;

import app.models.Key;
import org.jasypt.util.text.BasicTextEncryptor;

import java.io.File;
import java.io.FileReader;

/**
 * Created by jesse on 9/17/15.
 */
public class TextEncryptor {
    private static TextEncryptor ourInstance = new TextEncryptor();

    public static TextEncryptor getInstance() {
        return ourInstance;
    }

    private static final String KEY_FILE = "/resources/key.txt";
    private BasicTextEncryptor textEncryptor;
    private Key key;

    private TextEncryptor() {

        String json = ResourceLoader.getResource(KEY_FILE);
        key = JsonHelper.getInstance().json.fromJson(json, Key.class);
        textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword(key.key);
    }

    public static String encrypt (String str) {
        return TextEncryptor.getInstance().textEncryptor.encrypt(str);
    }

    public static String decrypt (String str) {
        return TextEncryptor.getInstance().textEncryptor.decrypt(str);
    }
}
