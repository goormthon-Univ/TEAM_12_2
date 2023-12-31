package com.letsRoll.letsRoll.Memoir.dto.res;

import com.letsRoll.letsRoll.Member.dto.MemberAssembler;
import com.letsRoll.letsRoll.Member.dto.res.MemberResDto;
import com.letsRoll.letsRoll.Member.entity.Member;
import com.letsRoll.letsRoll.Memoir.entity.Memoir;
import com.letsRoll.letsRoll.Project.dto.res.ProjectResDto;
import com.letsRoll.letsRoll.Project.entity.Project;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class MemoirResDto {
    private Long id;
    private String member; // Member 엔티티 대신 MemberResDto의 닉네임 추출
    private String content;
//    private ProjectResDto project; // Project 엔티티 대신 ProjectResDto 사용

    private LocalDateTime updatedAt;
}
