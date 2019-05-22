package org.jf.dexlib2.analysis;

import com.beust.jcommander.internal.Sets;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.DexFileFactory;
import org.jf.dexlib2.Opcodes;
import org.jf.dexlib2.dexbacked.DexBackedOdexFile;
import org.jf.dexlib2.dexbacked.OatFile;
import org.jf.dexlib2.iface.DexFile;
import org.jf.dexlib2.iface.MultiDexContainer;

public class ClassPathResolver {
   private final Iterable<String> classPathDirs;
   private final Opcodes opcodes;
   private final Set<File> loadedFiles;
   private final List<ClassProvider> classProviders;

   public ClassPathResolver(@Nonnull List<String> bootClassPathDirs, @Nonnull List<String> bootClassPathEntries, @Nonnull List<String> extraClassPathEntries, @Nonnull DexFile dexFile) throws IOException {
      this(bootClassPathDirs, bootClassPathEntries, extraClassPathEntries, dexFile, true);
   }

   public ClassPathResolver(@Nonnull List<String> bootClassPathDirs, @Nonnull List<String> extraClassPathEntries, @Nonnull DexFile dexFile) throws IOException {
      this(bootClassPathDirs, (List)null, extraClassPathEntries, dexFile, true);
   }

   private ClassPathResolver(@Nonnull List<String> bootClassPathDirs, @Nullable List<String> bootClassPathEntries, @Nonnull List<String> extraClassPathEntries, @Nonnull DexFile dexFile, boolean unused) throws IOException {
      this.loadedFiles = Sets.newHashSet();
      this.classProviders = Lists.newArrayList();
      this.classPathDirs = bootClassPathDirs;
      this.opcodes = dexFile.getOpcodes();
      if (bootClassPathEntries == null) {
         bootClassPathEntries = getDefaultBootClassPath(dexFile, this.opcodes.api);
      }

      Iterator var6 = bootClassPathEntries.iterator();

      String entry;
      while(var6.hasNext()) {
         entry = (String)var6.next();

         String jarEntry;
         try {
            this.loadLocalOrDeviceBootClassPathEntry(entry);
         } catch (ClassPathResolver.NoDexException var16) {
            if (!entry.endsWith(".jar")) {
               throw new ClassPathResolver.ResolveException(var16);
            }

            jarEntry = entry.substring(0, entry.length() - 4) + ".odex";

            try {
               this.loadLocalOrDeviceBootClassPathEntry(jarEntry);
            } catch (ClassPathResolver.NoDexException var14) {
               throw new ClassPathResolver.ResolveException("Neither %s nor %s contain a dex file", new Object[]{entry, jarEntry});
            } catch (ClassPathResolver.NotFoundException var15) {
               throw new ClassPathResolver.ResolveException(var16);
            }
         } catch (ClassPathResolver.NotFoundException var17) {
            if (!entry.endsWith(".odex")) {
               throw new ClassPathResolver.ResolveException(var17);
            }

            jarEntry = entry.substring(0, entry.length() - 5) + ".jar";

            try {
               this.loadLocalOrDeviceBootClassPathEntry(jarEntry);
            } catch (ClassPathResolver.NoDexException var12) {
               throw new ClassPathResolver.ResolveException("Neither %s nor %s contain a dex file", new Object[]{entry, jarEntry});
            } catch (ClassPathResolver.NotFoundException var13) {
               throw new ClassPathResolver.ResolveException(var17);
            }
         }
      }

      var6 = extraClassPathEntries.iterator();

      while(var6.hasNext()) {
         entry = (String)var6.next();

         try {
            this.loadLocalClassPathEntry(entry);
         } catch (ClassPathResolver.NoDexException var11) {
            throw new ClassPathResolver.ResolveException(var11);
         }
      }

      if (dexFile instanceof MultiDexContainer.MultiDexFile) {
         MultiDexContainer<? extends MultiDexContainer.MultiDexFile> container = ((MultiDexContainer.MultiDexFile)dexFile).getContainer();
         Iterator var19 = container.getDexEntryNames().iterator();

         while(var19.hasNext()) {
            String entry = (String)var19.next();
            this.classProviders.add(new DexClassProvider(container.getEntry(entry)));
         }
      } else {
         this.classProviders.add(new DexClassProvider(dexFile));
      }

   }

   @Nonnull
   public List<ClassProvider> getResolvedClassProviders() {
      return this.classProviders;
   }

   private boolean loadLocalClassPathEntry(@Nonnull String entry) throws ClassPathResolver.NoDexException, IOException {
      File entryFile = new File(entry);
      if (entryFile.exists() && entryFile.isFile()) {
         try {
            this.loadEntry(entryFile, true);
            return true;
         } catch (DexFileFactory.UnsupportedFileTypeException var4) {
            throw new ClassPathResolver.ResolveException(var4, "Couldn't load classpath entry %s", new Object[]{entry});
         }
      } else {
         return false;
      }
   }

   private void loadLocalOrDeviceBootClassPathEntry(@Nonnull String entry) throws IOException, ClassPathResolver.NoDexException, ClassPathResolver.NotFoundException {
      if (!this.loadLocalClassPathEntry(entry)) {
         List<String> pathComponents = splitDevicePath(entry);
         Joiner pathJoiner = Joiner.on(File.pathSeparatorChar);
         Iterator var4 = this.classPathDirs.iterator();

         while(true) {
            File directoryFile;
            do {
               if (!var4.hasNext()) {
                  throw new ClassPathResolver.NotFoundException("Could not find classpath entry %s", new Object[]{entry});
               }

               String directory = (String)var4.next();
               directoryFile = new File(directory);
            } while(!directoryFile.exists());

            for(int i = 0; i < pathComponents.size(); ++i) {
               String partialPath = pathJoiner.join((Iterable)pathComponents.subList(i, pathComponents.size()));
               File entryFile = new File(directoryFile, partialPath);
               if (entryFile.exists() && entryFile.isFile()) {
                  this.loadEntry(entryFile, true);
                  return;
               }
            }
         }
      }
   }

   private void loadEntry(@Nonnull File entryFile, boolean loadOatDependencies) throws IOException, ClassPathResolver.NoDexException {
      if (!this.loadedFiles.contains(entryFile)) {
         MultiDexContainer container;
         try {
            container = DexFileFactory.loadDexContainer(entryFile, this.opcodes);
         } catch (DexFileFactory.UnsupportedFileTypeException var9) {
            throw new ClassPathResolver.ResolveException(var9);
         }

         List<String> entryNames = container.getDexEntryNames();
         if (entryNames.size() == 0) {
            throw new ClassPathResolver.NoDexException("%s contains no dex file", new Object[]{entryFile});
         } else {
            this.loadedFiles.add(entryFile);
            Iterator var5 = entryNames.iterator();

            while(var5.hasNext()) {
               String entryName = (String)var5.next();
               this.classProviders.add(new DexClassProvider(container.getEntry(entryName)));
            }

            if (loadOatDependencies && container instanceof OatFile) {
               List<String> oatDependencies = ((OatFile)container).getBootClassPath();
               if (!oatDependencies.isEmpty()) {
                  try {
                     this.loadOatDependencies(entryFile.getParentFile(), oatDependencies);
                  } catch (ClassPathResolver.NotFoundException var7) {
                     throw new ClassPathResolver.ResolveException(var7, "Error while loading oat file %s", new Object[]{entryFile});
                  } catch (ClassPathResolver.NoDexException var8) {
                     throw new ClassPathResolver.ResolveException(var8, "Error while loading dependencies for oat file %s", new Object[]{entryFile});
                  }
               }
            }

         }
      }
   }

   @Nonnull
   private static List<String> splitDevicePath(@Nonnull String path) {
      return Lists.newArrayList(Splitter.on('/').split(path));
   }

   private void loadOatDependencies(@Nonnull File directory, @Nonnull List<String> oatDependencies) throws IOException, ClassPathResolver.NoDexException, ClassPathResolver.NotFoundException {
      Iterator var3 = oatDependencies.iterator();

      while(var3.hasNext()) {
         String oatDependency = (String)var3.next();
         String oatDependencyName = this.getFilenameForOatDependency(oatDependency);
         File file = new File(directory, oatDependencyName);
         if (!file.exists()) {
            throw new ClassPathResolver.NotFoundException("Cannot find dependency %s in %s", new Object[]{oatDependencyName, directory});
         }

         this.loadEntry(file, false);
      }

   }

   @Nonnull
   private String getFilenameForOatDependency(String oatDependency) {
      int index = oatDependency.lastIndexOf(47);
      String dependencyLeaf = oatDependency.substring(index + 1);
      return dependencyLeaf.endsWith(".art") ? dependencyLeaf.substring(0, dependencyLeaf.length() - 4) + ".oat" : dependencyLeaf;
   }

   @Nonnull
   private static List<String> getDefaultBootClassPath(@Nonnull DexFile dexFile, int apiLevel) {
      if (dexFile instanceof OatFile.OatDexFile) {
         List<String> bcp = ((OatFile.OatDexFile)dexFile).getContainer().getBootClassPath();
         if (!bcp.isEmpty()) {
            for(int i = 0; i < bcp.size(); ++i) {
               String entry = (String)bcp.get(i);
               if (entry.endsWith(".art")) {
                  bcp.set(i, entry.substring(0, entry.length() - 4) + ".oat");
               }
            }

            return bcp;
         } else {
            return Lists.newArrayList((Object[])("boot.oat"));
         }
      } else if (dexFile instanceof DexBackedOdexFile) {
         return ((DexBackedOdexFile)dexFile).getDependencies();
      } else if (apiLevel <= 8) {
         return Lists.newArrayList((Object[])("/system/framework/core.jar", "/system/framework/ext.jar", "/system/framework/framework.jar", "/system/framework/android.policy.jar", "/system/framework/services.jar"));
      } else if (apiLevel <= 11) {
         return Lists.newArrayList((Object[])("/system/framework/core.jar", "/system/framework/bouncycastle.jar", "/system/framework/ext.jar", "/system/framework/framework.jar", "/system/framework/android.policy.jar", "/system/framework/services.jar", "/system/framework/core-junit.jar"));
      } else if (apiLevel <= 13) {
         return Lists.newArrayList((Object[])("/system/framework/core.jar", "/system/framework/apache-xml.jar", "/system/framework/bouncycastle.jar", "/system/framework/ext.jar", "/system/framework/framework.jar", "/system/framework/android.policy.jar", "/system/framework/services.jar", "/system/framework/core-junit.jar"));
      } else if (apiLevel <= 15) {
         return Lists.newArrayList((Object[])("/system/framework/core.jar", "/system/framework/core-junit.jar", "/system/framework/bouncycastle.jar", "/system/framework/ext.jar", "/system/framework/framework.jar", "/system/framework/android.policy.jar", "/system/framework/services.jar", "/system/framework/apache-xml.jar", "/system/framework/filterfw.jar"));
      } else if (apiLevel <= 17) {
         return Lists.newArrayList((Object[])("/system/framework/core.jar", "/system/framework/core-junit.jar", "/system/framework/bouncycastle.jar", "/system/framework/ext.jar", "/system/framework/framework.jar", "/system/framework/telephony-common.jar", "/system/framework/mms-common.jar", "/system/framework/android.policy.jar", "/system/framework/services.jar", "/system/framework/apache-xml.jar"));
      } else if (apiLevel <= 18) {
         return Lists.newArrayList((Object[])("/system/framework/core.jar", "/system/framework/core-junit.jar", "/system/framework/bouncycastle.jar", "/system/framework/ext.jar", "/system/framework/framework.jar", "/system/framework/telephony-common.jar", "/system/framework/voip-common.jar", "/system/framework/mms-common.jar", "/system/framework/android.policy.jar", "/system/framework/services.jar", "/system/framework/apache-xml.jar"));
      } else if (apiLevel <= 19) {
         return Lists.newArrayList((Object[])("/system/framework/core.jar", "/system/framework/conscrypt.jar", "/system/framework/core-junit.jar", "/system/framework/bouncycastle.jar", "/system/framework/ext.jar", "/system/framework/framework.jar", "/system/framework/framework2.jar", "/system/framework/telephony-common.jar", "/system/framework/voip-common.jar", "/system/framework/mms-common.jar", "/system/framework/android.policy.jar", "/system/framework/services.jar", "/system/framework/apache-xml.jar", "/system/framework/webviewchromium.jar"));
      } else if (apiLevel <= 22) {
         return Lists.newArrayList((Object[])("/system/framework/core-libart.jar", "/system/framework/conscrypt.jar", "/system/framework/okhttp.jar", "/system/framework/core-junit.jar", "/system/framework/bouncycastle.jar", "/system/framework/ext.jar", "/system/framework/framework.jar", "/system/framework/telephony-common.jar", "/system/framework/voip-common.jar", "/system/framework/ims-common.jar", "/system/framework/mms-common.jar", "/system/framework/android.policy.jar", "/system/framework/apache-xml.jar"));
      } else {
         return apiLevel <= 23 ? Lists.newArrayList((Object[])("/system/framework/core-libart.jar", "/system/framework/conscrypt.jar", "/system/framework/okhttp.jar", "/system/framework/core-junit.jar", "/system/framework/bouncycastle.jar", "/system/framework/ext.jar", "/system/framework/framework.jar", "/system/framework/telephony-common.jar", "/system/framework/voip-common.jar", "/system/framework/ims-common.jar", "/system/framework/apache-xml.jar", "/system/framework/org.apache.http.legacy.boot.jar")) : Lists.newArrayList((Object[])("/system/framework/core-oj.jar", "/system/framework/core-libart.jar", "/system/framework/conscrypt.jar", "/system/framework/okhttp.jar", "/system/framework/core-junit.jar", "/system/framework/bouncycastle.jar", "/system/framework/ext.jar", "/system/framework/framework.jar", "/system/framework/telephony-common.jar", "/system/framework/voip-common.jar", "/system/framework/ims-common.jar", "/system/framework/apache-xml.jar", "/system/framework/org.apache.http.legacy.boot.jar"));
      }
   }

   public static class ResolveException extends RuntimeException {
      public ResolveException(String message, Object... formatArgs) {
         super(String.format(message, formatArgs));
      }

      public ResolveException(Throwable cause) {
         super(cause);
      }

      public ResolveException(Throwable cause, String message, Object... formatArgs) {
         super(String.format(message, formatArgs), cause);
      }
   }

   private static class NoDexException extends Exception {
      public NoDexException(String message, Object... formatArgs) {
         super(String.format(message, formatArgs));
      }
   }

   private static class NotFoundException extends Exception {
      public NotFoundException(String message, Object... formatArgs) {
         super(String.format(message, formatArgs));
      }
   }
}
