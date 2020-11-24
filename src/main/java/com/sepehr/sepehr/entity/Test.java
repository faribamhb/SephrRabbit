package com.sepehr.sepehr.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "cities")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Test implements Serializable {
    @javax.persistence.Id
    @SequenceGenerator(name = "testSeq",allocationSize = 1,sequenceName = "testSeq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int Id;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }
}
