package project.pictaround.dto.response;

import lombok.Builder;
import lombok.Getter;
import project.pictaround.domain.Member;

@Getter
@Builder
public class FindMeResponseDto {
    private String nickname;

    public static FindMeResponseDto of(Member member) {
        return FindMeResponseDto.builder()
                .nickname(member.getNickname())
                .build();
    }
}
