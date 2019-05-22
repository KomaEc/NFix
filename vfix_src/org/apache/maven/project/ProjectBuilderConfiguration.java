package org.apache.maven.project;

import java.util.Date;
import java.util.Properties;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.profiles.ProfileManager;

public interface ProjectBuilderConfiguration {
   ArtifactRepository getLocalRepository();

   ProfileManager getGlobalProfileManager();

   Properties getUserProperties();

   Properties getExecutionProperties();

   ProjectBuilderConfiguration setGlobalProfileManager(ProfileManager var1);

   ProjectBuilderConfiguration setLocalRepository(ArtifactRepository var1);

   ProjectBuilderConfiguration setUserProperties(Properties var1);

   ProjectBuilderConfiguration setExecutionProperties(Properties var1);

   Date getBuildStartTime();

   ProjectBuilderConfiguration setBuildStartTime(Date var1);
}
