# simple-monitor-server
This is a server application which currently accepts data from the "simple-monitor-client" application and sends email
if the client have gone offline in 120 seconds which is the default value. The value can be override 
from application.properties configuration file by changing "app.monitor.count-as-offline.seconds"-key's value, or
override it with application parameter -Dapp.monitor.count-as-offline.seconds=240

## Pre-requirements
- Java 8 or higher
- Maven 3 or higher

## Run application
In a console, execute the following command:<br/>
mvn spring-boot:run
