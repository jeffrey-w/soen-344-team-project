package main.picl.interpreter.printer;

import main.parser.Environment;

import java.io.PrintWriter;

public class AssignmentExprPrinter extends BinaryExprPrinter
{
    public AssignmentExprPrinter(PrintWriter stream, int currentLine){
        this.stream = stream;
        this.currentLine = currentLine;
    }

    @Override
    public int print(Object left, Object right) {
        Object value = null;
        String mnemonic = null;
        if (right instanceof Integer && (Integer) right == 0) {
            stream.println(currentLine++ + " CLRF " + ((Environment.EntryInfo)left).value);
            return currentLine;
        } else if (right instanceof Integer) {
            mnemonic = " MOVLW ";
            value = right;
        } else if (right instanceof Environment.EntryInfo){
            value = ((Environment.EntryInfo) right).value;
            if (((Environment.EntryInfo) right).type == null) {
                mnemonic = " MOVLW ";
            } else {
                mnemonic = " MOVFW ";
            }
        }
        stream.println(currentLine++ + mnemonic + value);
        stream.println(currentLine++ + " MOVWF " + ((Environment.EntryInfo)left).value);
        return currentLine;
    }
}
