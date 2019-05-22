package bsh.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.StringTokenizer;

class HttpdConnection extends Thread {
   Socket client;
   BufferedReader in;
   OutputStream out;
   PrintStream pout;
   boolean isHttp1;

   HttpdConnection(Socket var1) {
      this.client = var1;
      this.setPriority(4);
   }

   public void run() {
      try {
         this.in = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
         this.out = this.client.getOutputStream();
         this.pout = new PrintStream(this.out);
         String var1 = this.in.readLine();
         if (var1 == null) {
            this.error(400, "Empty Request");
         }

         if (var1.toLowerCase().indexOf("http/1.") != -1) {
            String var2;
            while(!(var2 = this.in.readLine()).equals("") && var2 != null) {
            }

            this.isHttp1 = true;
         }

         StringTokenizer var6 = new StringTokenizer(var1);
         if (var6.countTokens() < 2) {
            this.error(400, "Bad Request");
         } else {
            String var3 = var6.nextToken();
            if (var3.equals("GET")) {
               this.serveFile(var6.nextToken());
            } else {
               this.error(400, "Bad Request");
            }
         }

         this.client.close();
      } catch (IOException var5) {
         System.out.println("I/O error " + var5);

         try {
            this.client.close();
         } catch (Exception var4) {
         }
      }

   }

   private void serveFile(String var1) throws FileNotFoundException, IOException {
      if (var1.equals("/")) {
         var1 = "/remote/remote.html";
      }

      if (var1.startsWith("/remote/")) {
         var1 = "/bsh/util/lib/" + var1.substring(8);
      }

      if (var1.startsWith("/java")) {
         this.error(404, "Object Not Found");
      } else {
         try {
            System.out.println("sending file: " + var1);
            this.sendFileData(var1);
         } catch (FileNotFoundException var3) {
            this.error(404, "Object Not Found");
         }
      }

   }

   private void sendFileData(String var1) throws IOException, FileNotFoundException {
      InputStream var2 = this.getClass().getResourceAsStream(var1);
      if (var2 == null) {
         throw new FileNotFoundException(var1);
      } else {
         byte[] var3 = new byte[var2.available()];
         if (this.isHttp1) {
            this.pout.println("HTTP/1.0 200 Document follows");
            this.pout.println("Content-length: " + var3.length);
            if (var1.endsWith(".gif")) {
               this.pout.println("Content-type: image/gif");
            } else if (!var1.endsWith(".html") && !var1.endsWith(".htm")) {
               this.pout.println("Content-Type: application/octet-stream");
            } else {
               this.pout.println("Content-Type: text/html");
            }

            this.pout.println();
         }

         boolean var4 = false;

         int var5;
         do {
            var5 = var2.read(var3);
            if (var5 > 0) {
               this.pout.write(var3, 0, var5);
            }
         } while(var5 != -1);

         this.pout.flush();
      }
   }

   private void error(int var1, String var2) {
      var2 = "<html><h1>" + var2 + "</h1></html>";
      if (this.isHttp1) {
         this.pout.println("HTTP/1.0 " + var1 + " " + var2);
         this.pout.println("Content-type: text/html");
         this.pout.println("Content-length: " + var2.length() + "\n");
      }

      this.pout.println(var2);
   }
}
