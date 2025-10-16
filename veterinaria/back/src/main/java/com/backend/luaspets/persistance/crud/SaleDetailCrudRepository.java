package com.backend.luaspets.persistance.crud;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.luaspets.persistance.entity.Sale;
import com.backend.luaspets.persistance.entity.SaleDetail;

public interface SaleDetailCrudRepository extends JpaRepository<SaleDetail, Integer>{
    List<SaleDetail> findBySale(Sale sale);
}
