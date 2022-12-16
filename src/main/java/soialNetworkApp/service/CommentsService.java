package soialNetworkApp.service;

import lombok.RequiredArgsConstructor;
import soialNetworkApp.api.request.CommentRequest;
import soialNetworkApp.api.response.CommentResponse;
import soialNetworkApp.api.response.CommonResponse;
import soialNetworkApp.mappers.CommentMapper;
import soialNetworkApp.model.entities.Comment;
import soialNetworkApp.model.entities.Person;
import soialNetworkApp.model.entities.Post;
import soialNetworkApp.repository.CommentsRepository;
import soialNetworkApp.service.util.NetworkPageRequest;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentsService {

    private final CommentsRepository commentRepository;
    private final PersonsService personsService;
    private final NotificationsService notificationsService;
    private final CommentMapper commentMapper;

    @Value("${socialNetwork.timezone}")
    private String timezone;

    public CommonResponse<CommentResponse> createComment(Post post, CommentRequest commentRequest) {
        Person person = personsService.getPersonByContext();
        Comment parentComment = getCommentById(commentRequest.getParentId());
        post = parentComment == null ? post : null;
        Comment comment = commentRepository.save(commentMapper.commentRequestToNewComment(commentRequest, post, person, parentComment));
        if (post != null && person.getPersonSettings() != null && person.getPersonSettings().getPostCommentNotification()) {
            notificationsService.createNotification(comment, post.getAuthor());
        }
        if (post == null && person.getPersonSettings() != null && person.getPersonSettings().getCommentCommentNotification()) {
            notificationsService.createNotification(comment, parentComment.getAuthor());
        }
        return buildCommonResponse(comment);
    }

    public CommonResponse<List<CommentResponse>> getPostComments(Post post, int offset, int size) {
        Pageable pageable = NetworkPageRequest.of(offset, size);
        Page<Comment> commentPage = commentRepository.findCommentsByPostOrderByTimeAsc(pageable, post);
        return buildCommonResponseList(offset, size, commentPage);
    }

    public CommonResponse<CommentResponse> editComment(long commentId, CommentRequest commentRequest) {
        Comment comment = getCommentById(commentId);
        if (personsService.validatePerson(comment.getAuthor())) {
            comment.setCommentText(commentRequest.getCommentText());
            comment.setTime(LocalDateTime.now(ZoneId.of(timezone)));
        }
        return buildCommonResponse(commentRepository.save(comment));
    }

    public CommonResponse<CommentResponse> changeCommentDeleteStatus(long commentId, boolean status) {
        Comment comment = getCommentById(commentId);
        if (personsService.validatePerson(comment.getAuthor())) {
            comment.setIsDeleted(status);
        }
        return buildCommonResponse(commentRepository.save(comment));
    }

    private CommentResponse getCommentResponse(Comment comment) {
        CommentResponse response = commentMapper.commentToResponse(comment);
        response.setEmbeddedComments(embeddedCommentsToResponse(comment.getEmbeddedComments()));
        return response;
    }

    @Named("getCommentById")
    public Comment getCommentById(long commentId) {
        return commentRepository.findById(commentId).orElse(null);
    }

    @Named("commentsToResponse")
    public List<CommentResponse> embeddedCommentsToResponse(List<Comment> comments) {
        if (comments == null) {
            return new ArrayList<>();
        }
        comments.sort(Comparator.comparing(Comment::getTime));
        return comments.stream().map(comment -> {
            CommentResponse commentResponse = commentMapper.commentToResponse(comment);
            commentResponse.setEmbeddedComments(
                    comment.getEmbeddedComments().isEmpty() ?
                            new ArrayList<>() :
                            embeddedCommentsToResponse(comment.getEmbeddedComments()));
            return commentResponse;
        }).collect(Collectors.toList());
    }

    private CommonResponse<CommentResponse> buildCommonResponse(Comment comment) {
        return CommonResponse.<CommentResponse>builder()
                .timestamp(System.currentTimeMillis())
                .data(getCommentResponse(comment))
                .build();
    }

    private CommonResponse<List<CommentResponse>> buildCommonResponseList(int offset, int perPage, Page<Comment> comments) {
        return CommonResponse.<List<CommentResponse>>builder()
                .timestamp(System.currentTimeMillis())
                .offset(offset)
                .itemPerPage(perPage)
                .total(comments.getTotalElements())
                .data(comments.getContent().stream().map(this::getCommentResponse).collect(Collectors.toList()))
                .build();
    }
}
