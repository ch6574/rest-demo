#
# Maven project
#

# SSL certs (use password "qwerty")
keytool -genkey -keystore ./keystore_client -alias clientKey -dname "CN=Client, OU=Jersey, O=hillc"
keytool -export -alias clientKey -rfc -keystore ./keystore_client > ./client.cert
keytool -import -alias clientCert -file ./client.cert -keystore ./truststore_server

keytool -genkey -keystore ./keystore_server -alias serverKey -dname "CN=localhost, OU=Jersey, O=hillc"
keytool -export -alias serverKey -rfc -keystore ./keystore_server > ./server.cert
keytool -import -alias serverCert -file ./server.cert -keystore ./truststore_client


# Build as "clean compile package"
mvn clean compile package

# Boot up server
mvn exec:java
java -jar ./target/rest-demo-1.0-SNAPSHOT.jar

# Query it
# (via command line)
curl -sH Accept:application/xml  http://localhost:8080/rest-demo/clock
curl -sH Accept:application/json http://localhost:8080/rest-demo/clock
curl -sH Accept:application/json https://localhost:8081/rest-demo/clock --cacert ./server.cert 

# Optional
# json pretty print use:  | jq .
# xml pretty print use:   | xmllint --format -
