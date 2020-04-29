package mingzuozhibi.coreserver.commons.support.page;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Page {

    @AliasFor("sort")
    String[] value() default {};

    int page() default 1;

    int size() default 50;

    String[] sort() default {};

}
