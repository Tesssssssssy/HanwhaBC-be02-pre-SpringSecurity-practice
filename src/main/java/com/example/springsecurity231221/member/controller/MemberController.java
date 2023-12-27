package com.example.springsecurity231221.member.controller;

import com.example.springsecurity231221.member.model.Member;
import com.example.springsecurity231221.member.model.MemberDtoPost;
import com.example.springsecurity231221.member.model.MemberDtoRes;
import com.example.springsecurity231221.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/member")
public class MemberController {
    private MemberService memberService;
    private AuthenticationManager authenticationManager;

    public MemberController(MemberService memberService, AuthenticationManager authenticationManager) {
        this.memberService = memberService;
        this.authenticationManager = authenticationManager;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/signup")
    public ResponseEntity signUpUser(MemberDtoPost memberDtoPost) {
        memberService.signUpUser(memberDtoPost);
        return ResponseEntity.ok().body("회원가입 완료");
    }


    @RequestMapping(method = RequestMethod.GET, value = "/mypage")
    public ResponseEntity myPage() {
        return ResponseEntity.ok().body("mypage");
    }

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public ResponseEntity login(MemberDtoPost memberDtoPost) {
        // AuthenticationManager를 가져와서 처리해주어야 함.

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(memberDtoPost.getUsername(), memberDtoPost.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return ResponseEntity.ok().body(((Member)authentication.getPrincipal()).getId());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/kakao")
    public ResponseEntity kakao(String code) {
        System.out.println(code);

        return ResponseEntity.ok().body(code);
    }



    @RequestMapping(method = RequestMethod.GET, value = "/find")
    public ResponseEntity findMemberById(Integer id) {
        MemberDtoRes memberDtoRes = memberService.findMemberById(id);
        return ResponseEntity.ok().body(memberDtoRes);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/list")
    public ResponseEntity findMemberList() {
        List<MemberDtoRes> memberDtoResList=  memberService.findMemberList();
        return ResponseEntity.ok().body(memberDtoResList);
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/update")
    public ResponseEntity updateMember(MemberDtoPost memberDtoPost) {
        memberService.updateMember(memberDtoPost);
        return ResponseEntity.ok().body("Member 수정 완료");
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/delete")
    public ResponseEntity deleteMember(Integer id) {
        memberService.deleteMember(id);
        return ResponseEntity.ok().body("Member 삭제 완료");
    }
}
