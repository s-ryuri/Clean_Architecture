package gdsc.architecture.repository;

import gdsc.architecture.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {

    Optional<Member> findMemberByNickName(String nickName);
    Optional<Member> findMemberByEmail(String email);
}
