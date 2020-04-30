package mingzuozhibi.coreserver.commons.support.page;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort.Order;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PageSupport<T> {

    @Getter
    @Setter
    @AllArgsConstructor
    private static class SortObject<T, U> {
        private Function<T, U> keyExtractor;
        private Comparator<U> keyComparator;
        private boolean nullable;
    }

    private Map<String, SortObject<T, ?>> sorts = new HashMap<>();

    protected <U> void registSort(String name, Function<T, U> keyExtractor, Comparator<U> keyComparator, boolean nullable) {
        sorts.put(name, new SortObject<>(keyExtractor, keyComparator, nullable));
    }

    public List<T> filter(Set<T> discs, PageParams params) {
        return Optional.ofNullable(getComparator(params))
            .map(comparator -> discs.stream().sorted(comparator))
            .orElseGet(discs::stream)
            .skip((params.getPage() - 1) * params.getSize())
            .limit(params.getSize())
            .collect(Collectors.toList());
    }

    private Comparator<T> getComparator(PageParams params) {
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

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Comparator<T> getComparator(Order order) {
        var object = sorts.get(order.getProperty());

        Function keyExtractor = object.getKeyExtractor();
        Comparator keyComparator = object.getKeyComparator();
        boolean nullable = object.isNullable();

        if (order.isDescending()) keyComparator = keyComparator.reversed();
        if (nullable) keyComparator = Comparator.nullsLast(keyComparator);
        return Comparator.comparing(keyExtractor, keyComparator);
    }

}
