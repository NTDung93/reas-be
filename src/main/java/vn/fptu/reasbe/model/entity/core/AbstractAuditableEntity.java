package vn.fptu.reasbe.model.entity.core;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import jakarta.persistence.EntityListeners;
import lombok.Builder;
import vn.fptu.reasbe.utils.common.DateUtils;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class AbstractAuditableEntity extends AbstractEntity {
    @Size(max = 128)
    @Column(name = "USR_LOG_I", updatable = false)
    private String createdBy;

    @Column(name = "DTE_LOG_I", updatable = false)
    private LocalDateTime creationDate;

    @Size(max = 128)
    @Column(name = "USR_LOG_U")
    private String lastModifiedBy;

    @Column(name = "DTE_LOG_U")
    private LocalDateTime lastModificationDate;

    @Version
    @Column(name = "VERSION")
    private Integer version;

    @Override
    protected void onCreate() {
        super.onCreate();
        setCreationDate(DateUtils.getCurrentDateTime());
        setLastModificationDate(DateUtils.getCurrentDateTime());
        getCurrentAuditor().ifPresent(this::setCreatedBy);
    }

    @PreUpdate
    protected void onUpdate() {
        setLastModificationDate(DateUtils.getCurrentDateTime());
        getCurrentAuditor().ifPresent(this::setLastModifiedBy);
    }

    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }
        User userPrincipal = (User) authentication.getPrincipal();
        return Optional.ofNullable(userPrincipal.getUsername());
    }
}
