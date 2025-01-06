package vn.fptu.reasbe.model.entity;

import java.util.Set;

import vn.fptu.reasbe.model.entity.core.AbstractAuditableEntity;

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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "BRAND")
@AttributeOverride(name = "id", column = @Column(name = "BRAND_ID"))
public class Brand extends AbstractAuditableEntity {

    @NotNull
    @Column(name = "BRAND_NAME")
    private String brandName;

    @NotNull
    @Column(name = "IMAGE")
    private String image;

    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Product> products;
}
