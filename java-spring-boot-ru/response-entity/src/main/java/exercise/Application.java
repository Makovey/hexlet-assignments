package exercise;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import exercise.model.Post;
import lombok.Setter;
import org.springframework.web.server.ResponseStatusException;

@SpringBootApplication
@RestController
public class Application {
    // Хранилище добавленных постов
    @Setter
    private static  List<Post> posts = Data.getPosts();

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // BEGIN
    @GetMapping("/posts")
    ResponseEntity<List<Post>> getPosts() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .header("X-Total-Count", String.valueOf(posts.size()))
                .body(posts);
    }

    @GetMapping("/posts/{id}")
    ResponseEntity<Post> getPost(@PathVariable String id) {
        Optional<Post> optionalPost = posts
                .stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();

        return optionalPost.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/posts")
    ResponseEntity<Post> createPost(
            @RequestBody Post post
    ) {
        posts.add(post);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(post);
    }

    @PutMapping("/posts/{id}")
    ResponseEntity<Post> updatePost(
            @PathVariable String id,
            @RequestBody Post post
    ) {
        Optional<Post> optionalPost = posts
                .stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();

        if (optionalPost.isPresent()) {
            Post updatablePost = optionalPost.get();
            updatablePost.setTitle(post.getTitle());
            updatablePost.setBody(post.getBody());
            updatablePost.setId(post.getId());

            return ResponseEntity
                    .ok()
                    .body(post);
        }

        return ResponseEntity
                .notFound()
                .build();
    }

    // END

    @DeleteMapping("/posts/{id}")
    public void destroy(@PathVariable String id) {
        posts.removeIf(p -> p.getId().equals(id));
    }
}
