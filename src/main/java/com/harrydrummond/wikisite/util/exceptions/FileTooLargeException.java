package com.harrydrummond.wikisite.util.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FileTooLargeException extends Exception {


    public FileTooLargeException(String msg) {
        super(msg);
    }
}