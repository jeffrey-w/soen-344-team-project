package test;

import main.picl.SymbolTable;
import main.picl.Token;
import main.scanner.ISymbolTable;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SymbolTableTest {

    ISymbolTable symbolTable;

    @Before
    public void setUp() {
        symbolTable = new SymbolTable();
    }

    @Test
    public void testAddTypeFor() {
        try {
            symbolTable.addTypeFor("test", null);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetTypeFor() {
        assertEquals(Token.TokenType.MODULE, symbolTable.getTypeFor("MODULE"));
        assertEquals(Token.TokenType.IDENTIFIER, symbolTable.getTypeFor("TEST"));
    }

}
