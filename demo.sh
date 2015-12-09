#!/bin/bash

echo "### DEMO TIME ###"

echo "  building jar..."

mvn package > /dev/null

echo "  starting dropwizard server..."

java -jar target/campaignservice-1.0-SNAPSHOT.jar server config.yml  > /dev/null &

JAVA_PID=$!

echo "  waiting for server to come up" & sleep 10

echo "  PROBLEM 1" & sleep 3

echo "  POST /problem (submit problem 1)" & sleep 3
curl -X POST -d @src/test/resources/fixtures/sample_problem_1.json http://localhost:8080/problems --header "Content-Type:application/json"
echo
echo "  GET /solution/0 (read the greedy solution)"
echo & sleep 3
curl http://localhost:8080/solutions/0 --header "Content-Type:application/json"
echo "  wait for server to find optimal solution ..." & sleep 5
echo "  GET /solution/1 (read the optimal solution)"
echo & sleep 3
curl http://localhost:8080/solutions/1 --header "Content-Type:application/json"

echo -e "\n\n\n\n\n" & sleep 5

echo "  PROBLEM 2" & sleep 3

echo "  POST /problem (submit problem 2)" & sleep 3
curl -X POST -d @src/test/resources/fixtures/sample_problem_2.json http://localhost:8080/problems --header "Content-Type:application/json"
echo
echo "  GET /solution/2 (read the greedy solution)"
echo & sleep 3
curl http://localhost:8080/solutions/2 --header "Content-Type:application/json"
echo "  wait for server to find optimal solution ..." & sleep 5
echo "  GET /solution/3 (read the optimal solution)"
echo & sleep 3
curl http://localhost:8080/solutions/3 --header "Content-Type:application/json"

echo -e "\n\n\n\n\n" & sleep 5

echo "  PROBLEM 3" & sleep 3

echo "  POST /problem (submit problem 3)" & sleep 3
curl -X POST -d @src/test/resources/fixtures/sample_problem_3.json http://localhost:8080/problems --header "Content-Type:application/json"
echo
echo "  GET /solution/4 (read the greedy solution)"
echo & sleep 3
curl http://localhost:8080/solutions/4 --header "Content-Type:application/json"
echo "  wait for server to find optimal solution ..." & sleep 5
echo "  GET /solution/5 (read the optimal solution)"
echo & sleep 3
curl http://localhost:8080/solutions/5 --header "Content-Type:application/json"

echo -e "\n\n\n\n\n" & sleep 5

echo "  Stopping server ..."
kill $JAVA_PID
