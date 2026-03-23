package com.banking.common.audit;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class AuditContext {

    private Long userId;
    private ActorType actorType;
    private String ipAddress;
    private String correlationId;

    public AuditContext() {
        initFromSecurityContext();
    }

    private void initFromSecurityContext() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            if (auth.getPrincipal() instanceof Long) {
                this.userId = (Long) auth.getPrincipal();
            } else if (auth.getDetails() instanceof Long) {
                this.userId = (Long) auth.getDetails();
            }
            this.actorType = ActorType.USER;
        } else {
            this.actorType = ActorType.SYSTEM;
        }
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public ActorType getActorType() {
        return actorType;
    }

    public void setActorType(ActorType actorType) {
        this.actorType = actorType;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }
}