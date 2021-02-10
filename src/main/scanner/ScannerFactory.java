package main.scanner;

import main.picl.Scanner;

public class ScannerFactory
{
    public IScanner getScanner(String language, String source) throws UnsupportedLanguageException
    {
        if(language.equalsIgnoreCase("PICL"))
            return new Scanner(source);
        else
            throw new UnsupportedLanguageException("The specified language '" + language
                    + "' is not supported by this compiler.");
    }
}

