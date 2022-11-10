//package main.service;
//
//import main.model.entities.Post;
//import main.repository.PostsRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.stereotype.Service;
//
//import java.util.logging.Logger;
//
//@Service
//public class PostsService {
//
//    private final PostsRepository postsRepository;
//
//    @Autowired
//    public PostsService(PostsRepository postsRepository) {
//        this.postsRepository = postsRepository;
//    }
//
//    public Page<Post> getAllPosts(int page, int size) {
//        Logger.getLogger(this.getClass().getName()).info("getAllPosts with page " + page + " and size " + size);
//        Pageable pageable = PageRequest.of(page, size, Sort.by("time").descending());
//        return postsRepository.findAll(pageable);
//    }
//}
