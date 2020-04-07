package com.todarch.wisitbe.domain.picture;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PictureRepository extends JpaRepository<Picture, Long> {
  Optional<Picture> findByUrl(String picUrl);
}
