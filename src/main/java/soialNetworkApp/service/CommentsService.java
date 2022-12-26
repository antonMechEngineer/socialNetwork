package soialNetworkApp.service;

import lombok.RequiredArgsConstructor;
import soialNetworkApp.api.request.CommentRq;
import soialNetworkApp.api.response.CommentRs;
import soialNetworkApp.api.response.CommonRs;
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
    private final PersonCacheService personCacheService;

    @Value("${socialNetwork.timezone}")
    private String timezone;

    public CommonRs<CommentRs> createComment(Post post, CommentRq commentRq) {
        Person person = personCacheService.getPersonByContext();
        Comment parentComment = getCommentById(commentRq.getParentId());
        post = parentComment == null ? post : null;
        Comment comment = commentRepository.save(commentMapper.commentRequestToNewComment(commentRq, post, person, parentComment));
        if (post != null && person.getPersonSettings() != null && person.getPersonSettings().getPostCommentNotification()) {
            notificationsService.createNotification(comment, post.getAuthor());
        }
        if (post == null && person.getPersonSettings() != null && person.getPersonSettings().getCommentCommentNotification()) {
            notificationsService.createNotification(comment, parentComment.getAuthor());
        }
        return buildCommonResponse(comment);
    }

    public CommonRs<List<CommentRs>> getPostComments(Post post, int offset, int size) {
        Pageable pageable = NetworkPageRequest.of(offset, size);
        Page<Comment> commentPage = commentRepository.findCommentsByPostOrderByTimeAsc(pageable, post);
        return buildCommonResponseList(offset, size, commentPage);
    }

    public CommonRs<CommentRs> editComment(long commentId, CommentRq commentRq) {
        Comment comment = getCommentById(commentId);
        if (personsService.validatePerson(comment.getAuthor())) {
            comment.setCommentText(commentRq.getCommentText());
            comment.setTime(LocalDateTime.now(ZoneId.of(timezone)));
        }
        return buildCommonResponse(commentRepository.save(comment));
    }

    public CommonRs<CommentRs> changeCommentDeleteStatus(long commentId, boolean status) {
        Comment comment = getCommentById(commentId);
        if (personsService.validatePerson(comment.getAuthor())) {
            comment.setIsDeleted(status);
        }
        return buildCommonResponse(commentRepository.save(comment));
    }

    private CommentRs getCommentResponse(Comment comment) {
        CommentRs response = commentMapper.commentToResponse(comment);
        response.setEmbeddedComments(embeddedCommentsToResponse(comment.getEmbeddedComments()));
        return response;
    }

    @Named("getCommentById")
    public Comment getCommentById(long commentId) {
        return commentRepository.findById(commentId).orElse(null);
    }

    @Named("commentsToResponse")
    public List<CommentRs> embeddedCommentsToResponse(List<Comment> comments) {
        if (comments == null) {
            return new ArrayList<>();
        }
        comments.sort(Comparator.comparing(Comment::getTime));
        return comments.stream().map(comment -> {
            CommentRs commentRs = commentMapper.commentToResponse(comment);
            commentRs.setEmbeddedComments(
                    comment.getEmbeddedComments().isEmpty() ?
                            new ArrayList<>() :
                            embeddedCommentsToResponse(comment.getEmbeddedComments()));
            return commentRs;
        }).collect(Collectors.toList());
    }

    private CommonRs<CommentRs> buildCommonResponse(Comment comment) {
        return CommonRs.<CommentRs>builder()
                .timestamp(System.currentTimeMillis())
                .data(getCommentResponse(comment))
                .build();
    }

    private CommonRs<List<CommentRs>> buildCommonResponseList(int offset, int perPage, Page<Comment> comments) {
        return CommonRs.<List<CommentRs>>builder()
                .timestamp(System.currentTimeMillis())
                .offset(offset)
                .itemPerPage(perPage)
                .total(comments.getTotalElements())
                .data(comments.getContent().stream().map(this::getCommentResponse).collect(Collectors.toList()))
                .build();
    }
}
