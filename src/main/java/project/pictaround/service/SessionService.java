package project.pictaround.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.pictaround.config.SessionConst;
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

    public static final String SESSION_COOKIE_NAME = SessionConst.USER_SESSION;
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

        Session sessionEntity = sessionBuilder(member, sessionValue);

        log.info("member.getId() = {}", member.getId());
        log.info("sessionEntity = {}", sessionValue);
        // 세션 id 저장
        sessionRepository.save(sessionEntity);

        // 쿠키 생성
        setCookie(response, SESSION_COOKIE_NAME, sessionValue, SESSION_MAX_SECONDS);
    }

    @Transactional
    protected void updateSession(Session session, HttpServletResponse response) {
        LocalDateTime expiredAt = LocalDateTime.now().plusSeconds(SESSION_MAX_SECONDS);
        String sessionValue = UUID.randomUUID().toString().replaceAll("-", "");

        System.out.println(session.getId() + " " + sessionValue + " " + expiredAt);
        sessionRepository.updateSessionBy(expiredAt, sessionValue, session.getId());
        setCookie(response, SESSION_COOKIE_NAME, sessionValue, SESSION_MAX_SECONDS);
    }

    // member 찾기
    @Transactional
    public Member findMember(HttpServletRequest request, HttpServletResponse response, boolean needRefresh) {
        Cookie sessionCookie = findCookie(request);

        if (sessionCookie == null) return null;

        Session session = sessionRepository.findBySessionValue(sessionCookie.getValue());

        if (session == null) return null;

        LocalDateTime now = LocalDateTime.now();

        if (session.getExpiresAt().isBefore(now)) return null;

        Member member = memberRepository.findById(session.getUserId()).orElse(null);

        // 세션 연장
        if (needRefresh && member != null && now.isAfter(session.getExpiresAt().minusSeconds(REFRESH_SECONDS))) {
            updateSession(session, response);
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

    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie sessionCookie = findCookie(request);

        if (sessionCookie != null) {
            sessionRepository.deleteBySessionValue(sessionCookie.getValue());
        }

        setCookie(response, SESSION_COOKIE_NAME, null, 0);
    }

    private void setCookie(HttpServletResponse response, String cookieName, String cookieValue, int maxAge) {
        Cookie mySessionCookie = new Cookie(cookieName, cookieValue);
        mySessionCookie.setMaxAge(maxAge);
        mySessionCookie.setPath("/");
        mySessionCookie.setSecure(true);
        mySessionCookie.setAttribute("SameSite", "None");
        response.addCookie(mySessionCookie);
    }
}
