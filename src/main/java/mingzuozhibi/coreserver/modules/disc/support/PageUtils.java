package mingzuozhibi.coreserver.modules.disc.support;

import mingzuozhibi.coreserver.commons.support.page.PageParams;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class PageUtils {

    public static <T> List<T> filter(Set<T> discs, PageParams params, Function<PageParams, Comparator<T>> function) {
        Comparator<T> apply = function.apply(params);
        Stream<T> stream = discs.stream();
        if (apply != null) {
            stream = stream.sorted(apply);
        }
        return stream.skip((params.getPage() - 1) * params.getSize())
            .limit(params.getSize())
            .collect(Collectors.toList());
    }

}
