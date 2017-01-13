# Uniquora
> This is the first open-source project in Nazarbayev University. Created on 12/17/16
> Back-end of the system written in Spring Boot, Java.

#### Why Java and Spring Boot
* Java is one of the first languages for every CS student in NU
* Java is one of the most popular languages in the programming world
* Java is configurable and fast
* Java has the biggest active community ready to help
* Spring is one of the most popular frameworks written for Java
* Spring Boot makes it easy to build and deploy Spring applications

#### How to deploy
Project is configured in such way that you have to create postgresql database "jdbc:postgresql://localhost:5432/uniquora" with username and password "postgres:postgres".
You also have to create "/opt/uniquora/avatars" and "/opt/uniquora/logs" folder and give your user permissions to write in those folders. (You can change properties in application.properties and log4j2.xml, but don't push them here).

Then you are ready to build the project and start it
```sh
$ mvn clean install
$ java -jar target/uniquora-1.0.jar
```
...obviously make sure that you have installed Maven and Java 1.8. Fill the created database tables with your data.

#### What we have now
Application supports CORS: you can send following queries from any domain:

* *GET /api/isregistered?email=...* -> returns "true", "false" or "notfound" in case if student is not in database.
* *POST /api/register {"email":"...", "password":"..."}* -> returns "success" in case of registration and sends **code** and **id** to the **email** that has to be sent to confirm the registration. A mail to the same email address will be sent only once in 24 hours in order to prevent spaming via our service.
* *GET /api/confirm?code=...&id=...* -> returns "success" in case of successful confirmation
* *GET /api/avatar/{id}* -> returns avatar of registered user or "notfound" in case if avatar doesn't exist
* *POST /api/login {"email":"...", "password":"..."}* -> returns **JWT** in case of successful login

Following queries have to be sent with http header called **JWT** with value got from login query.
* *GET /api/courses/list* -> returns list of courses in database.
* *GET /api/questions/list?page=...* -> returns list of questions in a page wrapped in page information object. Pages start from 1, PageSize is 20.
* *GET /api/questions/get/{id}* -> returns detailed information about the question with the given **id** including answers
* *POST /api/questions/save {"title":"...", "text":"...", "courseId":"...", "isAnonymous":"false/true"}* -> returns **id** of the created question
* *POST /api/answers/save {"text":"...", "questionId":"..."}* -> returns **id** of the created answer

#### ToDo
  - Add rating/personal cabinet
  - Make students to be able to choose courses and search questions only by their courses
  - Add pagination for answers?
