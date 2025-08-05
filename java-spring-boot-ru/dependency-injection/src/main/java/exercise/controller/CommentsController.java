package exercise.controller;

import exercise.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.http.HttpStatus;

import java.util.List;

import exercise.model.Comment;
import exercise.repository.CommentRepository;
import exercise.exception.ResourceNotFoundException;

// BEGIN
@RestController
@RequestMapping("/comments")
public class CommentsController {
    private final CommentRepository commentRepository;

    public CommentsController(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @GetMapping
    ResponseEntity<List<Comment>> getAllComments() {
        return ResponseEntity
                .ok(commentRepository.findAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<Comment> getComment(@PathVariable Long id) {
        var comment = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        return ResponseEntity
                .ok(comment);
    }

    @PostMapping
    ResponseEntity<Comment> createComment(@RequestBody Comment comment) {
        commentRepository.save(comment);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @PutMapping("/{id}")
    ResponseEntity<Comment> updateComment(
            @PathVariable Long id,
            @RequestBody Comment comment
    ) {
        var myComment = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        myComment.setBody(comment.getBody());

        commentRepository.save(myComment);

        return ResponseEntity
                .ok(myComment);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Comment> deleteComment(@PathVariable Long id) {
        var myComment = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        commentRepository.delete(myComment);

        return ResponseEntity
                .ok()
                .build();
    }
}
// END
