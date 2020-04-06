package com.todarch.wisitbe.infrastructure.aspect;

import com.todarch.wisitbe.infrastructure.config.WisitProperties;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@AllArgsConstructor
public class JustInternalAspect {

    private final WisitProperties wisitProperties;
    private final Environment environment;

    @Around("@annotation(com.todarch.wisitbe.infrastructure.aspect.InternalOnly)")
    public Object doNotProceedOnProd(ProceedingJoinPoint joinPoint) throws Throwable {
      HttpServletRequest request = currentRequest();
      String internalHeaderVal = request.getHeader(wisitProperties.getInternalHeaderName());

      if (environment.acceptsProfiles(Profiles.of("prod"))) {
        if (!wisitProperties.getInternalHeaderValue().equals(internalHeaderVal)) {
          throw new RuntimeException("Not allowed to run this method");
        }
      }
      return joinPoint.proceed();
    }

  private HttpServletRequest currentRequest() {
    RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
    if (requestAttributes instanceof ServletRequestAttributes) {
      return ((ServletRequestAttributes) requestAttributes).getRequest();
    }
    throw new RuntimeException("no current request");
  }
}
