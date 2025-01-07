package vn.fptu.reasbe.model.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
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
import vn.fptu.reasbe.model.entity.core.AbstractAuditableEntity;

/**
 *
 * @author ntig
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USER_LOCATION")
@AttributeOverride(name = "id", column = @Column(name = "USER_LOCATION_ID"))
public class UserLocation extends AbstractAuditableEntity {

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "LOCATION_ID", nullable = false)
    private Location location;

    @NotNull
    @Column(name = "SPECIFIC_ADDRESS", unique = true)
    private String specificAddress;

    @NotNull
    @Column(name = "IS_PRIMARY")
    private boolean isPrimary;

    @OneToMany(mappedBy = "userLocation", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Item> items = new HashSet<>();
}
