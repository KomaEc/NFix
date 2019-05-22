package org.apache.maven.artifact.metadata;

import org.apache.maven.artifact.Artifact;

public class ArtifactMetadataRetrievalException extends Exception {
   private Artifact artifact;

   /** @deprecated */
   public ArtifactMetadataRetrievalException(String message) {
      this(message, (Throwable)null, (Artifact)null);
   }

   /** @deprecated */
   public ArtifactMetadataRetrievalException(Throwable cause) {
      this((String)null, cause, (Artifact)null);
   }

   /** @deprecated */
   public ArtifactMetadataRetrievalException(String message, Throwable cause) {
      this(message, cause, (Artifact)null);
   }

   public ArtifactMetadataRetrievalException(String message, Throwable cause, Artifact artifact) {
      super(message, cause);
      this.artifact = artifact;
   }

   public Artifact getArtifact() {
      return this.artifact;
   }
}
