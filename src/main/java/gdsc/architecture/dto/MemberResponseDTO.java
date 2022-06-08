package gdsc.architecture.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberResponseDTO {

    private String nickname;
    private String email;

    @Builder
    public MemberResponseDTO(String nickname, String email) {
        this.nickname = nickname;
        this.email = email;
    }
}
