package org.schmuh.quisine.services;

import lombok.NoArgsConstructor;
import org.schmuh.quisine.entity.Recipe;
import org.schmuh.quisine.entity.Tag;
import org.schmuh.quisine.repository.RecipeRepository;
import org.schmuh.quisine.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing {@link Tag} entities.
 *
 * <p>This service provides methods for retrieving and saving {@link Tag} entities. It interacts with the
 * {@link TagRepository} to handle the persistence of tags, including functionality to save tags individually and in bulk.
 */
@Service
@NoArgsConstructor
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    /**
     * Retrieves a list of all {@link Tag} entities from the database.
     *
     * <p>This method uses the {@link TagRepository} to fetch all tag records from the database.
     * It returns a list containing all the {@link Tag} entities.
     *
     * @return A list of all {@link Tag} entities.
     */
    public List<Tag> findAll() {
        return this.tagRepository.findAll();
    }

    /**
     * Saves a given {@link Tag} entity to the database.
     *
     * <p>This method uses the {@link TagRepository} to save the provided {@link Tag} entity.
     * If the tag already exists, it will be updated; otherwise, a new tag will be created.
     *
     * @param tag The {@link Tag} entity to be saved.
     */
    public void save(Tag tag) {
        this.tagRepository.save(tag);
    }

    /**
     * Saves a list of {@link Tag} entities to the database.
     *
     * <p>This method uses the {@link TagRepository} to save all the provided {@link Tag} entities.
     * If any tags already exist, they will be updated; otherwise, new tags will be created.
     *
     * @param tags The list of {@link Tag} entities to be saved.
     */
    public void saveAll(List<Tag> tags) {
        this.tagRepository.saveAll(tags);
    }
}
