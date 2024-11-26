package project.pictaround.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.pictaround.domain.Member;
import project.pictaround.dto.response.FindMeResponseDto;
import project.pictaround.exception.UnauthorizedException;
import project.pictaround.repository.MemberRepository;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class MemberService {

    MemberRepository memberRepository;
    SessionService sessionService;

    @Transactional
    public FindMeResponseDto findMe(HttpServletRequest request, HttpServletResponse response, boolean refresh) {
        Member member = sessionService.findMember(request, response, refresh);

        if (member == null) {
            sessionService.logout(request, response);
            throw new UnauthorizedException("EXPIRED TOKEN");
        }

        return FindMeResponseDto.of(member);
    }

}
