package gdsc.architecture.member.persistence;

import gdsc.architecture.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

    private final EntityManager em;
    @Override
    public Optional<Member> findMemberByNickName(String nickName) {
        Optional<Member> member = Optional.ofNullable(em.createQuery("select m from Member m where m.nickName =:nickName", Member.class)
                .setParameter("nickName", nickName)
                .getSingleResult());
        return member;
    }

    @Override
    public Optional<Member> findMemberByEmail(String email) {
        Optional<Member> member = Optional.ofNullable(em.createQuery("select m from Member m where m.email =:email", Member.class)
                .setParameter("email", email)
                .getSingleResult());
        return member;
    }

    @Override
    public Optional<Member> findById(Long memberId) {
        Optional<Member> member = Optional.ofNullable(em.createQuery("select m from Member m where m.id =:memberId", Member.class)
                .setParameter("memberId", memberId)
                .getSingleResult());
        return member;
    }

    @Override
    public void save(Member member) {
        em.persist(member);
    }

    @Override
    public List<Member> findAll() {
        List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();
        return members;
    }
}
