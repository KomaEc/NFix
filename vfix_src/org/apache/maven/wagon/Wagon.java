package org.apache.maven.wagon;

import java.io.File;
import java.util.List;
import org.apache.maven.wagon.authentication.AuthenticationException;
import org.apache.maven.wagon.authentication.AuthenticationInfo;
import org.apache.maven.wagon.authorization.AuthorizationException;
import org.apache.maven.wagon.events.SessionListener;
import org.apache.maven.wagon.events.TransferListener;
import org.apache.maven.wagon.proxy.ProxyInfo;
import org.apache.maven.wagon.proxy.ProxyInfoProvider;
import org.apache.maven.wagon.repository.Repository;

public interface Wagon {
   String ROLE = Wagon.class.getName();

   void get(String var1, File var2) throws TransferFailedException, ResourceDoesNotExistException, AuthorizationException;

   boolean getIfNewer(String var1, File var2, long var3) throws TransferFailedException, ResourceDoesNotExistException, AuthorizationException;

   void put(File var1, String var2) throws TransferFailedException, ResourceDoesNotExistException, AuthorizationException;

   void putDirectory(File var1, String var2) throws TransferFailedException, ResourceDoesNotExistException, AuthorizationException;

   boolean resourceExists(String var1) throws TransferFailedException, AuthorizationException;

   List getFileList(String var1) throws TransferFailedException, ResourceDoesNotExistException, AuthorizationException;

   boolean supportsDirectoryCopy();

   Repository getRepository();

   void connect(Repository var1) throws ConnectionException, AuthenticationException;

   void connect(Repository var1, ProxyInfo var2) throws ConnectionException, AuthenticationException;

   void connect(Repository var1, ProxyInfoProvider var2) throws ConnectionException, AuthenticationException;

   void connect(Repository var1, AuthenticationInfo var2) throws ConnectionException, AuthenticationException;

   void connect(Repository var1, AuthenticationInfo var2, ProxyInfo var3) throws ConnectionException, AuthenticationException;

   void connect(Repository var1, AuthenticationInfo var2, ProxyInfoProvider var3) throws ConnectionException, AuthenticationException;

   /** @deprecated */
   void openConnection() throws ConnectionException, AuthenticationException;

   void disconnect() throws ConnectionException;

   void setTimeout(int var1);

   int getTimeout();

   void addSessionListener(SessionListener var1);

   void removeSessionListener(SessionListener var1);

   boolean hasSessionListener(SessionListener var1);

   void addTransferListener(TransferListener var1);

   void removeTransferListener(TransferListener var1);

   boolean hasTransferListener(TransferListener var1);

   boolean isInteractive();

   void setInteractive(boolean var1);
}
