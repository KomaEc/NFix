package org.apache.commons.httpclient;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import org.apache.commons.httpclient.protocol.DefaultProtocolSocketFactory;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.commons.httpclient.protocol.SecureProtocolSocketFactory;
import org.apache.commons.httpclient.util.TimeoutController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HttpConnection {
   private static final byte[] CRLF = new byte[]{13, 10};
   private static final Log LOG;
   private boolean used;
   private String hostName;
   private String virtualName;
   private int portNumber;
   private String proxyHostName;
   private int proxyPortNumber;
   private Socket socket;
   private InputStream inputStream;
   private OutputStream outputStream;
   private int sendBufferSize;
   private InputStream lastResponseInputStream;
   protected boolean isOpen;
   private Protocol protocolInUse;
   private int soTimeout;
   private boolean soNodelay;
   private boolean usingSecureSocket;
   private boolean tunnelEstablished;
   private boolean staleCheckingEnabled;
   private int connectTimeout;
   private HttpConnectionManager httpConnectionManager;
   private InetAddress localAddress;
   // $FF: synthetic field
   static Class class$org$apache$commons$httpclient$HttpConnection;

   public HttpConnection(String host, int port) {
      this((String)null, -1, host, port, false);
   }

   /** @deprecated */
   public HttpConnection(String host, int port, boolean secure) {
      this((String)null, -1, host, port, secure);
   }

   public HttpConnection(String host, int port, Protocol protocol) {
      this((String)null, -1, host, (String)null, port, protocol);
   }

   public HttpConnection(String host, String virtualHost, int port, Protocol protocol) {
      this((String)null, -1, host, virtualHost, port, protocol);
   }

   public HttpConnection(String proxyHost, int proxyPort, String host, int port) {
      this(proxyHost, proxyPort, host, port, false);
   }

   /** @deprecated */
   public HttpConnection(String proxyHost, int proxyPort, String host, int port, boolean secure) {
      this(proxyHost, proxyPort, host, (String)null, port, Protocol.getProtocol(secure ? "https" : "http"));
   }

   public HttpConnection(HostConfiguration hostConfiguration) {
      this(hostConfiguration.getProxyHost(), hostConfiguration.getProxyPort(), hostConfiguration.getHost(), hostConfiguration.getVirtualHost(), hostConfiguration.getPort(), hostConfiguration.getProtocol());
      this.localAddress = hostConfiguration.getLocalAddress();
   }

   public HttpConnection(String proxyHost, int proxyPort, String host, String virtualHost, int port, Protocol protocol) {
      this.used = false;
      this.hostName = null;
      this.virtualName = null;
      this.portNumber = -1;
      this.proxyHostName = null;
      this.proxyPortNumber = -1;
      this.socket = null;
      this.inputStream = null;
      this.outputStream = null;
      this.sendBufferSize = -1;
      this.lastResponseInputStream = null;
      this.isOpen = false;
      this.soTimeout = 0;
      this.soNodelay = true;
      this.usingSecureSocket = false;
      this.tunnelEstablished = false;
      this.staleCheckingEnabled = true;
      this.connectTimeout = 0;
      if (host == null) {
         throw new IllegalArgumentException("host parameter is null");
      } else if (protocol == null) {
         throw new IllegalArgumentException("protocol is null");
      } else {
         this.proxyHostName = proxyHost;
         this.proxyPortNumber = proxyPort;
         this.hostName = host;
         this.virtualName = virtualHost;
         this.portNumber = protocol.resolvePort(port);
         this.protocolInUse = protocol;
      }
   }

   public String getHost() {
      return this.hostName;
   }

   public void setHost(String host) throws IllegalStateException {
      if (host == null) {
         throw new IllegalArgumentException("host parameter is null");
      } else {
         this.assertNotOpen();
         this.hostName = host;
      }
   }

   public String getVirtualHost() {
      return this.virtualName;
   }

   public void setVirtualHost(String host) throws IllegalStateException {
      this.assertNotOpen();
      this.virtualName = host;
   }

   public int getPort() {
      if (this.portNumber < 0) {
         return this.isSecure() ? 443 : 80;
      } else {
         return this.portNumber;
      }
   }

   public void setPort(int port) throws IllegalStateException {
      this.assertNotOpen();
      this.portNumber = port;
   }

   public String getProxyHost() {
      return this.proxyHostName;
   }

   public void setProxyHost(String host) throws IllegalStateException {
      this.assertNotOpen();
      this.proxyHostName = host;
   }

   public int getProxyPort() {
      return this.proxyPortNumber;
   }

   public void setProxyPort(int port) throws IllegalStateException {
      this.assertNotOpen();
      this.proxyPortNumber = port;
   }

   public boolean isSecure() {
      return this.protocolInUse.isSecure();
   }

   public Protocol getProtocol() {
      return this.protocolInUse;
   }

   /** @deprecated */
   public void setSecure(boolean secure) throws IllegalStateException {
      this.assertNotOpen();
      this.protocolInUse = secure ? Protocol.getProtocol("https") : Protocol.getProtocol("http");
   }

   public void setProtocol(Protocol protocol) {
      this.assertNotOpen();
      if (protocol == null) {
         throw new IllegalArgumentException("protocol is null");
      } else {
         this.protocolInUse = protocol;
      }
   }

   public InetAddress getLocalAddress() {
      return this.localAddress;
   }

   public void setLocalAddress(InetAddress localAddress) {
      this.assertNotOpen();
      this.localAddress = localAddress;
   }

   public boolean isOpen() {
      if (this.used && this.isOpen && this.isStaleCheckingEnabled() && this.isStale()) {
         LOG.debug("Connection is stale, closing...");
         this.close();
      }

      return this.isOpen;
   }

   public boolean isStaleCheckingEnabled() {
      return this.staleCheckingEnabled;
   }

   public void setStaleCheckingEnabled(boolean staleCheckEnabled) {
      this.staleCheckingEnabled = staleCheckEnabled;
   }

   protected boolean isStale() {
      boolean isStale = true;
      if (this.isOpen) {
         isStale = false;

         try {
            if (this.inputStream.available() == 0) {
               try {
                  this.socket.setSoTimeout(1);
                  this.inputStream.mark(1);
                  int byteRead = this.inputStream.read();
                  if (byteRead == -1) {
                     isStale = true;
                  } else {
                     this.inputStream.reset();
                  }
               } finally {
                  this.socket.setSoTimeout(this.soTimeout);
               }
            }
         } catch (InterruptedIOException var8) {
         } catch (IOException var9) {
            LOG.debug("An error occurred while reading from the socket, is appears to be stale", var9);
            isStale = true;
         }
      }

      return isStale;
   }

   public boolean isProxied() {
      return null != this.proxyHostName && 0 < this.proxyPortNumber;
   }

   public void setLastResponseInputStream(InputStream inStream) {
      this.lastResponseInputStream = inStream;
   }

   public InputStream getLastResponseInputStream() {
      return this.lastResponseInputStream;
   }

   public void setSoTimeout(int timeout) throws SocketException, IllegalStateException {
      LOG.debug("HttpConnection.setSoTimeout(" + timeout + ")");
      this.soTimeout = timeout;
      if (this.socket != null) {
         this.socket.setSoTimeout(timeout);
      }

   }

   public int getSoTimeout() throws SocketException {
      LOG.debug("HttpConnection.getSoTimeout()");
      return this.socket != null ? this.socket.getSoTimeout() : this.soTimeout;
   }

   public void setConnectionTimeout(int timeout) {
      this.connectTimeout = timeout;
   }

   public void open() throws IOException {
      LOG.trace("enter HttpConnection.open()");
      this.assertNotOpen();

      try {
         final int inbuffersize;
         if (null == this.socket) {
            final String host = null == this.proxyHostName ? this.hostName : this.proxyHostName;
            inbuffersize = null == this.proxyHostName ? this.portNumber : this.proxyPortNumber;
            this.usingSecureSocket = this.isSecure() && !this.isProxied();
            final ProtocolSocketFactory socketFactory = this.isSecure() && this.isProxied() ? new DefaultProtocolSocketFactory() : this.protocolInUse.getSocketFactory();
            if (this.connectTimeout == 0) {
               if (this.localAddress != null) {
                  this.socket = ((ProtocolSocketFactory)socketFactory).createSocket(host, inbuffersize, this.localAddress, 0);
               } else {
                  this.socket = ((ProtocolSocketFactory)socketFactory).createSocket(host, inbuffersize);
               }
            } else {
               HttpConnection.SocketTask task = new HttpConnection.SocketTask() {
                  public void doit() throws IOException {
                     if (HttpConnection.this.localAddress != null) {
                        this.setSocket(((ProtocolSocketFactory)socketFactory).createSocket(host, inbuffersize, HttpConnection.this.localAddress, 0));
                     } else {
                        this.setSocket(((ProtocolSocketFactory)socketFactory).createSocket(host, inbuffersize));
                     }

                  }
               };
               TimeoutController.execute((Runnable)task, (long)this.connectTimeout);
               this.socket = task.getSocket();
               if (task.exception != null) {
                  throw task.exception;
               }
            }
         }

         this.socket.setTcpNoDelay(this.soNodelay);
         this.socket.setSoTimeout(this.soTimeout);
         if (this.sendBufferSize != -1) {
            this.socket.setSendBufferSize(this.sendBufferSize);
         }

         int outbuffersize = this.socket.getSendBufferSize();
         if (outbuffersize > 2048) {
            outbuffersize = 2048;
         }

         inbuffersize = this.socket.getReceiveBufferSize();
         if (inbuffersize > 2048) {
            inbuffersize = 2048;
         }

         this.inputStream = new BufferedInputStream(this.socket.getInputStream(), inbuffersize);
         this.outputStream = new BufferedOutputStream(new HttpConnection.WrappedOutputStream(this.socket.getOutputStream()), outbuffersize);
         this.isOpen = true;
         this.used = false;
      } catch (IOException var5) {
         this.closeSocketAndStreams();
         throw var5;
      } catch (TimeoutController.TimeoutException var6) {
         if (LOG.isWarnEnabled()) {
            LOG.warn("The host " + this.hostName + ":" + this.portNumber + " (or proxy " + this.proxyHostName + ":" + this.proxyPortNumber + ") did not accept the connection within timeout of " + this.connectTimeout + " milliseconds");
         }

         throw new HttpConnection.ConnectionTimeoutException();
      }
   }

   public void tunnelCreated() throws IllegalStateException, IOException {
      LOG.trace("enter HttpConnection.tunnelCreated()");
      if (this.isSecure() && this.isProxied()) {
         if (this.usingSecureSocket) {
            throw new IllegalStateException("Already using a secure socket");
         } else {
            SecureProtocolSocketFactory socketFactory = (SecureProtocolSocketFactory)this.protocolInUse.getSocketFactory();
            this.socket = socketFactory.createSocket(this.socket, this.hostName, this.portNumber, true);
            if (this.sendBufferSize != -1) {
               this.socket.setSendBufferSize(this.sendBufferSize);
            }

            int outbuffersize = this.socket.getSendBufferSize();
            if (outbuffersize > 2048) {
               outbuffersize = 2048;
            }

            int inbuffersize = this.socket.getReceiveBufferSize();
            if (inbuffersize > 2048) {
               inbuffersize = 2048;
            }

            this.inputStream = new BufferedInputStream(this.socket.getInputStream(), inbuffersize);
            this.outputStream = new BufferedOutputStream(new HttpConnection.WrappedOutputStream(this.socket.getOutputStream()), outbuffersize);
            this.usingSecureSocket = true;
            this.tunnelEstablished = true;
            LOG.debug("Secure tunnel created");
         }
      } else {
         throw new IllegalStateException("Connection must be secure and proxied to use this feature");
      }
   }

   public boolean isTransparent() {
      return !this.isProxied() || this.tunnelEstablished;
   }

   public void flushRequestOutputStream() throws IOException {
      LOG.trace("enter HttpConnection.flushRequestOutputStream()");
      this.assertOpen();
      this.outputStream.flush();
   }

   public OutputStream getRequestOutputStream() throws IOException, IllegalStateException {
      LOG.trace("enter HttpConnection.getRequestOutputStream()");
      this.assertOpen();
      OutputStream out = this.outputStream;
      if (Wire.CONTENT_WIRE.enabled()) {
         out = new WireLogOutputStream((OutputStream)out, Wire.CONTENT_WIRE);
      }

      return (OutputStream)out;
   }

   /** @deprecated */
   public OutputStream getRequestOutputStream(boolean useChunking) throws IOException, IllegalStateException {
      LOG.trace("enter HttpConnection.getRequestOutputStream(boolean)");
      OutputStream out = this.getRequestOutputStream();
      if (useChunking) {
         out = new ChunkedOutputStream((OutputStream)out);
      }

      return (OutputStream)out;
   }

   /** @deprecated */
   public InputStream getResponseInputStream(HttpMethod method) throws IOException, IllegalStateException {
      LOG.trace("enter HttpConnection.getResponseInputStream(HttpMethod)");
      return this.getResponseInputStream();
   }

   public InputStream getResponseInputStream() throws IOException, IllegalStateException {
      LOG.trace("enter HttpConnection.getResponseInputStream()");
      this.assertOpen();
      return this.inputStream;
   }

   public boolean isResponseAvailable() throws IOException {
      LOG.trace("enter HttpConnection.isResponseAvailable()");
      this.assertOpen();
      return this.inputStream.available() > 0;
   }

   public boolean isResponseAvailable(int timeout) throws IOException {
      LOG.trace("enter HttpConnection.isResponseAvailable(int)");
      this.assertOpen();
      boolean result = false;
      if (this.inputStream.available() > 0) {
         result = true;
      } else {
         try {
            this.socket.setSoTimeout(timeout);
            this.inputStream.mark(1);
            int byteRead = this.inputStream.read();
            if (byteRead != -1) {
               this.inputStream.reset();
               LOG.debug("Input data available");
               result = true;
            } else {
               LOG.debug("Input data not available");
            }
         } catch (InterruptedIOException var13) {
            if (LOG.isDebugEnabled()) {
               LOG.debug("Input data not available after " + timeout + " ms");
            }
         } finally {
            try {
               this.socket.setSoTimeout(this.soTimeout);
            } catch (IOException var12) {
               LOG.debug("An error ocurred while resetting soTimeout, we will assume that no response is available.", var12);
               result = false;
            }

         }
      }

      return result;
   }

   public void write(byte[] data) throws IOException, IllegalStateException, HttpRecoverableException {
      LOG.trace("enter HttpConnection.write(byte[])");
      this.write(data, 0, data.length);
   }

   public void write(byte[] data, int offset, int length) throws IOException, IllegalStateException, HttpRecoverableException {
      LOG.trace("enter HttpConnection.write(byte[], int, int)");
      if (offset + length > data.length) {
         throw new HttpRecoverableException("Unable to write: offset=" + offset + " length=" + length + " data.length=" + data.length);
      } else if (data.length <= 0) {
         throw new HttpRecoverableException("Unable to write: data.length=" + data.length);
      } else {
         this.assertOpen();

         try {
            this.outputStream.write(data, offset, length);
         } catch (HttpRecoverableException var5) {
            throw var5;
         } catch (SocketException var6) {
            LOG.debug("HttpConnection: Socket exception while writing data", var6);
            throw new HttpRecoverableException(var6.toString());
         } catch (IOException var7) {
            LOG.debug("HttpConnection: Exception while writing data", var7);
            throw var7;
         }
      }
   }

   public void writeLine(byte[] data) throws IOException, IllegalStateException, HttpRecoverableException {
      LOG.trace("enter HttpConnection.writeLine(byte[])");
      this.write(data);
      this.writeLine();
   }

   public void writeLine() throws IOException, IllegalStateException, HttpRecoverableException {
      LOG.trace("enter HttpConnection.writeLine()");
      this.write(CRLF);
   }

   public void print(String data) throws IOException, IllegalStateException, HttpRecoverableException {
      LOG.trace("enter HttpConnection.print(String)");
      this.write(HttpConstants.getBytes(data));
   }

   public void printLine(String data) throws IOException, IllegalStateException, HttpRecoverableException {
      LOG.trace("enter HttpConnection.printLine(String)");
      this.writeLine(HttpConstants.getBytes(data));
   }

   public void printLine() throws IOException, IllegalStateException, HttpRecoverableException {
      LOG.trace("enter HttpConnection.printLine()");
      this.writeLine();
   }

   public String readLine() throws IOException, IllegalStateException {
      LOG.trace("enter HttpConnection.readLine()");
      this.assertOpen();
      return HttpParser.readLine(this.inputStream);
   }

   public void shutdownOutput() {
      LOG.trace("enter HttpConnection.shutdownOutput()");

      try {
         Class[] paramsClasses = new Class[0];
         Method shutdownOutput = this.socket.getClass().getMethod("shutdownOutput", paramsClasses);
         Object[] params = new Object[0];
         shutdownOutput.invoke(this.socket, params);
      } catch (Exception var4) {
         LOG.debug("Unexpected Exception caught", var4);
      }

   }

   public void close() {
      LOG.trace("enter HttpConnection.close()");
      this.closeSocketAndStreams();
   }

   public HttpConnectionManager getHttpConnectionManager() {
      return this.httpConnectionManager;
   }

   public void setHttpConnectionManager(HttpConnectionManager httpConnectionManager) {
      this.httpConnectionManager = httpConnectionManager;
   }

   public void releaseConnection() {
      LOG.trace("enter HttpConnection.releaseConnection()");
      this.used = true;
      if (this.httpConnectionManager != null) {
         this.httpConnectionManager.releaseConnection(this);
      }

   }

   protected void closeSocketAndStreams() {
      LOG.trace("enter HttpConnection.closeSockedAndStreams()");
      this.lastResponseInputStream = null;
      if (null != this.outputStream) {
         OutputStream temp = this.outputStream;
         this.outputStream = null;

         try {
            temp.close();
         } catch (Exception var5) {
            LOG.debug("Exception caught when closing output", var5);
         }
      }

      if (null != this.inputStream) {
         InputStream temp = this.inputStream;
         this.inputStream = null;

         try {
            temp.close();
         } catch (Exception var4) {
            LOG.debug("Exception caught when closing input", var4);
         }
      }

      if (null != this.socket) {
         Socket temp = this.socket;
         this.socket = null;

         try {
            temp.close();
         } catch (Exception var3) {
            LOG.debug("Exception caught when closing socket", var3);
         }
      }

      this.isOpen = false;
      this.used = false;
      this.tunnelEstablished = false;
      this.usingSecureSocket = false;
   }

   protected void assertNotOpen() throws IllegalStateException {
      if (this.isOpen) {
         throw new IllegalStateException("Connection is open");
      }
   }

   protected void assertOpen() throws IllegalStateException {
      if (!this.isOpen) {
         throw new IllegalStateException("Connection is not open");
      }
   }

   public int getSendBufferSize() throws SocketException {
      return this.socket == null ? -1 : this.socket.getSendBufferSize();
   }

   public void setSendBufferSize(int sendBufferSize) throws SocketException {
      this.sendBufferSize = sendBufferSize;
      if (this.socket != null) {
         this.socket.setSendBufferSize(sendBufferSize);
      }

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
      LOG = LogFactory.getLog(class$org$apache$commons$httpclient$HttpConnection == null ? (class$org$apache$commons$httpclient$HttpConnection = class$("org.apache.commons.httpclient.HttpConnection")) : class$org$apache$commons$httpclient$HttpConnection);
   }

   private class WrappedOutputStream extends OutputStream {
      private OutputStream out;

      public WrappedOutputStream(OutputStream out) {
         this.out = out;
      }

      private IOException handleException(IOException ioe) {
         boolean tempUsed = HttpConnection.this.used;
         HttpConnection.this.close();
         if (tempUsed) {
            HttpConnection.LOG.debug("Output exception occurred on a used connection.  Will treat as recoverable.", ioe);
            return new HttpRecoverableException(ioe.toString());
         } else {
            return ioe;
         }
      }

      public void write(int b) throws IOException {
         try {
            this.out.write(b);
         } catch (IOException var3) {
            throw this.handleException(var3);
         }
      }

      public void flush() throws IOException {
         try {
            this.out.flush();
         } catch (IOException var2) {
            throw this.handleException(var2);
         }
      }

      public void close() throws IOException {
         try {
            this.out.close();
         } catch (IOException var2) {
            throw this.handleException(var2);
         }
      }

      public void write(byte[] b, int off, int len) throws IOException {
         try {
            this.out.write(b, off, len);
         } catch (IOException var5) {
            throw this.handleException(var5);
         }
      }

      public void write(byte[] b) throws IOException {
         try {
            this.out.write(b);
         } catch (IOException var3) {
            throw this.handleException(var3);
         }
      }
   }

   private abstract class SocketTask implements Runnable {
      private Socket socket;
      private IOException exception;

      private SocketTask() {
      }

      protected void setSocket(Socket newSocket) {
         this.socket = newSocket;
      }

      protected Socket getSocket() {
         return this.socket;
      }

      public abstract void doit() throws IOException;

      public void run() {
         try {
            this.doit();
         } catch (IOException var2) {
            this.exception = var2;
         }

      }

      // $FF: synthetic method
      SocketTask(Object x1) {
         this();
      }
   }

   public static class ConnectionTimeoutException extends IOException {
   }
}
