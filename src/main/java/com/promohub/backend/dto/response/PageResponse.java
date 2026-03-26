package com.promohub.backend.dto.response;


import org.springframework.data.domain.Page;

import java.util.List;

public class PageResponse<T> {

    private List<T> content;
    private Long totalElements;
    private int totalPages;
    private Boolean first;
    private Boolean last;
    private int pageNumber;
    private int pageSize;

    public PageResponse() {
    }

    public static <T> PageResponse<T> from(Page<T> page) {
        PageResponse<T> r=new PageResponse<>();
        r.content = page.getContent();
        r.totalElements = page.getTotalElements();
        r.totalPages=page.getTotalPages();
        r.pageNumber=page.getNumber();
        r.pageSize=page.getSize();
        r.first = page.isFirst();
        r.last = page.isLast();
        return r;
    }

    public List<T> getContent() {
        return content;
    }

    public Boolean getFirst() {
        return first;
    }

    public Boolean getLast() {
        return last;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }
}
