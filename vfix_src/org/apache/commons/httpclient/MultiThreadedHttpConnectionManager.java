package org.apache.commons.httpclient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.WeakHashMap;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MultiThreadedHttpConnectionManager implements HttpConnectionManager {
   private static final Log LOG;
   public static final int DEFAULT_MAX_HOST_CONNECTIONS = 2;
   public static final int DEFAULT_MAX_TOTAL_CONNECTIONS = 20;
   private static final Map REFERENCE_TO_CONNECTION_SOURCE;
   private static final ReferenceQueue REFERENCE_QUEUE;
   private static MultiThreadedHttpConnectionManager.ReferenceQueueThread REFERENCE_QUEUE_THREAD;
   private static WeakHashMap ALL_CONNECTION_MANAGERS;
   private int maxHostConnections = 2;
   private int maxTotalConnections = 20;
   private boolean connectionStaleCheckingEnabled = true;
   private boolean shutdown = false;
   private MultiThreadedHttpConnectionManager.ConnectionPool connectionPool = new MultiThreadedHttpConnectionManager.ConnectionPool();
   // $FF: synthetic field
   static Class class$org$apache$commons$httpclient$MultiThreadedHttpConnectionManager;

   public static void shutdownAll() {
      Map var0 = REFERENCE_TO_CONNECTION_SOURCE;
      synchronized(var0) {
         WeakHashMap var1 = ALL_CONNECTION_MANAGERS;
         synchronized(var1) {
            Iterator connIter = ALL_CONNECTION_MANAGERS.keySet().iterator();

            while(true) {
               if (!connIter.hasNext()) {
                  break;
               }

               MultiThreadedHttpConnectionManager connManager = (MultiThreadedHttpConnectionManager)connIter.next();
               connIter.remove();
               connManager.shutdown();
            }
         }

         if (REFERENCE_QUEUE_THREAD != null) {
            REFERENCE_QUEUE_THREAD.shutdown();
            REFERENCE_QUEUE_THREAD = null;
         }

         REFERENCE_TO_CONNECTION_SOURCE.clear();
      }
   }

   private static void storeReferenceToConnection(MultiThreadedHttpConnectionManager.HttpConnectionWithReference connection, HostConfiguration hostConfiguration, MultiThreadedHttpConnectionManager.ConnectionPool connectionPool) {
      MultiThreadedHttpConnectionManager.ConnectionSource source = new MultiThreadedHttpConnectionManager.ConnectionSource();
      source.connectionPool = connectionPool;
      source.hostConfiguration = hostConfiguration;
      Map var4 = REFERENCE_TO_CONNECTION_SOURCE;
      synchronized(var4) {
         if (REFERENCE_QUEUE_THREAD == null) {
            REFERENCE_QUEUE_THREAD = new MultiThreadedHttpConnectionManager.ReferenceQueueThread();
            REFERENCE_QUEUE_THREAD.start();
         }

         REFERENCE_TO_CONNECTION_SOURCE.put(connection.reference, source);
      }
   }

   private static void shutdownCheckedOutConnections(MultiThreadedHttpConnectionManager.ConnectionPool connectionPool) {
      ArrayList connectionsToClose = new ArrayList();
      Map var2 = REFERENCE_TO_CONNECTION_SOURCE;
      Iterator i;
      synchronized(var2) {
         i = REFERENCE_TO_CONNECTION_SOURCE.keySet().iterator();

         while(true) {
            if (!i.hasNext()) {
               break;
            }

            Reference ref = (Reference)i.next();
            MultiThreadedHttpConnectionManager.ConnectionSource source = (MultiThreadedHttpConnectionManager.ConnectionSource)REFERENCE_TO_CONNECTION_SOURCE.get(ref);
            if (source.connectionPool == connectionPool) {
               i.remove();
               HttpConnection connection = (HttpConnection)ref.get();
               if (connection != null) {
                  connectionsToClose.add(connection);
               }
            }
         }
      }

      i = connectionsToClose.iterator();

      while(i.hasNext()) {
         HttpConnection connection = (HttpConnection)i.next();
         connection.close();
         connection.setHttpConnectionManager((HttpConnectionManager)null);
         connection.releaseConnection();
      }

   }

   private static void removeReferenceToConnection(MultiThreadedHttpConnectionManager.HttpConnectionWithReference connection) {
      Map var1 = REFERENCE_TO_CONNECTION_SOURCE;
      synchronized(var1) {
         REFERENCE_TO_CONNECTION_SOURCE.remove(connection.reference);
      }
   }

   public MultiThreadedHttpConnectionManager() {
      WeakHashMap var1 = ALL_CONNECTION_MANAGERS;
      synchronized(var1) {
         ALL_CONNECTION_MANAGERS.put(this, (Object)null);
      }
   }

   public synchronized void shutdown() {
      MultiThreadedHttpConnectionManager.ConnectionPool var1 = this.connectionPool;
      synchronized(var1) {
         if (!this.shutdown) {
            this.shutdown = true;
            this.connectionPool.shutdown();
         }

      }
   }

   public boolean isConnectionStaleCheckingEnabled() {
      return this.connectionStaleCheckingEnabled;
   }

   public void setConnectionStaleCheckingEnabled(boolean connectionStaleCheckingEnabled) {
      this.connectionStaleCheckingEnabled = connectionStaleCheckingEnabled;
   }

   public void setMaxConnectionsPerHost(int maxHostConnections) {
      this.maxHostConnections = maxHostConnections;
   }

   public int getMaxConnectionsPerHost() {
      return this.maxHostConnections;
   }

   public void setMaxTotalConnections(int maxTotalConnections) {
      this.maxTotalConnections = maxTotalConnections;
   }

   public int getMaxTotalConnections() {
      return this.maxTotalConnections;
   }

   public HttpConnection getConnection(HostConfiguration hostConfiguration) {
      while(true) {
         try {
            return this.getConnection(hostConfiguration, 0L);
         } catch (HttpException var3) {
            LOG.debug("Unexpected exception while waiting for connection", var3);
         }
      }
   }

   public HttpConnection getConnection(HostConfiguration hostConfiguration, long timeout) throws HttpException {
      LOG.trace("enter HttpConnectionManager.getConnection(HostConfiguration, long)");
      if (hostConfiguration == null) {
         throw new IllegalArgumentException("hostConfiguration is null");
      } else {
         if (LOG.isDebugEnabled()) {
            LOG.debug("HttpConnectionManager.getConnection:  config = " + hostConfiguration + ", timeout = " + timeout);
         }

         HttpConnection conn = this.doGetConnection(hostConfiguration, timeout);
         return new MultiThreadedHttpConnectionManager.HttpConnectionAdapter(conn);
      }
   }

   private HttpConnection doGetConnection(HostConfiguration hostConfiguration, long timeout) throws HttpException {
      HttpConnection connection = null;
      MultiThreadedHttpConnectionManager.ConnectionPool var5 = this.connectionPool;
      synchronized(var5) {
         hostConfiguration = new HostConfiguration(hostConfiguration);
         MultiThreadedHttpConnectionManager.HostConnectionPool hostPool = this.connectionPool.getHostPool(hostConfiguration);
         MultiThreadedHttpConnectionManager.WaitingThread waitingThread = null;
         boolean useTimeout = timeout > 0L;
         long timeToWait = timeout;
         long startWait = 0L;
         long endWait = 0L;

         while(connection == null) {
            if (this.shutdown) {
               throw new IllegalStateException("Connection factory has been shutdown.");
            }

            if (hostPool.freeConnections.size() > 0) {
               connection = this.connectionPool.getFreeConnection(hostConfiguration);
            } else if (hostPool.numConnections < this.maxHostConnections && this.connectionPool.numConnections < this.maxTotalConnections) {
               connection = this.connectionPool.createConnection(hostConfiguration);
            } else if (hostPool.numConnections < this.maxHostConnections && this.connectionPool.freeConnections.size() > 0) {
               this.connectionPool.deleteLeastUsedConnection();
               connection = this.connectionPool.createConnection(hostConfiguration);
            } else {
               try {
                  if (useTimeout && timeToWait <= 0L) {
                     throw new HttpException("Timeout waiting for connection");
                  }

                  if (LOG.isDebugEnabled()) {
                     LOG.debug("Unable to get a connection, waiting..., hostConfig=" + hostConfiguration);
                  }

                  if (waitingThread == null) {
                     waitingThread = new MultiThreadedHttpConnectionManager.WaitingThread();
                     waitingThread.hostConnectionPool = hostPool;
                     waitingThread.thread = Thread.currentThread();
                  }

                  if (useTimeout) {
                     startWait = System.currentTimeMillis();
                  }

                  hostPool.waitingThreads.addLast(waitingThread);
                  this.connectionPool.waitingThreads.addLast(waitingThread);
                  this.connectionPool.wait(timeToWait);
                  hostPool.waitingThreads.remove(waitingThread);
                  this.connectionPool.waitingThreads.remove(waitingThread);
               } catch (InterruptedException var22) {
               } finally {
                  if (useTimeout) {
                     endWait = System.currentTimeMillis();
                     timeToWait -= endWait - startWait;
                  }

               }
            }
         }

         return connection;
      }
   }

   public int getConnectionsInUse(HostConfiguration hostConfiguration) {
      MultiThreadedHttpConnectionManager.ConnectionPool var2 = this.connectionPool;
      synchronized(var2) {
         MultiThreadedHttpConnectionManager.HostConnectionPool hostPool = this.connectionPool.getHostPool(hostConfiguration);
         int var4 = hostPool.numConnections;
         return var4;
      }
   }

   public int getConnectionsInUse() {
      MultiThreadedHttpConnectionManager.ConnectionPool var1 = this.connectionPool;
      synchronized(var1) {
         int var2 = this.connectionPool.numConnections;
         return var2;
      }
   }

   public void releaseConnection(HttpConnection conn) {
      LOG.trace("enter HttpConnectionManager.releaseConnection(HttpConnection)");
      if (conn instanceof MultiThreadedHttpConnectionManager.HttpConnectionAdapter) {
         conn = ((MultiThreadedHttpConnectionManager.HttpConnectionAdapter)conn).getWrappedConnection();
      }

      SimpleHttpConnectionManager.finishLastResponse(conn);
      this.connectionPool.freeConnection(conn);
   }

   private HostConfiguration configurationForConnection(HttpConnection conn) {
      HostConfiguration connectionConfiguration = new HostConfiguration();
      connectionConfiguration.setHost(conn.getHost(), conn.getVirtualHost(), conn.getPort(), conn.getProtocol());
      if (conn.getLocalAddress() != null) {
         connectionConfiguration.setLocalAddress(conn.getLocalAddress());
      }

      if (conn.getProxyHost() != null) {
         connectionConfiguration.setProxy(conn.getProxyHost(), conn.getProxyPort());
      }

      return connectionConfiguration;
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   static {
      LOG = LogFactory.getLog(class$org$apache$commons$httpclient$MultiThreadedHttpConnectionManager == null ? (class$org$apache$commons$httpclient$MultiThreadedHttpConnectionManager = class$("org.apache.commons.httpclient.MultiThreadedHttpConnectionManager")) : class$org$apache$commons$httpclient$MultiThreadedHttpConnectionManager);
      REFERENCE_TO_CONNECTION_SOURCE = new HashMap();
      REFERENCE_QUEUE = new ReferenceQueue();
      ALL_CONNECTION_MANAGERS = new WeakHashMap();
   }

   private static class HttpConnectionAdapter extends HttpConnection {
      private HttpConnection wrappedConnection;

      public HttpConnectionAdapter(HttpConnection connection) {
         super(connection.getHost(), connection.getPort(), connection.getProtocol());
         this.wrappedConnection = connection;
      }

      protected boolean hasConnection() {
         return this.wrappedConnection != null;
      }

      HttpConnection getWrappedConnection() {
         return this.wrappedConnection;
      }

      public void close() {
         if (this.hasConnection()) {
            this.wrappedConnection.close();
         }

      }

      public InetAddress getLocalAddress() {
         return this.hasConnection() ? this.wrappedConnection.getLocalAddress() : null;
      }

      public boolean isStaleCheckingEnabled() {
         return this.hasConnection() ? this.wrappedConnection.isStaleCheckingEnabled() : false;
      }

      public void setLocalAddress(InetAddress localAddress) {
         if (this.hasConnection()) {
            this.wrappedConnection.setLocalAddress(localAddress);
         } else {
            throw new IllegalStateException("Connection has been released");
         }
      }

      public void setStaleCheckingEnabled(boolean staleCheckEnabled) {
         if (this.hasConnection()) {
            this.wrappedConnection.setStaleCheckingEnabled(staleCheckEnabled);
         } else {
            throw new IllegalStateException("Connection has been released");
         }
      }

      public String getHost() {
         return this.hasConnection() ? this.wrappedConnection.getHost() : null;
      }

      public HttpConnectionManager getHttpConnectionManager() {
         return this.hasConnection() ? this.wrappedConnection.getHttpConnectionManager() : null;
      }

      public InputStream getLastResponseInputStream() {
         return this.hasConnection() ? this.wrappedConnection.getLastResponseInputStream() : null;
      }

      public int getPort() {
         return this.hasConnection() ? this.wrappedConnection.getPort() : -1;
      }

      public Protocol getProtocol() {
         return this.hasConnection() ? this.wrappedConnection.getProtocol() : null;
      }

      public String getProxyHost() {
         return this.hasConnection() ? this.wrappedConnection.getProxyHost() : null;
      }

      public int getProxyPort() {
         return this.hasConnection() ? this.wrappedConnection.getProxyPort() : -1;
      }

      public OutputStream getRequestOutputStream() throws IOException, IllegalStateException {
         return this.hasConnection() ? this.wrappedConnection.getRequestOutputStream() : null;
      }

      public OutputStream getRequestOutputStream(boolean useChunking) throws IOException, IllegalStateException {
         return this.hasConnection() ? this.wrappedConnection.getRequestOutputStream(useChunking) : null;
      }

      public InputStream getResponseInputStream() throws IOException, IllegalStateException {
         return this.hasConnection() ? this.wrappedConnection.getResponseInputStream() : null;
      }

      public InputStream getResponseInputStream(HttpMethod method) throws IOException, IllegalStateException {
         return this.hasConnection() ? this.wrappedConnection.getResponseInputStream(method) : null;
      }

      public boolean isOpen() {
         return this.hasConnection() ? this.wrappedConnection.isOpen() : false;
      }

      public boolean isProxied() {
         return this.hasConnection() ? this.wrappedConnection.isProxied() : false;
      }

      public boolean isResponseAvailable() throws IOException {
         return this.hasConnection() ? this.wrappedConnection.isResponseAvailable() : false;
      }

      public boolean isResponseAvailable(int timeout) throws IOException {
         return this.hasConnection() ? this.wrappedConnection.isResponseAvailable(timeout) : false;
      }

      public boolean isSecure() {
         return this.hasConnection() ? this.wrappedConnection.isSecure() : false;
      }

      public boolean isTransparent() {
         return this.hasConnection() ? this.wrappedConnection.isTransparent() : false;
      }

      public void open() throws IOException {
         if (this.hasConnection()) {
            this.wrappedConnection.open();
         } else {
            throw new IllegalStateException("Connection has been released");
         }
      }

      public void print(String data) throws IOException, IllegalStateException, HttpRecoverableException {
         if (this.hasConnection()) {
            this.wrappedConnection.print(data);
         } else {
            throw new IllegalStateException("Connection has been released");
         }
      }

      public void printLine() throws IOException, IllegalStateException, HttpRecoverableException {
         if (this.hasConnection()) {
            this.wrappedConnection.printLine();
         } else {
            throw new IllegalStateException("Connection has been released");
         }
      }

      public void printLine(String data) throws IOException, IllegalStateException, HttpRecoverableException {
         if (this.hasConnection()) {
            this.wrappedConnection.printLine(data);
         } else {
            throw new IllegalStateException("Connection has been released");
         }
      }

      public String readLine() throws IOException, IllegalStateException {
         if (this.hasConnection()) {
            return this.wrappedConnection.readLine();
         } else {
            throw new IllegalStateException("Connection has been released");
         }
      }

      public void releaseConnection() {
         if (this.hasConnection()) {
            HttpConnection wrappedConnection = this.wrappedConnection;
            this.wrappedConnection = null;
            wrappedConnection.releaseConnection();
         }

      }

      public void setConnectionTimeout(int timeout) {
         if (this.hasConnection()) {
            this.wrappedConnection.setConnectionTimeout(timeout);
         }

      }

      public void setHost(String host) throws IllegalStateException {
         if (this.hasConnection()) {
            this.wrappedConnection.setHost(host);
         }

      }

      public void setHttpConnectionManager(HttpConnectionManager httpConnectionManager) {
         if (this.hasConnection()) {
            this.wrappedConnection.setHttpConnectionManager(httpConnectionManager);
         }

      }

      public void setLastResponseInputStream(InputStream inStream) {
         if (this.hasConnection()) {
            this.wrappedConnection.setLastResponseInputStream(inStream);
         }

      }

      public void setPort(int port) throws IllegalStateException {
         if (this.hasConnection()) {
            this.wrappedConnection.setPort(port);
         }

      }

      public void setProtocol(Protocol protocol) {
         if (this.hasConnection()) {
            this.wrappedConnection.setProtocol(protocol);
         }

      }

      public void setProxyHost(String host) throws IllegalStateException {
         if (this.hasConnection()) {
            this.wrappedConnection.setProxyHost(host);
         }

      }

      public void setProxyPort(int port) throws IllegalStateException {
         if (this.hasConnection()) {
            this.wrappedConnection.setProxyPort(port);
         }

      }

      public void setSecure(boolean secure) throws IllegalStateException {
         if (this.hasConnection()) {
            this.wrappedConnection.setSecure(secure);
         }

      }

      public void setSoTimeout(int timeout) throws SocketException, IllegalStateException {
         if (this.hasConnection()) {
            this.wrappedConnection.setSoTimeout(timeout);
         }

      }

      public void shutdownOutput() {
         if (this.hasConnection()) {
            this.wrappedConnection.shutdownOutput();
         }

      }

      public void tunnelCreated() throws IllegalStateException, IOException {
         if (this.hasConnection()) {
            this.wrappedConnection.tunnelCreated();
         }

      }

      public void write(byte[] data, int offset, int length) throws IOException, IllegalStateException, HttpRecoverableException {
         if (this.hasConnection()) {
            this.wrappedConnection.write(data, offset, length);
         } else {
            throw new IllegalStateException("Connection has been released");
         }
      }

      public void write(byte[] data) throws IOException, IllegalStateException, HttpRecoverableException {
         if (this.hasConnection()) {
            this.wrappedConnection.write(data);
         } else {
            throw new IllegalStateException("Connection has been released");
         }
      }

      public void writeLine() throws IOException, IllegalStateException, HttpRecoverableException {
         if (this.hasConnection()) {
            this.wrappedConnection.writeLine();
         } else {
            throw new IllegalStateException("Connection has been released");
         }
      }

      public void writeLine(byte[] data) throws IOException, IllegalStateException, HttpRecoverableException {
         if (this.hasConnection()) {
            this.wrappedConnection.writeLine(data);
         } else {
            throw new IllegalStateException("Connection has been released");
         }
      }

      public void flushRequestOutputStream() throws IOException {
         if (this.hasConnection()) {
            this.wrappedConnection.flushRequestOutputStream();
         } else {
            throw new IllegalStateException("Connection has been released");
         }
      }

      public int getSoTimeout() throws SocketException {
         if (this.hasConnection()) {
            return this.wrappedConnection.getSoTimeout();
         } else {
            throw new IllegalStateException("Connection has been released");
         }
      }

      public String getVirtualHost() {
         if (this.hasConnection()) {
            return this.wrappedConnection.getVirtualHost();
         } else {
            throw new IllegalStateException("Connection has been released");
         }
      }

      public void setVirtualHost(String host) throws IllegalStateException {
         if (this.hasConnection()) {
            this.wrappedConnection.setVirtualHost(host);
         } else {
            throw new IllegalStateException("Connection has been released");
         }
      }

      public int getSendBufferSize() throws SocketException {
         if (this.hasConnection()) {
            return this.wrappedConnection.getSendBufferSize();
         } else {
            throw new IllegalStateException("Connection has been released");
         }
      }

      public void setSendBufferSize(int sendBufferSize) throws SocketException {
         if (this.hasConnection()) {
            this.wrappedConnection.setSendBufferSize(sendBufferSize);
         } else {
            throw new IllegalStateException("Connection has been released");
         }
      }
   }

   private static class HttpConnectionWithReference extends HttpConnection {
      public WeakReference reference;

      public HttpConnectionWithReference(HostConfiguration hostConfiguration) {
         super(hostConfiguration);
         this.reference = new WeakReference(this, MultiThreadedHttpConnectionManager.REFERENCE_QUEUE);
      }
   }

   private static class ReferenceQueueThread extends Thread {
      private boolean shutdown = false;

      public ReferenceQueueThread() {
         this.setDaemon(true);
         this.setName("MultiThreadedHttpConnectionManager cleanup");
      }

      public void shutdown() {
         this.shutdown = true;
      }

      private void handleReference(Reference ref) {
         MultiThreadedHttpConnectionManager.ConnectionSource source = null;
         Map var3 = MultiThreadedHttpConnectionManager.REFERENCE_TO_CONNECTION_SOURCE;
         synchronized(var3) {
            source = (MultiThreadedHttpConnectionManager.ConnectionSource)MultiThreadedHttpConnectionManager.REFERENCE_TO_CONNECTION_SOURCE.remove(ref);
         }

         if (source != null) {
            if (MultiThreadedHttpConnectionManager.LOG.isDebugEnabled()) {
               MultiThreadedHttpConnectionManager.LOG.debug("Connection reclaimed by garbage collector, hostConfig=" + source.hostConfiguration);
            }

            source.connectionPool.handleLostConnection(source.hostConfiguration);
         }

      }

      public void run() {
         while(!this.shutdown) {
            try {
               Reference ref = MultiThreadedHttpConnectionManager.REFERENCE_QUEUE.remove(1000L);
               if (ref != null) {
                  this.handleReference(ref);
               }
            } catch (InterruptedException var2) {
               MultiThreadedHttpConnectionManager.LOG.debug("ReferenceQueueThread interrupted", var2);
            }
         }

      }
   }

   private static class WaitingThread {
      public Thread thread;
      public MultiThreadedHttpConnectionManager.HostConnectionPool hostConnectionPool;

      private WaitingThread() {
      }

      // $FF: synthetic method
      WaitingThread(Object x0) {
         this();
      }
   }

   private static class HostConnectionPool {
      public HostConfiguration hostConfiguration;
      public LinkedList freeConnections;
      public LinkedList waitingThreads;
      public int numConnections;

      private HostConnectionPool() {
         this.freeConnections = new LinkedList();
         this.waitingThreads = new LinkedList();
         this.numConnections = 0;
      }

      // $FF: synthetic method
      HostConnectionPool(Object x0) {
         this();
      }
   }

   private static class ConnectionSource {
      public MultiThreadedHttpConnectionManager.ConnectionPool connectionPool;
      public HostConfiguration hostConfiguration;

      private ConnectionSource() {
      }

      // $FF: synthetic method
      ConnectionSource(Object x0) {
         this();
      }
   }

   private class ConnectionPool {
      private LinkedList freeConnections;
      private LinkedList waitingThreads;
      private final Map mapHosts;
      private int numConnections;

      private ConnectionPool() {
         this.freeConnections = new LinkedList();
         this.waitingThreads = new LinkedList();
         this.mapHosts = new HashMap();
         this.numConnections = 0;
      }

      public synchronized void shutdown() {
         Iterator iter = this.freeConnections.iterator();

         while(iter.hasNext()) {
            HttpConnection conn = (HttpConnection)iter.next();
            iter.remove();
            conn.close();
         }

         MultiThreadedHttpConnectionManager.shutdownCheckedOutConnections(this);
         iter = this.waitingThreads.iterator();

         while(iter.hasNext()) {
            MultiThreadedHttpConnectionManager.WaitingThread waiter = (MultiThreadedHttpConnectionManager.WaitingThread)iter.next();
            iter.remove();
            waiter.thread.interrupt();
         }

         this.mapHosts.clear();
      }

      public synchronized HttpConnection createConnection(HostConfiguration hostConfiguration) {
         MultiThreadedHttpConnectionManager.HttpConnectionWithReference connection = null;
         MultiThreadedHttpConnectionManager.HostConnectionPool hostPool = this.getHostPool(hostConfiguration);
         if (hostPool.numConnections < MultiThreadedHttpConnectionManager.this.getMaxConnectionsPerHost() && this.numConnections < MultiThreadedHttpConnectionManager.this.getMaxTotalConnections()) {
            if (MultiThreadedHttpConnectionManager.LOG.isDebugEnabled()) {
               MultiThreadedHttpConnectionManager.LOG.debug("Allocating new connection, hostConfig=" + hostConfiguration);
            }

            connection = new MultiThreadedHttpConnectionManager.HttpConnectionWithReference(hostConfiguration);
            connection.setStaleCheckingEnabled(MultiThreadedHttpConnectionManager.this.connectionStaleCheckingEnabled);
            connection.setHttpConnectionManager(MultiThreadedHttpConnectionManager.this);
            ++this.numConnections;
            ++hostPool.numConnections;
            MultiThreadedHttpConnectionManager.storeReferenceToConnection(connection, hostConfiguration, this);
         } else if (MultiThreadedHttpConnectionManager.LOG.isDebugEnabled()) {
            if (hostPool.numConnections >= MultiThreadedHttpConnectionManager.this.getMaxConnectionsPerHost()) {
               MultiThreadedHttpConnectionManager.LOG.debug("No connection allocated, host pool has already reached maxConnectionsPerHost, hostConfig=" + hostConfiguration + ", maxConnectionsPerhost=" + MultiThreadedHttpConnectionManager.this.getMaxConnectionsPerHost());
            } else {
               MultiThreadedHttpConnectionManager.LOG.debug("No connection allocated, maxTotalConnections reached, maxTotalConnections=" + MultiThreadedHttpConnectionManager.this.getMaxTotalConnections());
            }
         }

         return connection;
      }

      public synchronized void handleLostConnection(HostConfiguration config) {
         MultiThreadedHttpConnectionManager.HostConnectionPool hostPool = this.getHostPool(config);
         --hostPool.numConnections;
         --this.numConnections;
         this.notifyWaitingThread(config);
      }

      public synchronized MultiThreadedHttpConnectionManager.HostConnectionPool getHostPool(HostConfiguration hostConfiguration) {
         MultiThreadedHttpConnectionManager.LOG.trace("enter HttpConnectionManager.ConnectionPool.getHostPool(HostConfiguration)");
         MultiThreadedHttpConnectionManager.HostConnectionPool listConnections = (MultiThreadedHttpConnectionManager.HostConnectionPool)this.mapHosts.get(hostConfiguration);
         if (listConnections == null) {
            listConnections = new MultiThreadedHttpConnectionManager.HostConnectionPool();
            listConnections.hostConfiguration = hostConfiguration;
            this.mapHosts.put(hostConfiguration, listConnections);
         }

         return listConnections;
      }

      public synchronized HttpConnection getFreeConnection(HostConfiguration hostConfiguration) {
         MultiThreadedHttpConnectionManager.HttpConnectionWithReference connection = null;
         MultiThreadedHttpConnectionManager.HostConnectionPool hostPool = this.getHostPool(hostConfiguration);
         if (hostPool.freeConnections.size() > 0) {
            connection = (MultiThreadedHttpConnectionManager.HttpConnectionWithReference)hostPool.freeConnections.removeFirst();
            this.freeConnections.remove(connection);
            MultiThreadedHttpConnectionManager.storeReferenceToConnection(connection, hostConfiguration, this);
            if (MultiThreadedHttpConnectionManager.LOG.isDebugEnabled()) {
               MultiThreadedHttpConnectionManager.LOG.debug("Getting free connection, hostConfig=" + hostConfiguration);
            }
         } else if (MultiThreadedHttpConnectionManager.LOG.isDebugEnabled()) {
            MultiThreadedHttpConnectionManager.LOG.debug("There were no free connections to get, hostConfig=" + hostConfiguration);
         }

         return connection;
      }

      public synchronized void deleteLeastUsedConnection() {
         HttpConnection connection = (HttpConnection)this.freeConnections.removeFirst();
         if (connection != null) {
            HostConfiguration connectionConfiguration = MultiThreadedHttpConnectionManager.this.configurationForConnection(connection);
            if (MultiThreadedHttpConnectionManager.LOG.isDebugEnabled()) {
               MultiThreadedHttpConnectionManager.LOG.debug("Reclaiming unused connection, hostConfig=" + connectionConfiguration);
            }

            connection.close();
            MultiThreadedHttpConnectionManager.HostConnectionPool hostPool = this.getHostPool(connectionConfiguration);
            hostPool.freeConnections.remove(connection);
            --hostPool.numConnections;
            --this.numConnections;
         } else if (MultiThreadedHttpConnectionManager.LOG.isDebugEnabled()) {
            MultiThreadedHttpConnectionManager.LOG.debug("Attempted to reclaim an unused connection but there were none.");
         }

      }

      public synchronized void notifyWaitingThread(HostConfiguration configuration) {
         this.notifyWaitingThread(this.getHostPool(configuration));
      }

      public synchronized void notifyWaitingThread(MultiThreadedHttpConnectionManager.HostConnectionPool hostPool) {
         MultiThreadedHttpConnectionManager.WaitingThread waitingThread = null;
         if (hostPool.waitingThreads.size() > 0) {
            if (MultiThreadedHttpConnectionManager.LOG.isDebugEnabled()) {
               MultiThreadedHttpConnectionManager.LOG.debug("Notifying thread waiting on host pool, hostConfig=" + hostPool.hostConfiguration);
            }

            waitingThread = (MultiThreadedHttpConnectionManager.WaitingThread)hostPool.waitingThreads.removeFirst();
            this.waitingThreads.remove(waitingThread);
         } else if (this.waitingThreads.size() > 0) {
            if (MultiThreadedHttpConnectionManager.LOG.isDebugEnabled()) {
               MultiThreadedHttpConnectionManager.LOG.debug("No-one waiting on host pool, notifying next waiting thread.");
            }

            waitingThread = (MultiThreadedHttpConnectionManager.WaitingThread)this.waitingThreads.removeFirst();
            waitingThread.hostConnectionPool.waitingThreads.remove(waitingThread);
         } else if (MultiThreadedHttpConnectionManager.LOG.isDebugEnabled()) {
            MultiThreadedHttpConnectionManager.LOG.debug("Notifying no-one, there are no waiting threads");
         }

         if (waitingThread != null) {
            waitingThread.thread.interrupt();
         }

      }

      public void freeConnection(HttpConnection conn) {
         HostConfiguration connectionConfiguration = MultiThreadedHttpConnectionManager.this.configurationForConnection(conn);
         if (MultiThreadedHttpConnectionManager.LOG.isDebugEnabled()) {
            MultiThreadedHttpConnectionManager.LOG.debug("Freeing connection, hostConfig=" + connectionConfiguration);
         }

         synchronized(this) {
            if (MultiThreadedHttpConnectionManager.this.shutdown) {
               conn.close();
            } else {
               MultiThreadedHttpConnectionManager.HostConnectionPool hostPool = this.getHostPool(connectionConfiguration);
               hostPool.freeConnections.add(conn);
               if (hostPool.numConnections == 0) {
                  MultiThreadedHttpConnectionManager.LOG.error("Host connection pool not found, hostConfig=" + connectionConfiguration);
                  hostPool.numConnections = 1;
               }

               this.freeConnections.add(conn);
               MultiThreadedHttpConnectionManager.removeReferenceToConnection((MultiThreadedHttpConnectionManager.HttpConnectionWithReference)conn);
               if (this.numConnections == 0) {
                  MultiThreadedHttpConnectionManager.LOG.error("Host connection pool not found, hostConfig=" + connectionConfiguration);
                  this.numConnections = 1;
               }

               this.notifyWaitingThread(hostPool);
            }
         }
      }

      // $FF: synthetic method
      ConnectionPool(Object x1) {
         this();
      }
   }
}
