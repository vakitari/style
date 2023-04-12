package edu.bip.client.entity;

import lombok.Data;

@Data
public class BookEntity {
    private Long id;
    private String title;
    private AuthorEntity author;
    private PublishingEntity publishing;
    private String year;
    private String king;
}
