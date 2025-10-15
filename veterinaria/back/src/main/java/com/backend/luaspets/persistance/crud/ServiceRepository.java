package com.backend.luaspets.persistance.crud;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.luaspets.persistance.entity.Services;

public interface ServiceRepository extends JpaRepository<Services, Integer> {

}
