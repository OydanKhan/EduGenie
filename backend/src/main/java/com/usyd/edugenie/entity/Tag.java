package com.usyd.edugenie.entity;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "Tag")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID tagId;

    @Column(length = 50, nullable = false, unique = true)
    private String name;

    // Default constructor
    public Tag() {
        // Default constructor for JPA
    }

    // Parameterized constructor for creating instances with both ID and name
    public Tag(UUID tagId, String name) {
        this.tagId = tagId;
        this.name = name;
    }

    // Parameterized constructor for creating instances with just the name
    public Tag(String name) {
        this.name = name;
    }

    // Getters and setters
    public UUID getTagId() {
        return tagId;
    }

    public void setTagId(UUID tagId) {
        this.tagId = tagId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(tagId, tag.tagId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagId);
    }

    @Override
    public String toString() {
        return "Tag{" +
            "tagId=" + tagId +
            ", name='" + name + '\'' +
            '}';
    }
}

