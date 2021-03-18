package main.parser;

/**
 * The {@code ISyntaxTree} interface specifies the operations on an intermediate representation of a source code string.
 *
 * @param <T> the type of object that represents nodes in this {@code ISyntaxTree}
 */
public interface ISyntaxTree<T> {

    /**
     * Provides the root of this {@code ISyntaxTree} from which all other nodes may be reached.
     *
     * @return the head of this {@code ISyntaxTree}
     */
    T getHead();

}
