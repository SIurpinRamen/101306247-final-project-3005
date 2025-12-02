#### Student Number: 101306247
## Compilation instructions
(NOTE: This was run on Windows 11 with Java 24.0.2 installed)
1. Create a new database in PostgreSQL
	- Change line 20, 21, and 22 in `Main.java` as needed to match your PostgreSQL server
	- Run the `DDL.sql` and then the `DML.sql` statements in PostgreSQL
2. Open a terminal, and cd into `project-root\app`
	- Compile with `javac *.java`
	- Run with `java -cp postgresql-42.7.8.jar Main.java`
	- Alternatively you can open `run.bat` in `project-root` (only works on Windows)

## Demo video
- https://www.youtube.com/watch?v=td1rt3_Phqk

## Addiontal info
- I forgot to mention in the video, but the project structure follows the spec with some additions:
	- Both diagrams are added to the `/docs` folder to view as PNGs along with the regular ERD doc
	- The `run.bat` is kept in the root folder but compiles/runs everything in `/app`
- DDL and DML statements are both kept in `/sql`