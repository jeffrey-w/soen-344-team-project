package main.parser;

/**
 * The {@code Parser} interface specifies the operations for recovering a {@code ISyntaxTree} from a string
 * representation of a source file.
 *
 * @param <T> the type of object that represents nodes in this {@code ISyntaxTree}
 */
public interface IParser<T> {

    /**
     * Provides the {@code ISyntaxTree} derived, according to some grammar, from a string provided to this {@code
     * IParser}**.
     *
     * @return the tree representation of a string supplied to this {@code IParser}
     */
    ISyntaxTree<T> parse();

}
