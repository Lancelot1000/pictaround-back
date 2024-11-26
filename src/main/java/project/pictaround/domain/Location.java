package project.pictaround.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Location {

    @Id
    @GeneratedValue
    @Column(name = "location_id")
    private Long id;

    private double latitude;

    private double longitude;

    private String name;

    private String address;

    @Setter
    private Long bestReviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @JsonBackReference
    private Category category;

    @Builder
    public Location(double latitude, double longitude, String name, String address, Category category) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.address = address;
        this.category = category;
    }


    @OneToMany(mappedBy = "location")
    @JsonManagedReference
    private List<Review> reviews = new ArrayList<>();

    // 연관관계 메서드
    public void setCategory(Category category) {
        this.category = category;
        category.getLocations().add(this);
    }
}
