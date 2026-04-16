# Neo4J Demo
This project is used for a lecture at the Johannes Kepler University Linz (JKU) in Austria.
You are free to use it even if you do not participate in the course.

---

## Running
ATTENTION: This application tries to create the subfolder `db-data` in the working directory.
If a folder of the same name is already present, the application will abort.
Otherwise, the folder is deleted on application shutdown.

### Requirements
- Java 21 or above
- A folder without a subdirectory `db-data` in it

### Running from the console
1. Download the latest release at the releases page https://github.com/mbahara/neo4j-demo/releases
2. Open a terminal and navigate to the downloaded JAR file
3. Make sure no subdirectory `db-data` is present in the current directory
4. Execute `java -jar neo4jdemo-0.0.1.jar`. The application with run on http://localhost:8080
5. When you are finished, switch back to the terminal and press [Ctrl]+[C] to shut down the application
6. Cleanup `db-data` in case of an error.

### Running from your IDE
Clone the project and set up the Spring Boot Application.

### Building
To build the project, use: `mvn package -DskipTests`

---