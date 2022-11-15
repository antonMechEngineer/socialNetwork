package main.service;

import main.model.entities.Tag;
import main.repository.TagsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagsService {

    private final TagsRepository tagRepository;

    @Autowired
    public TagsService(TagsRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    private Tag createTag(String tagName) {
        return tagRepository.save(new Tag(tagName));
    }

    public Tag getTagById(long tagId) {
        return tagRepository.findById(tagId).get();
    }

    public Tag getTagByTagName(String tagName) {
        Tag tag = tagRepository.findByTagName(tagName);
        return tag == null ? createTag(tagName) : tag;
    }
}
