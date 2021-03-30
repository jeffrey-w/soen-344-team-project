package main.picl.interpreter.printer;

import java.io.PrintWriter;

public abstract class BinaryPrinter
{
    PrintWriter stream;
    int currentLine;

    /**
     *
     * @param left
     * @param right
     * @return an int representing the new line number
     */
    public abstract int print(Object left, Object right);
}
