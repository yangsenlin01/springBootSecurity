package com.hand.dto;

/**
 * @author senlin.yang@com.hand-china.com
 * @version V1.0
 * @Date 2019-3-29
 * @description
 */
public class FileInfo {

    private String path;

    public FileInfo(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
