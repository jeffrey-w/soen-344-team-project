import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class PICLang {

    private static final int INVALID_ARGUMENT = 0x10;
    private static final int DATA_ERROR = 0x20;

    public static void main(String[] args) {
        try {
            run(args[0]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Usage: java PICLang <filename>");
            System.exit(DATA_ERROR);
        }
    }

    private static void run(String file) {
        try {
            Scanner scanner = new Scanner(new String(Files.readAllBytes(Path.of(file))));
            List<Token> tokens = scanner.scan();
            for (Token token : tokens) {
                System.out.println(token);
            }
        } catch (IOException e) {
            System.err.println("Unable to open " + file + ".");
            System.exit(INVALID_ARGUMENT);
        }
    }

    // This class is not instantiable.
    private PICLang() {
        // In case a maintainer tries to do so.
        throw new AssertionError();
    }

}
