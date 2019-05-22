package org.apache.maven;

import java.util.HashSet;
import java.util.Set;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.artifact.resolver.filter.ExclusionSetFilter;

public class MavenArtifactFilterManager {
   public static ArtifactFilter createStandardFilter() {
      Set artifacts = new HashSet();
      artifacts.add("classworlds");
      artifacts.add("commons-cli");
      artifacts.add("doxia-sink-api");
      artifacts.add("jsch");
      artifacts.add("maven-artifact");
      artifacts.add("maven-artifact-manager");
      artifacts.add("maven-core");
      artifacts.add("maven-error-diagnoser");
      artifacts.add("maven-model");
      artifacts.add("maven-monitor");
      artifacts.add("maven-plugin-api");
      artifacts.add("maven-plugin-descriptor");
      artifacts.add("maven-plugin-parameter-documenter");
      artifacts.add("maven-plugin-registry");
      artifacts.add("maven-profile");
      artifacts.add("maven-project");
      artifacts.add("maven-reporting-api");
      artifacts.add("maven-repository-metadata");
      artifacts.add("maven-settings");
      artifacts.add("plexus-container-default");
      artifacts.add("plexus-interactivity-api");
      artifacts.add("maven-toolchain");
      artifacts.add("wagon-provider-api");
      return new ExclusionSetFilter(artifacts);
   }
}
