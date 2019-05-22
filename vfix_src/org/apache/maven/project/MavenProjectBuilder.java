package org.apache.maven.project;

import java.io.File;
import java.util.List;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.profiles.ProfileManager;
import org.apache.maven.project.interpolation.ModelInterpolationException;
import org.apache.maven.wagon.events.TransferListener;

public interface MavenProjectBuilder {
   String ROLE = MavenProjectBuilder.class.getName();
   String STANDALONE_SUPERPOM_GROUPID = "org.apache.maven";
   String STANDALONE_SUPERPOM_ARTIFACTID = "super-pom";
   String STANDALONE_SUPERPOM_VERSION = "2.0";

   MavenProject build(File var1, ArtifactRepository var2, ProfileManager var3) throws ProjectBuildingException;

   MavenProject build(File var1, ArtifactRepository var2, ProfileManager var3, boolean var4) throws ProjectBuildingException;

   MavenProject buildWithDependencies(File var1, ArtifactRepository var2, ProfileManager var3, TransferListener var4) throws ProjectBuildingException, ArtifactResolutionException, ArtifactNotFoundException;

   MavenProject buildWithDependencies(File var1, ArtifactRepository var2, ProfileManager var3) throws ProjectBuildingException, ArtifactResolutionException, ArtifactNotFoundException;

   MavenProject buildFromRepository(Artifact var1, List var2, ArtifactRepository var3) throws ProjectBuildingException;

   MavenProject buildFromRepository(Artifact var1, List var2, ArtifactRepository var3, boolean var4) throws ProjectBuildingException;

   /** @deprecated */
   MavenProject buildStandaloneSuperProject(ArtifactRepository var1) throws ProjectBuildingException;

   /** @deprecated */
   MavenProject buildStandaloneSuperProject(ArtifactRepository var1, ProfileManager var2) throws ProjectBuildingException;

   MavenProject buildStandaloneSuperProject(ProjectBuilderConfiguration var1) throws ProjectBuildingException;

   MavenProject build(File var1, ProjectBuilderConfiguration var2) throws ProjectBuildingException;

   MavenProject build(File var1, ProjectBuilderConfiguration var2, boolean var3) throws ProjectBuildingException;

   void calculateConcreteState(MavenProject var1, ProjectBuilderConfiguration var2) throws ModelInterpolationException;

   void calculateConcreteState(MavenProject var1, ProjectBuilderConfiguration var2, boolean var3) throws ModelInterpolationException;
}
