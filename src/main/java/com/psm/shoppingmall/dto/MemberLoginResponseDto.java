package com.psm.shoppingmall.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberLoginResponseDto {

    private String accessToken;
    private String refreshToken;
    private Long memberId;
    private String nickName;
}
