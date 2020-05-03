package mingzuozhibi.coreserver.modules.shelf;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mingzuozhibi.coreserver.commons.base.BaseModel;
import mingzuozhibi.coreserver.modules.disc.Disc;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import java.time.Instant;

@Entity(name = "disc_shelf")
@Setter
@Getter
@NoArgsConstructor
public class Shelf extends BaseModel {

    @Column(length = 20, nullable = false, unique = true)
    private String asin;

    @Column(length = 80)
    private String type;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(nullable = false)
    private Instant createOn;

    @OneToOne(fetch = FetchType.LAZY)
    private Disc disc;

    public Shelf(String asin, String type, String title) {
        this.asin = asin;
        this.type = type;
        this.title = title;
        this.createOn = Instant.now();
    }

    @Override
    public String toString() {
        return String.format("asin='%s', type='%s', title='%s'", asin, type, title);
    }

}
