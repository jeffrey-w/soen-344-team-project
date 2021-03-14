package main.picl.scanner;

import main.scanner.IScanner;
import main.scanner.IScannerFactory;

/**
 * The type Scanner factory.
 */
public class ScannerFactory implements IScannerFactory
{
    public IScanner getScanner(String source)
    {
        return new Scanner(source);
    }
}

