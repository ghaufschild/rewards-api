package com.example.rewardswebapi;

import com.example.rewardswebapi.records.CustomerRewards;
import com.example.rewardswebapi.records.CustomerTransaction;
import com.example.rewardswebapi.records.MonthlyRewards;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.stream.Collectors.groupingBy;

@Service
public class RewardsService {

    public Map<UUID, CustomerRewards> calculateCustomerRewards(List<CustomerTransaction> customerTransactions) {
        Map<UUID, List<CustomerTransaction>> customerTransactionsByCustomerId = customerTransactions.stream()
                .collect(groupingBy(CustomerTransaction::customerId));
        Map<UUID, CustomerRewards> customerRewardsByCustomerId = new HashMap<>();

        customerTransactionsByCustomerId
                .forEach((customerId, individualCustomerTransactions) ->
                        customerRewardsByCustomerId.put(customerId, calculateCustomerRewardsForCustomer(individualCustomerTransactions)));

        return customerRewardsByCustomerId;
    }

    private CustomerRewards calculateCustomerRewardsForCustomer(List<CustomerTransaction> transactions) {
        Map<YearMonth, Long> rewardPointsPerMonth = new HashMap<>();
        long totalPoints = 0;

        for (CustomerTransaction transaction : transactions) {
            YearMonth yearMonth = YearMonth.of(transaction.transactionTimestamp().getYear(), transaction.transactionTimestamp().getMonth());
            long rewardPoints = calculateRewardPoints(transaction.totalPriceCents());

            rewardPointsPerMonth.merge(yearMonth, rewardPoints, Long::sum);
            totalPoints += rewardPoints;
        }

        List<MonthlyRewards> monthlyRewards = rewardPointsPerMonth.entrySet()
                .stream()
                .map(entry -> new MonthlyRewards(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(MonthlyRewards::yearMonth))
                .toList();

        return new CustomerRewards(monthlyRewards, totalPoints);
    }

    private int calculateRewardPoints(int amount) {
        int rewardPoints = 0;

        if (amount > 10000) {
            rewardPoints += 2 * (amount - 10000) / 100;
            amount = 10000;
        }

        if (amount > 5000) {
            rewardPoints += (amount - 5000) / 100;
        }

        return rewardPoints;
    }
}
