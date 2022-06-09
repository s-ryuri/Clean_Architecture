package gdsc.architecture.member.web;

import gdsc.architecture.dto.LoginDTO;
import gdsc.architecture.dto.MemberRequestDTO;
import gdsc.architecture.dto.MemberResponseDTO;
import gdsc.architecture.member.persistence.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("")
    public Result getAllMembers(){
        List<MemberResponseDTO> members = memberService.getAllMembers();
        return new Result(members);
    }

    @GetMapping("/member")
    public ResponseEntity<?> getMember(HttpSession httpSession){
        //세션 연습
        Object id = httpSession.getAttribute("sessionId");

        if(id == null){
            return ResponseEntity.badRequest().body("로그인이 필요합니다");
        }

        Member member = memberService.getMember((Long) id);
        if(member == null){
            return ResponseEntity.badRequest().body("멤버가 없습니다");
        }else{
            return ResponseEntity.ok().body(member.getId());
        }

    }

    @PostMapping("/signUp")
    public MemberResponseDTO signUp(@RequestBody MemberRequestDTO memberRequestDTO){

        MemberResponseDTO memberResponseDTO = memberService.signUp(memberRequestDTO.getEmail(),
                memberRequestDTO.getNickName(), memberRequestDTO.getPassword());

        return memberResponseDTO;

    }

    @PatchMapping("/{memberId}/nickName//{nickName}")
    public MemberResponseDTO changeNickName(@PathVariable("memberId") Long memberId, @PathVariable("nickName") String nickName){
        MemberResponseDTO memberResponseDTO = memberService.changeNickName(memberId, nickName);
        return memberResponseDTO;
    }

    @PostMapping("/login")
    public MemberResponseDTO login(HttpSession httpSession, @RequestBody LoginDTO loginDTO){
        Member member = memberService.login(loginDTO.getEmail(), loginDTO.getPassword());
        httpSession.setAttribute("sessionId",member.getId());

        MemberResponseDTO result = memberService.entityToDto(member);

        return result;
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }
}



