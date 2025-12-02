package com.backend.luaspets.persistence.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.luaspets.persistence.Model.Sale;
import com.backend.luaspets.persistence.Model.SaleDetail;

public interface SaleDetailRepository extends JpaRepository<SaleDetail, Integer>{
    List<SaleDetail> findBySale(Sale sale);
}
