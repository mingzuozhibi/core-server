package mingzuozhibi.coreserver.commons.support.page;

import org.springframework.data.domain.Sort.Order;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class PageSupport<T> {

    private Map<String, Comparator<T>> sorts = new HashMap<>();

    public PageSupport(Consumer<Map<String, Comparator<T>>> consumer) {
        consumer.accept(sorts);
    }

    public List<T> filter(Set<T> discs, PageParams params) {
        return Optional.ofNullable(getComparator(params))
            .map(comparator -> discs.stream().sorted(comparator))
            .orElseGet(discs::stream)
            .skip((params.getPage() - 1) * params.getSize())
            .limit(params.getSize())
            .collect(Collectors.toList());
    }

    public Comparator<T> getComparator(PageParams params) {
        Comparator<T> comparator = null;
        for (Order order : params.getSort().toList()) {
            if (comparator == null) {
                comparator = getComparator(order);
            } else {
                comparator = comparator.thenComparing(getComparator(order));
            }
        }
        return comparator;
    }

    public Comparator<T> getComparator(Order order) {
        var comparator = sorts.get(order.getProperty());
        if (order.isDescending()) {
            comparator = comparator.reversed();
        }
        return Comparator.nullsLast(comparator);
    }

}
