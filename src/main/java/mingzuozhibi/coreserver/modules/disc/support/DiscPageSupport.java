package mingzuozhibi.coreserver.modules.disc.support;

import mingzuozhibi.coreserver.commons.support.page.PageSupport;
import mingzuozhibi.coreserver.modules.disc.Disc;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static java.util.Comparator.comparing;

@Component
public class DiscPageSupport extends PageSupport<Disc> {

    public DiscPageSupport() {
        super(sorts -> {
            sorts.put("title", comparing(Disc::autoTitle, String::compareTo));
            sorts.put("rank", comparing(Disc::getThisRank, Integer::compareTo));
            sorts.put("date", comparing(Disc::getReleaseDate, LocalDate::compareTo));
        });
    }

}
