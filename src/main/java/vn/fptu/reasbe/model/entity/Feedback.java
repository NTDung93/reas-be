package vn.fptu.reasbe.model.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
@Table(name = "FEEDBACK")
@AttributeOverride(name = "id", column = @Column(name = "FEEDBACK_ID"))
public class Feedback extends AbstractAuditableEntity {

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "EXCHANGE_HISTORY_ID")
    private ExchangeHistory exchangeHistory;

    @OneToOne
    @JoinColumn(name = "ITEM_ID")
    private Item item;

    @NotNull
    @Column(name = "RATING")
    private Integer rating;

    @Column(name = "COMMENT")
    private String comment;

    @Column(name = "IMAGE_URL")
    private String imageUrl;
}
