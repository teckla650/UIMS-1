# University Information Management System (UIMS)

MDA5104 Object Oriented Technologies, CAT Two (group assignment).
Java implementation of the UIMS design from Assignment 2.

## Folder layout

```
UIMS
|-- README.md
|-- java_demo_output.txt     saved output of Main (both use cases)
|-- java_test_output.txt     saved output of TestRunner (14 tests)
`-- java
    `-- src/uims             all source files (package uims)
```

## Classes

| Class | Purpose |
|---|---|
| `User` (abstract) | Shared identity, login and session operations |
| `Student`, `Lecturer`, `Administrator` | Subclasses of `User` |
| `Role` | Enum: STUDENT, LECTURER, ADMINISTRATOR |
| `Course` | A unit with one lecturer, a department and many enrollments |
| `Department` | Groups courses and lecturers (aggregation) |
| `Enrollment` | Links a student to a course and owns the results |
| `StudentResult` | One assessment score and its grade |
| `FeeStatement` | Total, amount paid and balance |
| `Timetable` | A class slot, visible to students once an administrator publishes it |
| `Notification` (interface), `EmailNotification`, `SmsNotification` | Message sending |
| `Main`, `TimetableDemo`, `TestRunner` | Demo, timetable demo and the 14 test cases |

## Use cases

1. **Student registers for a unit**: `Student.registerForCourse()`
2. **Enter and approve student marks**: `Lecturer.enterMarks()` then `Administrator.approveResults()`

## Code conventions

- Javadoc (`/** ... */`) on every class and on every public method that is not a plain getter or setter.
- `@param`, `@return` and `@throws` only where the meaning is not obvious from the name.
- Inline `//` comments explain why, not what. No commented-out code.

## Requirements

JDK 17 or newer (built and tested with Java 21).

## Compile and run

From the `java` folder.

PowerShell:
```
mkdir out
javac -d out (Get-ChildItem src\uims\*.java).FullName
java -cp out uims.Main            # demonstration of both use cases
java -cp out uims.TestRunner      # tests T1 to T14
java -cp out uims.TimetableDemo   # Department and Timetable demo
```

Linux or macOS:
```
mkdir out
javac -d out src/uims/*.java
```

## Group members

1. Teckla Ndanu 22/05701
2. Sharon Wanjuki 26/05403
3. Philip Wambua 26/06125
4. Muthui Ava 26/10275
5. Aggrey Busolo 26/06462
