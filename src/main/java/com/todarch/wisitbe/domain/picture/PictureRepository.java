package com.todarch.wisitbe.domain.picture;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PictureRepository extends JpaRepository<Picture, Long> {
  Optional<Picture> findByUrl(String picUrl);

  long countAllByActiveAndCityId(boolean active, long cityId);

  default long countAllActiveByCityId(long cityId) {
    return countAllByActiveAndCityId(true, cityId);
  }
}
