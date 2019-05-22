package org.apache.maven.plugin.surefire;

import javax.annotation.Nonnull;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.surefire.booter.Classpath;

public interface ProviderInfo {
   @Nonnull
   String getProviderName();

   boolean isApplicable();

   Classpath getProviderClasspath() throws ArtifactResolutionException, ArtifactNotFoundException;

   void addProviderProperties() throws MojoExecutionException;
}
