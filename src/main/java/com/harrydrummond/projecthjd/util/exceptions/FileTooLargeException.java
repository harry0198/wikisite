package com.harrydrummond.projecthjd.util.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FileTooLargeException extends Exception {


    public FileTooLargeException(String msg) {
        super(msg);
    }
}