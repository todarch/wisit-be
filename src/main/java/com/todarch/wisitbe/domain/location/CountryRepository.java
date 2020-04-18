package com.todarch.wisitbe.domain.location;

import com.todarch.wisitbe.infrastructure.rest.errorhandling.ResourceNotFoundException;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Long> {
  List<Country> findAllByOrderByName();

  default Country getById(long countryId) {
    return findById(countryId)
        .orElseThrow(() -> new ResourceNotFoundException("Country not found"));
  }
}
