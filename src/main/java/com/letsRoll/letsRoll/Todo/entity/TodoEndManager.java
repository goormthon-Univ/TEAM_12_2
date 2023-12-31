package com.letsRoll.letsRoll.Todo.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.letsRoll.letsRoll.global.common.BaseEntity;
import com.letsRoll.letsRoll.Member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;

@Entity
@Getter
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TodoEndManager extends BaseEntity {
    @Id
    @Column(name = "todoManager_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "todoEndManager", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Todo> todoList;

    @Builder
    public TodoEndManager(@NonNull Member member, List<Todo> todoList) {
        this.member = member;
        this.todoList = todoList;
    }
}
