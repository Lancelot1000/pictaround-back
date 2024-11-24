package project.pictaround.dto.request;

import lombok.Getter;
import project.pictaround.domain.Member;
import project.pictaround.util.NicknameGenerator;

@Getter
public class JoinRequestDto {
    private String id;
    private String password;
    private String nickname;

    public static Member toEntity(JoinRequestDto dto) {
        return Member.builder()
                .loginId(dto.getId())
                .password(dto.getPassword())
                .nickname(NicknameGenerator.generateNickname())
                .build();
    }
}
