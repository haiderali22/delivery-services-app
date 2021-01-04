package com.hali.spring.deliveryms.order.config.jpa;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;

class AuditorAwareImpl implements AuditorAware<Long> 
{
    @Override
    public Optional<Long> getCurrentAuditor() {
        return Optional.of(1L);
        
        // Can use Spring Security to return currently logged in user
        // return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()
    }
}