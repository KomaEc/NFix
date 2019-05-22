package java_cup;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java_cup.runtime.Symbol;

public class ErrorManager {
   private static ErrorManager errorManager = new ErrorManager();
   private int errors = 0;
   private int warnings = 0;
   private int fatals = 0;

   public int getFatalCount() {
      return this.fatals;
   }

   public int getErrorCount() {
      return this.errors;
   }

   public int getWarningCount() {
      return this.warnings;
   }

   public static ErrorManager getManager() {
      return errorManager;
   }

   private ErrorManager() {
   }

   public void emit_fatal(String message) {
      System.err.println("Fatal : " + message);
      ++this.fatals;
   }

   public void emit_fatal(String message, Symbol sym) {
      System.err.println("Fatal: " + message + " @ " + sym);
      ++this.fatals;
   }

   public void emit_warning(String message) {
      System.err.println("Warning : " + message);
      ++this.warnings;
   }

   public void emit_warning(String message, Symbol sym) {
      System.err.println("Fatal: " + message + " @ " + sym);
      ++this.warnings;
   }

   public void emit_error(String message) {
      System.err.println("Error : " + message);
      ++this.errors;
   }

   public void emit_error(String message, Symbol sym) {
      System.err.println("Error: " + message + " @ " + sym);
      ++this.errors;
   }

   private static String convSymbol(Symbol symbol) {
      String result = symbol.value == null ? "" : " (\"" + symbol.value.toString() + "\")";
      Field[] fields = sym.class.getFields();

      for(int i = 0; i < fields.length; ++i) {
         if (Modifier.isPublic(fields[i].getModifiers())) {
            try {
               if (fields[i].getInt((Object)null) == symbol.sym) {
                  return fields[i].getName() + result;
               }
            } catch (Exception var5) {
            }
         }
      }

      return symbol.toString() + result;
   }
}
