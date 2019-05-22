package org.jf.dexlib2;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.DexBackedOdexFile;
import org.jf.dexlib2.dexbacked.OatFile;
import org.jf.dexlib2.dexbacked.ZipDexContainer;
import org.jf.dexlib2.iface.DexFile;
import org.jf.dexlib2.iface.MultiDexContainer;
import org.jf.dexlib2.writer.pool.DexPool;
import org.jf.util.ExceptionWithContext;

public final class DexFileFactory {
   @Nonnull
   public static DexBackedDexFile loadDexFile(@Nonnull String path, @Nonnull Opcodes opcodes) throws IOException {
      return loadDexFile(new File(path), opcodes);
   }

   @Nonnull
   public static DexBackedDexFile loadDexFile(@Nonnull File file, @Nonnull Opcodes opcodes) throws IOException {
      if (!file.exists()) {
         throw new DexFileFactory.DexFileNotFoundException("%s does not exist", new Object[]{file.getName()});
      } else {
         try {
            ZipDexContainer container = new ZipDexContainer(file, opcodes);
            return (new DexFileFactory.DexEntryFinder(file.getPath(), container)).findEntry("classes.dex", true);
         } catch (ZipDexContainer.NotAZipFileException var16) {
            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file));

            DexBackedOdexFile var18;
            try {
               DexBackedDexFile var19 = DexBackedDexFile.fromInputStream(opcodes, inputStream);
               return var19;
            } catch (DexBackedDexFile.NotADexFile var14) {
               try {
                  var18 = DexBackedOdexFile.fromInputStream(opcodes, inputStream);
               } catch (DexBackedOdexFile.NotAnOdexFile var13) {
                  OatFile oatFile = null;

                  try {
                     oatFile = OatFile.fromInputStream(inputStream, new DexFileFactory.FilenameVdexProvider(file));
                  } catch (OatFile.NotAnOatFileException var12) {
                  }

                  if (oatFile != null) {
                     if (oatFile.isSupportedVersion() == 0) {
                        throw new DexFileFactory.UnsupportedOatVersionException(oatFile);
                     }

                     List<OatFile.OatDexFile> oatDexFiles = oatFile.getDexFiles();
                     if (oatDexFiles.size() == 0) {
                        throw new DexFileFactory.DexFileNotFoundException("Oat file %s contains no dex files", new Object[]{file.getName()});
                     }

                     DexBackedDexFile var5 = (DexBackedDexFile)oatDexFiles.get(0);
                     return var5;
                  }

                  throw new DexFileFactory.UnsupportedFileTypeException("%s is not an apk, dex, odex or oat file.", new Object[]{file.getPath()});
               }
            } finally {
               inputStream.close();
            }

            return var18;
         }
      }
   }

   public static DexBackedDexFile loadDexEntry(@Nonnull File file, @Nonnull String dexEntry, boolean exactMatch, @Nonnull Opcodes opcodes) throws IOException {
      if (!file.exists()) {
         throw new DexFileFactory.DexFileNotFoundException("Container file %s does not exist", new Object[]{file.getName()});
      } else {
         try {
            ZipDexContainer container = new ZipDexContainer(file, opcodes);
            return (new DexFileFactory.DexEntryFinder(file.getPath(), container)).findEntry(dexEntry, exactMatch);
         } catch (ZipDexContainer.NotAZipFileException var14) {
            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file));

            DexBackedDexFile var7;
            try {
               OatFile oatFile = null;

               try {
                  oatFile = OatFile.fromInputStream(inputStream, new DexFileFactory.FilenameVdexProvider(file));
               } catch (OatFile.NotAnOatFileException var12) {
               }

               if (oatFile == null) {
                  throw new DexFileFactory.UnsupportedFileTypeException("%s is not an apk or oat file.", new Object[]{file.getPath()});
               }

               if (oatFile.isSupportedVersion() == 0) {
                  throw new DexFileFactory.UnsupportedOatVersionException(oatFile);
               }

               List<OatFile.OatDexFile> oatDexFiles = oatFile.getDexFiles();
               if (oatDexFiles.size() == 0) {
                  throw new DexFileFactory.DexFileNotFoundException("Oat file %s contains no dex files", new Object[]{file.getName()});
               }

               var7 = (new DexFileFactory.DexEntryFinder(file.getPath(), oatFile)).findEntry(dexEntry, exactMatch);
            } finally {
               inputStream.close();
            }

            return var7;
         }
      }
   }

   public static MultiDexContainer<? extends DexBackedDexFile> loadDexContainer(@Nonnull File file, @Nonnull Opcodes opcodes) throws IOException {
      if (!file.exists()) {
         throw new DexFileFactory.DexFileNotFoundException("%s does not exist", new Object[]{file.getName()});
      } else {
         ZipDexContainer zipDexContainer = new ZipDexContainer(file, opcodes);
         if (zipDexContainer.isZipFile()) {
            return zipDexContainer;
         } else {
            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file));

            OatFile var5;
            try {
               DexFileFactory.SingletonMultiDexContainer var17;
               try {
                  DexBackedDexFile dexFile = DexBackedDexFile.fromInputStream(opcodes, inputStream);
                  var17 = new DexFileFactory.SingletonMultiDexContainer(file.getPath(), dexFile);
                  return var17;
               } catch (DexBackedDexFile.NotADexFile var13) {
                  try {
                     DexBackedOdexFile odexFile = DexBackedOdexFile.fromInputStream(opcodes, inputStream);
                     var17 = new DexFileFactory.SingletonMultiDexContainer(file.getPath(), odexFile);
                     return var17;
                  } catch (DexBackedOdexFile.NotAnOdexFile var12) {
                     OatFile oatFile = null;

                     try {
                        oatFile = OatFile.fromInputStream(inputStream, new DexFileFactory.FilenameVdexProvider(file));
                     } catch (OatFile.NotAnOatFileException var11) {
                     }

                     if (oatFile == null) {
                        throw new DexFileFactory.UnsupportedFileTypeException("%s is not an apk, dex, odex or oat file.", new Object[]{file.getPath()});
                     }

                     if (oatFile.isSupportedVersion() == 0) {
                        throw new DexFileFactory.UnsupportedOatVersionException(oatFile);
                     }

                     var5 = oatFile;
                  }
               }
            } finally {
               inputStream.close();
            }

            return var5;
         }
      }
   }

   public static void writeDexFile(@Nonnull String path, @Nonnull DexFile dexFile) throws IOException {
      DexPool.writeTo(path, dexFile);
   }

   private DexFileFactory() {
   }

   private static boolean fullEntryMatch(@Nonnull String entry, @Nonnull String targetEntry) {
      if (entry.equals(targetEntry)) {
         return true;
      } else {
         if (entry.charAt(0) == '/') {
            entry = entry.substring(1);
         }

         if (targetEntry.charAt(0) == '/') {
            targetEntry = targetEntry.substring(1);
         }

         return entry.equals(targetEntry);
      }
   }

   private static boolean partialEntryMatch(String entry, String targetEntry) {
      if (entry.equals(targetEntry)) {
         return true;
      } else if (!entry.endsWith(targetEntry)) {
         return false;
      } else {
         char precedingChar = entry.charAt(entry.length() - targetEntry.length() - 1);
         char firstTargetChar = targetEntry.charAt(0);
         return firstTargetChar == ':' || firstTargetChar == '/' || precedingChar == ':' || precedingChar == '/';
      }
   }

   public static class FilenameVdexProvider implements OatFile.VdexProvider {
      private final File vdexFile;
      @Nullable
      private byte[] buf = null;
      private boolean loadedVdex = false;

      public FilenameVdexProvider(File oatFile) {
         File oatParent = oatFile.getAbsoluteFile().getParentFile();
         String baseName = Files.getNameWithoutExtension(oatFile.getAbsolutePath());
         this.vdexFile = new File(oatParent, baseName + ".vdex");
      }

      @Nullable
      public byte[] getVdex() {
         if (!this.loadedVdex) {
            if (this.vdexFile.exists()) {
               try {
                  this.buf = ByteStreams.toByteArray((InputStream)(new FileInputStream(this.vdexFile)));
               } catch (FileNotFoundException var2) {
                  this.buf = null;
               } catch (IOException var3) {
                  throw new RuntimeException(var3);
               }
            }

            this.loadedVdex = true;
         }

         return this.buf;
      }
   }

   private static class SingletonMultiDexContainer implements MultiDexContainer<DexBackedDexFile> {
      private final String entryName;
      private final DexBackedDexFile dexFile;

      public SingletonMultiDexContainer(@Nonnull String entryName, @Nonnull DexBackedDexFile dexFile) {
         this.entryName = entryName;
         this.dexFile = dexFile;
      }

      @Nonnull
      public List<String> getDexEntryNames() throws IOException {
         return ImmutableList.of(this.entryName);
      }

      @Nullable
      public DexBackedDexFile getEntry(@Nonnull String entryName) throws IOException {
         return entryName.equals(this.entryName) ? this.dexFile : null;
      }

      @Nonnull
      public Opcodes getOpcodes() {
         return this.dexFile.getOpcodes();
      }
   }

   protected static class DexEntryFinder {
      private final String filename;
      private final MultiDexContainer<? extends DexBackedDexFile> dexContainer;

      public DexEntryFinder(@Nonnull String filename, @Nonnull MultiDexContainer<? extends DexBackedDexFile> dexContainer) {
         this.filename = filename;
         this.dexContainer = dexContainer;
      }

      @Nonnull
      public DexBackedDexFile findEntry(@Nonnull String targetEntry, boolean exactMatch) throws IOException {
         if (exactMatch) {
            try {
               DexBackedDexFile dexFile = (DexBackedDexFile)this.dexContainer.getEntry(targetEntry);
               if (dexFile == null) {
                  throw new DexFileFactory.DexFileNotFoundException("Could not find entry %s in %s.", new Object[]{targetEntry, this.filename});
               } else {
                  return dexFile;
               }
            } catch (DexBackedDexFile.NotADexFile var9) {
               throw new DexFileFactory.UnsupportedFileTypeException("Entry %s in %s is not a dex file", new Object[]{targetEntry, this.filename});
            }
         } else {
            List<String> fullMatches = Lists.newArrayList();
            List<DexBackedDexFile> fullEntries = Lists.newArrayList();
            List<String> partialMatches = Lists.newArrayList();
            List<DexBackedDexFile> partialEntries = Lists.newArrayList();
            Iterator var7 = this.dexContainer.getDexEntryNames().iterator();

            while(var7.hasNext()) {
               String entry = (String)var7.next();
               if (DexFileFactory.fullEntryMatch(entry, targetEntry)) {
                  fullMatches.add(entry);
                  fullEntries.add(this.dexContainer.getEntry(entry));
               } else if (DexFileFactory.partialEntryMatch(entry, targetEntry)) {
                  partialMatches.add(entry);
                  partialEntries.add(this.dexContainer.getEntry(entry));
               }
            }

            if (fullEntries.size() == 1) {
               try {
                  DexBackedDexFile dexFile = (DexBackedDexFile)fullEntries.get(0);

                  assert dexFile != null;

                  return dexFile;
               } catch (DexBackedDexFile.NotADexFile var10) {
                  throw new DexFileFactory.UnsupportedFileTypeException("Entry %s in %s is not a dex file", new Object[]{fullMatches.get(0), this.filename});
               }
            } else if (fullEntries.size() > 1) {
               throw new DexFileFactory.MultipleMatchingDexEntriesException(String.format("Multiple entries in %s match %s: %s", this.filename, targetEntry, Joiner.on(", ").join((Iterable)fullMatches)), new Object[0]);
            } else if (partialEntries.size() == 0) {
               throw new DexFileFactory.DexFileNotFoundException("Could not find a dex entry in %s matching %s", new Object[]{this.filename, targetEntry});
            } else if (partialEntries.size() > 1) {
               throw new DexFileFactory.MultipleMatchingDexEntriesException(String.format("Multiple dex entries in %s match %s: %s", this.filename, targetEntry, Joiner.on(", ").join((Iterable)partialMatches)), new Object[0]);
            } else {
               return (DexBackedDexFile)partialEntries.get(0);
            }
         }
      }
   }

   public static class UnsupportedFileTypeException extends ExceptionWithContext {
      public UnsupportedFileTypeException(@Nonnull String message, Object... formatArgs) {
         super(String.format(message, formatArgs));
      }
   }

   public static class MultipleMatchingDexEntriesException extends ExceptionWithContext {
      public MultipleMatchingDexEntriesException(@Nonnull String message, Object... formatArgs) {
         super(String.format(message, formatArgs));
      }
   }

   public static class UnsupportedOatVersionException extends ExceptionWithContext {
      @Nonnull
      public final OatFile oatFile;

      public UnsupportedOatVersionException(@Nonnull OatFile oatFile) {
         super("Unsupported oat version: %d", oatFile.getOatVersion());
         this.oatFile = oatFile;
      }
   }

   public static class DexFileNotFoundException extends ExceptionWithContext {
      public DexFileNotFoundException(@Nullable String message, Object... formatArgs) {
         super(message, formatArgs);
      }
   }
}
