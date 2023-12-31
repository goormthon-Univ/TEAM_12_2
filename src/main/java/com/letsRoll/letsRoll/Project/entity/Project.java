package com.letsRoll.letsRoll.Project.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.letsRoll.letsRoll.Goal.entity.Goal;
import com.letsRoll.letsRoll.Member.entity.Member;
import com.letsRoll.letsRoll.Memoir.entity.Memoir;
import com.letsRoll.letsRoll.global.common.BaseEntity;
import com.letsRoll.letsRoll.global.enums.Mode;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project extends BaseEntity {
    @Id
    @Column(name = "project_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String title;

    @Column(length = 100)
    private String description;

    @NonNull
    private String password;

    @NonNull
    @Enumerated(EnumType.STRING)
    private Mode mode;

    @NonNull
    private LocalDate startDate;

    @Setter
    private LocalDate finishDate;

    @NonNull
    private LocalDate endDate;

    @OneToMany(mappedBy = "project")
    @JsonManagedReference
    private List<Goal> goals;

    @OneToMany(mappedBy = "project")
    @JsonManagedReference
    private List<Member> members;

    @Builder
    public Project(@NonNull String title, String description, @NonNull String password, @NonNull Mode mode, @NonNull LocalDate startDate, @NonNull LocalDate endDate) {
        this.title = title;
        this.description = description;
        this.password = password;
        this.mode = mode;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public float getProgress() {
        if (goals == null || goals.isEmpty()) {
            return 0.0f;
        }

        long completedGoals = goals.stream()
                .filter(Goal::getIsComplete)
                .count();

        return (float) completedGoals / goals.size() * 100;
    }
}
