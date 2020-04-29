package mingzuozhibi.coreserver.commons.message;

import mingzuozhibi.coreserver.commons.message.enums.Tag;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MsgsWired {

    Tag value() default Tag.Default;

}
