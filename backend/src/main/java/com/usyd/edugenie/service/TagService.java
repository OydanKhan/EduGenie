package com.usyd.edugenie.service;

import com.usyd.edugenie.entity.StudyNotesTags;
import com.usyd.edugenie.entity.Tag;
import com.usyd.edugenie.repository.StudyNotesTagsRepository;
import com.usyd.edugenie.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private StudyNotesTagsRepository studyNotesTagsRepository;

    public Tag createTag(Tag tag) {
        return tagRepository.save(tag);
    }


    public StudyNotesTags createCourseTag(StudyNotesTags courseTag) {
        return studyNotesTagsRepository.save(courseTag);
    }

    public Optional<Tag>
    getTagById(UUID tagId) {
        return tagRepository.findById(tagId);
    }

    public Optional<Tag> getTagByName(String tagName) {
        Tag tag = new Tag(UUID.randomUUID(), "Data Science");
        tag.setName(tagName);
        Example<Tag> example = Example.of(tag);
        List<Tag> result = tagRepository.findAll(example);
        if (result.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(result.get(0));
    }

    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    public void deleteTag(UUID tagId) {
        tagRepository.deleteById(tagId);
    }


}
