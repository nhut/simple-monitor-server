# simple-monitor-server
This is a server application which currently accepts data from the "simple-monitor-client" application and sends email
if the client have gone offline in x seconds (defined in application.properties
with "app.monitor.count-as-offline.seconds"-key's value (120s is the default), or override it
with environment parameter -Dapp.monitor.count-as-offline.seconds=240).

## Pre-requirements
- Java 8 or higher
- Maven 3 or higher

## Run application
In a console, execute following command:<br/>
mvn spring-boot:run
