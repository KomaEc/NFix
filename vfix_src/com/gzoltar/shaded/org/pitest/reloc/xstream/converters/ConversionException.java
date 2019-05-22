package com.gzoltar.shaded.org.pitest.reloc.xstream.converters;

import com.gzoltar.shaded.org.pitest.reloc.xstream.XStreamException;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.OrderRetainingMap;
import java.util.Iterator;
import java.util.Map;

public class ConversionException extends XStreamException implements ErrorWriter {
   private static final String SEPARATOR = "\n-------------------------------";
   private Map stuff;

   public ConversionException(String msg, Throwable cause) {
      super(msg, cause);
      this.stuff = new OrderRetainingMap();
      if (msg != null) {
         this.add("message", msg);
      }

      if (cause != null) {
         this.add("cause-exception", cause.getClass().getName());
         this.add("cause-message", cause instanceof ConversionException ? ((ConversionException)cause).getShortMessage() : cause.getMessage());
      }

   }

   public ConversionException(String msg) {
      super(msg);
      this.stuff = new OrderRetainingMap();
   }

   public ConversionException(Throwable cause) {
      this(cause.getMessage(), cause);
   }

   public String get(String errorKey) {
      return (String)this.stuff.get(errorKey);
   }

   public void add(String name, String information) {
      String key = name;

      StringBuilder var10000;
      for(int i = 0; this.stuff.containsKey(key); key = var10000.append(i).append("]").toString()) {
         String value = (String)this.stuff.get(key);
         if (information.equals(value)) {
            return;
         }

         var10000 = (new StringBuilder()).append(name).append("[");
         ++i;
      }

      this.stuff.put(key, information);
   }

   public void set(String name, String information) {
      String key = name;
      int i = 0;
      this.stuff.put(name, information);

      while(this.stuff.containsKey(key)) {
         if (i != 0) {
            this.stuff.remove(key);
         }

         StringBuilder var10000 = (new StringBuilder()).append(name).append("[");
         ++i;
         key = var10000.append(i).append("]").toString();
      }

   }

   public Iterator keys() {
      return this.stuff.keySet().iterator();
   }

   public String getMessage() {
      StringBuffer result = new StringBuffer();
      if (super.getMessage() != null) {
         result.append(super.getMessage());
      }

      if (!result.toString().endsWith("\n-------------------------------")) {
         result.append("\n---- Debugging information ----");
      }

      Iterator iterator = this.keys();

      while(iterator.hasNext()) {
         String k = (String)iterator.next();
         String v = this.get(k);
         result.append('\n').append(k);
         result.append("                    ".substring(Math.min(20, k.length())));
         result.append(": ").append(v);
      }

      result.append("\n-------------------------------");
      return result.toString();
   }

   public String getShortMessage() {
      return super.getMessage();
   }
}
