package main;

import main.scanner.IScanner;
import main.scanner.PicScanner;
import main.scanner.Token;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PICLang {

    private static final int USAGE_ERROR = 0x10;
    private static final int INVALID_ARGUMENT = 0x20;

    public static void main(String[] args) {
        try {
            run(args[0]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Usage: main.PICLang <filename>");
            System.exit(USAGE_ERROR);
        }
    }

    private static void run(String file) {
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(file));
            IScanner scanner = new PicScanner(new String(bytes));
            Token token;
            while ((token = scanner.getToken()) != Token.EOF) {
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
