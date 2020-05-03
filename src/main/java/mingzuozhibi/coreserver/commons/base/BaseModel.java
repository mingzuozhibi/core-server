package mingzuozhibi.coreserver.commons.base;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mingzuozhibi.coreserver.commons.gson.NotGson;

import javax.persistence.*;
import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@MappedSuperclass
public abstract class BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @NotGson
    private Long version;

}

