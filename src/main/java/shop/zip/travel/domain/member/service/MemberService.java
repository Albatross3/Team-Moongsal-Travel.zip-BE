package shop.zip.travel.domain.member.service;

import static shop.zip.travel.domain.member.dto.request.MemberSignupReq.toMember;

import org.springframework.stereotype.Service;
import shop.zip.travel.domain.member.dto.request.AccessTokenReissueReq;
import shop.zip.travel.domain.member.dto.request.MemberSigninReq;
import shop.zip.travel.domain.member.dto.request.MemberSignupReq;
import shop.zip.travel.domain.member.dto.response.MemberSigninRes;
import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.member.exception.DuplicatedEmailException;
import shop.zip.travel.domain.member.exception.DuplicatedNicknameException;
import shop.zip.travel.domain.member.exception.EmailNotMatchException;
import shop.zip.travel.domain.member.exception.InvalidRefreshTokenException;
import shop.zip.travel.domain.member.exception.MemberNotFoundException;
import shop.zip.travel.domain.member.exception.NotVerifiedAuthorizationCodeException;
import shop.zip.travel.domain.member.exception.PasswordNotMatchException;
import shop.zip.travel.domain.member.repository.MemberRepository;
import shop.zip.travel.global.error.ErrorCode;
import shop.zip.travel.global.security.JwtTokenProvider;
import shop.zip.travel.global.util.RedisUtil;

@Service
public class MemberService {

  private final MemberRepository memberRepository;
  private final RedisUtil redisUtil;
  private final JwtTokenProvider jwtTokenProvider;

  public MemberService(MemberRepository memberRepository, RedisUtil redisUtil,
      JwtTokenProvider jwtTokenProvider) {
    this.memberRepository = memberRepository;
    this.redisUtil = redisUtil;
    this.jwtTokenProvider = jwtTokenProvider;
  }

  public void createMember(MemberSignupReq memberSignupReq) {
    Member member = toMember(memberSignupReq);
    memberRepository.save(member);
  }

  public void validateDuplicatedEmail(String email) {
    if (memberRepository.existsByEmail(email)) {
      throw new DuplicatedEmailException(ErrorCode.DUPLICATED_EMAIL);
    }
  }

  public boolean validateDuplicatedNickname(String nickname) {
    return memberRepository.existsByNickname(nickname);
  }

  public void verifyCode(String email, String code) {
    if (redisUtil.getData(email) != null && redisUtil.getData(email).equals(code)) {
      redisUtil.deleteData(email);
    } else {
      throw new NotVerifiedAuthorizationCodeException(ErrorCode.NOT_VERIFIED_CODE);
    }
  }

  public MemberSigninRes login(MemberSigninReq memberSigninReq) {
    Member member = memberRepository.findByEmail(memberSigninReq.email())
        .orElseThrow(() -> new EmailNotMatchException(ErrorCode.EMAIL_NOT_MATCH));

    if (!member.getPassword().equals(memberSigninReq.password())) {
      throw new PasswordNotMatchException(ErrorCode.PASSWORD_NOT_MATCH);
    }

    String accessToken = jwtTokenProvider.createAccessToken(member.getId());
    String refreshToken = jwtTokenProvider.createRefreshToken();

    redisUtil.setDataWithExpire(String.valueOf(member.getId()), refreshToken, 120L);

    return new MemberSigninRes(accessToken, refreshToken);
  }

  public MemberSigninRes recreateAccessAndRefreshToken(AccessTokenReissueReq accessTokenReissueReq) {

    String accessToken = accessTokenReissueReq.accessToken();
    String refreshToken = accessTokenReissueReq.refreshToken();
    String memberId = jwtTokenProvider.getMemberIdUsingDecode(accessToken);

    if (jwtTokenProvider.validateRefreshToken(accessTokenReissueReq.refreshToken()) &&
        redisUtil.getData(memberId).equals(refreshToken)) {
      redisUtil.deleteData(memberId);
      String newAccessToken = jwtTokenProvider.createAccessToken(Long.parseLong(memberId));
      String newRefreshToken = jwtTokenProvider.createRefreshToken();
      redisUtil.setDataWithExpire(memberId, newRefreshToken, 120L);
      return new MemberSigninRes(newAccessToken, newRefreshToken);
    } else {
      throw new InvalidRefreshTokenException(ErrorCode.INVALID_REFRESH_TOKEN);
    }
  }

  public Member getMember(Long id) {
    return memberRepository.findById(id)
        .orElseThrow(() -> new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND));
  }
}

