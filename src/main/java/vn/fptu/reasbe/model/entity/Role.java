package vn.fptu.reasbe.model.entity;

import java.util.Set;

import vn.fptu.reasbe.model.entity.core.AbstractAuditableEntity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ROLE")
@AttributeOverride(name = "id", column = @Column(name = "ROLE_ID"))
public class Role extends AbstractAuditableEntity {

    @NotNull
    @Column(name = "NAME")
    private String name;

    @OneToMany(mappedBy = "role", orphanRemoval = true)
    private Set<User> users;
}
