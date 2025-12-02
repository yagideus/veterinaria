package com.backend.luaspets.Request;

import java.util.List;

import com.backend.luaspets.persistence.Model.SaleDetail;
import com.backend.luaspets.User.User;

import lombok.Data;

@Data
public class SaleRequest {
    private User user;
    private List<SaleDetail> saleDetails;
}
