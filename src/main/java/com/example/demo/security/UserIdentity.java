package com.example.demo.security;

import com.example.demo.model.Member;
import com.example.demo.model.MemberAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserIdentity {
    private static final MemberUserDetails ANONYMOUS_USER = new MemberUserDetails(new Member());

    private MemberUserDetails getMemberUserDetails() {
        var principal = SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        return "anonymousUser".equals(principal)
                ? ANONYMOUS_USER
                : (MemberUserDetails) principal;
    }

    public boolean isAnonymous() {
        return getMemberUserDetails() == ANONYMOUS_USER;
    }

    public String getId() {
        return getMemberUserDetails().getId();
    }

    public String getUsername() {
        return getMemberUserDetails().getUsername();
    }

    public String getNickname() {
        return getMemberUserDetails().getNickname();
    }

    public String getEmail() {
        return getMemberUserDetails().getEmail();
    }

    public List<MemberAuthority> getAuthorities() {
        return getMemberUserDetails().getMember().getAuthorities();
    }
}