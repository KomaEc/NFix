package bsh.util;

import java.io.IOException;
import java.net.ServerSocket;

public class Httpd extends Thread {
   ServerSocket ss;

   public static void main(String[] var0) throws IOException {
      (new Httpd(Integer.parseInt(var0[0]))).start();
   }

   public Httpd(int var1) throws IOException {
      this.ss = new ServerSocket(var1);
   }

   public void run() {
      try {
         while(true) {
            (new HttpdConnection(this.ss.accept())).start();
         }
      } catch (IOException var2) {
         System.out.println(var2);
      }
   }
}
