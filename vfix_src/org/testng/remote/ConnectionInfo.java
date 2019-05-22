package org.testng.remote;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ConnectionInfo {
   private Socket m_socket;
   private ObjectInputStream m_ois;
   private ObjectOutputStream m_oos;

   public ObjectInputStream getOis() throws IOException {
      if (this.m_ois == null) {
         this.m_ois = new ObjectInputStream(this.m_socket.getInputStream());
      }

      return this.m_ois;
   }

   public ObjectOutputStream getOos() throws IOException {
      if (this.m_oos == null) {
         this.m_oos = new ObjectOutputStream(this.m_socket.getOutputStream());
      }

      return this.m_oos;
   }

   public void setSocket(Socket s) {
      this.m_socket = s;
   }

   public Socket getSocket() {
      return this.m_socket;
   }
}
