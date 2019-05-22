package org.apache.maven.execution;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.codehaus.plexus.util.IOUtil;

public class DefaultRuntimeInformation implements RuntimeInformation, Initializable {
   private static final String MAVEN_GROUPID = "org.apache.maven";
   private static final String MAVEN_PROPERTIES = "META-INF/maven/org.apache.maven/maven-core/pom.properties";
   private ArtifactVersion applicationVersion;

   public ArtifactVersion getApplicationVersion() {
      return this.applicationVersion;
   }

   public void initialize() throws InitializationException {
      InputStream resourceAsStream = null;

      try {
         Properties properties = new Properties();
         resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("META-INF/maven/org.apache.maven/maven-core/pom.properties");
         if (resourceAsStream == null) {
            throw new IllegalStateException("Unable to find Maven properties in classpath: META-INF/maven/org.apache.maven/maven-core/pom.properties");
         }

         properties.load(resourceAsStream);
         String property = properties.getProperty("version");
         if (property == null) {
            throw new InitializationException("maven-core properties did not include the version");
         }

         this.applicationVersion = new DefaultArtifactVersion(property);
      } catch (IOException var7) {
         throw new InitializationException("Unable to read properties file from maven-core", var7);
      } finally {
         IOUtil.close(resourceAsStream);
      }

   }
}
