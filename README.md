# Synopsis
Demo of http and https via Grizzly/Jersey2 via simple xml and json clock interface (Java 8).

# Example
```bash
git clone https://github.com/ch6574/rest-demo.git
cd rest-demo
mvn clean compile package
mvn exec:java

# In a new shell
curl -sH Accept:application/xml  http://localhost:8080/rest-demo/clock
curl -sH Accept:application/json http://localhost:8080/rest-demo/clock
curl -sH Accept:application/json https://localhost:8081/rest-demo/clock --cacert ./server.cert
```

# PrettyPrinting
For JSON pipe output into "jq .", and for XML pipe output into "xmllint --format -".

# Note
Check README.txt for information on updating local SSL certs.

# License
GPL v3.
