#### Student Number: 101306247
## Compilation instructions
(NOTE: This was run on Windows 11 with Java 24.0.2 installed)
1. Create a new database in PostgreSQL
	- Change line 21, 22, and 23 in `Main.java` as needed to match your PostgreSQL server
	- Run the `DDL.sql` and then the `DML.sql` statements in PostgreSQL
2. Open a terminal, and cd into `project-root\app`
	- Compile with `javac *.java`
	- Run with `java -cp postgresql-42.7.8.jar Main.java`
	- Alternatively you can open `run.bat` in `project-root` (only works on Windows)

## Demo video
- 