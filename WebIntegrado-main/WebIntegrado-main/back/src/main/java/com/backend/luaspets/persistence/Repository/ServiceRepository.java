package com.backend.luaspets.persistence.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.luaspets.persistence.Model.Services;

public interface ServiceRepository extends JpaRepository<Services, Integer> {

}
