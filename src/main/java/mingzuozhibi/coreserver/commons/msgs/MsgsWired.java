package mingzuozhibi.coreserver.commons.msgs;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MsgsWired {

    Index value() default Index.Default;

}
