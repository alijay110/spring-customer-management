- Pre-Requisites
Docker compose installed
Mongo


### Start Kafka and Zookeeper ###
Execute a docker-compose up on folder /main/docker/kafka

#### Security ####
Some resources needs authentication to work:
POST "/api/user"
GET "/api/user/id/*"
GET "/api/user"
POST "/api/product/"
DELETE "/api/product/"

Basic Auth Credentials
User:admin
Pass: adminPass

### Tests ###

Use credit card generator online: https://ccardgenerator.com/generat-visa-card-numbers.php