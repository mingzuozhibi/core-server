package mingzuozhibi.coreserver.security;

import mingzuozhibi.coreserver.modules.auth.token.Token;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

class TokenAuthentication implements Authentication {

    private static final long serialVersionUID = 1L;

    private Token token;
    private boolean authenticated;

    public TokenAuthentication(Token token) {
        this.token = token;
        this.authenticated = true;
    }

    @Override
    public Set<GrantedAuthority> getAuthorities() {
        return token.getUser().getRoles().stream()
            .map(role -> "ROLE_" + role)
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toSet());
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return token.getUser().getUsername();
    }

    public Token getToken() {
        return token;
    }

}
