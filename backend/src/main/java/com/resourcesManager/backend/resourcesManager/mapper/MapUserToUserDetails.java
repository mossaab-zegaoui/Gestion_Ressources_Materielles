package com.resourcesManager.backend.resourcesManager.mapper;

import com.resourcesManager.backend.resourcesManager.model.Role;
import com.resourcesManager.backend.resourcesManager.model.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class MapUserToUserDetails implements UserDetails {
    private final User user;
    private final Collection<Role> roles;

    public MapUserToUserDetails(User user) {
        this.user = user;
        this.roles = user.getRoles();

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getNomRole()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
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
        return this.user.isEnabled();
    }

    public User getUser() {
        return this.user;
    }
}
