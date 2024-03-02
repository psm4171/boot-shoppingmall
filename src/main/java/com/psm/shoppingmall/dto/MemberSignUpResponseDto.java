package com.psm.shoppingmall.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class MemberSignUpResponseDto {

    private Long memberId;
    private String email;
    private String password;
    private String name;
    private LocalDateTime regdate;
}
