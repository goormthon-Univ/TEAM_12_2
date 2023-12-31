package com.letsRoll.letsRoll.Member.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberAddReq{
    private Long userId;
    private String nickname;
    private String role;
    private String password;

}
