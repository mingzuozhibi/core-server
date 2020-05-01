package mingzuozhibi.coreserver.commons.support.page;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public class PageParams {

    private int page = 1;
    private int size = 50;
    private Sort sort;

    public Pageable toPageable() {
        if (sort == null) {
            return PageRequest.of(page - 1, size);
        } else {
            return PageRequest.of(page - 1, size, sort);
        }
    }

}
