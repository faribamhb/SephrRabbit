package com.sepehr.sepehr.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
@Entity
@Table(name = "Person")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Person implements Serializable {
    @javax.persistence.Id
    @SequenceGenerator(name = "PersonSeq",allocationSize = 1,sequenceName = "PersonSeq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(name = "name")
    private String name;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
     @JoinColumn(name = "personId")
    List<Request> requests;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }
}
