package mingzuozhibi.coreserver.security;

import mingzuozhibi.coreserver.commons.message.enums.Index;
import mingzuozhibi.coreserver.commons.message.support.MsgsHelper;
import mingzuozhibi.coreserver.modules.user.User;
import mingzuozhibi.coreserver.modules.user.UserRepository;
import mingzuozhibi.coreserver.modules.user.enums.Role;
import mingzuozhibi.coreserver.security.support.PasswdEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SecurityFilter securityFilter;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MsgsHelper msgsHelper;

    @Value("${root.admin.username:admin}")
    private String rootAdminUsername;

    @Value("${root.admin.password:admin}")
    private String rootAdminPassword;

    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .anyRequest()
            .permitAll()

            .and()
            .csrf()
            .disable()

            .addFilterBefore(securityFilter, AnonymousAuthenticationFilter.class);

        if (userRepository.findByUsername(rootAdminUsername).isEmpty()) {
            String encode = PasswdEncoder.encode(rootAdminUsername, rootAdminPassword);
            User user = new User(rootAdminUsername, encode, true);
            user.getRoles().add(Role.RootAdmin);
            user.getRoles().add(Role.UserAdmin);
            userRepository.save(user);
            msgsHelper.notify(Index.User, "已经初始化超级管理员用户");
        }

    }

}
