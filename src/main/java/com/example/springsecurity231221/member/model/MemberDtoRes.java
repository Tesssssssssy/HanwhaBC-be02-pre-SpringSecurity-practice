package com.example.springsecurity231221.member.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Builder
@Getter
@Setter
public class MemberDtoRes {
    Integer id;
    String username;
}
