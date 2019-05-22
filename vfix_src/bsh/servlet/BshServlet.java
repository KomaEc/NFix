package bsh.servlet;

import bsh.EvalError;
import bsh.Interpreter;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BshServlet extends HttpServlet {
   static String bshVersion;
   static String exampleScript = "print(\"hello!\");";
   // $FF: synthetic field
   static Class class$bsh$servlet$BshServlet;

   static String getBshVersion() {
      if (bshVersion != null) {
         return bshVersion;
      } else {
         Interpreter var0 = new Interpreter();

         try {
            var0.eval((Reader)(new InputStreamReader((class$bsh$servlet$BshServlet == null ? (class$bsh$servlet$BshServlet = class$("bsh.servlet.BshServlet")) : class$bsh$servlet$BshServlet).getResource("getVersion.bsh").openStream())));
            bshVersion = (String)var0.eval("getVersion()");
         } catch (Exception var2) {
            bshVersion = "BeanShell: unknown version";
         }

         return bshVersion;
      }
   }

   public void doGet(HttpServletRequest var1, HttpServletResponse var2) throws ServletException, IOException {
      String var3 = var1.getParameter("bsh.script");
      String var4 = var1.getParameter("bsh.client");
      String var5 = var1.getParameter("bsh.servlet.output");
      String var6 = var1.getParameter("bsh.servlet.captureOutErr");
      boolean var7 = false;
      if (var6 != null && var6.equalsIgnoreCase("true")) {
         var7 = true;
      }

      Object var8 = null;
      Exception var9 = null;
      StringBuffer var10 = new StringBuffer();
      if (var3 != null) {
         try {
            var8 = this.evalScript(var3, var10, var7, var1, var2);
         } catch (Exception var12) {
            var9 = var12;
         }
      }

      var2.setHeader("Bsh-Return", String.valueOf(var8));
      if ((var5 == null || !var5.equalsIgnoreCase("raw")) && (var4 == null || !var4.equals("Remote"))) {
         this.sendHTML(var1, var2, var3, var9, var8, var10, var7);
      } else {
         this.sendRaw(var1, var2, var9, var8, var10);
      }

   }

   void sendHTML(HttpServletRequest var1, HttpServletResponse var2, String var3, Exception var4, Object var5, StringBuffer var6, boolean var7) throws IOException {
      SimpleTemplate var8 = new SimpleTemplate((class$bsh$servlet$BshServlet == null ? (class$bsh$servlet$BshServlet = class$("bsh.servlet.BshServlet")) : class$bsh$servlet$BshServlet).getResource("page.template"));
      var8.replace("version", getBshVersion());
      String var9 = var1.getRequestURI();
      var8.replace("servletURL", var9);
      if (var3 != null) {
         var8.replace("script", var3);
      } else {
         var8.replace("script", exampleScript);
      }

      if (var7) {
         var8.replace("captureOutErr", "CHECKED");
      } else {
         var8.replace("captureOutErr", "");
      }

      if (var3 != null) {
         var8.replace("scriptResult", this.formatScriptResultHTML(var3, var5, var4, var6));
      }

      var2.setContentType("text/html");
      PrintWriter var10 = var2.getWriter();
      var8.write(var10);
      var10.flush();
   }

   void sendRaw(HttpServletRequest var1, HttpServletResponse var2, Exception var3, Object var4, StringBuffer var5) throws IOException {
      var2.setContentType("text/plain");
      PrintWriter var6 = var2.getWriter();
      if (var3 != null) {
         var6.println("Script Error:\n" + var3);
      } else {
         var6.println(var5.toString());
      }

      var6.flush();
   }

   String formatScriptResultHTML(String var1, Object var2, Exception var3, StringBuffer var4) throws IOException {
      SimpleTemplate var5;
      if (var3 != null) {
         var5 = new SimpleTemplate(this.getClass().getResource("error.template"));
         String var9;
         if (var3 instanceof EvalError) {
            int var6 = ((EvalError)var3).getErrorLineNumber();
            String var7 = var3.getMessage();
            byte var8 = 4;
            var9 = escape(var7);
            if (var6 > -1) {
               var9 = var9 + "<hr>" + this.showScriptContextHTML(var1, var6, var8);
            }
         } else {
            var9 = escape(var3.toString());
         }

         var5.replace("error", var9);
      } else {
         var5 = new SimpleTemplate(this.getClass().getResource("result.template"));
         var5.replace("value", escape(String.valueOf(var2)));
         var5.replace("output", escape(var4.toString()));
      }

      return var5.toString();
   }

   String showScriptContextHTML(String var1, int var2, int var3) {
      StringBuffer var4 = new StringBuffer();
      BufferedReader var5 = new BufferedReader(new StringReader(var1));
      int var6 = Math.max(1, var2 - var3);
      int var7 = var2 + var3;

      for(int var8 = 1; var8 <= var2 + var3 + 1; ++var8) {
         if (var8 < var6) {
            try {
               var5.readLine();
            } catch (IOException var12) {
               throw new RuntimeException(var12.toString());
            }
         } else {
            if (var8 > var7) {
               break;
            }

            String var9;
            try {
               var9 = var5.readLine();
            } catch (IOException var11) {
               throw new RuntimeException(var11.toString());
            }

            if (var9 == null) {
               break;
            }

            if (var8 == var2) {
               var4.append("<font color=\"red\">" + var8 + ": " + var9 + "</font><br/>");
            } else {
               var4.append(var8 + ": " + var9 + "<br/>");
            }
         }
      }

      return var4.toString();
   }

   public void doPost(HttpServletRequest var1, HttpServletResponse var2) throws ServletException, IOException {
      this.doGet(var1, var2);
   }

   Object evalScript(String var1, StringBuffer var2, boolean var3, HttpServletRequest var4, HttpServletResponse var5) throws EvalError {
      ByteArrayOutputStream var6 = new ByteArrayOutputStream();
      PrintStream var7 = new PrintStream(var6);
      Interpreter var8 = new Interpreter((Reader)null, var7, var7, false);
      var8.set("bsh.httpServletRequest", var4);
      var8.set("bsh.httpServletResponse", var5);
      Object var9 = null;
      Object var10 = null;
      PrintStream var11 = System.out;
      PrintStream var12 = System.err;
      if (var3) {
         System.setOut(var7);
         System.setErr(var7);
      }

      try {
         var9 = var8.eval(var1);
      } finally {
         if (var3) {
            System.setOut(var11);
            System.setErr(var12);
         }

      }

      var7.flush();
      var2.append(var6.toString());
      return var9;
   }

   public static String escape(String var0) {
      String var1 = "&<>";
      String[] var2 = new String[]{"&amp;", "&lt;", "&gt;"};
      StringBuffer var3 = new StringBuffer();

      for(int var4 = 0; var4 < var0.length(); ++var4) {
         char var5 = var0.charAt(var4);
         int var6 = var1.indexOf(var5);
         if (var6 < 0) {
            var3.append(var5);
         } else {
            var3.append(var2[var6]);
         }
      }

      return var3.toString();
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
