package com.example._post.Controller;

import com.example._post.Model.Theme;
import com.example._post.Service.ThemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/theme")
public class ThemeController {
    @Autowired
    private ThemeService themeService;

    // Get all themes
    @GetMapping("/get-all")
    public List<Theme> getAllThemes() {
        return themeService.getAllThemes();
    }

    // Get a theme by ID
    @GetMapping("/{id}")
    public ResponseEntity<Theme> getThemeById(@PathVariable int id) {
        Theme theme = themeService.getThemeById(id);
        return ResponseEntity.ok(theme);
    }

    @PostMapping("/create")
    public ResponseEntity<Theme> addTheme(@RequestBody Theme theme) {
        Theme createdTheme = themeService.addTheme(theme);
        return new ResponseEntity<>(createdTheme, HttpStatus.CREATED);
    }

    // Update a theme by ID
    @PutMapping("/{id}")
    public ResponseEntity<Theme> updateTheme(@PathVariable int id, @RequestBody Theme theme) {
        Theme updatedTheme = themeService.updateTheme(id, theme);
        return ResponseEntity.ok(updatedTheme);
    }

    // Delete a theme by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable int id) {
        themeService.deleteTheme(id);
        return ResponseEntity.noContent().build();
    }
}
