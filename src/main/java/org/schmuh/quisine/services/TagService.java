package org.schmuh.quisine.services;

import lombok.NoArgsConstructor;
import org.schmuh.quisine.entity.Recipe;
import org.schmuh.quisine.entity.Tag;
import org.schmuh.quisine.repository.RecipeRepository;
import org.schmuh.quisine.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@NoArgsConstructor
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    public List<Tag> findAll() {
        return this.tagRepository.findAll();
    }

    public void save(Tag tag) {
        this.tagRepository.save(tag);
    }

    public void saveAll(List<Tag> tags) {
        this.tagRepository.saveAll(tags);
    }
}
