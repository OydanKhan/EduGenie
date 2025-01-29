package com.usyd.edugenie.service;

import com.usyd.edugenie.entity.StudyNotesTags;
import com.usyd.edugenie.entity.Tag;
import com.usyd.edugenie.repository.StudyNotesTagsRepository;
import com.usyd.edugenie.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    @Mock
    private TagRepository tagRepository;

    @Mock
    private StudyNotesTagsRepository studyNotesTagsRepository;

    @InjectMocks
    private TagService tagService;

    private Tag tag;
    private UUID tagId;

    @BeforeEach
    void setUp() {
        tagId = UUID.randomUUID();
        tag = new Tag(tagId, "Machine Learning");
    }

    @Test
    void testCreateTag() {
        when(tagRepository.save(any(Tag.class))).thenReturn(tag);

        Tag createdTag = tagService.createTag(tag);

        assertThat(createdTag).isNotNull();
        assertThat(createdTag.getTagId()).isEqualTo(tagId);
        assertThat(createdTag.getName()).isEqualTo("Machine Learning");
        verify(tagRepository, times(1)).save(tag);
    }

    @Test
    void testCreateCourseTag() {
        StudyNotesTags courseTag = new StudyNotesTags();
        when(studyNotesTagsRepository.save(any(StudyNotesTags.class))).thenReturn(courseTag);

        StudyNotesTags createdCourseTag = tagService.createCourseTag(courseTag);

        assertThat(createdCourseTag).isNotNull();
        verify(studyNotesTagsRepository, times(1)).save(courseTag);
    }

    @Test
    void testGetTagById() {
        when(tagRepository.findById(tagId)).thenReturn(Optional.of(tag));

        Optional<Tag> foundTag = tagService.getTagById(tagId);

        assertThat(foundTag).isPresent();
        assertThat(foundTag.get().getTagId()).isEqualTo(tagId);
        verify(tagRepository, times(1)).findById(tagId);
    }

    @Test
    void testGetTagByName() {
        // Using lenient() to avoid strict argument mismatch checks
        Tag exampleTag = new Tag(tagId, "Data Science");
        Example<Tag> example = Example.of(exampleTag);

        lenient().when(tagRepository.findAll(example)).thenReturn(List.of(tag));

        Optional<Tag> foundTag = tagService.getTagByName("Data Science");

        assertThat(foundTag).isPresent();
        assertThat(foundTag.get().getName()).isEqualTo("Machine Learning");
        verify(tagRepository, times(1)).findAll(example);
    }

    @Test
    void testGetAllTags() {
        List<Tag> tagList = List.of(tag);
        when(tagRepository.findAll()).thenReturn(tagList);

        List<Tag> allTags = tagService.getAllTags();

        assertThat(allTags).hasSize(1);
        assertThat(allTags.get(0).getTagId()).isEqualTo(tagId);
        verify(tagRepository, times(1)).findAll();
    }

    @Test
    void testDeleteTag() {
        doNothing().when(tagRepository).deleteById(tagId);

        tagService.deleteTag(tagId);

        verify(tagRepository, times(1)).deleteById(tagId);
    }
}
