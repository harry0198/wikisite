package com.harrydrummond.projecthjd.util.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CopyFileException extends Exception {

    public CopyFileException(String msg) {
        super(msg);
    }
}