package com.todarch.wisitbe.domain.user.list;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface ListedPictureRepository extends JpaRepository<ListedPicture, String> {
  List<ListedPicture> findAllByUserId(String userId);

  Optional<ListedPicture> findByUserIdAndPictureId(String userId, long pictureId);

  @Transactional
  void deleteByUserIdAndPictureId(String userId, long pictureId);
}
