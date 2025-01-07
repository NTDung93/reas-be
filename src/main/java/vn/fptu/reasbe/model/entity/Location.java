package vn.fptu.reasbe.model.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
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
@Table(name = "LOCATION")
@AttributeOverride(name = "id", column = @Column(name = "LOCATION_ID"))
public class Location extends AbstractAuditableEntity {

    @NotNull
    @Column(name = "AREA", unique = true)
    private String area;

    @NotNull
    @Column(name = "PROVINCE")
    private String province;

    @NotNull
    @Column(name = "CITY")
    private String city;

    @NotNull
    @Column(name = "DISTRICT")
    private String district;

    @NotNull
    @Column(name = "WARD")
    private String ward;

    @NotNull
    @Column(name = "CLUSTER")
    private String cluster;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserLocation> userLocations = new HashSet<>();
}
