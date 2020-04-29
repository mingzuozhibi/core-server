package mingzuozhibi.coreserver.commons.message;

import lombok.SneakyThrows;
import mingzuozhibi.coreserver.commons.message.enums.Tag;
import mingzuozhibi.coreserver.commons.message.support.MsgsHelper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class MsgsWiredProcessor implements BeanPostProcessor {

    @Autowired
    private MsgsHelper msgsHelper;

    @SneakyThrows
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        for (Field field : beanClass.getDeclaredFields()) {
            MsgsWired annotation = field.getAnnotation(MsgsWired.class);
            if (annotation != null) {
                Tag tag = annotation.value();
                field.setAccessible(true);
                field.set(bean, msgsHelper.with(tag));
                msgsHelper.debug(Tag.Default, "@MsgsWired: %s注入Msgs(%s)成功", beanName, tag.name());
            }
        }
        return bean;
    }

}
