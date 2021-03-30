package main.picl.interpreter.printer;

import java.io.PrintWriter;

public abstract class BinaryExprPrinter
{
    private PrintWriter stream;
    private int currentLine;

    /**
     *
     * @param left
     * @param right
     * @return an int representing the new line number
     */
    public abstract int print(Object left, Object right);
}
