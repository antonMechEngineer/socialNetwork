package main.service;

import lombok.RequiredArgsConstructor;
import main.api.request.FindPostRq;
import main.api.request.PostRequest;
import main.api.response.CommonResponse;
import main.api.response.PostResponse;
import main.errors.EmptyFieldException;
import main.errors.PersonNotFoundException;
import main.mappers.PostMapper;
import main.model.entities.Person;
import main.model.entities.Post;
import main.model.entities.Tag;
import main.repository.PersonsRepository;
import main.repository.PostsRepository;
import main.service.search.SearchPosts;
import main.service.util.NetworkPageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostsService {

    private final PostsRepository postsRepository;
    private final PersonsRepository personsRepository;
    private final TagsService tagsService;
    private final PostMapper postMapper;
    private final SearchPosts searchPosts;

    public CommonResponse<PostResponse> createPost(PostRequest postRequest, long userId, Long timestamp) throws PersonNotFoundException {
        Person person = personsRepository.findById(userId).orElse(null);
        if (!validatePerson(person)) {
            throw new PersonNotFoundException("Invalid user id");
        }
        LocalDateTime postPublishingTime = timestamp == null ? LocalDateTime.now() : new Timestamp(timestamp).toLocalDateTime();
        Post post = postMapper.postRequestToNewPost(postRequest, person, postPublishingTime);
        PostResponse postResponse = postMapper.postToResponse(postsRepository.save(updateTagsInPost(new ArrayList<>(post.getTags()), post)));
        return buildCommonResponse(postResponse);
    }

    public CommonResponse<List<PostResponse>> getFeeds(int offset, int size) {
        Pageable pageable = NetworkPageRequest.of(offset, size);
        Page<Post> postPage = postsRepository.findPostsByTimeBeforeAndIsDeletedFalseOrderByTimeDesc(pageable, LocalDateTime.now());
        return buildCommonResponse(offset, size, postPage.getContent(), postPage.getTotalElements());
    }

    public CommonResponse<List<PostResponse>> getAllPostsByAuthor(int offset, int size, Person postsAuthor) {
        Pageable pageable = NetworkPageRequest.of(offset, size);
        Page<Post> postPage = postsRepository.findPostsByAuthorOrderByTimeDesc(pageable, postsAuthor);
        return buildCommonResponse(offset, size, postPage.getContent(), postPage.getTotalElements());
    }

    public CommonResponse<PostResponse> getPostById(long postId) {
        PostResponse postResponse = postMapper.postToResponse(findPostById(postId));
        return buildCommonResponse(postResponse);
    }

    public CommonResponse<PostResponse> updatePost(long postId, PostRequest postRequest) throws PersonNotFoundException {
        Post post = findPostById(postId);
        if (!validatePerson(post.getAuthor())) {
            throw new PersonNotFoundException("Invalid user id");
        }
        post.setTitle(postRequest.getTitle());
        post.setPostText(postRequest.getPostText());
        List<Tag> tags = tagsService.stringsToTagsMapper(postRequest.getTags());
        Post newPost = updateTagsInPost(tags, post);
        PostResponse postResponse = postMapper.postToResponse(postsRepository.save(newPost));
        return buildCommonResponse(postResponse);
    }

    public CommonResponse<PostResponse> changeDeleteStatusInPost(long postId, boolean deleteStatus) throws PersonNotFoundException {
        Post post = findPostById(postId);
        if (!validatePerson(post.getAuthor())) {
            throw new PersonNotFoundException("Invalid user id");
        }
        post.setIsDeleted(deleteStatus);
        post.setTimeDelete(LocalDateTime.now());
        PostResponse postResponse = postMapper.postToResponse(postsRepository.save(post));
        return buildCommonResponse(postResponse);
    }

    public Post findPostById(long postId) {
        return postsRepository.findById(postId).orElse(null);
    }

    private Post updateTagsInPost(List<Tag> tags, Post post) {
        if (post.getTags() != null) {
            List<Tag> oldTags = post.getTags();
            oldTags.removeAll(tags);
            oldTags.forEach(tag -> tagsService.dropPostFromTag(tag, post));
        }
        tags.forEach(tag -> tagsService.addPostToTag(tag, post));
        post.setTags(tags);
        return post;
    }

    private List<PostResponse> postsToResponse(List<Post> posts) {
        return posts.stream().map(postMapper::postToResponse).collect(Collectors.toList());
    }

    private boolean validatePerson(Person person) {
        return person != null && person.equals(personsRepository.findPersonByEmail(
                (SecurityContextHolder.getContext().getAuthentication().getName())).orElse(null));
    }

    public CommonResponse<List<PostResponse>> findPosts(FindPostRq postRq, int offset, int perPage) throws SQLException, EmptyFieldException {
        if (postRq.getText() == null) {
            throw new EmptyFieldException("Field 'text' is required but empty");
        }
        return buildCommonResponse(offset, perPage, searchPosts.findPosts(postRq, offset, perPage), searchPosts.getTotal());
    }

    private CommonResponse<List<PostResponse>> buildCommonResponse(int offset, int perPage, List<Post> posts, Long total) {
        return CommonResponse.<List<PostResponse>>builder()
                .timestamp(System.currentTimeMillis())
                .offset(offset)
                .perPage(perPage)
                .total(total)
                .data(postsToResponse(posts))
                .build();
    }

    private CommonResponse<PostResponse> buildCommonResponse(PostResponse postResponse) {
        return CommonResponse.<PostResponse>builder()
                .timestamp(System.currentTimeMillis())
                .data(postResponse)
                .build();
    }
}
