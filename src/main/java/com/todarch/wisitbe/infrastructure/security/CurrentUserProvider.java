package com.todarch.wisitbe.infrastructure.security;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class CurrentUserProvider {

  public CurrentUser currentUser() {
    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    Object object = attributes.getAttribute(CurrentUser.class.getName(), RequestAttributes.SCOPE_REQUEST);
    if (!(object instanceof CurrentUser)) {
      throw new IllegalStateException(String.format("obj is not %s", CurrentUser.class));
    }

    return (CurrentUser) object;
  }
}
