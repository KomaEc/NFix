package bsh.util;

import bsh.NameSpace;
import java.io.IOException;
import java.net.ServerSocket;

public class Sessiond extends Thread {
   private ServerSocket ss;
   NameSpace globalNameSpace;

   public Sessiond(NameSpace var1, int var2) throws IOException {
      this.ss = new ServerSocket(var2);
      this.globalNameSpace = var1;
   }

   public void run() {
      try {
         while(true) {
            (new SessiondConnection(this.globalNameSpace, this.ss.accept())).start();
         }
      } catch (IOException var2) {
         System.out.println(var2);
      }
   }
}
