package com.gzoltar.shaded.org.pitest.classpath;

import com.gzoltar.shaded.org.pitest.functional.F;
import com.gzoltar.shaded.org.pitest.functional.FCollection;
import com.gzoltar.shaded.org.pitest.functional.predicate.Predicate;
import com.gzoltar.shaded.org.pitest.util.Log;
import com.gzoltar.shaded.org.pitest.util.PitError;
import com.gzoltar.shaded.org.pitest.util.StreamUtil;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.zip.ZipException;

public class ClassPath {
   private static final Logger LOG = Log.getLogger();
   private final CompoundClassPathRoot root;

   public ClassPath() {
      this(getClassPathElementsAsFiles());
   }

   public ClassPath(ClassPathRoot... roots) {
      this(Arrays.asList(roots));
   }

   public ClassPath(List<ClassPathRoot> roots) {
      this.root = new CompoundClassPathRoot(roots);
   }

   public ClassPath(Collection<File> files) {
      this(createRoots(FCollection.filter(files, exists())));
   }

   private static F<File, Boolean> exists() {
      return new F<File, Boolean>() {
         public Boolean apply(File a) {
            return a.exists() && a.canRead();
         }
      };
   }

   public Collection<String> classNames() {
      return this.root.classNames();
   }

   private static List<ClassPathRoot> createRoots(Collection<File> files) {
      Object lastFile = null;

      try {
         List<ClassPathRoot> rs = new ArrayList();
         Iterator i$ = files.iterator();

         while(i$.hasNext()) {
            File f = (File)i$.next();
            if (f.isDirectory()) {
               rs.add(new DirectoryClassPathRoot(f));
            } else {
               try {
                  if (!f.canRead()) {
                     throw new IOException("Can't read the file " + f);
                  }

                  rs.add(new ArchiveClassPathRoot(f));
               } catch (ZipException var6) {
                  LOG.warning("Can't open the archive " + f);
               }
            }
         }

         return rs;
      } catch (IOException var7) {
         throw new PitError("Error handling file " + lastFile, var7);
      }
   }

   public byte[] getClassData(String classname) throws IOException {
      InputStream is = this.root.getData(classname);
      if (is != null) {
         byte[] var3;
         try {
            var3 = StreamUtil.streamToByteArray(is);
         } finally {
            is.close();
         }

         return var3;
      } else {
         return null;
      }
   }

   public URL findResource(String name) {
      try {
         return this.root.getResource(name);
      } catch (IOException var3) {
         return null;
      }
   }

   public static Collection<String> getClassPathElementsAsPaths() {
      Set<String> filesAsString = new LinkedHashSet();
      FCollection.mapTo(getClassPathElementsAsFiles(), fileToString(), filesAsString);
      return filesAsString;
   }

   private static F<File, String> fileToString() {
      return new F<File, String>() {
         public String apply(File file) {
            return file.getPath();
         }
      };
   }

   public static Collection<File> getClassPathElementsAsFiles() {
      Set<File> us = new LinkedHashSet();
      FCollection.mapTo(getClassPathElementsAsAre(), stringToCanonicalFile(), us);
      return us;
   }

   private static F<String, File> stringToCanonicalFile() {
      return new F<String, File>() {
         public File apply(String fileAsString) {
            try {
               return (new File(fileAsString)).getCanonicalFile();
            } catch (IOException var3) {
               throw new PitError("Error transforming classpath element " + fileAsString, var3);
            }
         }
      };
   }

   private static List<String> getClassPathElementsAsAre() {
      String classPath = System.getProperty("java.class.path");
      String separator = File.pathSeparator;
      return (List)(classPath != null ? Arrays.asList(classPath.split(separator)) : new ArrayList());
   }

   public Collection<String> findClasses(Predicate<String> nameFilter) {
      return FCollection.filter(this.classNames(), nameFilter);
   }

   public String getLocalClassPath() {
      return (String)this.root.cacheLocation().value();
   }

   public ClassPath getComponent(Predicate<ClassPathRoot> predicate) {
      return new ClassPath((ClassPathRoot[])FCollection.filter(this.root, predicate).toArray(new ClassPathRoot[0]));
   }
}
