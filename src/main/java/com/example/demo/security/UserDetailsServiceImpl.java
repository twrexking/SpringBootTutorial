package com.example.demo.security;

import com.example.demo.model.Member;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDetailsServiceImpl implements UserDetailsService {
    private final Map<String, Member> memberMap = new HashMap<>();

    public UserDetailsServiceImpl(List<Member> members) {
        members.forEach(member -> memberMap.put(member.getUsername(), member));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var member = memberMap.get(username);
        if (member == null) {
            throw new UsernameNotFoundException("Can't find username: " + username);
        }

        return new MemberUserDetails(member);
    }
}
