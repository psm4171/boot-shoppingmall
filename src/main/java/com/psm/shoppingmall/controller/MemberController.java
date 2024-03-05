package com.psm.shoppingmall.controller;

import com.psm.shoppingmall.domain.Member;
import com.psm.shoppingmall.domain.RefreshToken;
import com.psm.shoppingmall.domain.Role;
import com.psm.shoppingmall.dto.MemberLoginDto;
import com.psm.shoppingmall.dto.MemberLoginResponseDto;
import com.psm.shoppingmall.dto.MemberSignUpDto;
import com.psm.shoppingmall.dto.MemberSignUpResponseDto;
import com.psm.shoppingmall.dto.RefreshTokenDto;
import com.psm.shoppingmall.security.jwt.util.JwtTokenizer;
import com.psm.shoppingmall.service.MemberService;
import com.psm.shoppingmall.service.RefreshTokenService;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
//import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Validated
@RequestMapping("/members")
public class MemberController {

    private final JwtTokenizer jwtTokenizer;
    private final RefreshTokenService refreshTokenService;
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody @Valid MemberSignUpDto memberSignUpDto,
        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        Member member = new Member();
        member.setName(memberSignUpDto.getName());
        member.setEmail(memberSignUpDto.getEmail());
        member.setPassword(memberSignUpDto.getPassword());

        Member saveMember = memberService.addMember(member);

        MemberSignUpResponseDto memberSignUpResponseDto = new MemberSignUpResponseDto();
        memberSignUpResponseDto.setMemberId(saveMember.getMemberId());
        memberSignUpResponseDto.setName(saveMember.getName());
        memberSignUpResponseDto.setRegdate(saveMember.getRegdate());
        memberSignUpResponseDto.setEmail(saveMember.getEmail());

        return new ResponseEntity(memberSignUpResponseDto, HttpStatus.CREATED);

    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid MemberLoginDto memberLoginDto,
        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        Member member = memberService.findByEmail(memberLoginDto.getEmail());

        // 클라이언트가 전송한 비밀번호와 조회된 회원의 비밀번호를 비교
        if (!passwordEncoder.matches(memberLoginDto.getPassword(), member.getPassword())) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        // member 객체가 가지고 있는 Role 목록을 가져와, 각 역할의 이름을 추출한 후, 리스트로 변환
        // 즉, 회원이 가지고 있는 역할들의 이름을 리스트 형태로 얻는다.
        List<String> roles = member.getRoles().stream().map(Role::getName)
            .collect(Collectors.toList());

        String accessToken = jwtTokenizer.createAccessToken(member.getMemberId(), member.getEmail(),
            roles);
        String refreshToken = jwtTokenizer.createRefreshToken(member.getMemberId(),
            member.getEmail(), roles);

        RefreshToken refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.setValue(refreshToken);
        refreshTokenEntity.setMemberId(member.getMemberId());
        refreshTokenService.addRefreshToken(refreshTokenEntity);

        MemberLoginResponseDto loginResponseDto = MemberLoginResponseDto.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .memberId(member.getMemberId())
            .nickName(member.getName())
            .build();

        return new ResponseEntity(loginResponseDto, HttpStatus.OK);
    }

    @DeleteMapping("/logout")
    public ResponseEntity logout(@RequestBody RefreshTokenDto refreshTokenDto) {
        refreshTokenService.deleteRefreshToken(refreshTokenDto.getRefreshToken());
        return new ResponseEntity(HttpStatus.OK);
    }


/*
* 1. 전달 받은 유저의 아이디로 유저가 존재하는지 확인
* 2. RefreshToken이 유효한지 체크
* 3. AccessToken을 발급하여 기존 RefreshToken과 함께 응답
*/
    @PostMapping("/refreshToken")
    public ResponseEntity requestRefresh(@RequestBody RefreshTokenDto refreshTokenDto) {
        RefreshToken refreshToken = refreshTokenService.findRefreshToken(
                refreshTokenDto.getRefreshToken())
            .orElseThrow(() -> new IllegalArgumentException("Refresh Token Not Found"));
        Claims claims = jwtTokenizer.parseRefreshToken(refreshToken.getValue());

        Long memberId = Long.valueOf((String) claims.get("memberId"));

        Member member = memberService.getMember(memberId)
            .orElseThrow(() -> new IllegalArgumentException("Member Not Found"));

        List roles = (List) claims.get("roles");
        String email = claims.getSubject();

        String accessToken = jwtTokenizer.createAccessToken(memberId, email, roles);

        MemberLoginResponseDto loginResponse = MemberLoginResponseDto.builder()
            .accessToken(accessToken)
            .refreshToken(refreshTokenDto.getRefreshToken())
            .memberId(member.getMemberId())
            .nickName(member.getName())
            .build();

        return new ResponseEntity(loginResponse, HttpStatus.OK);
    }

}
