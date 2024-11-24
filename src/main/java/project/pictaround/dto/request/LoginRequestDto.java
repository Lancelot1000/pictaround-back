package project.pictaround.dto.request;

import lombok.Getter;
import project.pictaround.domain.Member;
import project.pictaround.util.NicknameGenerator;

@Getter
public class LoginRequestDto {
    private String id;
    private String password;

    public static Member toEntity(LoginRequestDto dto) {
        return Member.builder()
                .loginId(dto.getId())
                .password(dto.getPassword())
                .build();
    }
}
