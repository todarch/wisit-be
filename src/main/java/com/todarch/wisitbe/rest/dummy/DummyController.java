package com.todarch.wisitbe.rest.dummy;

import com.todarch.wisitbe.domain.user.UserRepository;
import com.todarch.wisitbe.infrastructure.aspect.InternalOnly;
import com.todarch.wisitbe.infrastructure.security.CurrentUser;
import com.todarch.wisitbe.infrastructure.security.CurrentUserProvider;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
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

  private final UserRepository userRepository;

  /**
   * Makes it easy to be treated like a new registered user.
   */
  @GetMapping("/swap-me")
  public void swapMe() {
    CurrentUser currentUser = currentUserProvider.currentUser();
    userRepository.findById(currentUser.id())
        .ifPresent(user -> {
          user.setIp("swapped" + ThreadLocalRandom.current().nextLong(1000000));
          userRepository.saveAndFlush(user);
        });
  }

  /**
   * Sends back information comes with the request.
   * Helper to see what is going on behind a proxy.
   */
  @GetMapping("/my-info")
  public ResponseEntity<MyInfo> getMyInfo(HttpServletRequest request) {
    String userAgentInfo = request.getHeader("User-Agent");

    Map<String, String> headers = new HashMap<>();
    request.getHeaderNames().asIterator()
        .forEachRemaining(header -> headers.put(header, request.getHeader(header)));

    MyInfo myInfo = new MyInfo();
    myInfo.setAgent(userAgentInfo);
    myInfo.setHeaders(headers);
    myInfo.setRemoteAddr(request.getRemoteAddr());

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

  /**
   * Helper endpoint for listing internal links.
   */
  @InternalOnly
  @GetMapping("/internal-links")
  public ResponseEntity<List<String>> internalLinks() {
    List<String> links = List.of(
        "/pictures/new",
        "/reportings/reported-questions",
        "/location/cities"
    );
    return ResponseEntity.ok(links);
  }
}
