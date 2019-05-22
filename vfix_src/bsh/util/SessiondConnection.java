package bsh.util;

import bsh.Interpreter;
import bsh.NameSpace;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

class SessiondConnection extends Thread {
   NameSpace globalNameSpace;
   Socket client;

   SessiondConnection(NameSpace var1, Socket var2) {
      this.client = var2;
      this.globalNameSpace = var1;
   }

   public void run() {
      try {
         InputStream var1 = this.client.getInputStream();
         PrintStream var2 = new PrintStream(this.client.getOutputStream());
         Interpreter var3 = new Interpreter(new InputStreamReader(var1), var2, var2, true, this.globalNameSpace);
         var3.setExitOnEOF(false);
         var3.run();
      } catch (IOException var4) {
         System.out.println(var4);
      }

   }
}
