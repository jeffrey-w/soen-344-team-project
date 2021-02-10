package main.picl;

import main.picl.Scanner;
import main.scanner.IScanner;
import main.scanner.IScannerFactory;
import main.scanner.UnsupportedLanguageException;

public class ScannerFactory implements IScannerFactory
{
    public IScanner getScanner(String source) throws UnsupportedLanguageException
    {
        return new Scanner(source);
    }
}

