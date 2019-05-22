package org.jf.dexlib2.util;

import com.google.common.io.ByteStreams;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import javax.annotation.Nonnull;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.DexBackedOdexFile;
import org.jf.dexlib2.dexbacked.raw.HeaderItem;
import org.jf.dexlib2.dexbacked.raw.OdexHeaderItem;

public class DexUtil {
   public static void verifyDexHeader(@Nonnull InputStream inputStream) throws IOException {
      if (!inputStream.markSupported()) {
         throw new IllegalArgumentException("InputStream must support mark");
      } else {
         inputStream.mark(44);
         byte[] partialHeader = new byte[44];

         try {
            ByteStreams.readFully(inputStream, partialHeader);
         } catch (EOFException var6) {
            throw new DexBackedDexFile.NotADexFile("File is too short");
         } finally {
            inputStream.reset();
         }

         verifyDexHeader(partialHeader, 0);
      }
   }

   public static void verifyDexHeader(@Nonnull byte[] buf, int offset) {
      int dexVersion = HeaderItem.getVersion(buf, offset);
      if (dexVersion != -1) {
         if (!HeaderItem.isSupportedDexVersion(dexVersion)) {
            throw new DexUtil.UnsupportedFile(String.format("Dex version %03d is not supported", dexVersion));
         } else {
            int endian = HeaderItem.getEndian(buf, offset);
            if (endian == 2018915346) {
               throw new DexUtil.UnsupportedFile("Big endian dex files are not supported");
            } else if (endian != 305419896) {
               throw new DexUtil.InvalidFile(String.format("Invalid endian tag: 0x%x", endian));
            }
         }
      } else {
         StringBuilder sb = new StringBuilder("Not a valid dex magic value:");

         for(int i = 0; i < 8; ++i) {
            sb.append(String.format(" %02x", buf[i]));
         }

         throw new DexBackedDexFile.NotADexFile(sb.toString());
      }
   }

   public static void verifyOdexHeader(@Nonnull InputStream inputStream) throws IOException {
      if (!inputStream.markSupported()) {
         throw new IllegalArgumentException("InputStream must support mark");
      } else {
         inputStream.mark(8);
         byte[] partialHeader = new byte[8];

         try {
            ByteStreams.readFully(inputStream, partialHeader);
         } catch (EOFException var6) {
            throw new DexBackedOdexFile.NotAnOdexFile("File is too short");
         } finally {
            inputStream.reset();
         }

         verifyOdexHeader(partialHeader, 0);
      }
   }

   public static void verifyOdexHeader(@Nonnull byte[] buf, int offset) {
      int odexVersion = OdexHeaderItem.getVersion(buf, offset);
      if (odexVersion != -1) {
         if (!OdexHeaderItem.isSupportedOdexVersion(odexVersion)) {
            throw new DexUtil.UnsupportedFile(String.format("Odex version %03d is not supported", odexVersion));
         }
      } else {
         StringBuilder sb = new StringBuilder("Not a valid odex magic value:");

         for(int i = 0; i < 8; ++i) {
            sb.append(String.format(" %02x", buf[i]));
         }

         throw new DexBackedOdexFile.NotAnOdexFile(sb.toString());
      }
   }

   public static class UnsupportedFile extends RuntimeException {
      public UnsupportedFile() {
      }

      public UnsupportedFile(String message) {
         super(message);
      }

      public UnsupportedFile(String message, Throwable cause) {
         super(message, cause);
      }

      public UnsupportedFile(Throwable cause) {
         super(cause);
      }
   }

   public static class InvalidFile extends RuntimeException {
      public InvalidFile() {
      }

      public InvalidFile(String message) {
         super(message);
      }

      public InvalidFile(String message, Throwable cause) {
         super(message, cause);
      }

      public InvalidFile(Throwable cause) {
         super(cause);
      }
   }
}
