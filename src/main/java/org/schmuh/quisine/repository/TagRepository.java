package org.schmuh.quisine.repository;

import org.schmuh.quisine.entity.Recipe;
import org.schmuh.quisine.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for managing {@link Tag} entities.
 *
 * <p>This interface extends {@link JpaRepository}, providing CRUD operations for managing
 * {@link Tag} entities. It includes a custom method for finding tags by their name.
 *
 * <ul>
 *     <li>{@link JpaRepository} - Provides basic CRUD operations such as saving, deleting, and finding entities.</li>
 * </ul>
 *
 * <p>Methods:
 * <ul>
 *     <li><b>findByName:</b> Retrieves a {@link Tag} based on its name. Returns an {@link Optional} to handle
 *         cases where the tag may not exist in the database.</li>
 * </ul>
 */
public interface TagRepository extends JpaRepository<Tag, Long> {


    public Optional<Tag> findByName(String name);
}
