package com.springboot.blog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)  //Spring Boot response HTTP status whenever the Exception occur
public class ResourceNotFoundException  extends RuntimeException{
    private String resourceName;
    private String fileName;
    private long fileValue;

    public ResourceNotFoundException(String resourceName, String fileName, long fileValue) {
        super(String.format("%s not found with %s:'%S'", resourceName, fileName, fileValue)); // EX: post not found with id:1
        this.resourceName = resourceName;
        this.fileName = fileName;
        this.fileValue = fileValue;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getFileName() {
        return fileName;
    }

    public long getFileValue() {
        return fileValue;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFileValue(long fileValue) {
        this.fileValue = fileValue;
    }



    }


