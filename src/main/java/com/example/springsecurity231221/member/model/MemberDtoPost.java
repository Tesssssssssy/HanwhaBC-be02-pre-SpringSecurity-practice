package com.example.springsecurity231221.member.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class MemberDtoPost {
    Integer id;
    String username;
    String password;
}
