package com.todarch.wisitbe.domain.picture;

import com.todarch.wisitbe.infrastructure.rest.errorhandling.ResourceNotFoundException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PictureRepository extends JpaRepository<Picture, Long> {
  Optional<Picture> findByUrl(String picUrl);

  long countAllByActiveAndCityId(boolean active, long cityId);

  default long countAllActiveByCityId(long cityId) {
    return countAllByActiveAndCityId(true, cityId);
  }

  default Picture getById(long pictureId) {
    return findById(pictureId).orElseThrow(() ->
            new ResourceNotFoundException("Picture not found for id"));
  }
}
