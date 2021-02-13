package main.picl;

import main.scanner.IScanner;
import main.scanner.IScannerFactory;

public class ScannerFactory implements IScannerFactory
{
    public IScanner getScanner(String source)
    {
        return new Scanner(source, new SymbolTable());
    }
}

