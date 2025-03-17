package vn.fptu.reasbe.model.mongodb;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import vn.fptu.reasbe.model.enums.user.StatusOnline;

/**
 *
 * @author dungnguyen
 */
@Getter
@Setter
@Builder
@Document
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    String userName;
    String fullName;
    StatusOnline statusOnline = StatusOnline.OFFLINE;
    Integer refId;
}
