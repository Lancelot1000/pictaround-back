package project.pictaround.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
public class Review {

    @Id
    @GeneratedValue
    @Column(name = "review_id")
    private Long id;

    private String comment;
    private String imageLocation;
    private LocalDateTime createdDateTime;
    private int likeCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @JsonBackReference
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    @JsonBackReference
    private Location location;

    public Review() {

    }

    @Builder
    public Review(String comment, String imageLocation, LocalDateTime createdDateTime, int likeCount, Member member, Location location) {
        this.comment = comment;
        this.imageLocation = imageLocation;
        this.createdDateTime = createdDateTime;
        this.likeCount = likeCount;
        this.member = member;
        this.location = location;
    }

    // 연관관계 메서드
    public void setLocation(Location location) {
        this.location = location;
        if (!location.getReviews().contains(this)) {
            location.getReviews().add(this);
        }
    }

    public void setMember(Member member) {
        this.member = member;
        if (!member.getReviews().contains(this)) {
            member.getReviews().add(this);  // 한 번만 추가되도록 처리
        }
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", comment='" + comment + '\'' +
                ", imageLocation='" + imageLocation + '\'' +
                ", createdDateTime=" + createdDateTime +
                ", likeCount=" + likeCount +
                ", member=" + member +
                ", location=" + location +
                '}';
    }
}
