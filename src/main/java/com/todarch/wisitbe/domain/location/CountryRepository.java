package com.todarch.wisitbe.domain.location;

import com.todarch.wisitbe.infrastructure.rest.errorhandling.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Long> {

  default Country getById(long countryId) {
    return findById(countryId)
        .orElseThrow(() -> new ResourceNotFoundException("Country not found"));
  }
}
