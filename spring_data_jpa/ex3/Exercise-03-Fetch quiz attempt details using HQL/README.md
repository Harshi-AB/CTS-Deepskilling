# Exercise 3 - Fetch quiz attempt details using HQL

Retrieves the full detail of a quiz attempt made by a user: username,
attempted date, every question asked, every option under each question,
each option's score, and whether the user selected that option.

## Entities

| Entity           | Table              | Notes                                             |
|------------------|--------------------|----------------------------------------------------|
| `User`           | `user`             | Master data - registered quiz users                |
| `Question`       | `question`         | Master data - quiz questions                        |
| `QuizOption`     | `options`          | Master data - candidate answers + a `score`         |
| `Attempt`        | `attempt`          | One row per quiz attempt                            |
| `AttemptQuestion`| `attempt_question` | Which questions were part of an attempt             |
| `AttemptOption`  | `attempt_option`   | Which option the user picked for each question      |

`AttemptRepository.getAttempt(int userId, int attemptId)` runs a single HQL
query joining the tables in the order specified by the document:
`user -> attempt -> attempt_question -> question -> attempt_option -> options`,
using `left join fetch` on every one-to-many relationship so the entire
object graph is populated in one round trip.

**Design note:** the fetched collections (`attemptQuestionList`,
`question.optionList`, `attemptOptionList`) are declared as `java.util.Set`
rather than `List` in the entity classes. Fetch-joining more than one
`List` ("bag") association in a single HQL query raises Hibernate's
`MultipleBagFetchException`; `Set` avoids that restriction entirely.

## 1. Setup MySQL schema

```bash
mysql -u root -p < sql/schema.sql
```

## 2. Download dependency jars

See `lib/README.txt`. Place all jars inside `lib/`.

## 3. Update DB credentials

Edit `META-INF/persistence.xml` if needed.

## Compilation command

```bash
javac -cp "lib/*" *.java
```

## Run command

```bash
java -cp ".:lib/*" OrmLearnApplication
```
(Windows: `java -cp ".;lib/*" OrmLearnApplication`)

## Expected output

```
What is the extension of the hyper text markup language file?
 1) .xhtm      0.0    false
 2) .ht        0.0    false
 3) .html      1.0    true
 4) .htmx      0.0    false

What is the maximum level of heading tag can be used in a HTML page?
 1) 5          0.0    false
 2) 3          0.0    true
 3) 4          0.0    false
 4) 6          1.0    false

The HTML document itself begins with <html> and ends </html>. State True or False
 1) false      0.0    false
 2) true       1.0    true

Choose the right option to store text value in a variable
 1) 'John'     0.5    true
 2) John       0.0    false
 3) "John"     0.5    false
 4) /John/     0.0    false
```
