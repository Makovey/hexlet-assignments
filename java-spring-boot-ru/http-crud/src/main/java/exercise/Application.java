package exercise;

import java.util.List;
import java.util.Optional;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import exercise.model.Post;

@SpringBootApplication
@RestController
public class Application {
    // Хранилище добавленных постов
    private List<Post> posts = Data.getPosts();

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // BEGIN
    @GetMapping("/posts")
    List<Post> getPosts() {
        return posts;
    }

    @GetMapping("/posts/{id}")
    Optional<Post> getPostById(@PathVariable String id) {
        return posts
                .stream()
                .filter(x -> x.getId().equals(id))
                .findFirst();
    }

    @PostMapping("/posts")
    Post createPost(@RequestBody Post post) {
        posts.add(post);
        return post;
    }

    @PutMapping("/posts/{id}")
    Post updatePost(@PathVariable String id, @RequestBody Post post) {
        Optional<Post> passiblyPost = posts
                .stream()
                .filter(x -> x.getId().equals(id))
                .findFirst();

        if (passiblyPost.isPresent()) {
            Post updatetablePost = passiblyPost.get();
            updatetablePost.setId(post.getId());
            updatetablePost.setBody(post.getBody());
            updatetablePost.setTitle(post.getTitle());
        }

        return post;
    }

    @DeleteMapping("/posts/{id}")
    void deletePost(@PathVariable String id) {
        posts.stream().filter(x -> x.getId().equals(id)).findFirst().ifPresent(posts::remove);
    }
    // END
}
