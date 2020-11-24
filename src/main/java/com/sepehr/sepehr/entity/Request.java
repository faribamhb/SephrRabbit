package com.sepehr.sepehr.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Request")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Request implements Serializable {
    @javax.persistence.Id
    @SequenceGenerator(name = "RequestSeq",allocationSize = 1,sequenceName = "RequestSeq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    private String request;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }
}
