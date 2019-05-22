package org.apache.maven.execution;

import java.util.Date;
import java.util.List;
import java.util.Properties;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.monitor.event.EventDispatcher;
import org.apache.maven.monitor.event.EventMonitor;
import org.apache.maven.profiles.ProfileManager;
import org.apache.maven.settings.Settings;

public interface MavenExecutionRequest {
   ArtifactRepository getLocalRepository();

   List getGoals();

   void setSession(MavenSession var1);

   MavenSession getSession();

   void addEventMonitor(EventMonitor var1);

   EventDispatcher getEventDispatcher();

   Settings getSettings();

   String getBaseDirectory();

   void setRecursive(boolean var1);

   boolean isRecursive();

   void setReactorActive(boolean var1);

   boolean isReactorActive();

   void setPomFile(String var1);

   String getPomFile();

   void setFailureBehavior(String var1);

   String getFailureBehavior();

   ProfileManager getGlobalProfileManager();

   Properties getExecutionProperties();

   Properties getUserProperties();

   Date getStartTime();

   boolean isShowErrors();
}
