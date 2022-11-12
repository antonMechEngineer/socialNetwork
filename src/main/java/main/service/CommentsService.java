package main.service;

import main.api.request.CommentRequest;
import main.model.entities.Comment;
import main.model.entities.Post;
import main.repository.CommentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommentsService {

    private final CommentsRepository commentRepository;

    @Autowired
    public CommentsService(CommentsRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment createComment(Post post, CommentRequest commentRequest) {
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setParentComment(commentRepository.findById(commentRequest.getParentId()).orElse(null));
        comment.setCommentText(commentRequest.getCommentText());
        comment.setTime(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    public Page<Comment> getAllComments(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return commentRepository.findAll(pageable);
    }

    public Comment changeComment(long commentId, String newText) {
        Comment comment = commentRepository.findById(commentId).get();
        comment.setCommentText(newText);
        comment.setTime(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    public Comment deleteComment(long commentId) {
        Comment comment = commentRepository.findById(commentId).get();
        comment.setIsDeleted(true);
        return commentRepository.save(comment);
    }
}
