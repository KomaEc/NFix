package org.apache.maven.artifact.manager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.metadata.ArtifactMetadata;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.repository.ArtifactRepositoryFactory;
import org.apache.maven.artifact.repository.ArtifactRepositoryPolicy;
import org.apache.maven.artifact.repository.DefaultArtifactRepository;
import org.apache.maven.artifact.repository.layout.ArtifactRepositoryLayout;
import org.apache.maven.wagon.ConnectionException;
import org.apache.maven.wagon.ResourceDoesNotExistException;
import org.apache.maven.wagon.TransferFailedException;
import org.apache.maven.wagon.UnsupportedProtocolException;
import org.apache.maven.wagon.Wagon;
import org.apache.maven.wagon.authentication.AuthenticationException;
import org.apache.maven.wagon.authentication.AuthenticationInfo;
import org.apache.maven.wagon.authorization.AuthorizationException;
import org.apache.maven.wagon.events.TransferListener;
import org.apache.maven.wagon.observers.ChecksumObserver;
import org.apache.maven.wagon.proxy.ProxyInfo;
import org.apache.maven.wagon.proxy.ProxyInfoProvider;
import org.apache.maven.wagon.repository.Repository;
import org.apache.maven.wagon.repository.RepositoryPermissions;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.configurator.ComponentConfigurationException;
import org.codehaus.plexus.component.configurator.ComponentConfigurator;
import org.codehaus.plexus.component.repository.exception.ComponentLifecycleException;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.codehaus.plexus.configuration.PlexusConfiguration;
import org.codehaus.plexus.configuration.xml.XmlPlexusConfiguration;
import org.codehaus.plexus.context.Context;
import org.codehaus.plexus.context.ContextException;
import org.codehaus.plexus.logging.AbstractLogEnabled;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Contextualizable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.xml.Xpp3Dom;

public class DefaultWagonManager extends AbstractLogEnabled implements WagonManager, Contextualizable, Initializable {
   private static final String WILDCARD = "*";
   private static final String EXTERNAL_WILDCARD = "external:*";
   private static final String MAVEN_ARTIFACT_PROPERTIES = "META-INF/maven/org.apache.maven/maven-artifact/pom.properties";
   private static final String WAGON_PROVIDER_CONFIGURATION = "wagonProvider";
   private static int anonymousMirrorIdSeed = 0;
   private PlexusContainer container;
   private Map proxies = new HashMap();
   private Map authenticationInfoMap = new HashMap();
   private Map serverPermissionsMap = new HashMap();
   private Map mirrors = new LinkedHashMap();
   private Map<String, XmlPlexusConfiguration> serverConfigurationMap = new HashMap();
   private Map<String, String> serverWagonProviderMap = new HashMap();
   private TransferListener downloadMonitor;
   private boolean online = true;
   private ArtifactRepositoryFactory repositoryFactory;
   private boolean interactive = true;
   private Map<String, PlexusContainer> availableWagons = new HashMap();
   private RepositoryPermissions defaultRepositoryPermissions;
   private String httpUserAgent;
   private WagonProviderMapping providerMapping = new DefaultWagonProviderMapping();

   public Wagon getWagon(Repository repository) throws UnsupportedProtocolException, WagonConfigurationException {
      String protocol = repository.getProtocol();
      if (protocol == null) {
         throw new UnsupportedProtocolException("The repository " + repository + " does not specify a protocol");
      } else {
         Wagon wagon = this.getWagon(protocol, repository.getId());
         this.configureWagon(wagon, repository.getId(), protocol);
         return wagon;
      }
   }

   public Wagon getWagon(String protocol) throws UnsupportedProtocolException {
      return this.getWagon(protocol, (String)null);
   }

   private Wagon getWagon(String protocol, String repositoryId) throws UnsupportedProtocolException {
      String hint = this.getWagonHint(protocol, repositoryId);
      PlexusContainer container = this.getWagonContainer(hint);

      Wagon wagon;
      try {
         wagon = (Wagon)container.lookup(Wagon.ROLE, hint);
      } catch (ComponentLookupException var7) {
         throw new UnsupportedProtocolException("Cannot find wagon which supports the requested protocol: " + protocol, var7);
      }

      wagon.setInteractive(this.interactive);
      return wagon;
   }

   private String getWagonHint(String protocol, String repositoryId) {
      String impl = null;
      if (repositoryId != null && this.serverWagonProviderMap.containsKey(repositoryId)) {
         impl = (String)this.serverWagonProviderMap.get(repositoryId);
         this.getLogger().debug("Using Wagon implementation " + impl + " from settings for server " + repositoryId);
      } else {
         impl = this.providerMapping.getWagonProvider(protocol);
         if (impl != null) {
            this.getLogger().debug("Using Wagon implementation " + impl + " from default mapping for protocol " + protocol);
         }
      }

      String hint;
      if (impl != null) {
         hint = protocol + "-" + impl;
         PlexusContainer container = this.getWagonContainer(hint);
         if (container == null || !container.hasComponent(Wagon.ROLE, hint)) {
            this.getLogger().debug("Cannot find wagon for protocol-provider hint: '" + hint + "', configured for repository: '" + repositoryId + "'. Using protocol hint: '" + protocol + "' instead.");
            hint = protocol;
         }
      } else {
         hint = protocol;
      }

      return hint;
   }

   private PlexusContainer getWagonContainer(String hint) {
      PlexusContainer container = this.container;
      if (this.availableWagons.containsKey(hint)) {
         container = (PlexusContainer)this.availableWagons.get(hint);
      }

      return container;
   }

   public void putArtifact(File source, Artifact artifact, ArtifactRepository deploymentRepository) throws TransferFailedException {
      this.putRemoteFile(deploymentRepository, source, deploymentRepository.pathOf(artifact), this.downloadMonitor);
   }

   public void putArtifactMetadata(File source, ArtifactMetadata artifactMetadata, ArtifactRepository repository) throws TransferFailedException {
      this.getLogger().info("Uploading " + artifactMetadata);
      this.putRemoteFile(repository, source, repository.pathOfRemoteRepositoryMetadata(artifactMetadata), (TransferListener)null);
   }

   private void putRemoteFile(ArtifactRepository repository, File source, String remotePath, TransferListener downloadMonitor) throws TransferFailedException {
      this.failIfNotOnline();
      String protocol = repository.getProtocol();

      Wagon wagon;
      try {
         wagon = this.getWagon(protocol, repository.getId());
         this.configureWagon(wagon, repository);
      } catch (UnsupportedProtocolException var23) {
         throw new TransferFailedException("Unsupported Protocol: '" + protocol + "': " + var23.getMessage(), var23);
      }

      if (downloadMonitor != null) {
         wagon.addTransferListener(downloadMonitor);
      }

      Map checksums = new HashMap(2);
      HashMap sums = new HashMap(2);

      try {
         ChecksumObserver checksumObserver = new ChecksumObserver("MD5");
         wagon.addTransferListener(checksumObserver);
         checksums.put("md5", checksumObserver);
         checksumObserver = new ChecksumObserver("SHA-1");
         wagon.addTransferListener(checksumObserver);
         checksums.put("sha1", checksumObserver);
      } catch (NoSuchAlgorithmException var22) {
         throw new TransferFailedException("Unable to add checksum methods: " + var22.getMessage(), var22);
      }

      try {
         Repository artifactRepository = new Repository(repository.getId(), repository.getUrl());
         if (this.serverPermissionsMap.containsKey(repository.getId())) {
            RepositoryPermissions perms = (RepositoryPermissions)this.serverPermissionsMap.get(repository.getId());
            this.getLogger().debug("adding permissions to wagon connection: " + perms.getFileMode() + " " + perms.getDirectoryMode());
            artifactRepository.setPermissions(perms);
         } else if (this.defaultRepositoryPermissions != null) {
            artifactRepository.setPermissions(this.defaultRepositoryPermissions);
         } else {
            this.getLogger().debug("not adding permissions to wagon connection");
         }

         wagon.connect(artifactRepository, this.getAuthenticationInfo(repository.getId()), new ProxyInfoProvider() {
            public ProxyInfo getProxyInfo(String protocol) {
               return DefaultWagonManager.this.getProxy(protocol);
            }
         });
         wagon.put(source, remotePath);
         wagon.removeTransferListener(downloadMonitor);
         Iterator i = checksums.keySet().iterator();

         String extension;
         while(i.hasNext()) {
            extension = (String)i.next();
            ChecksumObserver observer = (ChecksumObserver)checksums.get(extension);
            sums.put(extension, observer.getActualChecksum());
         }

         i = checksums.keySet().iterator();

         while(i.hasNext()) {
            extension = (String)i.next();
            File temp = File.createTempFile("maven-artifact", (String)null);
            temp.deleteOnExit();
            FileUtils.fileWrite(temp.getAbsolutePath(), "UTF-8", (String)sums.get(extension));
            wagon.put(temp, remotePath + "." + extension);
         }

      } catch (ConnectionException var24) {
         throw new TransferFailedException("Connection failed: " + var24.getMessage(), var24);
      } catch (AuthenticationException var25) {
         throw new TransferFailedException("Authentication failed: " + var25.getMessage(), var25);
      } catch (AuthorizationException var26) {
         throw new TransferFailedException("Authorization failed: " + var26.getMessage(), var26);
      } catch (ResourceDoesNotExistException var27) {
         throw new TransferFailedException("Resource to deploy not found: " + var27.getMessage(), var27);
      } catch (IOException var28) {
         throw new TransferFailedException("Error creating temporary file for deployment: " + var28.getMessage(), var28);
      } finally {
         this.disconnectWagon(wagon);
         this.releaseWagon(protocol, wagon, repository.getId());
      }
   }

   public void getArtifact(Artifact artifact, List remoteRepositories) throws TransferFailedException, ResourceDoesNotExistException {
      boolean successful = false;
      Iterator iter = remoteRepositories.iterator();

      while(iter.hasNext() && !successful) {
         ArtifactRepository repository = (ArtifactRepository)iter.next();

         try {
            this.getArtifact(artifact, repository);
            successful = artifact.isResolved();
         } catch (ResourceDoesNotExistException var7) {
            this.getLogger().info("Unable to find resource '" + artifact.getId() + "' in repository " + repository.getId() + " (" + repository.getUrl() + ")");
         } catch (TransferFailedException var8) {
            this.getLogger().warn("Unable to get resource '" + artifact.getId() + "' from repository " + repository.getId() + " (" + repository.getUrl() + "): " + var8.getMessage());
         }
      }

      if (!successful && !artifact.getFile().exists()) {
         throw new ResourceDoesNotExistException("Unable to download the artifact from any repository");
      }
   }

   public void getArtifact(Artifact artifact, ArtifactRepository repository) throws TransferFailedException, ResourceDoesNotExistException {
      String remotePath = repository.pathOf(artifact);
      ArtifactRepositoryPolicy policy = artifact.isSnapshot() ? repository.getSnapshots() : repository.getReleases();
      if (!policy.isEnabled()) {
         this.getLogger().debug("Skipping disabled repository " + repository.getId());
      } else if (repository.isBlacklisted()) {
         this.getLogger().debug("Skipping blacklisted repository " + repository.getId());
      } else {
         this.getLogger().debug("Trying repository " + repository.getId());
         this.getRemoteFile(this.getMirrorRepository(repository), artifact.getFile(), remotePath, this.downloadMonitor, policy.getChecksumPolicy(), false);
         this.getLogger().debug("  Artifact resolved");
         artifact.setResolved(true);
      }

   }

   public void getArtifactMetadata(ArtifactMetadata metadata, ArtifactRepository repository, File destination, String checksumPolicy) throws TransferFailedException, ResourceDoesNotExistException {
      String remotePath = repository.pathOfRemoteRepositoryMetadata(metadata);
      this.getRemoteFile(this.getMirrorRepository(repository), destination, remotePath, (TransferListener)null, checksumPolicy, true);
   }

   public void getArtifactMetadataFromDeploymentRepository(ArtifactMetadata metadata, ArtifactRepository repository, File destination, String checksumPolicy) throws TransferFailedException, ResourceDoesNotExistException {
      String remotePath = repository.pathOfRemoteRepositoryMetadata(metadata);
      this.getRemoteFile(repository, destination, remotePath, (TransferListener)null, checksumPolicy, true);
   }

   private void getRemoteFile(ArtifactRepository repository, File destination, String remotePath, TransferListener downloadMonitor, String checksumPolicy, boolean force) throws TransferFailedException, ResourceDoesNotExistException {
      this.failIfNotOnline();
      String protocol = repository.getProtocol();

      Wagon wagon;
      try {
         wagon = this.getWagon(protocol, repository.getId());
         this.configureWagon(wagon, repository);
      } catch (UnsupportedProtocolException var46) {
         throw new TransferFailedException("Unsupported Protocol: '" + protocol + "': " + var46.getMessage(), var46);
      }

      if (downloadMonitor != null) {
         wagon.addTransferListener(downloadMonitor);
      }

      File temp = new File(destination + ".tmp");
      temp.deleteOnExit();
      boolean downloaded = false;

      try {
         this.getLogger().debug("Connecting to repository: '" + repository.getId() + "' with url: '" + repository.getUrl() + "'.");
         wagon.connect(new Repository(repository.getId(), repository.getUrl()), this.getAuthenticationInfo(repository.getId()), new ProxyInfoProvider() {
            public ProxyInfo getProxyInfo(String protocol) {
               return DefaultWagonManager.this.getProxy(protocol);
            }
         });
         boolean firstRun = true;

         for(boolean retry = true; firstRun || retry; firstRun = false) {
            retry = false;
            ChecksumObserver md5ChecksumObserver = null;
            ChecksumObserver sha1ChecksumObserver = null;

            try {
               md5ChecksumObserver = new ChecksumObserver("MD5");
               wagon.addTransferListener(md5ChecksumObserver);
               sha1ChecksumObserver = new ChecksumObserver("SHA-1");
               wagon.addTransferListener(sha1ChecksumObserver);
               if (destination.exists() && !force) {
                  try {
                     downloaded = wagon.getIfNewer(remotePath, temp, destination.lastModified());
                     if (!downloaded) {
                        destination.setLastModified(System.currentTimeMillis());
                     }
                  } catch (UnsupportedOperationException var45) {
                     wagon.get(remotePath, temp);
                     downloaded = true;
                  }
               } else {
                  wagon.get(remotePath, temp);
                  downloaded = true;
               }
            } catch (NoSuchAlgorithmException var47) {
               throw new TransferFailedException("Unable to add checksum methods: " + var47.getMessage(), var47);
            } finally {
               if (md5ChecksumObserver != null) {
                  wagon.removeTransferListener(md5ChecksumObserver);
               }

               if (sha1ChecksumObserver != null) {
                  wagon.removeTransferListener(sha1ChecksumObserver);
               }

            }

            if (downloaded) {
               if (downloadMonitor != null) {
                  wagon.removeTransferListener(downloadMonitor);
               }

               try {
                  this.verifyChecksum(sha1ChecksumObserver, destination, temp, remotePath, ".sha1", wagon);
               } catch (ChecksumFailedException var51) {
                  if (firstRun) {
                     this.getLogger().warn("*** CHECKSUM FAILED - " + var51.getMessage() + " - RETRYING");
                     retry = true;
                  } else {
                     this.handleChecksumFailure(checksumPolicy, var51.getMessage(), var51.getCause());
                  }
               } catch (ResourceDoesNotExistException var52) {
                  this.getLogger().debug("SHA1 not found, trying MD5", var52);

                  try {
                     this.verifyChecksum(md5ChecksumObserver, destination, temp, remotePath, ".md5", wagon);
                  } catch (ChecksumFailedException var49) {
                     if (firstRun) {
                        retry = true;
                     } else {
                        this.handleChecksumFailure(checksumPolicy, var49.getMessage(), var49.getCause());
                     }
                  } catch (ResourceDoesNotExistException var50) {
                     this.handleChecksumFailure(checksumPolicy, "Error retrieving checksum file for " + remotePath, var50);
                  }
               }

               if (downloadMonitor != null) {
                  wagon.addTransferListener(downloadMonitor);
               }
            }
         }
      } catch (ConnectionException var53) {
         throw new TransferFailedException("Connection failed: " + var53.getMessage(), var53);
      } catch (AuthenticationException var54) {
         throw new TransferFailedException("Authentication failed: " + var54.getMessage(), var54);
      } catch (AuthorizationException var55) {
         throw new TransferFailedException("Authorization failed: " + var55.getMessage(), var55);
      } finally {
         this.disconnectWagon(wagon);
         this.releaseWagon(protocol, wagon, repository.getId());
      }

      if (downloaded) {
         if (!temp.exists()) {
            throw new ResourceDoesNotExistException("Downloaded file does not exist: " + temp);
         }

         if (!temp.renameTo(destination)) {
            try {
               FileUtils.copyFile(temp, destination);
               temp.delete();
            } catch (IOException var44) {
               throw new TransferFailedException("Error copying temporary file to the final destination: " + var44.getMessage(), var44);
            }
         }
      }

   }

   public ArtifactRepository getMirrorRepository(ArtifactRepository repository) {
      ArtifactRepository mirror = this.getMirror(repository);
      if (mirror != null) {
         String id = mirror.getId();
         if (id == null) {
            id = repository.getId();
         }

         this.getLogger().debug("Using mirror: " + mirror.getUrl() + " (id: " + id + ")");
         repository = this.repositoryFactory.createArtifactRepository(id, mirror.getUrl(), repository.getLayout(), repository.getSnapshots(), repository.getReleases());
      }

      return repository;
   }

   private void failIfNotOnline() throws TransferFailedException {
      if (!this.isOnline()) {
         throw new TransferFailedException("System is offline.");
      }
   }

   private void handleChecksumFailure(String checksumPolicy, String message, Throwable cause) throws ChecksumFailedException {
      if ("fail".equals(checksumPolicy)) {
         throw new ChecksumFailedException(message, cause);
      } else {
         if (!"ignore".equals(checksumPolicy)) {
            this.getLogger().warn("*** CHECKSUM FAILED - " + message + " - IGNORING");
         }

      }
   }

   private void verifyChecksum(ChecksumObserver checksumObserver, File destination, File tempDestination, String remotePath, String checksumFileExtension, Wagon wagon) throws ResourceDoesNotExistException, TransferFailedException, AuthorizationException {
      try {
         String actualChecksum = checksumObserver.getActualChecksum();
         File tempChecksumFile = new File(tempDestination + checksumFileExtension + ".tmp");
         tempChecksumFile.deleteOnExit();
         wagon.get(remotePath + checksumFileExtension, tempChecksumFile);
         String expectedChecksum = FileUtils.fileRead(tempChecksumFile, "UTF-8");
         expectedChecksum = expectedChecksum.trim();
         int spacePos;
         if (!expectedChecksum.regionMatches(true, 0, "MD", 0, 2) && !expectedChecksum.regionMatches(true, 0, "SHA", 0, 3)) {
            spacePos = expectedChecksum.indexOf(32);
            if (spacePos != -1) {
               expectedChecksum = expectedChecksum.substring(0, spacePos);
            }
         } else {
            spacePos = expectedChecksum.lastIndexOf(32);
            expectedChecksum = expectedChecksum.substring(spacePos + 1);
         }

         if (expectedChecksum.equalsIgnoreCase(actualChecksum)) {
            File checksumFile = new File(destination + checksumFileExtension);
            if (checksumFile.exists()) {
               checksumFile.delete();
            }

            FileUtils.copyFile(tempChecksumFile, checksumFile);
         } else {
            throw new ChecksumFailedException("Checksum failed on download: local = '" + actualChecksum + "'; remote = '" + expectedChecksum + "'");
         }
      } catch (IOException var11) {
         throw new ChecksumFailedException("Invalid checksum file", var11);
      }
   }

   private void disconnectWagon(Wagon wagon) {
      try {
         wagon.disconnect();
      } catch (ConnectionException var3) {
         this.getLogger().error("Problem disconnecting from wagon - ignoring: " + var3.getMessage());
      }

   }

   private void releaseWagon(String protocol, Wagon wagon, String repositoryId) {
      String hint = this.getWagonHint(protocol, repositoryId);
      PlexusContainer container = this.getWagonContainer(hint);

      try {
         container.release(wagon);
      } catch (ComponentLifecycleException var7) {
         this.getLogger().error("Problem releasing wagon - ignoring: " + var7.getMessage());
      }

   }

   public ProxyInfo getProxy(String protocol) {
      ProxyInfo info = (ProxyInfo)this.proxies.get(protocol);
      if (info != null) {
         this.getLogger().debug("Using Proxy: " + info.getHost());
      }

      return info;
   }

   public AuthenticationInfo getAuthenticationInfo(String id) {
      return (AuthenticationInfo)this.authenticationInfoMap.get(id);
   }

   public ArtifactRepository getMirror(ArtifactRepository originalRepository) {
      ArtifactRepository selectedMirror = (ArtifactRepository)this.mirrors.get(originalRepository.getId());
      if (null == selectedMirror) {
         Set keySet = this.mirrors.keySet();
         if (keySet != null) {
            Iterator iter = keySet.iterator();

            while(iter.hasNext()) {
               String pattern = (String)iter.next();
               if (this.matchPattern(originalRepository, pattern)) {
                  selectedMirror = (ArtifactRepository)this.mirrors.get(pattern);
                  break;
               }
            }
         }
      }

      return selectedMirror;
   }

   public boolean matchPattern(ArtifactRepository originalRepository, String pattern) {
      boolean result = false;
      String originalId = originalRepository.getId();
      if (!"*".equals(pattern) && !pattern.equals(originalId)) {
         String[] repos = pattern.split(",");

         for(int i = 0; i < repos.length; ++i) {
            String repo = repos[i];
            if (repo.length() > 1 && repo.startsWith("!")) {
               if (originalId.equals(repo.substring(1))) {
                  result = false;
                  break;
               }
            } else {
               if (originalId.equals(repo)) {
                  result = true;
                  break;
               }

               if ("external:*".equals(repo) && this.isExternalRepo(originalRepository)) {
                  result = true;
               } else if ("*".equals(repo)) {
                  result = true;
               }
            }
         }
      } else {
         result = true;
      }

      return result;
   }

   public boolean isExternalRepo(ArtifactRepository originalRepository) {
      try {
         URL url = new URL(originalRepository.getUrl());
         return !url.getHost().equals("localhost") && !url.getHost().equals("127.0.0.1") && !url.getProtocol().equals("file");
      } catch (MalformedURLException var3) {
         return false;
      }
   }

   public void addProxy(String protocol, String host, int port, String username, String password, String nonProxyHosts) {
      ProxyInfo proxyInfo = new ProxyInfo();
      proxyInfo.setHost(host);
      proxyInfo.setType(protocol);
      proxyInfo.setPort(port);
      proxyInfo.setNonProxyHosts(nonProxyHosts);
      proxyInfo.setUserName(username);
      proxyInfo.setPassword(password);
      this.proxies.put(protocol, proxyInfo);
   }

   public void contextualize(Context context) throws ContextException {
      this.container = (PlexusContainer)context.get("plexus");
   }

   public void setDownloadMonitor(TransferListener downloadMonitor) {
      this.downloadMonitor = downloadMonitor;
   }

   public void addAuthenticationInfo(String repositoryId, String username, String password, String privateKey, String passphrase) {
      AuthenticationInfo authInfo = new AuthenticationInfo();
      authInfo.setUserName(username);
      authInfo.setPassword(password);
      authInfo.setPrivateKey(privateKey);
      authInfo.setPassphrase(passphrase);
      this.authenticationInfoMap.put(repositoryId, authInfo);
   }

   public void addPermissionInfo(String repositoryId, String filePermissions, String directoryPermissions) {
      RepositoryPermissions permissions = new RepositoryPermissions();
      boolean addPermissions = false;
      if (filePermissions != null) {
         permissions.setFileMode(filePermissions);
         addPermissions = true;
      }

      if (directoryPermissions != null) {
         permissions.setDirectoryMode(directoryPermissions);
         addPermissions = true;
      }

      if (addPermissions) {
         this.serverPermissionsMap.put(repositoryId, permissions);
      }

   }

   public void addMirror(String id, String mirrorOf, String url) {
      if (id == null) {
         id = "mirror-" + anonymousMirrorIdSeed++;
         this.getLogger().warn("You are using a mirror that doesn't declare an <id/> element. Using '" + id + "' instead:\nId: " + id + "\nmirrorOf: " + mirrorOf + "\nurl: " + url + "\n");
      }

      ArtifactRepository mirror = new DefaultArtifactRepository(id, url, (ArtifactRepositoryLayout)null);
      if (!this.mirrors.containsKey(mirrorOf)) {
         this.mirrors.put(mirrorOf, mirror);
      }

   }

   public void setOnline(boolean online) {
      this.online = online;
   }

   public boolean isOnline() {
      return this.online;
   }

   public void setInteractive(boolean interactive) {
      this.interactive = interactive;
   }

   public void registerWagons(Collection wagons, PlexusContainer extensionContainer) {
      Iterator i = wagons.iterator();

      while(i.hasNext()) {
         this.availableWagons.put(i.next(), extensionContainer);
      }

   }

   private void configureWagon(Wagon wagon, ArtifactRepository repository) throws WagonConfigurationException {
      this.configureWagon(wagon, repository.getId(), repository.getProtocol());
   }

   private void configureWagon(Wagon wagon, String repositoryId, String protocol) throws WagonConfigurationException {
      PlexusConfiguration config = (PlexusConfiguration)this.serverConfigurationMap.get(repositoryId);
      if (protocol.startsWith("http") || protocol.startsWith("dav")) {
         config = this.updateUserAgentForHttp(wagon, config);
      }

      if (config != null) {
         ComponentConfigurator componentConfigurator = null;

         try {
            componentConfigurator = (ComponentConfigurator)this.container.lookup(ComponentConfigurator.ROLE, "wagon");
            componentConfigurator.configureComponent(wagon, config, this.container.getContainerRealm());
         } catch (ComponentLookupException var15) {
            throw new WagonConfigurationException(repositoryId, "Unable to lookup wagon configurator. Wagon configuration cannot be applied.", var15);
         } catch (ComponentConfigurationException var16) {
            throw new WagonConfigurationException(repositoryId, "Unable to apply wagon configuration.", var16);
         } finally {
            if (componentConfigurator != null) {
               try {
                  this.container.release(componentConfigurator);
               } catch (ComponentLifecycleException var14) {
                  this.getLogger().error("Problem releasing configurator - ignoring: " + var14.getMessage());
               }
            }

         }
      }

   }

   private PlexusConfiguration updateUserAgentForHttp(Wagon wagon, PlexusConfiguration config) {
      if (config == null) {
         config = new XmlPlexusConfiguration("configuration");
      }

      if (this.httpUserAgent != null) {
         try {
            wagon.getClass().getMethod("setHttpHeaders", Properties.class);
            PlexusConfiguration headerConfig = ((PlexusConfiguration)config).getChild("httpHeaders", true);
            PlexusConfiguration[] children = headerConfig.getChildren("property");
            boolean found = false;
            this.getLogger().debug("Checking for pre-existing User-Agent configuration.");

            for(int i = 0; i < children.length; ++i) {
               PlexusConfiguration c = children[i].getChild("name", false);
               if (c != null && "User-Agent".equals(c.getValue((String)null))) {
                  found = true;
                  break;
               }
            }

            if (!found) {
               this.getLogger().debug("Adding User-Agent configuration.");
               XmlPlexusConfiguration propertyConfig = new XmlPlexusConfiguration("property");
               headerConfig.addChild(propertyConfig);
               XmlPlexusConfiguration nameConfig = new XmlPlexusConfiguration("name");
               nameConfig.setValue("User-Agent");
               propertyConfig.addChild(nameConfig);
               XmlPlexusConfiguration versionConfig = new XmlPlexusConfiguration("value");
               versionConfig.setValue(this.httpUserAgent);
               propertyConfig.addChild(versionConfig);
            } else {
               this.getLogger().debug("User-Agent configuration found.");
            }
         } catch (SecurityException var9) {
            this.getLogger().debug("setHttpHeaders method not accessible on wagon: " + wagon + "; skipping User-Agent configuration.");
         } catch (NoSuchMethodException var10) {
            this.getLogger().debug("setHttpHeaders method not found on wagon: " + wagon + "; skipping User-Agent configuration.");
         }
      }

      return (PlexusConfiguration)config;
   }

   public void addConfiguration(String repositoryId, Xpp3Dom configuration) {
      if (repositoryId != null && configuration != null) {
         XmlPlexusConfiguration xmlConf = new XmlPlexusConfiguration(configuration);

         for(int i = 0; i < configuration.getChildCount(); ++i) {
            Xpp3Dom domChild = configuration.getChild(i);
            if ("wagonProvider".equals(domChild.getName())) {
               this.serverWagonProviderMap.put(repositoryId, domChild.getValue());
               configuration.removeChild(i);
               break;
            }

            ++i;
         }

         this.serverConfigurationMap.put(repositoryId, xmlConf);
      } else {
         throw new IllegalArgumentException("arguments can't be null");
      }
   }

   public void setDefaultRepositoryPermissions(RepositoryPermissions defaultRepositoryPermissions) {
      this.defaultRepositoryPermissions = defaultRepositoryPermissions;
   }

   public void initialize() throws InitializationException {
      if (this.httpUserAgent == null) {
         InputStream resourceAsStream = null;

         try {
            Properties properties = new Properties();
            resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("META-INF/maven/org.apache.maven/maven-artifact/pom.properties");
            if (resourceAsStream != null) {
               try {
                  properties.load(resourceAsStream);
                  this.httpUserAgent = "maven-artifact/" + properties.getProperty("version") + " (Java " + System.getProperty("java.version") + "; " + System.getProperty("os.name") + " " + System.getProperty("os.version") + ")";
               } catch (IOException var7) {
                  this.getLogger().warn("Failed to load Maven artifact properties from:\nMETA-INF/maven/org.apache.maven/maven-artifact/pom.properties\n\nUser-Agent HTTP header may be incorrect for artifact resolution.");
               }
            }
         } finally {
            IOUtil.close(resourceAsStream);
         }
      }

   }

   public void setHttpUserAgent(String userAgent) {
      this.httpUserAgent = userAgent;
   }

   public String getHttpUserAgent() {
      return this.httpUserAgent;
   }
}
