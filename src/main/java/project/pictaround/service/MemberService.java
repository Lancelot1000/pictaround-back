package project.pictaround.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.pictaround.domain.Favorite;
import project.pictaround.domain.Member;
import project.pictaround.dto.response.FindMeResponseDto;
import project.pictaround.dto.response.MyFavoriteDto;
import project.pictaround.exception.UnauthorizedException;
import project.pictaround.repository.FavoriteRepository;
import project.pictaround.repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class MemberService {

    MemberRepository memberRepository;
    SessionService sessionService;

    public FindMeResponseDto findMe(HttpServletRequest request, HttpServletResponse response) {
        Member member = sessionService.findMember(request, response, false);

        if (member == null) {
            throw new UnauthorizedException("EXPIRED TOKEN");
        }

        return FindMeResponseDto.of(member);
    }
}
