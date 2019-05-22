package org.apache.maven.artifact.manager;

import java.io.File;
import java.util.Collection;
import java.util.List;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.metadata.ArtifactMetadata;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.wagon.ResourceDoesNotExistException;
import org.apache.maven.wagon.TransferFailedException;
import org.apache.maven.wagon.UnsupportedProtocolException;
import org.apache.maven.wagon.Wagon;
import org.apache.maven.wagon.authentication.AuthenticationInfo;
import org.apache.maven.wagon.events.TransferListener;
import org.apache.maven.wagon.proxy.ProxyInfo;
import org.apache.maven.wagon.repository.Repository;
import org.apache.maven.wagon.repository.RepositoryPermissions;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.util.xml.Xpp3Dom;

public interface WagonManager {
   String ROLE = WagonManager.class.getName();

   /** @deprecated */
   Wagon getWagon(String var1) throws UnsupportedProtocolException;

   Wagon getWagon(Repository var1) throws UnsupportedProtocolException, WagonConfigurationException;

   void getArtifact(Artifact var1, List var2) throws TransferFailedException, ResourceDoesNotExistException;

   void getArtifact(Artifact var1, ArtifactRepository var2) throws TransferFailedException, ResourceDoesNotExistException;

   void putArtifact(File var1, Artifact var2, ArtifactRepository var3) throws TransferFailedException;

   void putArtifactMetadata(File var1, ArtifactMetadata var2, ArtifactRepository var3) throws TransferFailedException;

   void getArtifactMetadata(ArtifactMetadata var1, ArtifactRepository var2, File var3, String var4) throws TransferFailedException, ResourceDoesNotExistException;

   void getArtifactMetadataFromDeploymentRepository(ArtifactMetadata var1, ArtifactRepository var2, File var3, String var4) throws TransferFailedException, ResourceDoesNotExistException;

   void setOnline(boolean var1);

   boolean isOnline();

   void addProxy(String var1, String var2, int var3, String var4, String var5, String var6);

   void addAuthenticationInfo(String var1, String var2, String var3, String var4, String var5);

   void addMirror(String var1, String var2, String var3);

   void setDownloadMonitor(TransferListener var1);

   void addPermissionInfo(String var1, String var2, String var3);

   ProxyInfo getProxy(String var1);

   AuthenticationInfo getAuthenticationInfo(String var1);

   void addConfiguration(String var1, Xpp3Dom var2);

   void setInteractive(boolean var1);

   void registerWagons(Collection var1, PlexusContainer var2);

   void setDefaultRepositoryPermissions(RepositoryPermissions var1);

   ArtifactRepository getMirrorRepository(ArtifactRepository var1);
}
