# Java Spring-boot application using Redis as a caching database

### [Running Application](https://spring-boot-redis-caching.herokuapp.com/swagger-ui.html)

## Entities involved
* Two main entities are involved
  * MasterHouse (master_houses)
  * Wizard (wizards)
* Wizards have a many-to-one relationship with MasterHouse
* Example:
  *  Harry Potter (wizard) belongs to Gryffindor (masterHouse)
  *  Ron Weasley (wizard) belongs to Gryffindor (masterHouse)
  *  Hermione Granger (wizard) belongs to Gryffindor (masterHouse)
  *  Griffindor (masterHouse) has Harry Potter, Ron Weasly and Hermione Granger (wizards)

## Caching Flow
* The house id is used as the key and the list of wizards belonging to that particular house are kept as the value in redis DB (key-value store)
* The key is removed from the cache when a new wizard is added/updated/removed to/from that house
* When the API is hit again, the updated list value is added to the cache and the process is repeated
* Addition to that the API to retreive wizard by #id is also cached and goes through the above mentioned process as well where the wizard's pkey is used as a key to store the corresponding DTO value

## Main classes/files
* Service classes where caching is used
  * [MasterHouseService](https://github.com/hardikSinghBehl/redis-caching-java-spring-boot/blob/main/src/main/java/com/hardik/bojack/service/MasterHouseService.java)
  * [WizardService](https://github.com/hardikSinghBehl/redis-caching-java-spring-boot/blob/main/src/main/java/com/hardik/bojack/service/WizardService.java)
* .sql migration files
  * [Flyway scripts](https://github.com/hardikSinghBehl/redis-caching-java-spring-boot/tree/main/src/main/resources/db/migration)
* Redis config classes
  * [Link](https://github.com/hardikSinghBehl/redis-caching-java-spring-boot/tree/main/src/main/java/com/hardik/bojack/configuration)

## Technologies used
* Java-15
* Spring-boot
* PostgreSQL
* Flyway
* Redis
* Open-API(Swagger)
* Lombok

## Setup Locally Without Docker

* Install Java 15
* Install Maven
* Install PostgreSQL
* Install Redis

Recommended way is to use [sdkman](https://sdkman.io/) for installing both maven and java

Create postgres user (superuser) with name and password as bojack

```
CREATE USER bojack WITH PASSWORD 'bojack' SUPERUSER;
```
Create Database with name 'bojack' and assign the above created user to the database with preferable CLI or GUI tool

```
create database bojack;
```

```
grant all privileges on database bojack to bojack;
```

Start the redis server and specify the port in application.properties file

```
sudo redis-server path-to/redis.conf
```

Run the below commands in the core

```
mvn clean
```

```
mvn install
```

Execute any of the two commands below to run the application

```
java -jar target/redis-caching-spring-boot-0.0.1-SNAPSHOT.jar
```

```
mvn spring-boot:run
```

The Default port is 9090 (can be changed in application.properties)

Go to the below URI to view Swagger-UI (API-docs)

```
http://localhost:9090/swagger-ui.html
```
