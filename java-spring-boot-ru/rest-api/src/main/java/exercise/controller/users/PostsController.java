package exercise.controller.users;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

import exercise.model.Post;
import exercise.Data;

// BEGIN
@RestController
@RequestMapping("/api")
class PostsController {

    private static List<Post> posts = Data.getPosts();

    @GetMapping("users/{id}/posts")
    public ResponseEntity<List<Post>> getUserPosts(@PathVariable Integer id) {
        var usersPosts = posts
                .stream()
                .filter(p -> p.getUserId() == id)
                .toList();

        return ResponseEntity
                .ok(usersPosts);
    }

    @PostMapping("/users/{id}/posts")
    public ResponseEntity<Post> createPost(
            @PathVariable Integer id,
            @RequestBody Post post
    ) {
        post.setUserId(id);
        posts.add(post);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(post);
    }
}
// END
