package bsh;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;

public class Remote {
   public static void main(String[] var0) throws Exception {
      if (var0.length < 2) {
         System.out.println("usage: Remote URL(http|bsh) file [ file ] ... ");
         System.exit(1);
      }

      String var1 = var0[0];
      String var2 = getFile(var0[1]);
      int var3 = eval(var1, var2);
      System.exit(var3);
   }

   public static int eval(String var0, String var1) throws IOException {
      String var2 = null;
      if (var0.startsWith("http:")) {
         var2 = doHttp(var0, var1);
      } else {
         if (!var0.startsWith("bsh:")) {
            throw new IOException("Unrecognized URL type.Scheme must be http:// or bsh://");
         }

         var2 = doBsh(var0, var1);
      }

      try {
         return Integer.parseInt(var2);
      } catch (Exception var4) {
         return 0;
      }
   }

   static String doBsh(String var0, String var1) {
      String var2 = "";
      String var3 = "";
      String var4 = "-1";

      try {
         var0 = var0.substring(6);
         int var6 = var0.indexOf(":");
         var2 = var0.substring(0, var6);
         var3 = var0.substring(var6 + 1, var0.length());
      } catch (Exception var11) {
         System.err.println("Bad URL: " + var0 + ": " + var11);
         return var4;
      }

      try {
         System.out.println("Connecting to host : " + var2 + " at port : " + var3);
         Socket var13 = new Socket(var2, Integer.parseInt(var3) + 1);
         OutputStream var7 = var13.getOutputStream();
         InputStream var8 = var13.getInputStream();
         sendLine(var1, var7);
         BufferedReader var9 = new BufferedReader(new InputStreamReader(var8));

         String var10;
         while((var10 = var9.readLine()) != null) {
            System.out.println(var10);
         }

         var4 = "1";
         return var4;
      } catch (Exception var12) {
         System.err.println("Error communicating with server: " + var12);
         return var4;
      }
   }

   private static void sendLine(String var0, OutputStream var1) throws IOException {
      var1.write(var0.getBytes());
      var1.flush();
   }

   static String doHttp(String var0, String var1) {
      String var2 = null;
      StringBuffer var3 = new StringBuffer();
      var3.append("bsh.client=Remote");
      var3.append("&bsh.script=");
      var3.append(URLEncoder.encode(var1));
      String var4 = var3.toString();

      try {
         URL var5 = new URL(var0);
         HttpURLConnection var6 = (HttpURLConnection)var5.openConnection();
         var6.setRequestMethod("POST");
         var6.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
         var6.setDoOutput(true);
         var6.setDoInput(true);
         PrintWriter var7 = new PrintWriter(new OutputStreamWriter(var6.getOutputStream(), "8859_1"), true);
         var7.print(var4);
         var7.flush();
         int var8 = var6.getResponseCode();
         if (var8 != 200) {
            System.out.println("Error, HTTP response: " + var8);
         }

         var2 = var6.getHeaderField("Bsh-Return");
         BufferedReader var9 = new BufferedReader(new InputStreamReader(var6.getInputStream()));

         String var10;
         while((var10 = var9.readLine()) != null) {
            System.out.println(var10);
         }

         System.out.println("Return Value: " + var2);
      } catch (MalformedURLException var11) {
         System.out.println(var11);
      } catch (IOException var12) {
         System.out.println(var12);
      }

      return var2;
   }

   static String getFile(String var0) throws FileNotFoundException, IOException {
      StringBuffer var1 = new StringBuffer();
      BufferedReader var2 = new BufferedReader(new FileReader(var0));

      String var3;
      while((var3 = var2.readLine()) != null) {
         var1.append(var3).append("\n");
      }

      return var1.toString();
   }
}
