package com.rcastro.customer.management.customermanagement.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;

import javax.validation.constraints.Min;

public class Pagination  {

    public static enum SortOrder {
        ASC, DESC;

        public boolean isDescending(){
            return this.equals(DESC);
        }
    }

    @Min(0)
    private int page;

    @Min(1)
    private int size;

    @NonNull
    private SortOrder sort;

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public SortOrder getSort() {
        return sort;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setSort(SortOrder sort) {
        this.sort = sort;
    }

    public Pageable getPageable(){

        if(sort.isDescending()){
            return PageRequest.of(page, size, Sort.by(Sort.Direction.DESC));
        }

        return PageRequest.of(page, size, Sort.by(Sort.Direction.ASC,"_id"));
    }
}
