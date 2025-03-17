package vn.fptu.reasbe.model.dto.core;

import java.util.List;

import org.springframework.data.domain.Page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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

    public static Pageable getPageable(int pageNo, int pageSize, String sortBy, String sortDir){
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        return PageRequest.of(pageNo, pageSize, sort);
    }
}
