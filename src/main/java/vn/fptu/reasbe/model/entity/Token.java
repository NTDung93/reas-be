package vn.fptu.reasbe.model.entity;

import vn.fptu.reasbe.model.entity.core.AbstractAuditableEntity;

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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "TOKEN")
@AttributeOverride(name = "id", column = @Column(name = "TOKEN_ID"))
public class Token extends AbstractAuditableEntity {

    @NotNull
    @Column(name = "ACCESS_TOKEN")
    private String accessToken;

    @NotNull
    @Column(name = "REFRESH_TOKEN")
    private String refreshToken;

    @NotNull
    @Column(name = "LOGGED_OUT")
    private boolean loggedOut;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;
}

