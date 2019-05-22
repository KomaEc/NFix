package org.apache.maven.artifact.repository.metadata;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.metadata.ArtifactMetadata;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.repository.metadata.io.xpp3.MetadataXpp3Reader;
import org.apache.maven.artifact.repository.metadata.io.xpp3.MetadataXpp3Writer;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.ReaderFactory;
import org.codehaus.plexus.util.WriterFactory;
import org.codehaus.plexus.util.xml.XmlStreamReader;
import org.codehaus.plexus.util.xml.XmlStreamWriter;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

public abstract class AbstractRepositoryMetadata implements RepositoryMetadata {
   private Metadata metadata;

   protected AbstractRepositoryMetadata(Metadata metadata) {
      this.metadata = metadata;
   }

   public String getRemoteFilename() {
      return "maven-metadata.xml";
   }

   public String getLocalFilename(ArtifactRepository repository) {
      return "maven-metadata-" + repository.getKey() + ".xml";
   }

   public void storeInLocalRepository(ArtifactRepository localRepository, ArtifactRepository remoteRepository) throws RepositoryMetadataStoreException {
      try {
         this.updateRepositoryMetadata(localRepository, remoteRepository);
      } catch (IOException var4) {
         throw new RepositoryMetadataStoreException("Error updating group repository metadata", var4);
      } catch (XmlPullParserException var5) {
         throw new RepositoryMetadataStoreException("Error updating group repository metadata", var5);
      }
   }

   protected void updateRepositoryMetadata(ArtifactRepository localRepository, ArtifactRepository remoteRepository) throws IOException, XmlPullParserException {
      MetadataXpp3Reader mappingReader = new MetadataXpp3Reader();
      Metadata metadata = null;
      File metadataFile = new File(localRepository.getBasedir(), localRepository.pathOfLocalRepositoryMetadata(this, remoteRepository));
      if (metadataFile.exists()) {
         XmlStreamReader reader = null;

         try {
            reader = ReaderFactory.newXmlReader(metadataFile);
            metadata = mappingReader.read((Reader)reader, false);
         } finally {
            IOUtil.close((Reader)reader);
         }
      }

      boolean changed;
      if (metadata == null) {
         metadata = this.metadata;
         changed = true;
      } else {
         changed = metadata.merge(this.metadata);
      }

      String version = metadata.getVersion();
      if (version != null && ("LATEST".equals(version) || "RELEASE".equals(version))) {
         metadata.setVersion((String)null);
      }

      if (!changed && metadataFile.exists()) {
         metadataFile.setLastModified(System.currentTimeMillis());
      } else {
         XmlStreamWriter writer = null;

         try {
            metadataFile.getParentFile().mkdirs();
            writer = WriterFactory.newXmlWriter(metadataFile);
            MetadataXpp3Writer mappingWriter = new MetadataXpp3Writer();
            mappingWriter.write(writer, metadata);
         } finally {
            IOUtil.close((Writer)writer);
         }
      }

   }

   public String toString() {
      return "repository metadata for: '" + this.getKey() + "'";
   }

   protected static Metadata createMetadata(Artifact artifact, Versioning versioning) {
      Metadata metadata = new Metadata();
      metadata.setGroupId(artifact.getGroupId());
      metadata.setArtifactId(artifact.getArtifactId());
      metadata.setVersion(artifact.getVersion());
      metadata.setVersioning(versioning);
      return metadata;
   }

   protected static Versioning createVersioning(Snapshot snapshot) {
      Versioning versioning = new Versioning();
      versioning.setSnapshot(snapshot);
      versioning.updateTimestamp();
      return versioning;
   }

   public void setMetadata(Metadata metadata) {
      this.metadata = metadata;
   }

   public Metadata getMetadata() {
      return this.metadata;
   }

   public void merge(ArtifactMetadata metadata) {
      AbstractRepositoryMetadata repoMetadata = (AbstractRepositoryMetadata)metadata;
      this.metadata.merge(repoMetadata.getMetadata());
   }

   public String extendedToString() {
      StringBuffer buffer = new StringBuffer();
      buffer.append("\nRepository Metadata\n--------------------------");
      buffer.append("\nGroupId: ").append(this.getGroupId());
      buffer.append("\nArtifactId: ").append(this.getArtifactId());
      buffer.append("\nMetadata Type: ").append(this.getClass().getName());
      return buffer.toString();
   }
}
