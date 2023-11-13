package com.letsRoll.letsRoll.Memoir.entity;

import com.letsRoll.letsRoll.Member.entity.Member;
import com.letsRoll.letsRoll.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Memoir extends BaseEntity {

    @Id
    @Column(name = "memoir_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @NonNull
    private String content;

    @Builder
    public Memoir(@NonNull Member member, @NonNull String content) {
        this.member = member;
        this.content = content;
    }
}