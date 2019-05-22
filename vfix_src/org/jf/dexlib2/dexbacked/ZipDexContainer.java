package org.jf.dexlib2.dexbacked;

import com.google.common.collect.Lists;
import com.google.common.io.ByteStreams;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.Opcodes;
import org.jf.dexlib2.iface.MultiDexContainer;
import org.jf.dexlib2.util.DexUtil;

public class ZipDexContainer implements MultiDexContainer<ZipDexContainer.ZipDexFile> {
   private final File zipFilePath;
   private final Opcodes opcodes;

   public ZipDexContainer(@Nonnull File zipFilePath, @Nonnull Opcodes opcodes) {
      this.zipFilePath = zipFilePath;
      this.opcodes = opcodes;
   }

   @Nonnull
   public Opcodes getOpcodes() {
      return this.opcodes;
   }

   @Nonnull
   public List<String> getDexEntryNames() throws IOException {
      List<String> entryNames = Lists.newArrayList();
      ZipFile zipFile = this.getZipFile();

      try {
         Enumeration entriesEnumeration = zipFile.entries();

         while(entriesEnumeration.hasMoreElements()) {
            ZipEntry entry = (ZipEntry)entriesEnumeration.nextElement();
            if (this.isDex(zipFile, entry)) {
               entryNames.add(entry.getName());
            }
         }

         ArrayList var8 = entryNames;
         return var8;
      } finally {
         zipFile.close();
      }
   }

   @Nullable
   public ZipDexContainer.ZipDexFile getEntry(@Nonnull String entryName) throws IOException {
      ZipFile zipFile = this.getZipFile();

      ZipDexContainer.ZipDexFile var4;
      try {
         ZipEntry entry = zipFile.getEntry(entryName);
         if (entry == null) {
            var4 = null;
            return var4;
         }

         var4 = this.loadEntry(zipFile, entry);
      } finally {
         zipFile.close();
      }

      return var4;
   }

   public boolean isZipFile() {
      ZipFile zipFile = null;

      boolean var3;
      try {
         zipFile = this.getZipFile();
         boolean var2 = true;
         return var2;
      } catch (IOException var15) {
         var3 = false;
      } catch (ZipDexContainer.NotAZipFileException var16) {
         var3 = false;
         return var3;
      } finally {
         if (zipFile != null) {
            try {
               zipFile.close();
            } catch (IOException var14) {
            }
         }

      }

      return var3;
   }

   protected boolean isDex(@Nonnull ZipFile zipFile, @Nonnull ZipEntry zipEntry) throws IOException {
      BufferedInputStream inputStream = new BufferedInputStream(zipFile.getInputStream(zipEntry));

      boolean var5;
      try {
         DexUtil.verifyDexHeader(inputStream);
         return true;
      } catch (DexBackedDexFile.NotADexFile var11) {
         var5 = false;
         return var5;
      } catch (DexUtil.InvalidFile var12) {
         var5 = false;
         return var5;
      } catch (DexUtil.UnsupportedFile var13) {
         var5 = false;
      } finally {
         inputStream.close();
      }

      return var5;
   }

   protected ZipFile getZipFile() throws IOException {
      try {
         return new ZipFile(this.zipFilePath);
      } catch (IOException var2) {
         throw new ZipDexContainer.NotAZipFileException();
      }
   }

   @Nonnull
   protected ZipDexContainer.ZipDexFile loadEntry(@Nonnull ZipFile zipFile, @Nonnull ZipEntry zipEntry) throws IOException {
      InputStream inputStream = zipFile.getInputStream(zipEntry);

      ZipDexContainer.ZipDexFile var5;
      try {
         byte[] buf = ByteStreams.toByteArray(inputStream);
         var5 = new ZipDexContainer.ZipDexFile(this.opcodes, buf, zipEntry.getName());
      } finally {
         inputStream.close();
      }

      return var5;
   }

   public static class NotAZipFileException extends RuntimeException {
   }

   public class ZipDexFile extends DexBackedDexFile implements MultiDexContainer.MultiDexFile {
      private final String entryName;

      protected ZipDexFile(@Nonnull Opcodes opcodes, @Nonnull byte[] buf, @Nonnull String entryName) {
         super(opcodes, buf, 0);
         this.entryName = entryName;
      }

      @Nonnull
      public String getEntryName() {
         return this.entryName;
      }

      @Nonnull
      public MultiDexContainer getContainer() {
         return ZipDexContainer.this;
      }
   }
}
