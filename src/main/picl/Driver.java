package main.picl;

import main.scanner.IScanner;
import main.scanner.IScannerFactory;
import main.scanner.UnsupportedLanguageException;
import main.tokens.IToken;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Driver {

    private static final int USAGE_ERROR = 0x10;
    private static final int INVALID_ARGUMENT = 0x20;

    public static void main(String[] args) {
        try {
            run(args[0]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Usage: main.picl.Driver <filename>");
            System.exit(USAGE_ERROR);
        }
    }

    private static void run(String file) {
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(file));
            IScannerFactory scannerFactory = new ScannerFactory();
            try {
                IScanner scanner = scannerFactory.getScanner(new String(bytes));
                IToken token;
                do {
                    token = scanner.getToken();
                    System.out.println(token);
                } while (!token.isEOF());
            }
            catch (UnsupportedLanguageException e)
            {
                System.out.println(e.getMessage());
            }
        } catch (IOException e) {
            System.err.println("Unable to open " + file + ".");
            System.exit(INVALID_ARGUMENT);
        }
    }

    // This class is not instantiable.
    private Driver() {
        // In case a maintainer tries to do so.
        throw new AssertionError();
    }

}
