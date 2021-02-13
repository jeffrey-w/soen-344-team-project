package test;

import main.picl.Scanner;
import main.picl.SymbolTable;
import main.picl.Token;
import main.scanner.IScanner;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ScannerTest {

    static final String SOURCE = "*/+-~& OR =#>=<<=>.,:!?(:= x IF WHILE REPEAT INC DEC ROL ROR 1 ) THEN DO ; END ELSE ELSIF UNTIL RETURN INT SET BOOL CONST BEGIN PROCEDURE MODULE";

    @Test
    public void getToken() {
        IScanner scanner = new Scanner(SOURCE, new SymbolTable());
        for (Token.TokenType expected : Token.TokenType.values()) {
            assertEquals(expected, scanner.getToken().getType());
        }
    }

}