CampaignService
===============

CampaignService is a RESTful service that finds optimal
campaign sets to maximize ad revenue.

##How to

### Requirements
- Java 8
- Maven

### Demo

    ./demo.sh

### Run tests

    mvn test

Run slow tests, (16+ minutes)

    JUNIT_SLOW_TEST=1 mvn test

### Run server only

    mvn package && java -jar target/campaignservice-1.0-SNAPSHOT.jar server config.yml

## Endpoints

- POST /problems
- GET /problems/{id}
- GET /solutions/{id}

### Usage example
Assuming you have a problem in json format (problem.json)

**Submit a problem**

    curl -X POST -d @problem.json http://localhost:8080/problems --header "Content-Type:application/json"

**Read a problem** to check the solutionId

    curl http://localhost:8080/problems/0 --header "Content-Type:application/json"

**Read a solution**

    curl http://localhost:8080/solutions/0 --header "Content-Type:application/json"

## Features

The problem of campaign optimization kan be modeled as an Unbounded Knapsack packing problem.
This means that the problem is NP-complete, and there are no known algorithms that can solve the problem in polynomial time.

However there are some tricks that we can employ to get an optimal solution in reasonable time. =)

### Strategy
- When a problem is submitted, we will use a GreedySolver to quickly return an "OK" approximation in O(n) time.
- I have a ThreadPool that can search for better solutions in the background. Number of threads is limited to "cores - 1"
- I can either use DynamicSolver or BranchAndBoundSolver to look for an optimal solution, depending on the properties of the problem.
- The background search using the BnBSolver is time bound to 1 minute, and then the best available solution is returned.
- Problem resource is updated with the better solution as it becomes available.
- Clients poll for the problem resource if they wait for the optimal solution.

### Solvers
- **GreedySolver**
https://en.wikipedia.org/wiki/Knapsack_problem#Greedy_approximation_algorithm
A quick approximation that will deliver M/2 value where M is max revenue.
- **DynamicSolver**
http://www.ifors.ms.unimelb.edu.au/tutorial/knapsack/dp_unbounded.html
A quick solver that trades memory for speed, and is suitable for problems where the maximum amount of impressions is low, or can be reduced by orders of magnitude.
- **BranchAndBoundSolver**
http://www.ifors.ms.unimelb.edu.au/tutorial/knapsack/bb_unbounded.html
A brute force solver, the can incrementally deliver better results, and starts at same solution as GreedySolver, so the algorithm can be limited in CPU time.

## Suggestions for improvements
- BnBSolver can be made parallel to use all CPU cores. On the other hand, single core approach allows multiple problems to be solved in parallel.
- Add metrics and monitoring annotations
- Service has no authentication or rate limiting, OK if internal use only.
- DAO is crudely implemented inside the Resource classes. Refactor and maybe make persistent.
- Add end-to-end tests
- Not tracking test coverage
