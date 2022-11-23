package main.errors;

public class FileAlreadyUploadException extends Exception{

    public FileAlreadyUploadException(String message) {
        super(message);
    }
}
