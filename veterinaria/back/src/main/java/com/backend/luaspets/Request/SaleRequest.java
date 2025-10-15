package com.backend.luaspets.Request;

import java.util.List;

import com.backend.luaspets.User.User;
import com.backend.luaspets.persistance.entity.SaleDetail;

import lombok.Data;

@Data
public class SaleRequest {
    private User user;
    private List<SaleDetail> saleDetails;
}
