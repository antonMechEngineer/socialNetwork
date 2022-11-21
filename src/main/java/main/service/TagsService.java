package main.service;

import lombok.RequiredArgsConstructor;
import main.model.entities.Post;
import main.model.entities.Tag;
import main.repository.TagsRepository;
import org.mapstruct.Named;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagsService {

    private final TagsRepository tagRepository;

    private Tag createTag(String tagName) {
        return tagRepository.save(new Tag(tagName));
    }

    private Tag getTagByTagName(String tagName) {
        Tag tag = tagRepository.findByTagName(tagName);
        return tag == null ? createTag(tagName) : tag;
    }

    @Named("getTagsByStrings")
    public List<Tag> stringsToTagsMapper(List<String> tags) {
        if (tags == null) {
            return new ArrayList<>();
        }
        return tags.stream().map(this::getTagByTagName).collect(Collectors.toList());
    }

    @Named("getStringsByTags")
    public List<String> tagsToStringsMapper(List<Tag> tags) {
        if (tags == null) {
            return new ArrayList<>();
        }
        return tags.stream().map(Tag::getTagName).collect(Collectors.toList());
    }

    public Tag addPostToTag(Tag tag, Post post) {
        List<Post> posts = new ArrayList<>();
        if (tag.getPosts() != null) {
            if (tag.getPosts().contains(post)) {
                return tag;
            }
            posts.addAll(tag.getPosts());
        }
        posts.add(post);
        tag.setPosts(posts);
        return tag;
    }

    public Tag dropPostFromTag(Tag tag, Post post) {
        List<Post> posts = new ArrayList<>(tag.getPosts());
        posts.remove(post);
        tag.setPosts(posts);
        return tagRepository.save(tag);
    }
}
