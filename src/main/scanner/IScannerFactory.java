package main.scanner;

public interface IScannerFactory
{
    IScanner getScanner(String source);
}
