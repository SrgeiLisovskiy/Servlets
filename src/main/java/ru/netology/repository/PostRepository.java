package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;


@Repository
public class PostRepository {
    private Map<Long, Post> posts;
    private AtomicLong postID;

    public PostRepository() {
        posts = new ConcurrentHashMap<>();
        postID = new AtomicLong(0);
    }

    public List<Post> all() {
        return new ArrayList<>(posts.values());
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(posts.get(id));
    }

    public Post save(Post post) {
        if (post.getId() != 0 && posts.containsKey(post.getId())) {
            posts.replace(post.getId(), post);
        } else {
            long newPostID = post.getId() == 0 ? postID.incrementAndGet() : post.getId();
            post.setId(newPostID);
            posts.put(post.getId(), post);
        }
        return post;
    }

    public void removeById(long id) {
        posts.remove(id);
    }
}
