package project.pictaround.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.pictaround.domain.Member;
import project.pictaround.domain.Session;
import project.pictaround.repository.MemberRepository;
import project.pictaround.repository.SessionRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class SessionService {

    public static final String SESSION_COOKIE_NAME = "USER_SESSION";
    public static final int SESSION_MAX_SECONDS = 6 * 60 * 60;
    public static final int REFRESH_SECONDS = 3 * 60 * 60;

    SessionRepository sessionRepository;
    MemberRepository memberRepository;

    public SessionService(SessionRepository sessionRepository, MemberRepository memberRepository) {
        this.sessionRepository = sessionRepository;
        this.memberRepository = memberRepository;
    }

    // 세션 생성
    @Transactional
    public void createSession(Member member, HttpServletResponse response) {

        // 세션 id 생성
        String sessionValue = UUID.randomUUID().toString().replaceAll("-", "");

        // 기존에 있다면 삭제 처리
        List<Session> sessions = sessionRepository.findByUserId(member.getId());

        for (Session session : sessions) {
            sessionRepository.deleteBySessionValue(session.getSessionValue());
        }

        Session sessionEntity = sessionBuilder(member, sessionValue);

        // 세션 id 저장
        sessionRepository.save(sessionEntity);

        // 쿠키 생성
        Cookie mySessionCookie = new Cookie(SESSION_COOKIE_NAME, sessionValue);
        mySessionCookie.setMaxAge(SESSION_MAX_SECONDS);
        mySessionCookie.setPath("/");
        response.addCookie(mySessionCookie);
    }

    // member 찾기
    @Transactional
    public Member findMember(HttpServletRequest request, HttpServletResponse response, boolean needRefresh) {
        Cookie sessionCookie = findCookie(request);

        log.info("sessionCookie = {}", sessionCookie);

        if (sessionCookie == null) return null;

        log.info("sessionCookie.getValue() = {}", sessionCookie.getValue());

        Session session = sessionRepository.findBySessionValue(sessionCookie.getValue());

        log.info("session = {}", session);

        if (session == null) return null;

        LocalDateTime now = LocalDateTime.now();

        if (session.getExpiresAt().isBefore(now)) return null;

        Member member = memberRepository.findById(session.getUserId()).orElse(null);

        // 세션 연장
        if (needRefresh && member != null && session.getExpiresAt().isAfter(now.minusSeconds(REFRESH_SECONDS))) {
            sessionRepository.deleteBySessionValue(session.getSessionValue());
            createSession(member, response);
        }

        return member;
    }

    // 세션 찾기
    private Cookie findCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return null;

        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(SESSION_COOKIE_NAME))
                .findAny()
                .orElse(null);
    }


    private static Session sessionBuilder(Member member, String sessionValue) {
        LocalDateTime now = LocalDateTime.now();

        return Session.builder()
                .sessionValue(sessionValue)
                .userId(member.getId())
                .createdAt(now)
                .expiresAt(now.plusSeconds(SESSION_MAX_SECONDS)).build();
    }


}
