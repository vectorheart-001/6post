package com.example._post.Controller;

import com.example._post.DTO.PostCreateDTO;
import com.example._post.DTO.RegisterDTO;
import com.example._post.Model.Post;
import com.example._post.Service.PostService;
import com.example._post.Util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/post")
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private JWTUtil jwtUtil;
    @GetMapping(value = "/get_test")
    public String get_test() {
        return "Authed!";
    }

    @PostMapping(value = "/create_post/{themeId}")
    public String create_post(@RequestHeader("Authorization") String authHeader,@PathVariable int themeId, @RequestBody PostCreateDTO postDTO) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.extractUsername(token);
        postService.CreatePost(postDTO,username,themeId);
        return "Created post";
    }

    @GetMapping(value = "/get_all")
    public List<Post> get_all() {
        return postService.GetAll();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Post>> searchPosts(@RequestParam(value = "searchTerm", required = false, defaultValue = "") String searchTerm, @RequestParam(value = "themeId", required = false) Integer themeId) {
        List<Post> posts;
        if (themeId != null) {
            posts = postService.searchPostsByTheme(searchTerm, themeId);
        } else {
            posts = postService.searchPosts(searchTerm);
        }
        return ResponseEntity.ok(posts);
    }
    @PutMapping(value = "/edit_post/{id}")
    public ResponseEntity<?> edit_post(@RequestBody PostCreateDTO postDTO,@PathVariable int id,@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.extractUsername(token);
        var response = postService.EditPost(postDTO,id,username);
        if(response == false)
        {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/delete_post")
    public ResponseEntity<?> edit_post(int id,@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.extractUsername(token);
        var response = postService.DeletePost(id,username);
        if(!response)
        {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/like_post")
    public void like_post(int id) {
        postService.LikePost(id);
    }

    @GetMapping("/get_by_theme/{themeId}")
    public List<Post> getPostsByTheme(@PathVariable int themeId) {
        return postService.getPostsByTheme(themeId); // Assuming you have this service method
    }

    @GetMapping("/get_user_posts")
    public ResponseEntity<List<Post>> getUserPosts(@RequestHeader("Authorization") String authHeader){
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.extractUsername(token);
        return ResponseEntity.ok(postService.getPostsByUsername(username));
    }
}
