package com.usyd.edugenie.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class TagTest {

    private Tag tag;
    private UUID tagId;
    private String tagName;

    @BeforeEach
    void setUp() {
        tagId = UUID.randomUUID();
        tagName = "Artificial Intelligence";
        tag = new Tag(tagId, tagName);
    }

    @Test
    void testNoArgsConstructor() {
        Tag emptyTag = new Tag();
        assertThat(emptyTag).isNotNull();
        assertThat(emptyTag.getTagId()).isNull();
        assertThat(emptyTag.getName()).isNull();
    }

    @Test
    void testParameterizedConstructorWithIdAndName() {
        assertThat(tag.getTagId()).isEqualTo(tagId);
        assertThat(tag.getName()).isEqualTo(tagName);
    }

    @Test
    void testParameterizedConstructorWithName() {
        Tag tagWithOnlyName = new Tag(tagName);
        assertThat(tagWithOnlyName.getTagId()).isNull();
        assertThat(tagWithOnlyName.getName()).isEqualTo(tagName);
    }

    @Test
    void testGettersAndSetters() {
        Tag tag = new Tag();
        tag.setTagId(tagId);
        tag.setName("Machine Learning");

        assertThat(tag.getTagId()).isEqualTo(tagId);
        assertThat(tag.getName()).isEqualTo("Machine Learning");
    }

    @Test
    void testEqualsAndHashCode() {
        Tag tag1 = new Tag(tagId, tagName);
        Tag tag2 = new Tag(tagId, tagName);

        assertThat(tag1).isEqualTo(tag2);
        assertThat(tag1.hashCode()).isEqualTo(tag2.hashCode());
    }

    @Test
    void testNotEquals() {
        Tag differentTag = new Tag(UUID.randomUUID(), "Data Science");

        assertThat(tag).isNotEqualTo(differentTag);
    }

    @Test
    void testToString() {
        String expectedString = "Tag{tagId=" + tagId + ", name='" + tagName + "'}";
        assertThat(tag.toString()).isEqualTo(expectedString);
    }
}
