package vn.fptu.reasbe.model.entity;

import java.util.Set;

import org.hibernate.Length;

import vn.fptu.reasbe.model.entity.core.AbstractAuditableEntity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "\"user\"")
@AttributeOverride(name = "id", column = @Column(name = "USER_ID"))
public class User extends AbstractAuditableEntity {

    @NotNull
    @Column(name = "USER_NAME", unique = true)
    private String userName;

    @NotNull
    @Column(name = "FULL_NAME")
    private String fullName;

    @NotNull
    @Column(name = "PASSWORD")
    private String password;

    @NotNull
    @Column(name = "EMAIL", unique = true)
    private String email;

    @NotNull
    @Column(name = "PHONE")
    private String phone;

    @NotNull
    @Column(name = "GENDER")
    private String gender;

    @NotNull
    @Column(name = "POINT")
    private long point = 0L;

    @NotNull
    @Column(name = "IS_FIRST_LOGIN")
    private boolean isFirstLogin;

    @NotNull
    @Column(name = "IMAGE", length = Length.LOB_DEFAULT)
    private String image;

    @OneToMany(mappedBy = "user")
    private Set<Token> tokens;

    @ManyToOne
    @JoinColumn(name = "ROLE_ID")
    private Role role;
}

