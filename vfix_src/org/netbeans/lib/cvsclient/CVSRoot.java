package org.netbeans.lib.cvsclient;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.StringTokenizer;
import org.netbeans.lib.cvsclient.connection.Connection;
import org.netbeans.lib.cvsclient.connection.ConnectionFactory;

public class CVSRoot {
   public static final String METHOD_LOCAL = "local";
   public static final String METHOD_FORK = "fork";
   public static final String METHOD_SERVER = "server";
   public static final String METHOD_PSERVER = "pserver";
   public static final String METHOD_EXT = "ext";
   private String method;
   private String username;
   private String password;
   private String hostname;
   private int port;
   private String repository;

   public static CVSRoot parse(String var0) throws IllegalArgumentException {
      return new CVSRoot(var0);
   }

   public static CVSRoot parse(Properties var0) throws IllegalArgumentException {
      return new CVSRoot(var0);
   }

   protected CVSRoot(Properties var1) throws IllegalArgumentException {
      String var2 = var1.getProperty("method");
      if (var2 != null) {
         this.method = var2.intern();
      }

      this.hostname = var1.getProperty("hostname");
      if (this.hostname.length() == 0) {
         this.hostname = null;
      }

      if (this.hostname != null) {
         this.username = var1.getProperty("username");
         this.password = var1.getProperty("password");

         try {
            int var3 = Integer.parseInt(var1.getProperty("port"));
            if (var3 <= 0) {
               throw new IllegalArgumentException("The port is not a positive number.");
            }

            this.port = var3;
         } catch (NumberFormatException var4) {
            throw new IllegalArgumentException("The port is not a number: '" + var1.getProperty("port") + "'.");
         }
      }

      String var5 = var1.getProperty("repository");
      if (var5 == null) {
         throw new IllegalArgumentException("Repository is obligatory.");
      } else {
         this.repository = var5;
      }
   }

   protected CVSRoot(String var1) throws IllegalArgumentException {
      int var2 = 0;
      boolean var3;
      int var4;
      String var9;
      if (!var1.startsWith(":")) {
         var3 = var1.startsWith("/");
         if (!var3) {
            if (var1.indexOf(58) == 1 && var1.indexOf(92) == 2) {
               this.method = "local";
               this.repository = var1;
               return;
            }

            var2 = var1.indexOf(58);
            if (var2 < 0) {
               var4 = var1.indexOf(47);
               if (var4 < 0) {
                  throw new IllegalArgumentException("CVSROOT must be an absolute pathname.");
               }

               this.method = "server";
            } else {
               this.method = "ext";
            }

            var2 = 0;
         } else {
            this.method = "local";
         }
      } else {
         var2 = var1.indexOf(58, 1);
         if (var2 < 0) {
            throw new IllegalArgumentException("The connection method does not end with ':'.");
         }

         var4 = var2;
         int var5 = var1.indexOf(";", 1);
         if (var5 != -1 && var5 < var2) {
            var4 = var5;
            String var6 = var1.substring(var5 + 1, var2);
            StringTokenizer var7 = new StringTokenizer(var6, "=;");

            while(var7.hasMoreTokens()) {
               String var8 = var7.nextToken();
               if (!var7.hasMoreTokens()) {
                  throw new IllegalArgumentException("Undefined " + var8 + " option value.");
               }

               var9 = var7.nextToken();
               if ("hostname".equals(var8)) {
                  this.hostname = var9;
               } else if ("username".equals(var8)) {
                  this.username = var9;
               } else if ("password".equals(var8)) {
                  this.password = var9;
               }

               if ("port".equals(var8)) {
                  try {
                     this.port = Integer.parseInt(var9, 10);
                  } catch (NumberFormatException var14) {
                     throw new IllegalArgumentException("Port option must be number.");
                  }
               }
            }
         }

         this.method = var1.substring(1, var4).intern();
         if ("extssh".equals(this.method)) {
            this.method = "ext";
         }

         ++var2;
         var3 = this.isLocalMethod(this.method);
      }

      if (var3) {
         this.repository = var1.substring(var2);
      } else {
         var4 = var1.indexOf(64, var2);
         if (var4 < 0) {
            var4 = var2;
         }

         int var16 = -1;
         int var17 = var1.indexOf(58, var4);
         String var15;
         if (var17 == -1) {
            var16 = var1.indexOf(47, var4);
            if (var16 < 0) {
               throw new IllegalArgumentException("cvsroot " + var1 + " is malformed, host name is missing.");
            }

            var15 = var1.substring(var2, var16);
         } else {
            var15 = var1.substring(var2, var17);
         }

         int var18 = var15.indexOf(64);
         int var10;
         if (var18 == -1) {
            if (var15.length() > 0) {
               this.hostname = var15;
            }
         } else {
            var9 = var15.substring(0, var18);
            if (var9.length() > 0) {
               var10 = var9.indexOf(58);
               if (var10 != -1) {
                  this.username = var9.substring(0, var10);
                  this.password = var9.substring(var10 + 1);
               } else {
                  this.username = var9;
               }
            }

            this.hostname = var15.substring(var18 + 1);
         }

         if (this.hostname == null || this.hostname.length() == 0) {
            throw new IllegalArgumentException("Didn't specify hostname in CVSROOT '" + var1 + "'.");
         }

         if (var17 > 0) {
            var9 = var1.substring(var17 + 1);
            var10 = 0;

            int var11;
            char var12;
            for(var11 = 0; var9.length() > var10 && Character.isDigit(var12 = var9.charAt(var10)); ++var10) {
               int var13 = Character.digit(var12, 10);
               var11 = var11 * 10 + var13;
            }

            this.port = var11;
            if (var10 > 0) {
               var9 = var9.substring(var10);
            }

            this.repository = var9;
         } else {
            this.port = 0;
            this.repository = var1.substring(var16);
         }
      }

   }

   public boolean isLocal() {
      return this.hostname == null;
   }

   public String toString() {
      if (this.hostname == null) {
         return this.method == null ? this.repository : ":" + this.method + ":" + this.repository;
      } else {
         StringBuffer var1 = new StringBuffer();
         if (this.method != null) {
            var1.append(':');
            var1.append(this.method);
            var1.append(':');
         }

         if (this.username != null) {
            var1.append(this.username);
            var1.append('@');
         }

         var1.append(this.hostname);
         var1.append(':');
         if (this.port > 0) {
            var1.append(this.port);
         }

         var1.append(this.repository);
         return var1.toString();
      }
   }

   public int getCompatibilityLevel(CVSRoot var1) {
      if (this.equals(var1)) {
         return 0;
      } else {
         boolean var2 = this.isSameRepository(var1);
         boolean var3 = this.isSameHost(var1);
         boolean var4 = this.isSameMethod(var1);
         boolean var5 = this.isSamePort(var1);
         boolean var6 = this.isSameUser(var1);
         if (var2 && var3 && var4 && var5 && var6) {
            return 1;
         } else if (var2 && var3 && var4) {
            return 2;
         } else {
            return var2 && var3 ? 3 : -1;
         }
      }
   }

   private boolean isSameRepository(CVSRoot var1) {
      if (this.repository.equals(var1.repository)) {
         return true;
      } else {
         try {
            return (new File(this.repository)).getCanonicalFile().equals((new File(var1.repository)).getCanonicalFile());
         } catch (IOException var3) {
            return false;
         }
      }
   }

   private boolean isSameHost(CVSRoot var1) {
      String var2 = var1.getHostName();
      if (this.hostname == var2) {
         return true;
      } else {
         return this.hostname != null ? this.hostname.equalsIgnoreCase(var2) : false;
      }
   }

   private boolean isSameMethod(CVSRoot var1) {
      if (this.method == null) {
         return var1.getMethod() == null;
      } else {
         return this.method.equals(var1.getMethod());
      }
   }

   private boolean isSamePort(CVSRoot var1) {
      if (this.isLocal() == var1.isLocal()) {
         if (this.isLocal()) {
            return true;
         } else if (this.port == var1.getPort()) {
            return true;
         } else {
            try {
               Connection var2 = ConnectionFactory.getConnection(this);
               Connection var3 = ConnectionFactory.getConnection(var1);
               return var2.getPort() == var3.getPort();
            } catch (IllegalArgumentException var4) {
               return false;
            }
         }
      } else {
         return false;
      }
   }

   private boolean isSameUser(CVSRoot var1) {
      String var2 = var1.getUserName();
      if (var2 == this.getUserName()) {
         return true;
      } else {
         return var2 != null ? var2.equals(this.getUserName()) : false;
      }
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof CVSRoot)) {
         return false;
      } else {
         CVSRoot var2 = (CVSRoot)var1;
         return this.toString().equals(var2.toString());
      }
   }

   public int hashCode() {
      return this.toString().hashCode();
   }

   public String getMethod() {
      return this.method;
   }

   protected void setMethod(String var1) {
      if (var1 != null) {
         this.method = var1.intern();
      } else {
         var1 = null;
      }

      if (this.isLocalMethod(var1)) {
         this.username = null;
         this.password = null;
         this.hostname = null;
         this.port = 0;
      } else if (this.hostname == null) {
         throw new IllegalArgumentException("Hostname must not be null when setting a remote method.");
      }

   }

   private boolean isLocalMethod(String var1) {
      return "local" == var1 || "fork" == var1;
   }

   public String getUserName() {
      return this.username;
   }

   protected void setUserName(String var1) {
      this.username = var1;
   }

   public String getPassword() {
      return this.password;
   }

   protected void setPassword(String var1) {
      this.password = var1;
   }

   public String getHostName() {
      return this.hostname;
   }

   protected void setHostName(String var1) {
      this.hostname = var1;
   }

   public int getPort() {
      return this.port;
   }

   public void setPort(int var1) {
      this.port = var1;
   }

   public String getRepository() {
      return this.repository;
   }

   protected void setRepository(String var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("The repository must not be null.");
      } else {
         this.repository = var1;
      }
   }
}
