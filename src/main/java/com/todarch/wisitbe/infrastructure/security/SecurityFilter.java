package com.todarch.wisitbe.infrastructure.security;

import com.todarch.wisitbe.domain.user.User;
import com.todarch.wisitbe.domain.user.UserRepository;
import com.todarch.wisitbe.infrastructure.messaging.event.UserCreatedEvent;
import com.todarch.wisitbe.infrastructure.messaging.publisher.WisitEventPublisher;
import java.io.IOException;
import java.security.Principal;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.KeycloakPrincipal;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.filter.GenericFilterBean;

/**
 * Associates each request with a session using ip as an identifier.
 * Creates a new user if not registered on database yet.
 */
@Component
@AllArgsConstructor
@Slf4j
public class SecurityFilter extends GenericFilterBean {

  private final UserRepository userRepository;

  private final WisitEventPublisher wisitEventPublisher;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    toCurrentUser(request)
        .ifPresent(currentUser -> request.setAttribute(CurrentUser.class.getName(), currentUser));

    chain.doFilter(request, response);
  }

  private Optional<KeycloakPrincipal> toKeycloakPrincipal() {
    ServletRequestAttributes attributes =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    Principal principal = attributes.getRequest().getUserPrincipal();
    return Optional.ofNullable(principal)
        .map(KeycloakPrincipal.class::cast);
  }

  private Optional<CurrentUser> toCurrentUser(ServletRequest request) {
    return toKeycloakPrincipal().map(keycloakPrincipal -> {
      String currentUserId = keycloakPrincipal.getName();
      String username =
          keycloakPrincipal.getKeycloakSecurityContext().getToken().getPreferredUsername();

      HttpServletRequest httpServletRequest = (HttpServletRequest) request;
      String userIp = extractUserIp(httpServletRequest);
      User loadedUser = userRepository.findById(currentUserId)
          .orElseGet(() -> {
            User user = new User();
            user.setId(currentUserId);
            user.setIp(userIp);
            user.setUserAgent(httpServletRequest.getHeader("User-Agent"));
            user.setUsername(username);
            User newUser = userRepository.saveAndFlush(user);
            UserCreatedEvent userCreatedEvent = new UserCreatedEvent();
            userCreatedEvent.setUserId(newUser.getId());
            wisitEventPublisher.publishEvent(userCreatedEvent);
            return newUser;
          });
      return new CurrentUser(loadedUser);
    });
  }

  /**
   * Extracts user ip from requests taking into account proxy.
   */
  private String extractUserIp(HttpServletRequest httpServletRequest) {
    String userIpFromProxyHeader = "x-forwarded-for";
    String userIpFromProxy = httpServletRequest.getHeader(userIpFromProxyHeader);
    if (userIpFromProxy == null) {
      return httpServletRequest.getRemoteAddr();
    }
    return userIpFromProxy;
  }
}
