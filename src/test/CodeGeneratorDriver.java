package test;

import main.parser.IParser;
import main.picl.interpreter.CodeGenerator;
import main.picl.interpreter.INode;
import main.picl.parser.Parser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CodeGeneratorDriver {

    private static final int INVALID_ARGUMENT = 0x10;

    public static void main(String[] args) {
        File programDirectory = new File("./programs");
        File[] programs = programDirectory.listFiles();
        if (programs != null) {
            for (File program : programs) {
                String fileName = program.getName().replace(".mod", "");
                System.out.println(fileName);
                run(program.getPath(), fileName + ".out");
            }
        }
    }

    private static void run(String file, String output) {
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(file));
            IParser<INode> parser = new Parser(new String(bytes));
            CodeGenerator generator = new CodeGenerator(parser.parse(), output);
            generator.generate();
        } catch (IOException e) {
            System.err.println("Unable to open '" + file + "'.");
            System.exit(INVALID_ARGUMENT);
        }
    }

}
