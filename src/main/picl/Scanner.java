package main.picl;

import main.scanner.AbstractScanner;
import main.tables.IBranchTable;
import main.tables.IKeywordTable;
import main.tokens.IToken;

/**
 * The {@code PICScanner} provides an concrete implementation of the {@code IScanner} interface. It reads source files
 * written in the PIC programming language created by Niklaus Wirth.
 */
public class PICScanner extends AbstractScanner {

    private static final IBranchTable SYMBOLS = new BranchTable();
    private static final IKeywordTable KEYWORDS = new KeywordTable();

    /**
     * Creates a new {@code PICScanner} to read the specified {@code source} file.
     *
     * @param sourceFileContent the source file content that will be read by the returned {@code PICScanner}
     * @throws NullPointerException if the specified {@code source} is {@code null}
     */
    public PICScanner(String sourceFileContent) {
        super(sourceFileContent);
    }

    @Override
    protected boolean isSymbol() {
        return SYMBOLS.isSymbol(peekCharacter());
    }

    @Override
    protected IToken scanSymbol() {
        return SYMBOLS.tokenFor(nextCharacter(), peekCharacter(), getPosition());
    }

    @Override
    protected boolean isNumber() {
        return peekCharacter() == '$' || ( peekCharacter() >= '0' && peekCharacter() <= '9' );
    }

    @Override
    protected IToken scanNumber() {
        if (peekCharacter() == '$') {
            return hexNumber();
        }
        while (isDigit(peekCharacter())) { // TODO error on more than 3 digits
            nextCharacter();
        }
        return new PICToken(PICToken.TokenType.NUMBER, currentLexeme());
    }

    private IToken hexNumber() {
        do {
            nextCharacter();
        } while (isHexDigit(peekCharacter())); // TODO error on more than 2 digits
        return new PICToken(PICToken.TokenType.NUMBER, currentLexeme());
    }

    private boolean isHexDigit(char character) {
        return isDigit(character) || ( character >= 'A' && character <= 'F' );
    }

    private boolean isDigit(char character) {
        return character >= '0' && character <= '9';
    }

    private boolean isAlphabet(char character) {
        return character >= 'A' && character <= 'z';
    }

    @Override
    protected IToken scanIdentifier() {
        // TODO error if peekCharacter is not alpha
        while (isAlphanumeric(peekCharacter())) {
            nextCharacter();
        }
        return KEYWORDS.tokenFor(currentLexeme());
    }

    private boolean isAlphanumeric(char character) {
        return isDigit(character) || isAlphabet(character);
    }

}
