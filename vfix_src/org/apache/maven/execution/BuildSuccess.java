package org.apache.maven.execution;

import org.apache.maven.project.MavenProject;

public class BuildSuccess {
   private final MavenProject project;
   private final long time;

   public BuildSuccess(MavenProject project, long time) {
      this.project = project;
      this.time = time;
   }

   public MavenProject getProject() {
      return this.project;
   }

   public long getTime() {
      return this.time;
   }
}
