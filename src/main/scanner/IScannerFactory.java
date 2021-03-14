package main.scanner;

/**
 * The interface Scanner factory.
 */
public interface IScannerFactory
{
    /**
     * Gets scanner.
     *
     * @param source the source
     * @return the scanner
     */
    IScanner getScanner(String source);
}
