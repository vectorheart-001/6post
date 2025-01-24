package com.example._post.Repository;

import com.example._post.Model.Post;
import com.example._post.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IPostRepository extends JpaRepository<Post,Long> {
    @Modifying
    @Query("UPDATE Post p SET p.likes = p.likes + 1 WHERE p.id = :id")
    int incrementLikes(@Param("id") int id);
    @Query("SELECT p FROM Post p WHERE p.themeId = :themeId")
    List<Post> findByThemeId(@Param("themeId") String themeId);

    @Query("SELECT p FROM Post p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Post> searchPosts(@Param("searchTerm") String searchTerm);

    @Query("SELECT p FROM Post p WHERE (LOWER(p.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND p.themeId = :themeId")
    List<Post> searchPostsByTheme(@Param("searchTerm") String searchTerm, @Param("themeId") String themeId);

    List<Post> findByAuthor(String author);
}
