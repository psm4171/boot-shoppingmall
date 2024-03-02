package com.psm.shoppingmall.dto;

import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class RefreshTokenDto {

    @NotEmpty
    String refreshToken;
}
