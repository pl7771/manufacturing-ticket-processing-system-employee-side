#How to start using the application
_Terminal in project root!
e.g. C:\HoGent\2021-java-tile_03>mvn javafx:run_

##Create the database (using Docker)
docker run -d --name SQLServer -e "ACCEPT_EULA=Y" -e "SA_PASSWORD=p@ssw0rd" -e "MSSQL_PID=Developer" -p 1433:1433 microsoft/mssql-server-linux:2017-latest

##Migrate the database using flyway
mvn flyway:clean - CLEAR DATABASE

mvn flyway:migrate - RUN MIGRATION  (_see resources/db/migration_)

##Clean target folder and run the JavaFX application
mvn clean install - CLEAN PROJECT, RECOMPILE

mvn clean javafx:run - **RUN JAVAFX** APPLICATION: 

