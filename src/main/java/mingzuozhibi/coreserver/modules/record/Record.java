package mingzuozhibi.coreserver.modules.record;

import mingzuozhibi.coreserver.modules.disc.Disc;

import java.time.LocalDate;

public interface Record {

    Disc getDisc();

    LocalDate getDate();

    Double getAddPoint();

    Double getSumPoint();

    Double getPowPoint();

    Double getAverRank();

}
