package com.example.demo.security;

import com.example.demo.model.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class MemberUserDetails implements UserDetails {
    private final Member member;

    public MemberUserDetails(Member member) {
        this.member = member;
    }

    public Member getMember() {
        return member;
    }

    public String getId() {
        return member.getId();
    }

    public String getNickname() {
        return member.getNickname();
    }

    public String getEmail() {
        return member.getEmail();
    }

    public String getUsername() {
        return member.getUsername();
    }

    public String getPassword() {
        return member.getPassword();
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return member.getAuthorities()
                .stream()
                .map(Enum::name)
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    public boolean isEnabled() {
        return true;
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }
}