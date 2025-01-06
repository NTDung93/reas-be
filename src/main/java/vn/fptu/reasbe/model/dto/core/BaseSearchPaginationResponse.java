package vn.fptu.reasbe.model.dto.core;

import java.util.List;

import org.springframework.data.domain.Page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author ntig
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseSearchPaginationResponse<T> {
    private int pageNo;
    private int pageSize;
    private int totalPages;
    private long totalRecords;
    private boolean last;
    private List<T> content;

    public static <T> BaseSearchPaginationResponse of(Page<T> page) {
        return new BaseSearchPaginationResponse<>(page.getNumber(), page.getSize(), page.getTotalPages(), page.getTotalElements(), page.isLast(), page.getContent());
    }
}
