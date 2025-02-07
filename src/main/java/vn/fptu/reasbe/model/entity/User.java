package vn.fptu.reasbe.model.entity;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.Length;

import jakarta.persistence.CascadeType;
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
@Table(name = "\"USER\"")
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
    @Column(name = "IS_FIRST_LOGIN")
    private boolean isFirstLogin;

    @NotNull
    @Column(name = "IMAGE", length = Length.LOB_DEFAULT)
    private String image;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Token> tokens;

    @ManyToOne
    @JoinColumn(name = "ROLE_ID")
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserLocation> userLocations = new HashSet<>();

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Notification> notifications = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FinancialReport> financialReports = new HashSet<>();

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Item> items = new HashSet<>();

    @OneToMany(mappedBy = "reporter", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CriticalReport> criticalReports = new HashSet<>();

    @OneToMany(mappedBy = "answerer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CriticalReport> criticalReportResponses = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserSubscription> userSubscriptions = new HashSet<>();
}

