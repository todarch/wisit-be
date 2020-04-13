package com.todarch.wisitbe.explorer.flickr;

import com.todarch.wisitbe.explorer.flickr.model.FlickrPhoto;
import com.todarch.wisitbe.explorer.flickr.model.FlickrSearchPhotoResponse;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


@Component
@AllArgsConstructor
@Slf4j
public class FlickrClient {

  private final FlickrProperties flickrProperties;

  private final RestTemplate restTemplate = new RestTemplate();

  /**
   * Searches photos for the given city and returns the list of urls.
   */
  public List<String> searchPhotos(@NonNull String city) {
    log.info("Searching photos {}", city);
    HttpHeaders headers = new HttpHeaders();
    headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

    ThreadLocalRandom random = ThreadLocalRandom.current();

    UriComponentsBuilder builder =
        UriComponentsBuilder.fromHttpUrl(flickrProperties.getApiBaseUrl())
            .queryParam("method", "flickr.photos.search")
            .queryParam("api_key", flickrProperties.getApiKey())
            .queryParam("tags", city)
            .queryParam("text", city)
            .queryParam("accuracy", "~11")
            .queryParam("sort", "interestingness-desc")
            .queryParam("page", random.nextInt(1, 3000))
            .queryParam("per_page", random.nextInt(1, 10))
            .queryParam("content_type", 1)
            .queryParam("format", "json")
            .queryParam("nojsoncallback", 1);

    HttpEntity<?> entity = new HttpEntity<>(headers);

    String url = builder.toUriString();
    log.info("prepared url: {}", url);

    HttpEntity<FlickrSearchPhotoResponse> response =
        restTemplate.exchange(
            url,
            HttpMethod.GET,
            entity,
            FlickrSearchPhotoResponse.class);

    FlickrSearchPhotoResponse flickrSearchPhotoResponse = response.getBody();

    if (flickrSearchPhotoResponse != null && flickrSearchPhotoResponse.isOk()) {
      return flickrSearchPhotoResponse.getPhotos().getPhoto()
          .stream()
          .map(FlickrPhoto::toUrl)
          .collect(Collectors.toList());
    } else {
      return Collections.emptyList();
    }
  }
}
