package com.psm.shoppingmall.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberSignUpDto {

    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z0-9+-\\_.] +@[a-zA-Z0-9-] +\\.[a-zA-Z0-9-.]+$",
        message = "이메일 형식을 맞춰야합니다.")
    private String email;

    @NotEmpty
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}",
        message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.")
    private String password;

    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z가-힣\\\\s]{2,15}",
        message = "이름은 영문자, 한글, 공백포함 2~15글자까지 가능합니다.")
    private String name;

}
