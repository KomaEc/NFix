

all:
	mvn install:install-file -Dfile=./tinkerpop-3.2.1-SNAPSHOT.pom -DgroupId=org.apache.tinkerpop -DartifactId=tinkerpop -Dpackaging=pom -Dversion=3.2.1-SNAPSHOT;
	mvn install:install-file -Dfile=./gremlin-shaded-3.2.1-SNAPSHOT.jar;
	mvn compile;
	mvn test-compile
clean:
	mvn clean