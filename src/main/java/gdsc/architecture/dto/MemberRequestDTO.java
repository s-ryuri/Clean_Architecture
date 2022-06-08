package gdsc.architecture.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberRequestDTO {

    private String nickName;
    private String email;
    private String password;

    @Builder
    public MemberRequestDTO(String nickname, String email, String password) {
        this.nickName = nickname;
        this.email = email;
        this.password = password;
    }
}
