package project.pictaround.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.pictaround.dto.response.FindMeResponseDto;
import project.pictaround.service.MemberService;

@Slf4j
@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/user/me")
    public ResponseEntity<FindMeResponseDto> findMe(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false, defaultValue = "false") String refresh) {
        FindMeResponseDto me = memberService.findMe(request, response, refresh.equals("true"));

        return ResponseEntity.status(HttpStatus.OK).body(me);
    }
}
