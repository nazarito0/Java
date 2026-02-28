import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class CharsetConversion {
    public static void main(String[] args) {
        try {
            String utf8String = "Привіт, світ!";

            byte[] utf8Bytes = utf8String.getBytes(StandardCharsets.UTF_8);

            String koi8uString = new String(utf8Bytes, Charset.forName("KOI8-U"));

            System.out.println("Original String (UTF-8): " + utf8String);
            System.out.println("Converted String (KOI8-U): " + koi8uString);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
