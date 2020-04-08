package com.todarch.wisitbe.infrastructure.security;

import com.todarch.wisitbe.infrastructure.rest.errorhandling.InternalApplicationException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class CurrentUserProvider {

  /**
   * Provides user information executing request.
   */
  public CurrentUser currentUser() {
    ServletRequestAttributes attributes =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

    if (attributes == null) {
      throw new InternalApplicationException("RequestContextHolder.getRequestAttributes() is null");
    }

    Object object =
        attributes.getAttribute(CurrentUser.class.getName(), RequestAttributes.SCOPE_REQUEST);

    if (!(object instanceof CurrentUser)) {
      throw new InternalApplicationException(String.format("obj is not %s", CurrentUser.class));
    }

    return (CurrentUser) object;

  }
}
