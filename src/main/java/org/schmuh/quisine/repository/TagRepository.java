package org.schmuh.quisine.repository;

import org.schmuh.quisine.entity.Recipe;
import org.schmuh.quisine.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {


    public Optional<Tag> findByName(String name);
}
