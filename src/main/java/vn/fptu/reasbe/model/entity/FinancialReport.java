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
@Table(name = "FINANCIAL_REPORT")
@AttributeOverride(name = "id", column = @Column(name = "FINANCIAL_REPORT_ID"))
public class FinancialReport extends AbstractAuditableEntity {

    @NotNull
    @Column(name = "MONTH")
    private int month;

    @NotNull
    @Column(name = "YEAR")
    private int year;

    @NotNull
    @Column(name = "TOTAL_REVENUE")
    private int totalRevenue;

    @NotNull
    @Column(name = "TOTAL_TRANSACTION")
    private int totalTransaction;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;
}
