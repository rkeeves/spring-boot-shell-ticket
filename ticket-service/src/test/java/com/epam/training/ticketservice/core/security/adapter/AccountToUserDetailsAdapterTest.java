package com.epam.training.ticketservice.core.security.adapter;

import com.epam.training.ticketservice.core.account.entity.Account;
import com.epam.training.ticketservice.core.security.service.SecurityService;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AccountToUserDetailsAdapterTest {

    @Test
    void givenNonPrivilegedAccount_whenAnyGetterIsCalled_returnsExpected(){
        // given
        String username = "username";
        String password = "password";
        var expectedAuthorities = Set.of(SecurityService.NON_PRIVILEGED_ROLE_NAME);
        var account = Account.builder()
                .username(username)
                .password(password)
                .privileged(false)
                .build();
        // when
        var adapter = new AccountToUserDetailsAdapter(account);
        // then
        assertEquals(username, adapter.getUsername());
        assertEquals(password, adapter.getPassword());
        var actualAuthorities = adapter.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        assertEquals(expectedAuthorities, actualAuthorities);
        assertTrue(adapter.isAccountNonExpired());
        assertTrue(adapter.isAccountNonLocked());
        assertTrue(adapter.isCredentialsNonExpired());
        assertTrue(adapter.isEnabled());
    }

    @Test
    void givenPrivilegedAccount_whenAnyGetterIsCalled_returnsExpected(){
        // given
        String username = "username";
        String password = "password";
        var expectedAuthorities = Set.of(SecurityService.NON_PRIVILEGED_ROLE_NAME, SecurityService.PRIVILEGED_ROLE_NAME);
        var account = Account.builder()
                .username(username)
                .password(password)
                .privileged(true)
                .build();
        // when
        var adapter = new AccountToUserDetailsAdapter(account);
        // then
        assertEquals(username, adapter.getUsername());
        assertEquals(password, adapter.getPassword());
        var actualAuthorities = adapter.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        assertEquals(expectedAuthorities, actualAuthorities);
        assertTrue(adapter.isAccountNonExpired());
        assertTrue(adapter.isAccountNonLocked());
        assertTrue(adapter.isCredentialsNonExpired());
        assertTrue(adapter.isEnabled());
    }
}