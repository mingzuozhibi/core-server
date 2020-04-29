package mingzuozhibi.coreserver.commons.message;

import mingzuozhibi.coreserver.commons.message.enums.Index;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MsgsWired {

    Index value() default Index.Default;

}
