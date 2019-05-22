package org.apache.maven.project;

import java.io.File;
import java.util.List;

public interface MavenProjectHelper {
   String ROLE = MavenProjectHelper.class.getName();

   void attachArtifact(MavenProject var1, File var2, String var3);

   void attachArtifact(MavenProject var1, String var2, File var3);

   void attachArtifact(MavenProject var1, String var2, String var3, File var4);

   void addResource(MavenProject var1, String var2, List var3, List var4);

   void addTestResource(MavenProject var1, String var2, List var3, List var4);
}
