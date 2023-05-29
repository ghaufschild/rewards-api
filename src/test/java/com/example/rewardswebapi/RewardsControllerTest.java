package com.example.rewardswebapi;

import com.example.rewardswebapi.records.CustomerRewards;
import com.example.rewardswebapi.records.CustomerTransaction;
import com.example.rewardswebapi.records.MonthlyRewards;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.util.*;

@SpringBootTest
@AutoConfigureMockMvc
class RewardsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RewardsService rewardsService;

    @Test
    void testCalculateCustomerRewards() throws Exception {
        // Create a sample list of transactions
        UUID customerId = UUID.fromString("b91587d7-840a-477c-a31d-5784c3d74854");
        List<CustomerTransaction> customerTransactions = Arrays.asList(
                new CustomerTransaction(customerId, LocalDateTime.of(2023, Month.JANUARY, 1, 10, 0), 10000),
                new CustomerTransaction(customerId, LocalDateTime.of(2023, Month.FEBRUARY, 15, 14, 30), 20000),
                new CustomerTransaction(customerId, LocalDateTime.of(2023, Month.MARCH, 20, 9, 45), 15000)
        );

        // Create a sample response from the rewards service
        List<MonthlyRewards> monthlyRewards = Arrays.asList(
                new MonthlyRewards(YearMonth.of(2023, Month.JANUARY), 50L),
                new MonthlyRewards(YearMonth.of(2023, Month.FEBRUARY), 250L),
                new MonthlyRewards(YearMonth.of(2023, Month.MARCH), 150L)
        );
        CustomerRewards customerRewards = new CustomerRewards(monthlyRewards, 450L);

        // Create a map of customer rewards by customer ID
        Map<UUID, CustomerRewards> customerRewardsMap = new HashMap<>();
        customerRewardsMap.put(customerId, customerRewards);

        // Mock the behavior of the rewards service
        Mockito.when(rewardsService.calculateCustomerRewards(customerTransactions)).thenReturn(customerRewardsMap);

        // Perform the HTTP POST request to the "/rewards" endpoint
        mockMvc.perform(MockMvcRequestBuilders.post("/rewards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[{\"customerId\":\"b91587d7-840a-477c-a31d-5784c3d74854\",\"transactionTimestamp\":\"2023-01-01T10:00:00\",\"totalPriceCents\":10000},"
                                + "{\"customerId\":\"b91587d7-840a-477c-a31d-5784c3d74854\",\"transactionTimestamp\":\"2023-02-15T14:30:00\",\"totalPriceCents\":20000},"
                                + "{\"customerId\":\"b91587d7-840a-477c-a31d-5784c3d74854\",\"transactionTimestamp\":\"2023-03-20T09:45:00\",\"totalPriceCents\":15000}]"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.b91587d7-840a-477c-a31d-5784c3d74854.totalPoints").value(450));
    }
}
