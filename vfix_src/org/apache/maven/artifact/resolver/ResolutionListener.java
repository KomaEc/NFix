package org.apache.maven.artifact.resolver;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.versioning.VersionRange;

public interface ResolutionListener {
   String ROLE = ResolutionListener.class.getName();
   int TEST_ARTIFACT = 1;
   int PROCESS_CHILDREN = 2;
   int FINISH_PROCESSING_CHILDREN = 3;
   int INCLUDE_ARTIFACT = 4;
   int OMIT_FOR_NEARER = 5;
   int UPDATE_SCOPE = 6;
   /** @deprecated */
   int MANAGE_ARTIFACT = 7;
   int OMIT_FOR_CYCLE = 8;
   int UPDATE_SCOPE_CURRENT_POM = 9;
   int SELECT_VERSION_FROM_RANGE = 10;
   int RESTRICT_RANGE = 11;
   int MANAGE_ARTIFACT_VERSION = 12;
   int MANAGE_ARTIFACT_SCOPE = 13;

   void testArtifact(Artifact var1);

   void startProcessChildren(Artifact var1);

   void endProcessChildren(Artifact var1);

   void includeArtifact(Artifact var1);

   void omitForNearer(Artifact var1, Artifact var2);

   void updateScope(Artifact var1, String var2);

   /** @deprecated */
   void manageArtifact(Artifact var1, Artifact var2);

   void omitForCycle(Artifact var1);

   void updateScopeCurrentPom(Artifact var1, String var2);

   void selectVersionFromRange(Artifact var1);

   void restrictRange(Artifact var1, Artifact var2, VersionRange var3);
}
