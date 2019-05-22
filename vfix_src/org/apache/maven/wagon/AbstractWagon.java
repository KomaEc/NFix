package org.apache.maven.wagon;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import org.apache.maven.wagon.authentication.AuthenticationException;
import org.apache.maven.wagon.authentication.AuthenticationInfo;
import org.apache.maven.wagon.authorization.AuthorizationException;
import org.apache.maven.wagon.events.SessionEvent;
import org.apache.maven.wagon.events.SessionEventSupport;
import org.apache.maven.wagon.events.SessionListener;
import org.apache.maven.wagon.events.TransferEvent;
import org.apache.maven.wagon.events.TransferEventSupport;
import org.apache.maven.wagon.events.TransferListener;
import org.apache.maven.wagon.proxy.ProxyInfo;
import org.apache.maven.wagon.proxy.ProxyInfoProvider;
import org.apache.maven.wagon.proxy.ProxyUtils;
import org.apache.maven.wagon.repository.Repository;
import org.apache.maven.wagon.repository.RepositoryPermissions;
import org.apache.maven.wagon.resource.Resource;
import org.codehaus.plexus.util.IOUtil;

public abstract class AbstractWagon implements Wagon {
   protected static final int DEFAULT_BUFFER_SIZE = 4096;
   protected Repository repository;
   protected SessionEventSupport sessionEventSupport = new SessionEventSupport();
   protected TransferEventSupport transferEventSupport = new TransferEventSupport();
   protected AuthenticationInfo authenticationInfo;
   protected boolean interactive = true;
   private int connectionTimeout = 60000;
   private ProxyInfoProvider proxyInfoProvider;
   /** @deprecated */
   protected ProxyInfo proxyInfo;
   private RepositoryPermissions permissionsOverride;

   public Repository getRepository() {
      return this.repository;
   }

   public ProxyInfo getProxyInfo() {
      return this.proxyInfoProvider != null ? this.proxyInfoProvider.getProxyInfo((String)null) : null;
   }

   public AuthenticationInfo getAuthenticationInfo() {
      return this.authenticationInfo;
   }

   public void openConnection() throws ConnectionException, AuthenticationException {
      try {
         this.openConnectionInternal();
      } catch (ConnectionException var2) {
         this.fireSessionConnectionRefused();
         throw var2;
      } catch (AuthenticationException var3) {
         this.fireSessionConnectionRefused();
         throw var3;
      }
   }

   public void connect(Repository repository) throws ConnectionException, AuthenticationException {
      this.connect(repository, (AuthenticationInfo)null, (ProxyInfoProvider)((ProxyInfoProvider)null));
   }

   public void connect(Repository repository, ProxyInfo proxyInfo) throws ConnectionException, AuthenticationException {
      this.connect(repository, (AuthenticationInfo)null, (ProxyInfo)proxyInfo);
   }

   public void connect(Repository repository, ProxyInfoProvider proxyInfoProvider) throws ConnectionException, AuthenticationException {
      this.connect(repository, (AuthenticationInfo)null, (ProxyInfoProvider)proxyInfoProvider);
   }

   public void connect(Repository repository, AuthenticationInfo authenticationInfo) throws ConnectionException, AuthenticationException {
      this.connect(repository, authenticationInfo, (ProxyInfoProvider)null);
   }

   public void connect(Repository repository, AuthenticationInfo authenticationInfo, final ProxyInfo proxyInfo) throws ConnectionException, AuthenticationException {
      this.connect(repository, authenticationInfo, new ProxyInfoProvider() {
         public ProxyInfo getProxyInfo(String protocol) {
            return protocol != null && proxyInfo != null && !protocol.equalsIgnoreCase(proxyInfo.getType()) ? null : proxyInfo;
         }
      });
      this.proxyInfo = proxyInfo;
   }

   public void connect(Repository repository, AuthenticationInfo authenticationInfo, ProxyInfoProvider proxyInfoProvider) throws ConnectionException, AuthenticationException {
      if (repository == null) {
         throw new IllegalStateException("The repository specified cannot be null.");
      } else {
         if (this.permissionsOverride != null) {
            repository.setPermissions(this.permissionsOverride);
         }

         this.repository = repository;
         if (authenticationInfo == null) {
            authenticationInfo = new AuthenticationInfo();
         }

         if (authenticationInfo.getUserName() == null && repository.getUsername() != null) {
            authenticationInfo.setUserName(repository.getUsername());
            if (repository.getPassword() != null && authenticationInfo.getPassword() == null) {
               authenticationInfo.setPassword(repository.getPassword());
            }
         }

         this.authenticationInfo = authenticationInfo;
         this.proxyInfoProvider = proxyInfoProvider;
         this.fireSessionOpening();
         this.openConnection();
         this.fireSessionOpened();
      }
   }

   protected abstract void openConnectionInternal() throws ConnectionException, AuthenticationException;

   public void disconnect() throws ConnectionException {
      this.fireSessionDisconnecting();

      try {
         this.closeConnection();
      } catch (ConnectionException var2) {
         this.fireSessionError(var2);
         throw var2;
      }

      this.fireSessionDisconnected();
   }

   protected abstract void closeConnection() throws ConnectionException;

   protected void createParentDirectories(File destination) throws TransferFailedException {
      File destinationDirectory = destination.getParentFile();

      try {
         destinationDirectory = destinationDirectory.getCanonicalFile();
      } catch (IOException var4) {
      }

      if (destinationDirectory != null && !destinationDirectory.exists()) {
         destinationDirectory.mkdirs();
         if (!destinationDirectory.exists()) {
            throw new TransferFailedException("Specified destination directory cannot be created: " + destinationDirectory);
         }
      }

   }

   public void setTimeout(int timeoutValue) {
      this.connectionTimeout = timeoutValue;
   }

   public int getTimeout() {
      return this.connectionTimeout;
   }

   protected void getTransfer(Resource resource, File destination, InputStream input) throws TransferFailedException {
      this.getTransfer(resource, destination, input, true, Integer.MAX_VALUE);
   }

   protected void getTransfer(Resource resource, OutputStream output, InputStream input) throws TransferFailedException {
      this.getTransfer(resource, output, input, true, Integer.MAX_VALUE);
   }

   protected void getTransfer(Resource resource, File destination, InputStream input, boolean closeInput, int maxSize) throws TransferFailedException {
      this.fireTransferDebug("attempting to create parent directories for destination: " + destination.getName());
      this.createParentDirectories(destination);
      OutputStream output = new LazyFileOutputStream(destination);
      this.fireGetStarted(resource, destination);

      try {
         this.getTransfer(resource, (OutputStream)output, input, closeInput, maxSize);
      } catch (TransferFailedException var12) {
         if (destination.exists()) {
            boolean deleted = destination.delete();
            if (!deleted) {
               destination.deleteOnExit();
            }
         }

         throw var12;
      } finally {
         IOUtil.close((OutputStream)output);
      }

      this.fireGetCompleted(resource, destination);
   }

   protected void getTransfer(Resource resource, OutputStream output, InputStream input, boolean closeInput, int maxSize) throws TransferFailedException {
      try {
         this.transfer(resource, input, output, 5, maxSize);
         this.finishGetTransfer(resource, input, output);
      } catch (IOException var11) {
         this.fireTransferError(resource, var11, 5);
         String msg = "GET request of: " + resource.getName() + " from " + this.repository.getName() + " failed";
         throw new TransferFailedException(msg, var11);
      } finally {
         if (closeInput) {
            IOUtil.close(input);
         }

         this.cleanupGetTransfer(resource);
      }

   }

   protected void finishGetTransfer(Resource resource, InputStream input, OutputStream output) throws TransferFailedException {
   }

   protected void cleanupGetTransfer(Resource resource) {
   }

   protected void putTransfer(Resource resource, File source, OutputStream output, boolean closeOutput) throws TransferFailedException, AuthorizationException, ResourceDoesNotExistException {
      this.firePutStarted(resource, source);
      this.transfer(resource, source, output, closeOutput);
      this.firePutCompleted(resource, source);
   }

   protected void transfer(Resource resource, File source, OutputStream output, boolean closeOutput) throws TransferFailedException, AuthorizationException, ResourceDoesNotExistException {
      FileInputStream input = null;

      try {
         input = new FileInputStream(source);
         this.putTransfer(resource, (InputStream)input, output, closeOutput);
      } catch (FileNotFoundException var10) {
         this.fireTransferError(resource, var10, 6);
         throw new TransferFailedException("Specified source file does not exist: " + source, var10);
      } finally {
         IOUtil.close((InputStream)input);
      }

   }

   protected void putTransfer(Resource resource, InputStream input, OutputStream output, boolean closeOutput) throws TransferFailedException, AuthorizationException, ResourceDoesNotExistException {
      try {
         this.transfer(resource, input, output, 6);
         this.finishPutTransfer(resource, input, output);
      } catch (IOException var10) {
         this.fireTransferError(resource, var10, 6);
         String msg = "PUT request to: " + resource.getName() + " in " + this.repository.getName() + " failed";
         throw new TransferFailedException(msg, var10);
      } finally {
         if (closeOutput) {
            IOUtil.close(output);
         }

         this.cleanupPutTransfer(resource);
      }

   }

   protected void cleanupPutTransfer(Resource resource) {
   }

   protected void finishPutTransfer(Resource resource, InputStream input, OutputStream output) throws TransferFailedException, AuthorizationException, ResourceDoesNotExistException {
   }

   protected void transfer(Resource resource, InputStream input, OutputStream output, int requestType) throws IOException {
      this.transfer(resource, input, output, requestType, Integer.MAX_VALUE);
   }

   protected void transfer(Resource resource, InputStream input, OutputStream output, int requestType, int maxSize) throws IOException {
      byte[] buffer = new byte[4096];
      TransferEvent transferEvent = new TransferEvent(this, resource, 3, requestType);
      transferEvent.setTimestamp(System.currentTimeMillis());

      int n;
      for(int remaining = maxSize; remaining > 0; remaining -= n) {
         n = input.read(buffer, 0, Math.min(buffer.length, remaining));
         if (n == -1) {
            break;
         }

         this.fireTransferProgress(transferEvent, buffer, n);
         output.write(buffer, 0, n);
      }

      output.flush();
   }

   protected void fireTransferProgress(TransferEvent transferEvent, byte[] buffer, int n) {
      this.transferEventSupport.fireTransferProgress(transferEvent, buffer, n);
   }

   protected void fireGetCompleted(Resource resource, File localFile) {
      long timestamp = System.currentTimeMillis();
      TransferEvent transferEvent = new TransferEvent(this, resource, 2, 5);
      transferEvent.setTimestamp(timestamp);
      transferEvent.setLocalFile(localFile);
      this.transferEventSupport.fireTransferCompleted(transferEvent);
   }

   protected void fireGetStarted(Resource resource, File localFile) {
      long timestamp = System.currentTimeMillis();
      TransferEvent transferEvent = new TransferEvent(this, resource, 1, 5);
      transferEvent.setTimestamp(timestamp);
      transferEvent.setLocalFile(localFile);
      this.transferEventSupport.fireTransferStarted(transferEvent);
   }

   protected void fireGetInitiated(Resource resource, File localFile) {
      long timestamp = System.currentTimeMillis();
      TransferEvent transferEvent = new TransferEvent(this, resource, 0, 5);
      transferEvent.setTimestamp(timestamp);
      transferEvent.setLocalFile(localFile);
      this.transferEventSupport.fireTransferInitiated(transferEvent);
   }

   protected void firePutInitiated(Resource resource, File localFile) {
      long timestamp = System.currentTimeMillis();
      TransferEvent transferEvent = new TransferEvent(this, resource, 0, 6);
      transferEvent.setTimestamp(timestamp);
      transferEvent.setLocalFile(localFile);
      this.transferEventSupport.fireTransferInitiated(transferEvent);
   }

   protected void firePutCompleted(Resource resource, File localFile) {
      long timestamp = System.currentTimeMillis();
      TransferEvent transferEvent = new TransferEvent(this, resource, 2, 6);
      transferEvent.setTimestamp(timestamp);
      transferEvent.setLocalFile(localFile);
      this.transferEventSupport.fireTransferCompleted(transferEvent);
   }

   protected void firePutStarted(Resource resource, File localFile) {
      long timestamp = System.currentTimeMillis();
      TransferEvent transferEvent = new TransferEvent(this, resource, 1, 6);
      transferEvent.setTimestamp(timestamp);
      transferEvent.setLocalFile(localFile);
      this.transferEventSupport.fireTransferStarted(transferEvent);
   }

   protected void fireSessionDisconnected() {
      long timestamp = System.currentTimeMillis();
      SessionEvent sessionEvent = new SessionEvent(this, 3);
      sessionEvent.setTimestamp(timestamp);
      this.sessionEventSupport.fireSessionDisconnected(sessionEvent);
   }

   protected void fireSessionDisconnecting() {
      long timestamp = System.currentTimeMillis();
      SessionEvent sessionEvent = new SessionEvent(this, 2);
      sessionEvent.setTimestamp(timestamp);
      this.sessionEventSupport.fireSessionDisconnecting(sessionEvent);
   }

   protected void fireSessionLoggedIn() {
      long timestamp = System.currentTimeMillis();
      SessionEvent sessionEvent = new SessionEvent(this, 7);
      sessionEvent.setTimestamp(timestamp);
      this.sessionEventSupport.fireSessionLoggedIn(sessionEvent);
   }

   protected void fireSessionLoggedOff() {
      long timestamp = System.currentTimeMillis();
      SessionEvent sessionEvent = new SessionEvent(this, 8);
      sessionEvent.setTimestamp(timestamp);
      this.sessionEventSupport.fireSessionLoggedOff(sessionEvent);
   }

   protected void fireSessionOpened() {
      long timestamp = System.currentTimeMillis();
      SessionEvent sessionEvent = new SessionEvent(this, 6);
      sessionEvent.setTimestamp(timestamp);
      this.sessionEventSupport.fireSessionOpened(sessionEvent);
   }

   protected void fireSessionOpening() {
      long timestamp = System.currentTimeMillis();
      SessionEvent sessionEvent = new SessionEvent(this, 5);
      sessionEvent.setTimestamp(timestamp);
      this.sessionEventSupport.fireSessionOpening(sessionEvent);
   }

   protected void fireSessionConnectionRefused() {
      long timestamp = System.currentTimeMillis();
      SessionEvent sessionEvent = new SessionEvent(this, 4);
      sessionEvent.setTimestamp(timestamp);
      this.sessionEventSupport.fireSessionConnectionRefused(sessionEvent);
   }

   protected void fireSessionError(Exception exception) {
      long timestamp = System.currentTimeMillis();
      SessionEvent sessionEvent = new SessionEvent(this, exception);
      sessionEvent.setTimestamp(timestamp);
      this.sessionEventSupport.fireSessionError(sessionEvent);
   }

   protected void fireTransferDebug(String message) {
      this.transferEventSupport.fireDebug(message);
   }

   protected void fireSessionDebug(String message) {
      this.sessionEventSupport.fireDebug(message);
   }

   public boolean hasTransferListener(TransferListener listener) {
      return this.transferEventSupport.hasTransferListener(listener);
   }

   public void addTransferListener(TransferListener listener) {
      this.transferEventSupport.addTransferListener(listener);
   }

   public void removeTransferListener(TransferListener listener) {
      this.transferEventSupport.removeTransferListener(listener);
   }

   public void addSessionListener(SessionListener listener) {
      this.sessionEventSupport.addSessionListener(listener);
   }

   public boolean hasSessionListener(SessionListener listener) {
      return this.sessionEventSupport.hasSessionListener(listener);
   }

   public void removeSessionListener(SessionListener listener) {
      this.sessionEventSupport.removeSessionListener(listener);
   }

   protected void fireTransferError(Resource resource, Exception e, int requestType) {
      TransferEvent transferEvent = new TransferEvent(this, resource, e, requestType);
      this.transferEventSupport.fireTransferError(transferEvent);
   }

   public SessionEventSupport getSessionEventSupport() {
      return this.sessionEventSupport;
   }

   public void setSessionEventSupport(SessionEventSupport sessionEventSupport) {
      this.sessionEventSupport = sessionEventSupport;
   }

   public TransferEventSupport getTransferEventSupport() {
      return this.transferEventSupport;
   }

   public void setTransferEventSupport(TransferEventSupport transferEventSupport) {
      this.transferEventSupport = transferEventSupport;
   }

   protected void postProcessListeners(Resource resource, File source, int requestType) throws TransferFailedException {
      byte[] buffer = new byte[4096];
      TransferEvent transferEvent = new TransferEvent(this, resource, 3, requestType);
      transferEvent.setTimestamp(System.currentTimeMillis());
      transferEvent.setLocalFile(source);
      FileInputStream input = null;

      try {
         input = new FileInputStream(source);

         while(true) {
            int n = input.read(buffer);
            if (n == -1) {
               return;
            }

            this.fireTransferProgress(transferEvent, buffer, n);
         }
      } catch (IOException var11) {
         this.fireTransferError(resource, var11, requestType);
         throw new TransferFailedException("Failed to post-process the source file", var11);
      } finally {
         IOUtil.close((InputStream)input);
      }
   }

   public void putDirectory(File sourceDirectory, String destinationDirectory) throws TransferFailedException, ResourceDoesNotExistException, AuthorizationException {
      throw new UnsupportedOperationException("The wagon you are using has not implemented putDirectory()");
   }

   public boolean supportsDirectoryCopy() {
      return false;
   }

   protected static String getPath(String basedir, String dir) {
      String path = basedir;
      if (!basedir.endsWith("/") && !dir.startsWith("/")) {
         path = basedir + "/";
      }

      path = path + dir;
      return path;
   }

   public boolean isInteractive() {
      return this.interactive;
   }

   public void setInteractive(boolean interactive) {
      this.interactive = interactive;
   }

   public List getFileList(String destinationDirectory) throws TransferFailedException, ResourceDoesNotExistException, AuthorizationException {
      throw new UnsupportedOperationException("The wagon you are using has not implemented getFileList()");
   }

   public boolean resourceExists(String resourceName) throws TransferFailedException, AuthorizationException {
      throw new UnsupportedOperationException("The wagon you are using has not implemented resourceExists()");
   }

   protected ProxyInfo getProxyInfo(String protocol, String host) {
      if (this.proxyInfoProvider != null) {
         ProxyInfo proxyInfo = this.proxyInfoProvider.getProxyInfo(protocol);
         if (!ProxyUtils.validateNonProxyHosts(proxyInfo, host)) {
            return proxyInfo;
         }
      }

      return null;
   }

   public RepositoryPermissions getPermissionsOverride() {
      return this.permissionsOverride;
   }

   public void setPermissionsOverride(RepositoryPermissions permissionsOverride) {
      this.permissionsOverride = permissionsOverride;
   }
}
