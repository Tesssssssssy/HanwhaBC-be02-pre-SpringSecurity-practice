package com.example.springsecurity231221.member.service;

import com.example.springsecurity231221.member.model.*;
import com.example.springsecurity231221.member.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MemberService implements UserDetailsService {
    /*
        이미 UserDetails에서 만들어 놓은 UserDetailsService가 있다.
        여기서 DB와 상관 없이 인증, 인가 기능이 있다.
        그런데 내 DB의 Member 테이블 사용하고 싶다면
        내가 직접 구현해서 사용하면 된다.
    */
    private MemberRepository memberRepository;
//    private HttpServletRequest httpServletRequest;
    // Spring이 알아서 Session 관리를 해주기 때문에 굳이 설정해줄 필요가 없다.

    private PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
//        this.httpServletRequest = httpServletRequest;
        this.passwordEncoder = passwordEncoder;
    }

    public void signUpUser(MemberDtoPost memberDtoPost) {
        if (!memberRepository.findByUsername(memberDtoPost.getUsername()).isPresent()) {
//            String encodedPassword = PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(password);

            memberRepository.save(Member.builder()
                            .username(memberDtoPost.getUsername())
                    //      .password(password)
                            .password(passwordEncoder.encode(memberDtoPost.getPassword()))     // 이대로하면 입력한 비밀번호 그대로 저장됨.
                            .authority("ROLE_USER") // Spring에서 지정한 기본 사용자 (<-> "ROLE_ADMIN")
                            .build());
        }
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // DB에 있는지 없는지 확인해야 함.
        Optional<Member> result = memberRepository.findByUsername(username);
        Member member = null;
        if (result.isPresent()) {
            // 여기서 로그인 처리를 하면 된다. (조획 결과 db에 있는 유저이니까)
            member = result.get();

//            httpServletRequest.getSession().setAttribute("isLogined", true);
        }
        return member;
        // username을 입력받아서 member를 반환한다.
    }

    public MemberDtoRes findMemberById(Integer id) {
        Optional<Member> result = memberRepository.findById(id);

        if (result.isPresent()) {
            Member member = result.get();

            return MemberDtoRes.builder()
                    .id(member.getId())
                    .username(member.getUsername())
                    .build();

        } else {
            return null;
        }
    }

    public List<MemberDtoRes> findMemberList() {
        // Member Entity -> MemberDto로 바꿔줘야 함.
        List<Member> result = memberRepository.findAll();

        List<MemberDtoRes> memberDtoResList = new ArrayList<>();

        if (!result.isEmpty()) {
            for (Member member : result) {
                MemberDtoRes memberDtoRes = MemberDtoRes.builder()
                        .id(member.getId())
                        .username(member.getUsername())
                        .build();
                memberDtoResList.add(memberDtoRes);
            }
            return memberDtoResList;
        } else {
            return null;
        }
    }

    public void updateMember(MemberDtoPost memberDtoPost) {
        Optional<Member> result = memberRepository.findById(memberDtoPost.getId());

        if (result.isPresent()) {
            Member member = result.get();
            member.setUsername(memberDtoPost.getUsername());
            member.setPassword(memberDtoPost.getPassword());
            memberRepository.save(member);
        }
    }

    public void deleteMember(Integer id) {
        memberRepository.delete(Member.builder().id(id).build());
    }
}
