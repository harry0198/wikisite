package com.harrydrummond.wikisite.util.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidFileTypeException extends Exception {

    public InvalidFileTypeException(String msg) {
        super(msg);
    }
}