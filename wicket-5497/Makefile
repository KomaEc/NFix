all:
	mvn install:install-file -Dfile=wicket-parent-7.0.0-SNAPSHOT.pom -DgroupId=org.apache.wicket -DartifactId=wicket-parent -Dversion=7.0.0-SNAPSHOT -Dpackaging=pom;
	mvn install:install-file -Dfile=wicket-util-7.0.0-SNAPSHOT.jar -DgroupId=org.apache.wicket -DartifactId=wicket-util -Dversion=7.0.0-SNAPSHOT -Dpackaging=bundle;
	mvn install:install-file -Dfile=wicket-request-7.0.0-SNAPSHOT.jar -DgroupId=org.apache.wicket -DartifactId=wicket-request -Dversion=7.0.0-SNAPSHOT -Dpackaging=bundle;
	mvn compile;
	mvn test-compile

clean:
	mvn clean