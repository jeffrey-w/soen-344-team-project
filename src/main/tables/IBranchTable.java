package main.tables;

import main.scanner.Position;
import main.tokens.IToken;

/**
 * The {@code IBranchTable} specifies operations related to dispatching on characters to read a reserved symbol in a
 * formal language.
 */
public interface IBranchTable {

    /**
     * Determines whether or not the specified {@code character} is a reserved symbol.
     *
     * @return {@code true} if the specified {@code character} is a reserved symbol
     */
    boolean isSymbol(char character);

    /**
     * Dispatches on the specified {@code character} (and optionally the {@code lookahead} character) to provide the
     * {@code IToken} for the reserved symbol at the specified {@code Position} in a source file.
     *
     * @param character the literal representation of the reserved symbol being read
     * @param lookahead the character just after the other specified {@code character}
     * @param position the {@code Position} of the specified {@code character} in the source file in which it appears;
     * should the {@code lookahead} character belong to the same reserved symbol as the specified {@code character}, the
     * current index of the position is advanced
     * @return the {@code IToken} for the reserved symbol represented by the specified {@code character} (and possibly
     * the specified {@code lookahead} character as well)
     */
    IToken tokenFor(char character, char lookahead, Position position);

}
