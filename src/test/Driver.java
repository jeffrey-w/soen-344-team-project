package test;

import main.picl.scanner.ScannerFactory;
import main.scanner.IScanner;
import main.scanner.IScannerFactory;
import main.scanner.IToken;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Driver {

    private static final int INVALID_ARGUMENT = 0x10;

    public static void main(String[] args) {
        File programDirectory = new File("./programs");
        File[] programs = programDirectory.listFiles();
        if (programs != null) {
            for (File program : programs) {
                run(program.getPath());
            }
        }
    }

    private static void run(String file) {
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(file));
            IScannerFactory scannerFactory = new ScannerFactory();
            IScanner scanner = scannerFactory.getScanner(new String(bytes));
            IToken token;
            do {
                token = scanner.getToken();
                System.out.println(token);
            } while (!token.isEOF());
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
