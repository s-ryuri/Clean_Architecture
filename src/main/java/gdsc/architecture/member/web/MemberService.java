package gdsc.architecture.member.web;

import gdsc.architecture.dto.MemberResponseDTO;
import gdsc.architecture.member.persistence.Member;

import java.util.List;
import java.util.stream.Collectors;

public interface MemberService {

    List<MemberResponseDTO> getAllMembers();

    MemberResponseDTO signUp(String email,String nickName, String password);

    MemberResponseDTO changeNickName(Long memberId,String nickName);

    Member login(String email, String password);

    Member getMember(Long memberId);

    default MemberResponseDTO entityToDto(Member member){
        MemberResponseDTO memberResponseDTO = MemberResponseDTO.builder()
                .email(member.getEmail())
                .nickname(member.getNickName())
                .build();

        return memberResponseDTO;
    }

    default List<MemberResponseDTO> entityToDtoList(List<Member> members){
        List<MemberResponseDTO> result = members.stream().map(m -> entityToDto(m))
                .collect(Collectors.toList());
        return result;
    }

}
