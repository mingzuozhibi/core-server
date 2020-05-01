package mingzuozhibi.coreserver.modules.disc.support;

import mingzuozhibi.coreserver.commons.support.page.PageSupport;
import mingzuozhibi.coreserver.modules.disc.Disc;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DiscPageSupport extends PageSupport<Disc> {

    public DiscPageSupport() {
        registSort("title", Disc::autoTitle, String::compareTo, false);
        registSort("rank", Disc::getThisRank, Integer::compareTo, true);
        registSort("date", Disc::getReleaseDate, LocalDate::compareTo, true);
    }

}
