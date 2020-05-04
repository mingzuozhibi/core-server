package mingzuozhibi.coreserver;

import mingzuozhibi.coreserver.commons.message.enums.Index;
import mingzuozhibi.coreserver.commons.message.support.MsgsHelper;
import mingzuozhibi.coreserver.commons.support.ReflectUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableScheduling
@SpringBootApplication
public class CoreServerApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        ReflectUtils.disableAccessWarnings();
        var context = SpringApplication.run(CoreServerApplication.class, args);
        var msgs = context.getBean(MsgsHelper.class).with(Index.Default);
        msgs.notify("core-server已经启动");
    }

}
