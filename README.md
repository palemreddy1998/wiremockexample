# Run wiremock as standalone
java -jar wiremock-standalone-2.8.0.jar --port 9999
http://localhost:9999/__admin/

# To proxy 
java -jar wiremock-standalone-2.8.0.jar --port 9999  --proxy-all http://wiremock.org --record-mappings

# verbose
java -jar wiremock-standalone-2.8.0.jar --port 9999  --proxy-all http://wiremock.org --record-mappings --verbose

# do CURL to local mappings
curl --verbose -X post --data{"id":"Test001"} http://localhost:9999/__admin/

# Run tests
mvn clean test

# Run with https port 
java -jar wiremock-standalone-2.8.0.jar --https-port 9999
https://localhost:9999/__admin/