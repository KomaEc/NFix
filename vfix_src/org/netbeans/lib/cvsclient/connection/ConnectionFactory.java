package org.netbeans.lib.cvsclient.connection;

import org.netbeans.lib.cvsclient.CVSRoot;

public class ConnectionFactory {
   protected ConnectionFactory() {
   }

   public static Connection getConnection(String var0) throws IllegalArgumentException {
      CVSRoot var1 = CVSRoot.parse(var0);
      return getConnection(var1);
   }

   public static Connection getConnection(CVSRoot var0) throws IllegalArgumentException {
      if (var0.isLocal()) {
         LocalConnection var3 = new LocalConnection();
         var3.setRepository(var0.getRepository());
         return var3;
      } else {
         String var1 = var0.getMethod();
         if ("pserver" == var1) {
            PServerConnection var2 = new PServerConnection(var0);
            return var2;
         } else {
            throw new IllegalArgumentException("Unrecognized CVS Root: " + var0);
         }
      }
   }
}
