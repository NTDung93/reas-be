package vn.fptu.reasbe.model.entity.core;

import java.time.LocalDateTime;

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

    @PrePersist
    protected void onCreate() {
        setCreationDate(DateUtils.getCurrentDateTime());
        setLastModificationDate(DateUtils.getCurrentDateTime());
    }

    @PreUpdate
    protected void onUpdate() {
        setLastModificationDate(DateUtils.getCurrentDateTime());
    }
}
