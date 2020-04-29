package mingzuozhibi.coreserver.modules.disc.support;

import mingzuozhibi.coreserver.commons.support.page.PageParams;
import mingzuozhibi.coreserver.modules.disc.Disc;
import org.springframework.data.domain.Sort.Order;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Comparator.comparing;
import static java.util.Comparator.nullsLast;

public abstract class DiscComparators {

    public static Map<String, Comparator<Disc>> SORTS;

    static {
        SORTS = new HashMap<>();
        SORTS.put("title", comparing(Disc::autoTitle, String::compareTo));
        SORTS.put("rank", comparing(Disc::getThisRank, Integer::compareTo));
        SORTS.put("date", comparing(Disc::getReleaseDate, LocalDate::compareTo));
    }

    public static Comparator<Disc> sorted(PageParams params) {
        List<Order> orders = params.getSort().toList();
        Comparator<Disc> comparator = null;
        for (Order order : orders) {
            if (comparator == null) {
                comparator = getComparator(order);
            } else {
                comparator = comparator.thenComparing(getComparator(order));
            }
        }
        return comparator;
    }

    private static Comparator<Disc> getComparator(Order order) {
        var comparator = SORTS.get(order.getProperty());
        if (order.isDescending()) {
            comparator = comparator.reversed();
        }
        return nullsLast(comparator);
    }

}
