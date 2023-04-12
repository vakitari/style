package edu.bip.client.entity;

import lombok.Data;
import java.util.List;

@Data
public class AuthorEntity {
    private Long id;
    private String name;
    private String surName;
    private String lastName;
    private List<BookEntity> book;



    @Override
    public String toString() {
        return surName + " " + Character.toUpperCase(name.charAt(0)) + ". " + Character.toUpperCase(lastName.charAt(0)) + "." ;
    }
}
