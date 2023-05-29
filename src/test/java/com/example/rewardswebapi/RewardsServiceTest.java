package com.example.rewardswebapi;

import com.example.rewardswebapi.records.CustomerRewards;
import com.example.rewardswebapi.records.CustomerTransaction;
import com.example.rewardswebapi.records.MonthlyRewards;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

class RewardsServiceTest {

    public RewardsService rewardsService = new RewardsService();

    @Test
    void testCalculateCustomerRewardsOneCustomerMultipleMonths() {
        UUID customer1 = UUID.randomUUID();

        List<CustomerTransaction> transactions = new ArrayList<>();
        transactions.add(new CustomerTransaction(customer1, LocalDateTime.of(2023, Month.JANUARY, 10, 9, 0), 5000));
        transactions.add(new CustomerTransaction(customer1, LocalDateTime.of(2023, Month.JANUARY, 15, 14, 30), 12000));
        transactions.add(new CustomerTransaction(customer1, LocalDateTime.of(2023, Month.FEBRUARY, 5, 11, 45), 7500));
        transactions.add(new CustomerTransaction(customer1, LocalDateTime.of(2023, Month.FEBRUARY, 20, 16, 15), 3000));
        transactions.add(new CustomerTransaction(customer1, LocalDateTime.of(2023, Month.MARCH, 8, 8, 30), 8000));
        transactions.add(new CustomerTransaction(customer1, LocalDateTime.of(2023, Month.MARCH, 22, 13, 0), 6000));
        long januaryPoints = 90L;
        long februaryPoints = 25L;
        long marchPoints = 40L;
        long expectedTotalPoints = januaryPoints + februaryPoints + marchPoints;

        // Calculate customer rewards
        Map<UUID, CustomerRewards> customerRewards = rewardsService.calculateCustomerRewards(transactions);
        Assertions.assertEquals(1, customerRewards.size());

        // Assert the monthly breakdown
        CustomerRewards customer1RewardsBreakdown = customerRewards.get(customer1);
        Assertions.assertNotNull(customer1RewardsBreakdown);

        List<MonthlyRewards> monthlyRewards = customer1RewardsBreakdown.monthlyRewards();
        Assertions.assertEquals(3, monthlyRewards.size());

        MonthlyRewards januaryBreakdown = monthlyRewards.get(0);
        Assertions.assertEquals(YearMonth.of(2023, Month.JANUARY), januaryBreakdown.yearMonth());
        Assertions.assertEquals(januaryPoints, januaryBreakdown.monthlyPoints());

        MonthlyRewards februaryBreakdown = monthlyRewards.get(1);
        Assertions.assertEquals(YearMonth.of(2023, Month.FEBRUARY), februaryBreakdown.yearMonth());
        Assertions.assertEquals(februaryPoints, februaryBreakdown.monthlyPoints());

        MonthlyRewards marchBreakdown = monthlyRewards.get(2);
        Assertions.assertEquals(YearMonth.of(2023, Month.MARCH), marchBreakdown.yearMonth());
        Assertions.assertEquals(marchPoints, marchBreakdown.monthlyPoints());

        // Assert the total points
        Assertions.assertEquals(expectedTotalPoints, customer1RewardsBreakdown.totalPoints());
    }

    @Test
    void testCalculateCustomerRewardsMultipleCustomerOneMonth() {
        UUID customer1 = UUID.randomUUID();
        UUID customer2 = UUID.randomUUID();
        UUID customer3 = UUID.randomUUID();

        List<CustomerTransaction> transactions = new ArrayList<>();
        transactions.add(new CustomerTransaction(customer1, LocalDateTime.of(2023, Month.JANUARY, 10, 9, 0), 6000));
        transactions.add(new CustomerTransaction(customer2, LocalDateTime.of(2023, Month.JANUARY, 15, 14, 30), 12000));
        transactions.add(new CustomerTransaction(customer3, LocalDateTime.of(2023, Month.JANUARY, 10, 9, 0), 7000));

        long expectedCustomer1Points = 10L;
        long expectedCustomer2Points = 90L;
        long expectedCustomer3Points = 20L;

        // Calculate customer rewards
        Map<UUID, CustomerRewards> customerRewards = rewardsService.calculateCustomerRewards(transactions);
        Assertions.assertEquals(3, customerRewards.size());

        // Assert customer 1 rewards
        CustomerRewards customer1Rewards = customerRewards.get(customer1);
        Assertions.assertNotNull(customer1Rewards);

        List<MonthlyRewards> customer1MonthlyRewards = customer1Rewards.monthlyRewards();
        Assertions.assertEquals(1, customer1MonthlyRewards.size());

        MonthlyRewards customer1Jan = customer1MonthlyRewards.get(0);
        Assertions.assertEquals(YearMonth.of(2023, Month.JANUARY), customer1Jan.yearMonth());
        Assertions.assertEquals(expectedCustomer1Points, customer1Jan.monthlyPoints());
        Assertions.assertEquals(expectedCustomer1Points, customer1Rewards.totalPoints());

        // Assert customer 2 rewards
        CustomerRewards customer2Rewards = customerRewards.get(customer2);
        Assertions.assertNotNull(customer2Rewards);

        List<MonthlyRewards> customer2MonthlyRewards = customer2Rewards.monthlyRewards();
        Assertions.assertEquals(1, customer2MonthlyRewards.size());

        MonthlyRewards customer2Jan = customer2MonthlyRewards.get(0);
        Assertions.assertEquals(YearMonth.of(2023, Month.JANUARY), customer2Jan.yearMonth());
        Assertions.assertEquals(expectedCustomer2Points, customer2Jan.monthlyPoints());
        Assertions.assertEquals(expectedCustomer2Points, customer2Rewards.totalPoints());

        // Assert customer 2 rewards
        CustomerRewards customer3Rewards = customerRewards.get(customer3);
        Assertions.assertNotNull(customer3Rewards);

        List<MonthlyRewards> customer3MonthlyRewards = customer3Rewards.monthlyRewards();
        Assertions.assertEquals(1, customer3MonthlyRewards.size());

        MonthlyRewards customer3Jan = customer3MonthlyRewards.get(0);
        Assertions.assertEquals(YearMonth.of(2023, Month.JANUARY), customer3Jan.yearMonth());
        Assertions.assertEquals(expectedCustomer3Points, customer3Jan.monthlyPoints());
        Assertions.assertEquals(expectedCustomer3Points, customer3Rewards.totalPoints());
    }

    @Test
    void testCalculateCustomerRewardsMultipleCustomerMultipleMonths() {
        UUID customer1 = UUID.randomUUID();
        UUID customer2 = UUID.randomUUID();

        List<CustomerTransaction> transactions = new ArrayList<>();
        transactions.add(new CustomerTransaction(customer1, LocalDateTime.of(2023, Month.JANUARY, 10, 9, 0), 6000));
        transactions.add(new CustomerTransaction(customer1, LocalDateTime.of(2023, Month.FEBRUARY, 15, 14, 30), 12000));
        transactions.add(new CustomerTransaction(customer1, LocalDateTime.of(2023, Month.MARCH, 5, 11, 45), 7500));
        transactions.add(new CustomerTransaction(customer2, LocalDateTime.of(2023, Month.JANUARY, 10, 9, 0), 7000));
        transactions.add(new CustomerTransaction(customer2, LocalDateTime.of(2023, Month.FEBRUARY, 15, 14, 30), 20000));
        transactions.add(new CustomerTransaction(customer2, LocalDateTime.of(2023, Month.MARCH, 5, 11, 45), 80000));
        long expectedCustomer1JanPoints = 10L;
        long expectedCustomer1FebPoints = 90L;
        long expectedCustomer1MarPoints = 25L;
        long expectedCustomer1TotalPoints = expectedCustomer1JanPoints + expectedCustomer1FebPoints + expectedCustomer1MarPoints;
        long expectedCustomer2JanPoints = 20L;
        long expectedCustomer2FebPoints = 250L;
        long expectedCustomer2MarPoints = 1450L;
        long expectedCustomer2TotalPoints = expectedCustomer2JanPoints + expectedCustomer2FebPoints + expectedCustomer2MarPoints;

        // Calculate customer rewards
        Map<UUID, CustomerRewards> customerRewards = rewardsService.calculateCustomerRewards(transactions);
        Assertions.assertEquals(2, customerRewards.size());

        // Assert customer 1 rewards
        CustomerRewards customer1Rewards = customerRewards.get(customer1);
        Assertions.assertNotNull(customer1Rewards);

        List<MonthlyRewards> customer1MonthlyRewards = customer1Rewards.monthlyRewards();
        Assertions.assertEquals(3, customer1MonthlyRewards.size());

        MonthlyRewards customer1Jan = customer1MonthlyRewards.get(0);
        Assertions.assertEquals(YearMonth.of(2023, Month.JANUARY), customer1Jan.yearMonth());
        Assertions.assertEquals(expectedCustomer1JanPoints, customer1Jan.monthlyPoints());

        MonthlyRewards customer1Feb = customer1MonthlyRewards.get(1);
        Assertions.assertEquals(YearMonth.of(2023, Month.FEBRUARY), customer1Feb.yearMonth());
        Assertions.assertEquals(expectedCustomer1FebPoints, customer1Feb.monthlyPoints());

        MonthlyRewards customer1Mar = customer1MonthlyRewards.get(2);
        Assertions.assertEquals(YearMonth.of(2023, Month.MARCH), customer1Mar.yearMonth());
        Assertions.assertEquals(expectedCustomer1MarPoints, customer1Mar.monthlyPoints());

        Assertions.assertEquals(expectedCustomer1TotalPoints, customer1Rewards.totalPoints());

        // Assert customer 2 rewards
        CustomerRewards customer2Rewards = customerRewards.get(customer2);
        Assertions.assertNotNull(customer2Rewards);

        List<MonthlyRewards> monthlyRewards = customer2Rewards.monthlyRewards();
        Assertions.assertEquals(3, monthlyRewards.size());

        MonthlyRewards customer2Jan = monthlyRewards.get(0);
        Assertions.assertEquals(YearMonth.of(2023, Month.JANUARY), customer2Jan.yearMonth());
        Assertions.assertEquals(expectedCustomer2JanPoints, customer2Jan.monthlyPoints());

        MonthlyRewards customer2Feb = monthlyRewards.get(1);
        Assertions.assertEquals(YearMonth.of(2023, Month.FEBRUARY), customer2Feb.yearMonth());
        Assertions.assertEquals(expectedCustomer2FebPoints, customer2Feb.monthlyPoints());

        MonthlyRewards customer2Mar = monthlyRewards.get(2);
        Assertions.assertEquals(YearMonth.of(2023, Month.MARCH), customer2Mar.yearMonth());
        Assertions.assertEquals(expectedCustomer2MarPoints, customer2Mar.monthlyPoints());

        Assertions.assertEquals(expectedCustomer2TotalPoints, customer2Rewards.totalPoints());
    }

}