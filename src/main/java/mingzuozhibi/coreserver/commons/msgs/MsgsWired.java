package mingzuozhibi.coreserver.commons.msgs;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MsgsWired {

    Msgs.Tag value() default Msgs.Tag.Default;

}
