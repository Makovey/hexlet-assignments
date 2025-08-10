package exercise.controller;

import exercise.model.Comment;
import exercise.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

import exercise.model.Post;
import exercise.repository.PostRepository;
import exercise.exception.ResourceNotFoundException;
import exercise.dto.PostDTO;
import exercise.dto.CommentDTO;

// BEGIN
@RestController
@RequestMapping("/posts")
public class PostsController {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @GetMapping
    ResponseEntity<List<PostDTO>> getAllPosts() {
        var posts = postRepository
                .findAll()
                .stream()
                .map(this::toPostDTO)
                .toList();

        posts.forEach(
                p -> {
                    var comments = commentRepository
                            .findByPostId(p.getId())
                            .stream()
                            .map(this::toCommentDTO)
                            .toList();

                    p.setComments(comments);
                }
        );

        return ResponseEntity
                .ok(posts);
    }

    @GetMapping("/{id}")
    ResponseEntity<PostDTO> getPostById(
            @PathVariable Long id
    ) {
        var post = postRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post with id " + id + " not found"));

        var comments = commentRepository
                .findByPostId(post.getId())
                .stream()
                .map(this::toCommentDTO)
                .toList();

        var dto = toPostDTO(post);
        dto.setComments(comments);

        return ResponseEntity
                .ok(dto);
    }

    private PostDTO toPostDTO(Post post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setTitle(post.getTitle());
        postDTO.setBody(post.getBody());

        return postDTO;
    }

    private CommentDTO toCommentDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setBody(comment.getBody());

        return commentDTO;
    }
}
// END
