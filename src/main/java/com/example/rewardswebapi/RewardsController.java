package com.example.rewardswebapi;

import com.example.rewardswebapi.records.CustomerRewards;
import com.example.rewardswebapi.records.CustomerTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class RewardsController {

    private final RewardsService rewardsService;

    @Autowired
    public RewardsController(RewardsService rewardsService) {
        this.rewardsService = rewardsService;
    }

    @PostMapping("/rewards")
    public Map<UUID, CustomerRewards> calculateCustomerRewards(@RequestBody List<CustomerTransaction> customerTransactions) {
        return rewardsService.calculateCustomerRewards(customerTransactions);
    }
}
