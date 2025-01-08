package vn.fptu.reasbe.model.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.fptu.reasbe.model.entity.core.AbstractAuditableEntity;
import vn.fptu.reasbe.model.enums.notification.TypeNotification;

/**
 *
 * @author ntig
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "NOTIFICATION")
@AttributeOverride(name = "id", column = @Column(name = "NOTIFICATION_ID"))
public class Notification extends AbstractAuditableEntity {

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User receiver;

    @NotNull
    @Column(name = "TYPE_NOTIFICATION", length = 10)
    private TypeNotification typeNotification;

    @NotNull
    @Column(name = "TITLE")
    private String title;

    @NotNull
    @Column(name = "CONTENT")
    private String content;

    @NotNull
    @Column(name = "IS_READ")
    private boolean isRead;
}
