package org.schmuh.quisine.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity class representing a tag associated with recipes.
 *
 * <p>This class is mapped to the "Tag" table in the database, where each record represents a tag.
 * Tags are used to categorize or label recipes. The class uses JPA annotations to define the mapping
 * between the entity and the database table.
 *
 * <ul>
 *     <li><b>id:</b> The unique identifier for the tag (primary key). Generated automatically.</li>
 *     <li><b>name:</b> The name of the tag, which is required to be unique within the table.</li>
 * </ul>
 *
 * <p>Annotations:
 * <ul>
 *     <li>{@link Entity} - Marks this class as a JPA entity that will be mapped to a database table.</li>
 *     <li>{@link Table} - Specifies the name of the database table ("Tag") and the unique constraint
 *         on the "name" column to ensure tag names are unique.</li>
 *     <li>{@link Id} - Marks the primary key field of the entity.</li>
 *     <li>{@link GeneratedValue} - Specifies the strategy for generating the ID value (auto-incremented).</li>
 *     <li>{@link Column} - Defines how the fields map to columns in the database table.</li>
 * </ul>
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="Tag", uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @Column
    public String name;
}
