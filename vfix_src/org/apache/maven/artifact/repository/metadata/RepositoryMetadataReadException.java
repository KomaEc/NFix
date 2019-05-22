package org.apache.maven.artifact.repository.metadata;

public class RepositoryMetadataReadException extends Exception {
   public RepositoryMetadataReadException(String message) {
      super(message);
   }

   public RepositoryMetadataReadException(String message, Exception e) {
      super(message, e);
   }
}
