all:
	mvn install:install-file -Dfile=./fontbox-2.0.0-SNAPSHOT.jar -DgroupId=org.apache.pdfbox -DartifactId=fontbox -Dversion=2.0.0-SNAPSHOT -Dpackaging=bundle -DgeneratePom=true;
	mvn compile;
	mvn test-compile
clean:
	mvn clean