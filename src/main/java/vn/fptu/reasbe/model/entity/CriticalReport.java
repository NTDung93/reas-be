package vn.fptu.reasbe.model.entity;

import org.hibernate.Length;

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
@Table(name = "CRITICAL_REPORT")
@AttributeOverride(name = "id", column = @Column(name = "CRITICAL_REPORT_ID"))
public class CriticalReport extends AbstractAuditableEntity {

    @NotNull
    @Column(name = "TYPE_REPORT")
    private String typeReport; // TODO: ntig change to enum TypeReport later

    @NotNull
    @Column(name = "CONTENT_REPORT")
    private String contentReport;

    @Column(name = "CONTENT_RESPONSE")
    private String contentResponse;

    @Column(name = "IMAGE_URL", length = Length.LOB_DEFAULT)
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "REPORTER_ID")
    private User reporter;

    @ManyToOne
    @JoinColumn(name = "ANSWERER_ID")
    private User answerer;
}
