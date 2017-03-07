# Uniquora
> This is the first open source project in Nazarbayev University. Created on 12/17/16.<br>
> The project, back-end of the Uniquora application, is written in Spring Boot, Java.

## For NU students

#### Why to start NU Open Source project?
* We believe that the stack of technologies that was used here can help contributors to learn programming skills that will be necessary or helpful to start a career in IT industry.
* Authors were inspired by Open Source Software class, SST.

#### Why Java and Spring Boot were chosen for this project?
* Java is one of the first languages for every CS student in NU.
* Java is one of the most popular languages in the programming world.
* Java is very configurable and fast.
* Java has the biggest active community of programmers ready to help.
* Spring is one of the most popular frameworks written in/for Java.
* Spring Boot makes it easy to build and deploy Spring applications.

## Technical part

#### How to deploy
The project is configured in such a way that you have to create a postgresql database "jdbc:postgresql://localhost:5432/uniquora" with username and password "postgres:postgres".
You also have to create "/opt/uniquora/avatars" and "/opt/uniquora/logs" folder and give your unix user permissions to write in those folders. (You can change all those properties in application.properties and log4j2.xml files, but please don't push them here).

Now you are ready to build the project and run it
```sh
$ cd project/directory
$ mvn clean install
$ java -Dpassword=X -Dgmail=Y -jar target/uniquora-1.0.jar (X and Y are gmail accounts from which you want to send confirmation mails)
```
...obviously, make sure that you have installed Maven and Java 1.8. Fill the created database tables with your own initial data.

#### What we have now (for Front-end applications)
Application supports CORS: you can send following queries from any domain:

* ``GET /api/isregistered?email=...`` -> returns "true", "false" or "notfound" in case if student is not in database.
* ``POST /api/register {"email":"...", "password":"..."}`` -> returns "success" in case of registration and sends **code** and **id** to the **email** that has to be sent to confirm the registration. A mail to the same email address will be sent only once in 24 hours in order to prevent spaming via our service.
* ``GET /api/confirm?code=...&id=...`` -> returns "success" in case of successful confirmation
* ``GET /api/avatar/{id}`` -> returns avatar of registered user or "notfound" in case if avatar doesn't exist
* ``POST /api/login {"email":"...", "password":"..."}`` -> returns **JWT** in case of successful login. Password has to be sent in md5

Following queries have to be sent with http header called **JWT** with value got from login query.
* ``GET /api/courses/list`` -> returns list of courses in database.
* ``GET /api/questions/list?page=...`` -> returns list of questions in a page wrapped in page information object. Pages start from 1, PageSize is 20.
* ``GET /api/questions/get/{id}`` -> returns detailed information about the question with the given **id** including answers
* ``POST /api/questions/save {"title":"...", "text":"...", "courseId":"...", "isAnonymous":"false/true"}`` -> returns created question in JSON
* ``POST /api/answers/save {"text":"...", "questionId":"..."}`` -> returns created answer in JSON
* ``GET /api/whoami`` -> returns information about the user if jwt is valid
* ``GET /api/search?query=...`` -> returns list of titles and ids of found questions. Query has to be more than one character.

#### ToDo:
1) Add rating/personal cabinet<br>
2) Make students to be able to choose courses and search questions only by their courses<br>
3) Searching for questions<br>
4) New avatar type has to be chosen<br>
5) No tests were written yet. Because YOLO!<br>
...
