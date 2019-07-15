package com.hand.exception;

/**
 * @author senlin.yang@com.hand-china.com
 * @version V1.0
 * @Date 2019-1-29
 * @description
 */
public class UserNotExistException extends RuntimeException {

    private String id;

    public UserNotExistException(String id, String message) {
        super(message);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
