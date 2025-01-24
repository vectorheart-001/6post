package com.example._post.Service;

import com.example._post.DTO.PostCreateDTO;
import com.example._post.Model.Post;
import com.example._post.Repository.IPostRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    @Autowired
    private IPostRepository postRepository;
    public String CreatePost(PostCreateDTO postDTO,String username,int themeId){
        Post post = new Post();
        post.author = username;
        post.title = postDTO.getTitle();
        post.description = postDTO.getDescription();
        post.themeId = String.valueOf(themeId);


        postRepository.save(post);
        return "Created post";
    }

    public List<Post> GetAll(){
        return postRepository.findAll();
    }
    @Transactional
    public void LikePost(int id){
        postRepository.incrementLikes(id);
    }

    public boolean DeletePost(int id,String username){
        Optional<Post> post = postRepository.findById((long) id);
        if(!post.get().getAuthor().equals(username))
        {
            return false;
        }
        postRepository.deleteById((long) id);
        return true;
    }
    @Transactional
    public boolean EditPost(PostCreateDTO postDTO,int id,String username){
        Optional<Post> post = postRepository.findById((long) id);
        if (!post.get().getAuthor().equals(username))
        {
            return false;
        }
        post.get().title = postDTO.getTitle();
        post.get().description = postDTO.getDescription();
        postRepository.save(post.get());
        return true;
    };

    public List<Post> getPostsByTheme(int themeId) {
        var themes = postRepository.findByThemeId(String.valueOf(themeId));
        return postRepository.findByThemeId(String.valueOf(themeId)); // Use findByThemeId
    }

    public List<Post> searchPosts(String searchTerm) {
        return postRepository.searchPosts(searchTerm);
    }

    public List<Post> searchPostsByTheme(String searchTerm, int themeId) {
        return postRepository.searchPostsByTheme(searchTerm, String.valueOf(themeId));
    }

    public List<Post> getPostsByUsername(String username) {
        return postRepository.findByAuthor(username);
    }
}
