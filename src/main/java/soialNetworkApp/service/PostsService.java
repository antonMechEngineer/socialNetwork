package soialNetworkApp.service;

import lombok.RequiredArgsConstructor;
import soialNetworkApp.api.request.FindPostRq;
import soialNetworkApp.api.request.PostRequest;
import soialNetworkApp.api.response.CommonResponse;
import soialNetworkApp.api.response.PostResponse;
import soialNetworkApp.errors.EmptyFieldException;
import soialNetworkApp.errors.PersonNotFoundException;
import soialNetworkApp.mappers.PostMapper;
import soialNetworkApp.model.entities.Person;
import soialNetworkApp.model.entities.Post;
import soialNetworkApp.model.entities.Tag;
import soialNetworkApp.model.enums.FriendshipStatusTypes;
import soialNetworkApp.repository.FriendshipsRepository;
import soialNetworkApp.repository.PersonsRepository;
import soialNetworkApp.repository.PostsRepository;
import soialNetworkApp.service.search.SearchPosts;
import soialNetworkApp.service.util.NetworkPageRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostsService {

    private final PostsRepository postsRepository;
    private final PersonsRepository personsRepository;
    private final FriendshipsRepository friendshipsRepository;
    private final TagsService tagsService;
    private final NotificationsService notificationsService;
    private final PostMapper postMapper;
    private final SearchPosts searchPosts;

    @Value("${socialNetwork.timezone}")
    private String timezone;

    public CommonResponse<PostResponse> createPost(PostRequest postRequest, long userId, Long timestamp) throws PersonNotFoundException {
        Person person = personsRepository.findById(userId).orElse(null);
        if (!validatePerson(person)) {
            throw new PersonNotFoundException("Invalid user id");
        }
        LocalDateTime postPublishingTime = timestamp == null ? LocalDateTime.now(ZoneId.of(timezone)) : new Timestamp(timestamp).toLocalDateTime();
        Post post = postMapper.postRequestToNewPost(postRequest, person, postPublishingTime);
        PostResponse postResponse = postMapper.postToResponse(postsRepository.save(updateTagsInPost(new ArrayList<>(post.getTags()), post)));
        if (person.getPersonSettings() != null && person.getPersonSettings().getPostNotification()) {
            createNotifications(post, person);
        }
        return buildCommonResponse(postResponse);
    }

    public CommonResponse<List<PostResponse>> getFeeds(int offset, int size) {
        Pageable pageable = NetworkPageRequest.of(offset, size);
        Page<Post> postPage = postsRepository.findPostsByTimeBeforeAndIsDeletedFalseOrderByTimeDesc(pageable, LocalDateTime.now(ZoneId.of(timezone)));
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
        post.setTimeDelete(LocalDateTime.now(ZoneId.of(timezone)));
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

    private void createNotifications(Post post, Person person) {
        friendshipsRepository.findFriendshipsByDstPerson(person).forEach(friendship -> {
            if (friendship.getFriendshipStatus().getCode().equals(FriendshipStatusTypes.FRIEND) ||
                    friendship.getFriendshipStatus().getCode().equals(FriendshipStatusTypes.SUBSCRIBED)) {
                notificationsService.createNotification(post, friendship.getSrcPerson());
            }
        });
//        friendshipsRepository.findFriendshipBySrcPerson(person).forEach(friendship -> {
//            if (friendship.getFriendshipStatus().getCode().equals(FriendshipStatusTypes.FRIEND)) {
//                notificationsService.createNotification(post, friendship.getDstPerson());
//            }
//        });
    }
}