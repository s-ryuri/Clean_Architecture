package gdsc.architecture.service;

import gdsc.architecture.dto.MemberResponseDTO;
import gdsc.architecture.entity.Member;
import gdsc.architecture.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;


    public Member getMember(Long memberId){
        Optional<Member> member = memberRepository.findById(memberId);
        return member.get();
    }

    @Override
    public List<MemberResponseDTO> getAllMembers() {
        List<Member> members = memberRepository.findAll();

        List<MemberResponseDTO> result = entityToDtoList(members);

        return result;
    }

    @Override
    @Transactional
    public MemberResponseDTO signUp(String email, String nickName, String password) {
        log.info("email : {}",email);
        log.info("nickName : {}",nickName);
        log.info("password : {}",password);

        duplicateEmail(email);
        duplicateNickName(nickName);
        checkLength(nickName);
        checkLength(password);
        checkPassword(password);

        String encodePassword = encodeBcrypt(password);

        Member member = Member.builder()
                .email(email).nickName(nickName).password(encodePassword)
                .build();

        memberRepository.save(member);

        MemberResponseDTO memberResponseDTO = entityToDto(member);

        return memberResponseDTO;
    }

    @Override
    @Transactional
    public MemberResponseDTO changeNickName(Long memberId,String nickName) {
        Optional<Member> findMember = memberRepository.findById(memberId);

        if(findMember.isEmpty()){
            throw new RuntimeException("없는 아이디입니다.");
        }

        duplicateNickName(nickName);

        Member member = findMember.get();
        member.changeNickname(nickName);

        return entityToDto(member);
    }

    @Override
    public Member login(String email, String password) {
        Optional<Member> member = memberRepository.findMemberByEmail(email);
        if(member.isEmpty()){
            throw new RuntimeException("없는 아이디 입니다.");
        }
        checkLoginPassword(password,member.get().getPassword());
        return member.get();
    }

    public void checkLoginPassword(String inputPassword,String memberPassword){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if(!encoder.matches(inputPassword,memberPassword)){
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
    }

    @Override
    public MemberResponseDTO entityToDto(Member member) {
        return MemberService.super.entityToDto(member);
    }

    @Override
    public List<MemberResponseDTO> entityToDtoList(List<Member> members){
        return MemberService.super.entityToDtoList(members);
    }

    public String encodeBcrypt(String password){
        return new BCryptPasswordEncoder().encode(password);
    }

    public void checkPassword(String password){
        final String reg = "[a-zA-Z]{8,30}";
        log.info("패스워드 체크");
        Matcher matcher = Pattern.compile(reg).matcher(password);
        if(!matcher.find()){
            log.info("비밀번호 형식이 옳지 않습니다.");
            throw new RuntimeException("비밀번호 형식이 옳지 않습니다.");
        }
    }

    public void checkLength(String str){
        final String reg = "[a-zA-Z]{5,20}";

        Matcher matcher = Pattern.compile(reg).matcher(str);
        if(!matcher.find()){
            log.info("{}의 길이 형식이 옳지 않습니다.",str);
            throw new RuntimeException("길이가 옳지 않습니다.");
        }
    }

    public void duplicateNickName(String nickName){
        memberRepository.findMemberByNickName(nickName).ifPresent(m ->{
                    throw new RuntimeException("중복된 닉네임입니다.");
                });
    }

    public void duplicateEmail(String email){
        memberRepository.findMemberByEmail(email).ifPresent(m ->{
            throw new RuntimeException("중복된 이메일입니다.");
        });

    }
}
