# Prerequisites

 * jdk19, postgres, any ide
 * optional - docker for desktop, jhipster, node, git

# TODO LIST

    * Final booking
    * API to configure application data (config data like create theatre, screen layout, shows)
    * optimize the code and design to meet further usecases

## Steps to run the application

Step 1: pull/download the code to local.

Step 2. Go to booking_gradle\src\main\resources\scripts folder, you will be finding postgres.yml, just docker-compose this file.(postgres and pgadmin will be running into you local docker container).

Step 3: run all the .sql files specified in queryOrder.txt using pgadmin to generate all the tables.

Step 4: start running the application.



## API

```
POST - http://localhost:8081/api/request-booking]
```

```
request body -
 {
  
    "city": "Chennai",
    "theatre": "PVR",
    "Screen": "AUDI 1",
    "movieName": "Avenger end game",
    "showDate": "25-12.2023",
    "show": "9:30 AM",
    "platformName": "Bookmyshow",
    "seatDetails": [
                      { "seatRow": "A", 
                        "seatNumbers": [1,2,3]
                       },
                       {
                         "seatRow": "A", 
                         "seatNumbers": [1,2]
                        }
                    ]
}
```

## About Jhipster

This application was generated using JHipster 7.9.3, you can find documentation and help at [https://www.jhipster.tech/documentation-archive/v7.9.3](https://www.jhipster.tech/documentation-archive/v7.9.3).

This is a "microservice" application intended to be part of a microservice architecture, please refer to the [Doing microservices with JHipster][] page of the documentation for more information.
This application is configured for Service Discovery and Configuration with . On launch, it will refuse to start if it is not able to connect to .

