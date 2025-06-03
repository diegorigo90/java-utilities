package it.diegorigo.files;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileInfo {

    private String filename;
    private String path;
    private long size;
    private byte[] data;
}
