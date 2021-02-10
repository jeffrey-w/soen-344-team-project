package main.scanner;

public class UnsupportedLanguageException extends Exception
{
    public UnsupportedLanguageException(String errorMessage) {
        super(errorMessage);
    }
}
