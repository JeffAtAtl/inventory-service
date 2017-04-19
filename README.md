# inventory-service (NCR programming exercise)

Cloud Service Programming Exercise – Inventory Service
Design and implement a simple service for managing product inventory.  The service will be able to get and update inventory counts for specific products at specific stores/locations.
Products are identified by a unique SKU (ID), and will contain counts of that product at different stores. Each store will be uniquely identified by a store number. For example, here is a sample table defining product inventory information:
 
| SKU | Name | Store | Count |
| --- | --- | --- | --- |
| 123 | iPhone 7 32GB | 900 | 5 |
| 234 | iPhone 7 Plus 32 GB | 900 | 2 |
| 123 | iPhone 7 32GB | 900 | 4 |
 
Functional Requirements:

The following features should be supported by the service:
 - Register/define a new product with a SKU and product name
 - Get the inventory count for a product at a particular store (for instance, get number of iPhones at store number 900)
 - Update the inventory count for a product as a particular store (for instance, update number of iPhones at store number 900 to 20)
 
Non-Functional Requirements:
 - The sample project must include instructions for building and running the project.  Preferably, projects should use a build tool such as Maven, Gradle, Make, etc.
 - The service should handle any error conditions (such as invalid input or internal errors) with suitable HTTP error responses. 
 - The developer is responsible for designing the API signatures, including the input/output data structures, and any exceptions deemed necessary.
 - Although Java is preferred, the choice of language and frameworks is at the discretion of the developer.  Ideally, the application will run as a simple process/executable, and not require an external container or web server to run.
 - Projects can be submitted to us either via a zip/tarball containing all source, or alternatively, a link to an available GitHub, Bitbucket, or similar repository.
 
# Implementation

I used Spring boot SPRING INITIALIZR (http://start.spring.io/) to create a Spring Boot Gradle project with JPA, H2, HATEOAS and REST Docs.

I took the project and imported it into Eclipse (Mars with Gradle plugin) 

I started with an example at https://spring.io/guides/tutorials/bookmarks/ making changes to fulfill your requirements.

Service can be built and tested by running:

gradlew build

and run by running:

gradlew bootRun
 
