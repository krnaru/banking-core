# Running the application

To run the application, you need to have the following installed on your machine:
<ol>
<li>Java JDK 21</li>
<li>Docker</li>
</ol>

### Instructions

1. navigate into the source folder ```(./banking-core (for my case))``` and run the following command:
   - ```docker build -t core-banking .``` to build the docker image
   - ```docker-compose up``` to run the app, database and RabbitMQ
2. The application will be running on port 8080, so http://localhost:8080 would be used to test endpoints.
3. To stop the application, run ```docker-compose down```



# Important choices
1. I chose to use Spring Boot for this project because it is easy to set up and has a lot of features that make it easy to build a microservice.
2. I chose to use RabbitMQ for the messaging system because it is easy to set up, and it was one of the requirements, would have tried to implement Kafka otherwise.
3. Chose flyway for database migration because I have previous experience with it, and it's pretty good dependency to use for integration testing.
4. Added @Transactional to the service methods to ensure that the database is in a consistent state after each transaction.

# Estimation
### Estimate on how many transactions can your account application can handle per second on your development machine
Thousands. Testing the application with creating an account with a sample request as such:
```
{
	"customerId": "${req}",
	"country": "USA",
	"currencies": [ "USD", "EUR"]
}
```
With a 1000 threads per second got a 0% error rate. 

### Describe what do you have to consider to be able to scale applications horizontally
- Definitely would add caching for frequently accessed data (accounts, maybe by account Id?) to reduce the load on the database.
- Should use load balancers to distribute incoming network traffic across multiple instances of your application. That would require mostly deploying the app using something like kubernetes.
- Session state, if needed, should be managed in a centralized data store accessible by all instances.
- Utilizing read replicas for load distribution in database accesses and consider partitioning data if applicable.
- RabbitMQ should be clustered to handle increased loads and ensure high availability and fault tolerance.
- If applicable, break down the application into microservices to scale different components independently based on load.
