REQUIRED JAR FILES (place inside this lib/ folder)
====================================================
This project uses plain Hibernate (as the JPA provider) + MySQL Connector/J.
No Maven/Gradle is used, so the jars below must be downloaded manually and
placed in this lib/ folder before compiling.

Download each jar from Maven Central (https://mvnrepository.com) - search the
artifact name and grab the version listed:

 1. hibernate-core-5.6.15.Final.jar
 2. hibernate-commons-annotations-5.1.2.Final.jar
 3. javax.persistence-api-2.2.jar
 4. jboss-logging-3.4.3.Final.jar
 5. antlr-2.7.7.jar
 6. jandex-2.4.2.Final.jar
 7. classmate-1.5.1.jar
 8. byte-buddy-1.12.18.jar
 9. jaxb-api-2.3.1.jar
10. javax.activation-api-1.2.0.jar
11. dom4j-2.1.3.jar
12. mysql-connector-j-8.0.33.jar   (older name: mysql-connector-java-8.0.33.jar)
13. FastInfoset-1.2.16.jar
14. istack-commons-runtime-3.0.12.jar
15. jaxb-runtime-2.3.1.jar   (only needed if hbm2ddl/schema export XML tools are used)

Quick way (Linux/Mac, curl) - run from inside lib/:
--------------------------------------------------------------------
curl -O https://repo1.maven.org/maven2/org/hibernate/hibernate-core/5.6.15.Final/hibernate-core-5.6.15.Final.jar
curl -O https://repo1.maven.org/maven2/org/hibernate/common/hibernate-commons-annotations/5.1.2.Final/hibernate-commons-annotations-5.1.2.Final.jar
curl -O https://repo1.maven.org/maven2/javax/persistence/javax.persistence-api/2.2/javax.persistence-api-2.2.jar
curl -O https://repo1.maven.org/maven2/org/jboss/logging/jboss-logging/3.4.3.Final/jboss-logging-3.4.3.Final.jar
curl -O https://repo1.maven.org/maven2/antlr/antlr/2.7.7/antlr-2.7.7.jar
curl -O https://repo1.maven.org/maven2/org/jboss/jandex/2.4.2.Final/jandex-2.4.2.Final.jar
curl -O https://repo1.maven.org/maven2/com/fasterxml/classmate/1.5.1/classmate-1.5.1.jar
curl -O https://repo1.maven.org/maven2/net/bytebuddy/byte-buddy/1.12.18/byte-buddy-1.12.18.jar
curl -O https://repo1.maven.org/maven2/javax/xml/bind/jaxb-api/2.3.1/jaxb-api-2.3.1.jar
curl -O https://repo1.maven.org/maven2/javax/activation/javax.activation-api/1.2.0/javax.activation-api-1.2.0.jar
curl -O https://repo1.maven.org/maven2/org/dom4j/dom4j/2.1.3/dom4j-2.1.3.jar
curl -O https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.0.33/mysql-connector-j-8.0.33.jar
--------------------------------------------------------------------

TIP: If you already have these jars from an earlier Digital Nurture exercise
(e.g. a previous Hibernate/JPA module), just copy them into this lib/ folder
instead of downloading again - the same jar set is reused by all 6 exercises.

After the jars are here, compile and run using the commands given in the
project's README.md (they use "lib/*" as classpath so every jar in this
folder is automatically included).
