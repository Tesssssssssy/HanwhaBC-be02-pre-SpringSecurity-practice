package com.example.springsecurity231221.member.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "email")
    String username;
    /*
        Spring Security에서 인증을 받으려면 username으로 해야 함.
        만약 email로 받았다면 받은 email을 setter에서 username으로 설정해주면 된다.
        그리고 Spring Security에 전달해줄 때에는 무조건 username으로 해야 함.
    */

    String password;
    String authority;   // 권한 설정할 수 있게.


    @OneToMany(mappedBy = "member")
    List<Orders> orderList = new ArrayList<>();

    /*
        Spring Security가 어차피 권한 주고 username, password 사용하니까,
        회원 만들 때 휴면 유저, 인증, 인가 등에 대한 메소드들을 이미 만들어 놓았음.
    */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton((GrantedAuthority) () -> authority );
        // 권한은 여러 개가 있을 수 있다.
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
