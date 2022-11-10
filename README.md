# Setup service

## Install java
- Install jdk11
  - Verify installed java version by running command
```shell script
java -version
javac -version
```

## Install colima to run containers
- Set up colima for docker
    - `brew install colima`
    - start colima by executing `colima start`
    - In the terminal type `docker ps` and you shouldn't see any error
    - Possibly a line indicating headers like "CONTAINER ID, IMAGE etc" will be displayed


## Install postgres
- Setup Postgres using docker
    - Start db command
      `docker run --name postgresbankdb -e POSTGRES_DB=bankaccount -e POSTGRES_USER=bankaccount -e POSTGRES_PASSWORD=bankaccount -p 5432:5432 -d postgres`
    - Install below tools to connect to the database
   ```
      brew install --cask pgadmin4   
      brew install psqlodbc
   ```
    - Connect to db command
      `psql -h localhost -U bankaccount -d bankaccount`
    - db password:
      `bankaccount`