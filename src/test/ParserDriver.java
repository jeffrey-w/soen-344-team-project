package test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import main.picl.interpreter.PrettyPrinter;
import main.picl.parser.Parser;

public class ParserDriver {
    public static void main(String[] args) {
        try {
            byte[] bytes = Files.readAllBytes(Paths.get("./programs/Example.mod"));
            Parser parser = new Parser(new String(bytes));
            PrettyPrinter printer = new PrettyPrinter(parser.parse());
            printer.print();
        } catch (IOException e) {
            System.err.println("Unable to open file.");
            System.exit(0x10); // TODO
        }
    }
}
