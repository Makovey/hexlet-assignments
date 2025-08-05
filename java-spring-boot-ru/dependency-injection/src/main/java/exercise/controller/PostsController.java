package exercise.controller;

import exercise.repository.CommentRepository;
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

import exercise.model.Post;
import exercise.repository.PostRepository;
import exercise.exception.ResourceNotFoundException;

// BEGIN
@RestController
@RequestMapping("/posts")
public class PostsController {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    PostsController(PostRepository postRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    @GetMapping
    ResponseEntity<List<Post>> getAllPosts() {
        return ResponseEntity
                .ok(postRepository.findAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<Post> getPost(@PathVariable Long id) {
        var post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post with id " + id + " not found"));
        return ResponseEntity
                .ok(post);
    }

    @PostMapping
    ResponseEntity<Post> createPost(@RequestBody Post post) {
        postRepository.save(post);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @PutMapping("/{id}")
    ResponseEntity<Post> updatePost(
            @PathVariable Long id,
            @RequestBody Post post
    ) {
        var myPost = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        myPost.setBody(post.getBody());
        myPost.setTitle(post.getTitle());

        postRepository.save(myPost);

        return ResponseEntity
                .ok(myPost);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deletePost(@PathVariable Long id) {
        var myPost = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        postRepository.delete(myPost);
        commentRepository.deleteByPostId(myPost.getId());

        return ResponseEntity
                .ok()
                .build();
    }
}
// END
