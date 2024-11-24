package project.pictaround.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.pictaround.config.SessionConst;
import project.pictaround.dto.request.JoinRequestDto;
import project.pictaround.dto.request.LoginRequestDto;
import project.pictaround.dto.response.SuccessResponse;
import project.pictaround.dto.response.MessageResponse;
import project.pictaround.service.LoginService;

@Slf4j
@RestController
public class AuthController {

    private final LoginService loginService;

    public AuthController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<SuccessResponse> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletRequest request, HttpServletResponse response) {
        loginService.loginMember(loginRequestDto, response);

        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.SESSION_USER, loginRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse(true));
    }

    @PostMapping("/auth/join")
    public ResponseEntity<SuccessResponse> join(@RequestBody JoinRequestDto joinRequestDto) {
        loginService.createMember(joinRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse(true));
    }


    @GetMapping("/auth/check-id/{username}")
    public ResponseEntity<MessageResponse> checkUserId(@PathVariable String username) {
        boolean nameDuplicate = loginService.isExistsByName(username);

        if (nameDuplicate) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse(false, "DUPLICATED"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true));
    }
}
