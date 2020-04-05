package com.todarch.wisitbe.rest.dummy;

import com.todarch.wisitbe.infrastructure.security.CurrentUser;
import com.todarch.wisitbe.infrastructure.security.CurrentUserProvider;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dummy")
@AllArgsConstructor
public class DummyController {

  private final CurrentUserProvider currentUserProvider;

  @GetMapping("/my-info")
  public ResponseEntity<MyInfo> getMyInfo(HttpServletRequest request) {
    String userAgentInfo = request.getHeader("User-Agent");
    String remoteAddr = request.getRemoteAddr();

    Map<String, String> headers = new HashMap<>();
    request.getHeaderNames().asIterator()
        .forEachRemaining(header -> headers.put(header, request.getHeader(header)));

    MyInfo myInfo = new MyInfo();
    myInfo.setAgent(userAgentInfo);
    myInfo.setHeaders(headers);
    myInfo.setRemoteAddr(remoteAddr);

    return ResponseEntity.ok(myInfo);
  }

  @GetMapping("/current-user")
  public ResponseEntity<CurrentUser> currentUser() {
    return ResponseEntity.ok(currentUserProvider.currentUser());
  }

  @Data
  static class MyInfo {
    private String remoteAddr;
    private String agent;
    private Map<String, String> headers;
  }
}
