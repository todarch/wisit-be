package com.todarch.wisitbe.domain.location;

import com.todarch.wisitbe.infrastructure.rest.errorhandling.ResourceNotFoundException;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Long> {
  List<City> findAllByOrderByName();

  Optional<City> findByName(String name);

  default City getById(Long cityId) {
    return findById(cityId)
        .orElseThrow(() -> new ResourceNotFoundException("City not found: " + cityId));
  }
}
