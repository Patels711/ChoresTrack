# ChoresTrack

ChoresTrack is a Java desktop app for recording household chores and related earnings. A user can register or sign in, choose a chore or activity, review the resulting balance, and open the chores, activity, and notifications screens. The JavaFX interface is defined in FXML and its controllers coordinate the screens and stored data.

## Features

- Login and account registration screens, with a password recovery screen.
- A dashboard with a balance and selectable chores and activities.
- Chores, activity history, and notifications views.
- Local SQLite storage for account balances and transaction messages.

## Technology

- Java; the Maven project compiles its application source for Java 11 and uses a Java 17 toolchain in its project properties.
- JavaFX 21 (`javafx-controls`, `javafx-fxml`) for the desktop UI.
- SQLite with Xerial SQLite JDBC 3.44.1.0.
- Maven and the JavaFX Maven plugin.

## Requirements

- JDK 17 or later.
- Maven 3.8 or later.

## Run

From the repository root:

```bash
mvn clean javafx:run
```

The Maven JavaFX plugin starts `com.example.App`. On first run, the app creates or opens `app_data.db` in the working directory and initializes its `users` and `messages` tables.

## Project layout

```text
pom.xml                                      Maven dependencies and JavaFX run target
app_data.db                                  SQLite data file (created/updated by the app)
src/main/java/com/example/App.java           JavaFX entry point and SQLite setup
src/main/java/com/example/*Controller.java   Screen and account controllers
src/main/java/com/example/UserManager.java   Flat-file account helper
src/main/java/com/example/data.txt           Account records used by UserManager
src/main/java/module-info.java               Java module declarations
src/main/resources/com/example/*.fxml        Login, registration, dashboard, and list layouts
src/main/resources/                           App images, fonts, and bundled resources
target/                                       Maven build output (generated)
```

## Data and current limitations

The code has two local data stores: balances and transaction messages are stored in SQLite, while `UserManager` reads and writes usernames and passwords in `src/main/java/com/example/data.txt` as plain text. Use only throwaway local demo accounts; do not enter a real or reused password. The repository currently includes the data file and database file, so inspect them before sharing a copy of the project.

## Sources

- [OpenJFX documentation](https://openjfx.io/openjfx-docs/)
- [Maven documentation](https://maven.apache.org/guides/)
- [Xerial SQLite JDBC](https://github.com/xerial/sqlite-jdbc)
- [Java 17 documentation](https://docs.oracle.com/en/java/javase/17/)
