package com.att.tdp.popcorn_palace.ServiceLayer;

public class Response{
    private String message;
    private boolean isError;
    
    public Response(String message, boolean isError) {
        this.message = message;
        this.isError = isError;
    }

    public String getMessage() {
        return message;
    }


    public boolean isError() {
        return isError;
    }
}
