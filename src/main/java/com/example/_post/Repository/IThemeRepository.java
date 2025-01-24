package com.example._post.Repository;

import com.example._post.Model.Theme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IThemeRepository extends JpaRepository<Theme,Long> {
}
