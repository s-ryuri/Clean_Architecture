package gdsc.architecture.member.domain;

import gdsc.architecture.member.persistence.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    Optional<Member> findMemberByNickName(String nickName);
    Optional<Member> findMemberByEmail(String email);
    Optional<Member> findById(Long memberId);

    List<Member> findAll();

    void save(Member member);
}
