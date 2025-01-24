package com.example._post.Service;

import com.example._post.Model.Theme;
import com.example._post.Repository.IThemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ThemeService {
    @Autowired
    private IThemeRepository themeRepository;

    // Get all themes
    public List<Theme> getAllThemes() {
        return themeRepository.findAll();
    }

    // Get a theme by ID
    public Theme getThemeById(int id) {
        return themeRepository.findById((long) id)
                .orElseThrow(() -> new RuntimeException("Theme not found with id: " + id));
    }

    // Add a new theme
    public Theme addTheme(Theme theme) {
        return themeRepository.save(theme);
    }

    // Update an existing theme
    public Theme updateTheme(int id, Theme updatedTheme) {
        Theme existingTheme = getThemeById(id);
        existingTheme.setName(updatedTheme.getName());
        existingTheme.setDescription(updatedTheme.getDescription());
        return themeRepository.save(existingTheme);
    }

    // Delete a theme by ID
    public void deleteTheme(int id) {
        themeRepository.deleteById((long) id);
    }

}
