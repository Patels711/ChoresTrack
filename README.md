# ChoresTrack

ChoresTrack is a Java desktop application for organizing household chores and tracking chore-related earnings. It uses JavaFX for its interface and SQLite for persistent application data.

## Features

- Create an account, sign in, and manage account details.
- Browse chore activities and record selected chores.
- Track chore income and account balances.
- View activity and notification screens.
- Store application data in SQLite.

## Built with

- Java 17
- JavaFX 21
- SQLite JDBC
- Maven

## Run

Install JDK 17 or newer and Maven, then run from the project directory:

```bash
mvn clean javafx:run
```

The Maven JavaFX plugin launches `com.example.App`. Application data and the bundled SQLite JDBC driver are part of the project resources.
