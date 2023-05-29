# Getting Started

To get started, follow these steps:

1. Download the repository.
2. Start up the application. It should run on port 8080.

## Endpoint

The endpoint available in this application is POST `/rewards`.
You can use this endpoint to calculate rewards for customer transactions.

You can import the provided Postman JSON file in this project, which contains a pre-filled request body for
the `/rewards` endpoint.

The given request body gives an example of these three scenarios:

* Customer 1 has multiple transactions throughout multiple months.
* Customer 2 has transactions in the 1st and 3rd months, but not in the second.
* Customer 3 has transactions in all months, but none of them earn points.

## Request

The POST `/rewards` endpoint expects a list of transactions in JSON format.
Each transaction should have the following structure:

```json
{
  "customerId": "61cc8e42-32a8-4d86-90f2-21486096dbeb",
  "transactionTimestamp": "2023-01-10T09:00:00",
  "totalPriceCents": 5000
}
```

## Response

The response from the `/rewards` endpoint is a JSON object representing a map of each customer's ID and their
corresponding rewards' breakdown.

If a customer did not earn any points, they will still be included in the response object for each month that they have
a transaction.

The response format is as follows:

```json
{
  "61cc8e42-32a8-4d86-90f2-21486096dbeb": {
    "monthlyRewards": [
      {
        "yearMonth": "2023-01",
        "monthlyPoints": 90
      },
      {
        "yearMonth": "2023-02",
        "monthlyPoints": 25
      },
      {
        "yearMonth": "2023-03",
        "monthlyPoints": 30
      }
    ],
    "totalPoints": 145
  },
  "b91587d7-840a-477c-a31d-5784c3d74854": {
    "monthlyRewards": [
      {
        "yearMonth": "2023-01",
        "monthlyPoints": 90
      },
      {
        "yearMonth": "2023-02",
        "monthlyPoints": 25
      },
      {
        "yearMonth": "2023-03",
        "monthlyPoints": 30
      }
    ],
    "totalPoints": 145
  }
}
```

## Not using Postman

If you would not like to use Postman and would prefer another technology.
The endpoint is POST `localhost:8080/rewards`.
The request object I placed in there is:

```json
[
  {
    "customerId": "61cc8e42-32a8-4d86-90f2-21486096dbeb",
    "transactionTimestamp": "2023-01-10T09:00:00",
    "totalPriceCents": 5000
  },
  {
    "customerId": "61cc8e42-32a8-4d86-90f2-21486096dbeb",
    "transactionTimestamp": "2023-01-15T14:30:00",
    "totalPriceCents": 12000
  },
  {
    "customerId": "61cc8e42-32a8-4d86-90f2-21486096dbeb",
    "transactionTimestamp": "2023-01-28T16:45:00",
    "totalPriceCents": 30000
  },
  {
    "customerId": "61cc8e42-32a8-4d86-90f2-21486096dbeb",
    "transactionTimestamp": "2023-02-05T11:45:00",
    "totalPriceCents": 7500
  },
  {
    "customerId": "61cc8e42-32a8-4d86-90f2-21486096dbeb",
    "transactionTimestamp": "2023-02-20T16:15:00",
    "totalPriceCents": 3000
  },
  {
    "customerId": "61cc8e42-32a8-4d86-90f2-21486096dbeb",
    "transactionTimestamp": "2023-02-20T16:15:00",
    "totalPriceCents": 500
  },
  {
    "customerId": "61cc8e42-32a8-4d86-90f2-21486096dbeb",
    "transactionTimestamp": "2023-03-08T08:30:00",
    "totalPriceCents": 8000
  },
  {
    "customerId": "b91587d7-840a-477c-a31d-5784c3d74854",
    "transactionTimestamp": "2023-01-05T08:00:00",
    "totalPriceCents": 7999
  },
  {
    "customerId": "b91587d7-840a-477c-a31d-5784c3d74854",
    "transactionTimestamp": "2023-01-06T11:07:00",
    "totalPriceCents": 14900
  },
  {
    "customerId": "b91587d7-840a-477c-a31d-5784c3d74854",
    "transactionTimestamp": "2023-03-08T08:30:00",
    "totalPriceCents": 8300
  },
  {
    "customerId": "b91587d7-840a-477c-a31d-5784c3d74854",
    "transactionTimestamp": "2023-03-14T04:30:00",
    "totalPriceCents": 4000
  },
  {
    "customerId": "a2354242-631b-4198-abef-ad0b24a7cb5d",
    "transactionTimestamp": "2023-01-14T03:30:00",
    "totalPriceCents": 4000
  },
  {
    "customerId": "a2354242-631b-4198-abef-ad0b24a7cb5d",
    "transactionTimestamp": "2023-02-02T14:35:00",
    "totalPriceCents": 1000
  },
  {
    "customerId": "a2354242-631b-4198-abef-ad0b24a7cb5d",
    "transactionTimestamp": "2023-03-18T23:30:00",
    "totalPriceCents": 2500
  }
]
```