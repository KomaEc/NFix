package org.apache.tools.zip;

import java.util.Hashtable;
import java.util.Vector;
import java.util.zip.ZipException;

public class ExtraFieldUtils {
   private static Hashtable implementations = new Hashtable();
   // $FF: synthetic field
   static Class class$org$apache$tools$zip$AsiExtraField;
   // $FF: synthetic field
   static Class class$org$apache$tools$zip$JarMarker;

   public static void register(Class c) {
      try {
         ZipExtraField ze = (ZipExtraField)c.newInstance();
         implementations.put(ze.getHeaderId(), c);
      } catch (ClassCastException var2) {
         throw new RuntimeException(c + " doesn't implement ZipExtraField");
      } catch (InstantiationException var3) {
         throw new RuntimeException(c + " is not a concrete class");
      } catch (IllegalAccessException var4) {
         throw new RuntimeException(c + "'s no-arg constructor is not public");
      }
   }

   public static ZipExtraField createExtraField(ZipShort headerId) throws InstantiationException, IllegalAccessException {
      Class c = (Class)implementations.get(headerId);
      if (c != null) {
         return (ZipExtraField)c.newInstance();
      } else {
         UnrecognizedExtraField u = new UnrecognizedExtraField();
         u.setHeaderId(headerId);
         return u;
      }
   }

   public static ZipExtraField[] parse(byte[] data) throws ZipException {
      Vector v = new Vector();

      int start;
      int length;
      for(start = 0; start <= data.length - 4; start += length + 4) {
         ZipShort headerId = new ZipShort(data, start);
         length = (new ZipShort(data, start + 2)).getValue();
         if (start + 4 + length > data.length) {
            throw new ZipException("data starting at " + start + " is in unknown format");
         }

         try {
            ZipExtraField ze = createExtraField(headerId);
            ze.parseFromLocalFileData(data, start + 4, length);
            v.addElement(ze);
         } catch (InstantiationException var6) {
            throw new ZipException(var6.getMessage());
         } catch (IllegalAccessException var7) {
            throw new ZipException(var7.getMessage());
         }
      }

      if (start != data.length) {
         throw new ZipException("data starting at " + start + " is in unknown format");
      } else {
         ZipExtraField[] result = new ZipExtraField[v.size()];
         v.copyInto(result);
         return result;
      }
   }

   public static byte[] mergeLocalFileDataData(ZipExtraField[] data) {
      int sum = 4 * data.length;

      for(int i = 0; i < data.length; ++i) {
         sum += data[i].getLocalFileDataLength().getValue();
      }

      byte[] result = new byte[sum];
      int start = 0;

      for(int i = 0; i < data.length; ++i) {
         System.arraycopy(data[i].getHeaderId().getBytes(), 0, result, start, 2);
         System.arraycopy(data[i].getLocalFileDataLength().getBytes(), 0, result, start + 2, 2);
         byte[] local = data[i].getLocalFileDataData();
         System.arraycopy(local, 0, result, start + 4, local.length);
         start += local.length + 4;
      }

      return result;
   }

   public static byte[] mergeCentralDirectoryData(ZipExtraField[] data) {
      int sum = 4 * data.length;

      for(int i = 0; i < data.length; ++i) {
         sum += data[i].getCentralDirectoryLength().getValue();
      }

      byte[] result = new byte[sum];
      int start = 0;

      for(int i = 0; i < data.length; ++i) {
         System.arraycopy(data[i].getHeaderId().getBytes(), 0, result, start, 2);
         System.arraycopy(data[i].getCentralDirectoryLength().getBytes(), 0, result, start + 2, 2);
         byte[] local = data[i].getCentralDirectoryData();
         System.arraycopy(local, 0, result, start + 4, local.length);
         start += local.length + 4;
      }

      return result;
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   static {
      register(class$org$apache$tools$zip$AsiExtraField == null ? (class$org$apache$tools$zip$AsiExtraField = class$("org.apache.tools.zip.AsiExtraField")) : class$org$apache$tools$zip$AsiExtraField);
      register(class$org$apache$tools$zip$JarMarker == null ? (class$org$apache$tools$zip$JarMarker = class$("org.apache.tools.zip.JarMarker")) : class$org$apache$tools$zip$JarMarker);
   }
}
