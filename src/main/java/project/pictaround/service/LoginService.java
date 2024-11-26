package project.pictaround.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.pictaround.domain.Member;
import project.pictaround.dto.request.JoinRequestDto;
import project.pictaround.dto.request.LoginRequestDto;
import project.pictaround.exception.UnauthorizedException;
import project.pictaround.exception.UserAlreadyExistsException;
import project.pictaround.repository.MemberRepository;

@Slf4j
@Service
@Transactional(readOnly = true)
public class LoginService {

    MemberRepository memberRepository;
    SessionService sessionService;

    public LoginService(MemberRepository memberRepository, SessionService sessionService) {
        this.memberRepository = memberRepository;
        this.sessionService = sessionService;
    }

    @Transactional
    public void loginMember(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        Member loginInfo = LoginRequestDto.toEntity(loginRequestDto);

        if (!isExistsByName(loginInfo.getLoginId())) {
            throw new UnauthorizedException("invalid username or password");
        }

        Member memberInfo = memberRepository.findByLoginId(loginInfo.getLoginId());

        if (!checkPassword(memberInfo, loginInfo)) {
            throw new UnauthorizedException("invalid username or password");
        }

        sessionService.createSession(memberInfo, response);
    }

    @Transactional
    public void createMember(JoinRequestDto joinRequestDto) {
        boolean isExists = isExistsByName(joinRequestDto.getId());

        if (isExists) {
            throw new UserAlreadyExistsException("DUPLICATED");
        } else {
            log.info(joinRequestDto.toString());
            memberRepository.save(JoinRequestDto.toEntity(joinRequestDto));
        }
    }

    public boolean isExistsByName(String name) {
        return memberRepository.existsByLoginId(name);
    }

    private static boolean checkPassword(Member memberInfo, Member member) {
        return memberInfo.getPassword().equals(member.getPassword());
    }
}
