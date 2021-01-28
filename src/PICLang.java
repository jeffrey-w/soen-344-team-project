import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class PICLang {

    private static final int USAGE_ERROR = 0x10;
    private static final int INVALID_ARGUMENT = 0x20;

    public static void main(String[] args) {
        try {
            run(args[0]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Usage: PICLang <filename>");
            System.exit(USAGE_ERROR);
        }
    }

    private static void run(String file) {
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(file));
            Scanner scanner = new Scanner(new String(bytes));
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
