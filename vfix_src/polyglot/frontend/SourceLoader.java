package polyglot.frontend;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import polyglot.main.Report;
import polyglot.util.InternalCompilerError;

public class SourceLoader {
   protected ExtensionInfo sourceExt;
   protected Collection sourcePath;
   protected int caseInsensitive;
   protected Set loadedSources;
   protected Map directoryContentsCache;
   static File current_dir = null;

   public SourceLoader(ExtensionInfo sourceExt, Collection sourcePath) {
      this.sourcePath = sourcePath;
      this.sourceExt = sourceExt;
      this.directoryContentsCache = new HashMap();
      this.caseInsensitive = 0;
      this.loadedSources = new HashSet();
   }

   public FileSource fileSource(String fileName) throws IOException {
      this.setCaseInsensitive(fileName);
      File sourceFile = new File(fileName);
      if (!sourceFile.exists()) {
         throw new FileNotFoundException(fileName);
      } else if (this.loadedSources.contains(this.fileKey(sourceFile))) {
         throw new FileNotFoundException(fileName);
      } else {
         this.loadedSources.add(this.fileKey(sourceFile));
         String[] exts = this.sourceExt.fileExtensions();
         boolean ok = false;

         for(int i = 0; i < exts.length; ++i) {
            String ext = exts[i];
            if (fileName.endsWith("." + ext)) {
               ok = true;
               break;
            }
         }

         if (ok) {
            if (Report.should_report((String)"frontend", 2)) {
               Report.report(2, "Loading class from " + sourceFile);
            }

            return new FileSource(sourceFile);
         } else {
            String extString = "";

            for(int i = 0; i < exts.length; ++i) {
               if (exts.length == 2 && i == exts.length - 1) {
                  extString = extString + " or ";
               } else if (exts.length != 1 && i == exts.length - 1) {
                  extString = extString + ", or ";
               } else if (i != 0) {
                  extString = extString + ", ";
               }

               extString = extString + "\"." + exts[i] + "\"";
            }

            if (exts.length == 1) {
               throw new IOException("Source \"" + fileName + "\" does not have the extension " + extString + ".");
            } else {
               throw new IOException("Source \"" + fileName + "\" does not have any of the extensions " + extString + ".");
            }
         }
      }
   }

   protected static File current_dir() {
      if (current_dir == null) {
         current_dir = new File(System.getProperty("user.dir"));
      }

      return current_dir;
   }

   public boolean packageExists(String name) {
      String fileName = name.replace('.', File.separatorChar);
      Iterator i = this.sourcePath.iterator();

      File f;
      do {
         if (!i.hasNext()) {
            return false;
         }

         File directory = (File)i.next();
         f = new File(directory, fileName);
      } while(!f.exists() || !f.isDirectory());

      return true;
   }

   public FileSource classSource(String className) {
      String[] exts = this.sourceExt.fileExtensions();
      int k = 0;

      label68:
      while(k < exts.length) {
         String fileName = className.replace('.', File.separatorChar) + "." + exts[k];
         Iterator i = this.sourcePath.iterator();

         while(true) {
            File sourceFile;
            do {
               File directory;
               Object dirContents;
               String firstPart;
               do {
                  if (!i.hasNext()) {
                     ++k;
                     continue label68;
                  }

                  directory = (File)i.next();
                  dirContents = (Set)this.directoryContentsCache.get(directory);
                  if (dirContents == null) {
                     dirContents = new HashSet();
                     this.directoryContentsCache.put(directory, dirContents);
                     if (directory.exists()) {
                        String[] contents = directory.list();

                        for(int j = 0; j < contents.length; ++j) {
                           ((Set)dirContents).add(contents[j]);
                        }
                     }
                  }

                  int index = fileName.indexOf(File.separatorChar);
                  if (index < 0) {
                     index = fileName.length();
                  }

                  firstPart = fileName.substring(0, index);
               } while(!((Set)dirContents).contains(firstPart));

               if (directory != null && directory.equals(current_dir())) {
                  sourceFile = new File(fileName);
               } else {
                  sourceFile = new File(directory, fileName);
               }
            } while(this.loadedSources.contains(this.fileKey(sourceFile)));

            try {
               if (Report.should_report((String)"frontend", 2)) {
                  Report.report(2, "Loading " + className + " from " + sourceFile);
               }

               FileSource s = new FileSource(sourceFile);
               this.loadedSources.add(this.fileKey(sourceFile));
               return s;
            } catch (IOException var12) {
            }
         }
      }

      return null;
   }

   public Object fileKey(File file) {
      this.setCaseInsensitive(file.getAbsolutePath());
      return this.caseInsensitive() ? file.getAbsolutePath().toLowerCase() : file.getAbsolutePath();
   }

   public boolean caseInsensitive() {
      if (this.caseInsensitive == 0) {
         throw new InternalCompilerError("unknown case sensitivity");
      } else {
         return this.caseInsensitive == 1;
      }
   }

   protected void setCaseInsensitive(String fileName) {
      if (this.caseInsensitive == 0) {
         File f1 = new File(fileName.toUpperCase());
         File f2 = new File(fileName.toLowerCase());
         if (f1.equals(f2)) {
            this.caseInsensitive = 1;
         } else if (f1.exists() && f2.exists()) {
            boolean f1Exists = false;
            boolean f2Exists = false;
            File dir;
            if (f1.getParent() != null) {
               dir = new File(f1.getParent());
            } else {
               dir = current_dir();
            }

            File[] ls = dir.listFiles();

            for(int i = 0; i < ls.length; ++i) {
               if (f1.equals(ls[i])) {
                  f1Exists = true;
               }

               if (f2.equals(ls[i])) {
                  f2Exists = true;
               }
            }

            if (f1Exists && f2Exists) {
               this.caseInsensitive = -1;
            } else {
               this.caseInsensitive = 1;
            }
         } else {
            this.caseInsensitive = -1;
         }

      }
   }

   protected String canonicalize(String fileName) {
      return fileName;
   }
}
