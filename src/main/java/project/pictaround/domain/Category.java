package project.pictaround.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 50, nullable = false)
    private String label;

    @Builder
    public Category(String name, String label) {
        this.name = name;
        this.label = label;
    }

    @OneToMany(mappedBy = "category")
    @JsonManagedReference
    private List<Location> locations = new ArrayList<>();

}
