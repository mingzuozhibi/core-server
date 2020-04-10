package mingzuozhibi.coreserver.commons.base;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mingzuozhibi.coreserver.commons.gson.GsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@MappedSuperclass
public abstract class BaseModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @GsonIgnore
    private Long version;

}

