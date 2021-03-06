package gdsc.architecture.member.domain;

import gdsc.architecture.dto.MemberResponseDTO;
import gdsc.architecture.member.persistence.Member;
import gdsc.architecture.member.web.MemberService;
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
        Member member = memberRepository.findById(memberId).orElseThrow(IllegalStateException::new);
        return member;
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

        Member member = Member.builder()
                .email(email).nickName(nickName).password(encodeBcrypt(password))
                .build();

        memberRepository.save(member);

        MemberResponseDTO memberResponseDTO = entityToDto(member);

        return memberResponseDTO;
    }

    @Override
    @Transactional
    public MemberResponseDTO changeNickName(Long memberId,String nickName) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalStateException("???????????? ???????????? ????????????."));

        duplicateNickName(nickName);

        member.changeNickname(nickName);

        return entityToDto(member);
    }

    @Override
    public Member login(String email, String password) {
        Member member = memberRepository.findMemberByEmail(email).orElseThrow(()-> new IllegalStateException("???????????? ???????????? ????????????."));

        checkLoginPassword(password,member.getPassword());
        return member;
    }



    @Override
    public MemberResponseDTO entityToDto(Member member) {
        return MemberService.super.entityToDto(member);
    }

    @Override
    public List<MemberResponseDTO> entityToDtoList(List<Member> members){
        return MemberService.super.entityToDtoList(members);
    }

    private String encodeBcrypt(String password){
        return new BCryptPasswordEncoder().encode(password);
    }


    private void checkLoginPassword(String inputPassword,String memberPassword){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if(!encoder.matches(inputPassword,memberPassword)){
            throw new IllegalStateException("??????????????? ???????????? ????????????.");
        }
    }

    private void checkPassword(String password){
        final String reg = "[a-zA-Z]{8,30}";
        log.info("???????????? ??????");
        Matcher matcher = Pattern.compile(reg).matcher(password);
        if(!matcher.find()){
            log.info("???????????? ????????? ?????? ????????????.");
            throw new IllegalStateException("???????????? ????????? ?????? ????????????.");
        }
    }

    private void checkLength(String str){
        final String reg = "[a-zA-Z]{5,20}";

        Matcher matcher = Pattern.compile(reg).matcher(str);
        if(!matcher.find()){
            log.info("{}??? ?????? ????????? ?????? ????????????.",str);
            throw new IllegalStateException("????????? ?????? ????????????.");
        }
    }

    private void duplicateNickName(String nickName){
        memberRepository.findMemberByNickName(nickName).ifPresent(m ->{
                    throw new IllegalStateException("????????? ??????????????????.");
                });
    }

    private void duplicateEmail(String email){
        memberRepository.findMemberByEmail(email).ifPresent(m ->{
            throw new IllegalStateException("????????? ??????????????????.");
        });
    }
}
