package test;

import main.picl.scanner.Scanner;
import main.picl.scanner.Token;
import main.scanner.IScanner;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * The type Scanner test.
 */
public class ScannerTest {

    /**
     * The Source.
     */
    static final String SOURCE =
            "*/+-~& OR =#>=<<=>.,:!?(:= x IF WHILE REPEAT INC DEC ROL ROR 1 ) THEN DO ; END ELSE ELSIF UNTIL RETURN "
                    + "INT SET BOOL CONST BEGIN PROCEDURE MODULE";

    /**
     * Gets token.
     */
    @Test
    public void getToken() {
        IScanner scanner = new Scanner(SOURCE);
        for (Token.TokenType expected : Token.TokenType.values()) {
            assertEquals(expected, scanner.getToken().getType());
        }
    }

}