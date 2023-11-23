## Redis Powered Caching in Java Spring Boot
##### A reference proof-of-concept that leverages caching to reduce network calls and improve latency.

---

### Overview

* Database tables and their corresponding initial data are established using [Flyway migration scripts](https://github.com/hardikSinghBehl/redis-caching-java-spring-boot/tree/main/src/main/resources/db/migration).
* The application primarily revolves around the below database entities
  * master_houses
  * wizards
* The `wizards` entity has a Many-to-One relationship with `master_houses`. This [database diagram](https://github.com/hardikSinghBehl/redis-caching-java-spring-boot/blob/main/docs/database_diagram.png) can be referenced for more information. 
* The responsibility of connecting to the provisioned cache instance as configured in the `application.yml` file is managed by [RedisConfiguration.java](https://github.com/hardikSinghBehl/redis-caching-java-spring-boot/blob/main/src/main/java/com/behl/cachetropolis/configuration/RedisConfiguration.java).
* The [service layer](https://github.com/hardikSinghBehl/redis-caching-java-spring-boot/tree/main/src/main/java/com/behl/cachetropolis/service) of the application caches the list of wizards corresponding to a particular house where house ID is kept as the cache key. The stored key is removed from the cache when any wizard record is added, updated, or removed from that particular house to maintain strong read consistency.
* The above approach is followed for individual wizard records as well, utilizing the wizard ID as the cache key.

### Testing

Testcontainers have been leveraged to test the caching mechanism within the application. Scenarios of cache hit, cache invalidation, cache updation and data consistency has been tested in the service layer test classes listed below:

* [WizardServiceTest.java](https://github.com/hardikSinghBehl/redis-caching-java-spring-boot/blob/main/src/test/java/com/behl/cachetropolis/service/WizardServiceTest.java)
* [MasterHouseServiceTest.java](https://github.com/hardikSinghBehl/redis-caching-java-spring-boot/blob/main/src/test/java/com/behl/cachetropolis/service/MasterHouseServiceTest.java)

Tests can be executed with the below command

```
mvn test
```

---

### Local Setup Instructions
Execute the following commands in the project's base directory to launch the necessary containers:  

```bash
sudo docker-compose build
```
```bash
sudo docker-compose up -d
```
Docker Compose will start containers for MySQL, Redis, and the Backend Application, all within the same network.

Access Swagger UI at `http://localhost:8080/swagger-ui.html`  
