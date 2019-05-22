package org.apache.maven;

import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.reactor.MavenExecutionException;

public interface Maven {
   String ROLE = Maven.class.getName();
   String POMv4 = "pom.xml";
   String RELEASE_POMv4 = "release-pom.xml";

   void execute(MavenExecutionRequest var1) throws MavenExecutionException;
}
