package com.harrydrummond.wikisite.util.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MissingFileException extends Exception {

    public MissingFileException(String msg) {
        super(msg);
    }
}