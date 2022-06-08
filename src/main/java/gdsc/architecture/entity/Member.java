package gdsc.architecture.entity;

import gdsc.architecture.dto.MemberResponseDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@Size(min=5, max=20)
    @Column(nullable = false,unique = true)
    private String nickName;

    //@Size(min=5, max=20)
    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false)
    //@Pattern(regexp = "^[a-zA-Z]*$")
    private String password;


    public MemberResponseDTO changeNickname(String newNickname){
        this.nickName = newNickname;
        return MemberResponseDTO.builder().email(email)
                .nickname(nickName)
                .build();
    }


    @Builder
    public Member(String nickName, String email, String password) {
        this.nickName = nickName;
        this.email = email;
        this.password = password;
    }

    public MemberResponseDTO toDto(){
        MemberResponseDTO memberResponseDTO = MemberResponseDTO.builder().email(email)
                .nickname(nickName)
                .build();

        return memberResponseDTO;
    }
}
