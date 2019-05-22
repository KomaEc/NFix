package org.netbeans.lib.cvsclient.command;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.netbeans.lib.cvsclient.util.BundleUtilities;

public class CommandException extends Exception {
   private Exception underlyingException;
   private String localizedMessage;
   private String message;

   public CommandException(Exception var1, String var2) {
      this.underlyingException = var1;
      this.localizedMessage = var2;
   }

   public CommandException(String var1, String var2) {
      super(var1);
      this.message = var1;
      this.localizedMessage = var2;
   }

   public Exception getUnderlyingException() {
      return this.underlyingException;
   }

   public void printStackTrace() {
      if (this.underlyingException != null) {
         this.underlyingException.printStackTrace();
      } else {
         super.printStackTrace();
      }

   }

   public void printStackTrace(PrintStream var1) {
      if (this.underlyingException != null) {
         this.underlyingException.printStackTrace(var1);
      } else {
         super.printStackTrace(var1);
      }

   }

   public void printStackTrace(PrintWriter var1) {
      if (this.underlyingException != null) {
         this.underlyingException.printStackTrace(var1);
      } else {
         super.printStackTrace(var1);
      }

   }

   public String getLocalizedMessage() {
      return this.localizedMessage == null ? this.message : this.localizedMessage;
   }

   public String getMessage() {
      return this.message == null ? this.localizedMessage : this.message;
   }

   protected static String getBundleString(String var0) {
      String var1 = null;

      try {
         ResourceBundle var2 = BundleUtilities.getResourceBundle(CommandException.class, "Bundle");
         if (var2 != null) {
            var1 = var2.getString(var0);
         }
      } catch (MissingResourceException var3) {
      }

      return var1;
   }

   public static String getLocalMessage(String var0) {
      return getLocalMessage(var0, (Object[])null);
   }

   public static String getLocalMessage(String var0, Object[] var1) {
      String var2 = getBundleString(var0);
      if (var2 == null) {
         return null;
      } else {
         if (var1 != null) {
            var2 = MessageFormat.format(var2, var1);
         }

         return var2;
      }
   }
}
