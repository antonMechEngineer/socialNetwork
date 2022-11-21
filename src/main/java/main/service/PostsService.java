package main.service;

import lombok.RequiredArgsConstructor;
import main.api.request.PostRequest;
import main.api.response.CommonResponse;
import main.api.response.PostResponse;
import main.mappers.PostMapper;
import main.model.entities.*;
import main.model.enums.LikeTypes;
import main.repository.LikesRepository;
import main.repository.PostsRepository;
import main.service.util.NetworkPageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostsService {

    private final PostsRepository postsRepository;
    private final LikesRepository likesRepository;
    private final PersonsService personsService;
    private final TagsService tagsService;
    private final PostMapper postMapper;

    public CommonResponse<PostResponse> createPost(PostRequest postRequest, long userId, Long timestamp) {
        LocalDateTime postPublishingTime = timestamp == null ? LocalDateTime.now() : new Timestamp(timestamp).toLocalDateTime();
        Post post = postMapper.postRequestToNewPost(postRequest, personsService.getPersonById(userId), postPublishingTime);
        return CommonResponse.<PostResponse>builder()
                .error("success")
                .timestamp(System.currentTimeMillis())
                .data(postMapper.postToResponse(
                        postsRepository.save(
                                updateTagsInPost(new ArrayList<>(post.getTags()), post)), getLikesList(post, LikeTypes.POST).size(), getMyLike(post)))
                .build();
    }

    public CommonResponse<List<PostResponse>> getFeeds(int offset, int size) {
        Pageable pageable = NetworkPageRequest.of(offset, size);
        Page<Post> postPage = postsRepository.findPostsByTimeBeforeAndIsDeletedFalseOrderByTimeDesc(pageable, LocalDateTime.now());
        return CommonResponse.<List<PostResponse>>builder()
                .error("success")
                .timestamp(System.currentTimeMillis())
                .total(postPage.getTotalElements())
                .itemPerPage(postPage.getSize())
                .offset(offset)
                .data(new ArrayList<>(postsToResponse(postPage.getContent())))
                .build();
    }

    public CommonResponse<List<PostResponse>> getAllPostsByAuthor(int offset, int size, Person postsAuthor) {
        Pageable pageable = NetworkPageRequest.of(offset, size);
        Page<Post> postPage = postsRepository.findPostsByAuthorOrderByTimeDesc(pageable, postsAuthor);
        return CommonResponse.<List<PostResponse>>builder()
                .error("success")
                .timestamp(System.currentTimeMillis())
                .offset(offset)
                .perPage(postPage.getSize())
                .total(postPage.getTotalElements())
                .data(new ArrayList<>(postsToResponse(postPage.getContent())))
                .build();
    }

    public CommonResponse<PostResponse> getPostById(long postId) {
        return CommonResponse.<PostResponse>builder()
                .error("success")
                .timestamp(System.currentTimeMillis())
                .data(postMapper.postToResponse(findPostById(postId), getLikesList(findPostById(postId), LikeTypes.POST).size(), getMyLike(findPostById(postId))))
                .build();
    }

    public CommonResponse<PostResponse> updatePost(long postId, PostRequest postRequest) {
        Post post = findPostById(postId);
        post.setTitle(postRequest.getTitle());
        post.setPostText(postRequest.getPostText());
        List<Tag> tags = tagsService.stringsToTagsMapper(postRequest.getTags());
        Post newPost = updateTagsInPost(tags, post);
        return CommonResponse.<PostResponse>builder()
                .error("success")
                .timestamp(System.currentTimeMillis())
                .data(postMapper.postToResponse(postsRepository.save(newPost), getLikesList(post, LikeTypes.POST).size(), getMyLike(post)))
                .build();
    }

    public CommonResponse<PostResponse> changeDeleteStatusInPost(long postId, boolean deleteStatus) {
        Post post = findPostById(postId);
        post.setIsDeleted(deleteStatus);
        post.setTimeDelete(LocalDateTime.now());
        return CommonResponse.<PostResponse>builder()
                .error("success")
                .timestamp(System.currentTimeMillis())
                .data(postMapper.postToResponse(postsRepository.save(post), getLikesList(post, LikeTypes.POST).size(), getMyLike(post)))
                .build();
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
        return posts.stream().map(post -> postMapper.postToResponse(post, getLikesList(post, LikeTypes.POST).size(), getMyLike(post))).collect(Collectors.toList());
    }

    private List<Like> getLikesList(Liked liked, LikeTypes type) {
        return likesRepository.findLikesByEntity(String.valueOf(type), liked.getId());
    }

    private boolean getMyLike(Post post) {
        Person person = personsService.getPersonByEmail((SecurityContextHolder.getContext().getAuthentication().getName()));
        Like like = likesRepository.findLikesByPersonAndEntity(String.valueOf(post.getType()), post.getId(), person.getId()).stream().findFirst().orElse(null);
        return like != null && person.getId().equals(like.getPerson().getId());
    }
}
