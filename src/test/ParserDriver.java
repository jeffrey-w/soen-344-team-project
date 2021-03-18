package test;

import main.parser.IParser;
import main.picl.interpreter.PrettyPrinter;
import main.picl.parser.Parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * The type Parser driver.
 */
public class ParserDriver {

    private static final int INVALID_ARGUMENT = 0x10;

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        try {
            byte[] bytes = Files.readAllBytes(Paths.get("./programs/Example.mod"));
            IParser parser = new Parser(new String(bytes));
            PrettyPrinter printer = new PrettyPrinter(parser.parse());
            printer.print();
        } catch (IOException e) {
            System.err.println("Unable to open file.");
            System.exit(INVALID_ARGUMENT);
        }
    }

}
