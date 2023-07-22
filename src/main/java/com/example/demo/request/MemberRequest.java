package com.example.demo.request;

public class MemberRequest {

    private String name;

    private Integer page = 1;

    private Integer size = 10;

    public String getName() {
        return name;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getSize() {
        return size;
    }
}
