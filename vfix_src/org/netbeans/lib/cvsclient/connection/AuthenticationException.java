package org.netbeans.lib.cvsclient.connection;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.netbeans.lib.cvsclient.util.BundleUtilities;

public class AuthenticationException extends Exception {
   private Throwable underlyingThrowable;
   private String message;
   private String localizedMessage;

   public AuthenticationException(String var1, String var2) {
      super(var1);
      this.message = var1;
      this.localizedMessage = var2;
   }

   public AuthenticationException(String var1, Throwable var2, String var3) {
      this(var1, var3);
      this.initCause(var2);
   }

   public AuthenticationException(Throwable var1, String var2) {
      this.localizedMessage = var2;
      this.initCause(var1);
   }

   public Throwable getUnderlyingThrowable() {
      return this.getCause();
   }

   public String getLocalizedMessage() {
      return this.localizedMessage == null ? this.message : this.localizedMessage;
   }

   public String getMessage() {
      return this.message;
   }

   protected static String getBundleString(String var0) {
      String var1 = null;

      try {
         ResourceBundle var2 = BundleUtilities.getResourceBundle(AuthenticationException.class, "Bundle");
         if (var2 != null) {
            var1 = var2.getString(var0);
         }
      } catch (MissingResourceException var3) {
      }

      return var1;
   }
}
