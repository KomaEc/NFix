package org.apache.tools.ant.taskdefs.condition;

import java.io.IOException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;

public class Socket extends ProjectComponent implements Condition {
   private String server = null;
   private int port = 0;

   public void setServer(String server) {
      this.server = server;
   }

   public void setPort(int port) {
      this.port = port;
   }

   public boolean eval() throws BuildException {
      if (this.server == null) {
         throw new BuildException("No server specified in socket condition");
      } else if (this.port == 0) {
         throw new BuildException("No port specified in socket condition");
      } else {
         this.log("Checking for listener at " + this.server + ":" + this.port, 3);
         java.net.Socket s = null;

         boolean var3;
         try {
            s = new java.net.Socket(this.server, this.port);
            return true;
         } catch (IOException var13) {
            var3 = false;
         } finally {
            if (s != null) {
               try {
                  s.close();
               } catch (IOException var12) {
               }
            }

         }

         return var3;
      }
   }
}
