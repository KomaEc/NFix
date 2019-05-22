package org.jboss.net.sockets;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.channels.SocketChannel;

public class TimeoutSocket extends Socket {
   private Socket s;

   public TimeoutSocket(Socket s) {
      this.s = s;
   }

   public InetAddress getInetAddress() {
      return this.s.getInetAddress();
   }

   public InetAddress getLocalAddress() {
      return this.s.getLocalAddress();
   }

   public int getPort() {
      return this.s.getPort();
   }

   public int getLocalPort() {
      return this.s.getLocalPort();
   }

   public SocketAddress getRemoteSocketAddress() {
      return this.s.getRemoteSocketAddress();
   }

   public SocketAddress getLocalSocketAddress() {
      return this.s.getLocalSocketAddress();
   }

   public SocketChannel getChannel() {
      return this.s.getChannel();
   }

   public InputStream getInputStream() throws IOException {
      InputStream is = this.s.getInputStream();
      InterruptableInputStream iis = new InterruptableInputStream(is);
      return iis;
   }

   public OutputStream getOutputStream() throws IOException {
      return this.s.getOutputStream();
   }

   public void setTcpNoDelay(boolean on) throws SocketException {
      this.s.setTcpNoDelay(on);
   }

   public boolean getTcpNoDelay() throws SocketException {
      return this.s.getTcpNoDelay();
   }

   public void setSoLinger(boolean on, int linger) throws SocketException {
      this.s.setSoLinger(on, linger);
   }

   public int getSoLinger() throws SocketException {
      return this.s.getSoLinger();
   }

   public void sendUrgentData(int data) throws IOException {
      this.s.sendUrgentData(data);
   }

   public void setOOBInline(boolean on) throws SocketException {
      this.s.setOOBInline(on);
   }

   public boolean getOOBInline() throws SocketException {
      return this.s.getOOBInline();
   }

   public synchronized void setSoTimeout(int timeout) throws SocketException {
      this.s.setSoTimeout(1000);
   }

   public synchronized int getSoTimeout() throws SocketException {
      return this.s.getSoTimeout();
   }

   public synchronized void setSendBufferSize(int size) throws SocketException {
      this.s.setSendBufferSize(size);
   }

   public synchronized int getSendBufferSize() throws SocketException {
      return this.s.getSendBufferSize();
   }

   public synchronized void setReceiveBufferSize(int size) throws SocketException {
      this.s.setReceiveBufferSize(size);
   }

   public synchronized int getReceiveBufferSize() throws SocketException {
      return this.s.getReceiveBufferSize();
   }

   public void setKeepAlive(boolean on) throws SocketException {
      this.s.setKeepAlive(on);
   }

   public boolean getKeepAlive() throws SocketException {
      return this.s.getKeepAlive();
   }

   public void setTrafficClass(int tc) throws SocketException {
      this.s.setTrafficClass(tc);
   }

   public int getTrafficClass() throws SocketException {
      return this.s.getTrafficClass();
   }

   public void setReuseAddress(boolean on) throws SocketException {
      this.s.setReuseAddress(on);
   }

   public boolean getReuseAddress() throws SocketException {
      return this.s.getReuseAddress();
   }

   public synchronized void close() throws IOException {
      this.s.close();
   }

   public void shutdownInput() throws IOException {
      this.s.shutdownInput();
   }

   public void shutdownOutput() throws IOException {
      this.s.shutdownOutput();
   }

   public String toString() {
      return this.s.toString();
   }

   public boolean isConnected() {
      return this.s.isConnected();
   }

   public boolean isBound() {
      return this.s.isBound();
   }

   public boolean isClosed() {
      return this.s.isClosed();
   }

   public boolean isInputShutdown() {
      return this.s.isInputShutdown();
   }

   public boolean isOutputShutdown() {
      return this.s.isOutputShutdown();
   }
}
