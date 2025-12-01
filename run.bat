:: Access the application files
cd app

:: Compile the application
javac *.java

:: Run the application
java -cp postgresql-42.7.8.jar Main.java

@echo off
pause

:: Clean the class files
del /q *.class
