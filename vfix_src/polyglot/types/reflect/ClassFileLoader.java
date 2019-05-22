package polyglot.types.reflect;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import polyglot.frontend.ExtensionInfo;
import polyglot.main.Report;
import polyglot.util.InternalCompilerError;

public class ClassFileLoader {
   private ExtensionInfo extensionInfo;
   Map zipCache = new HashMap();
   Set packageCache = new HashSet();
   Map dirContentsCache = new HashMap();
   static final Object not_found = new Object();
   static Collection verbose = new HashSet();

   public ClassFileLoader(ExtensionInfo ext) {
      this.extensionInfo = ext;
   }

   public boolean packageExists(File dir, String name) {
      if (Report.should_report((Collection)verbose, 3)) {
         Report.report(3, "looking in " + dir + " for " + name.replace('.', File.separatorChar));
      }

      try {
         String entryName;
         if (!dir.getName().endsWith(".jar") && !dir.getName().endsWith(".zip")) {
            entryName = name.replace('.', File.separatorChar);
            File f = new File(dir, entryName);
            return f.exists() && f.isDirectory();
         } else {
            entryName = name.replace('.', '/');
            if (this.packageCache.contains(entryName)) {
               return true;
            } else {
               this.loadZip(dir);
               return this.packageCache.contains(entryName);
            }
         }
      } catch (FileNotFoundException var5) {
         return false;
      } catch (IOException var6) {
         throw new InternalCompilerError(var6);
      }
   }

   public ClassFile loadClass(File dir, String name) {
      if (Report.should_report((Collection)verbose, 3)) {
         Report.report(3, "looking in " + dir + " for " + name.replace('.', File.separatorChar) + ".class");
      }

      try {
         if (!dir.getName().endsWith(".jar") && !dir.getName().endsWith(".zip")) {
            return this.loadFromFile(name, dir);
         } else {
            ZipFile zip = this.loadZip(dir);
            String entryName = name.replace('.', '/') + ".class";
            return this.loadFromZip(dir, zip, entryName);
         }
      } catch (FileNotFoundException var5) {
         return null;
      } catch (IOException var6) {
         throw new InternalCompilerError(var6);
      }
   }

   ZipFile loadZip(File dir) throws IOException {
      Object o = this.zipCache.get(dir);
      if (o != not_found) {
         ZipFile zip = (ZipFile)o;
         if (zip != null) {
            return zip;
         }

         if (dir.exists()) {
            if (Report.should_report((Collection)verbose, 2)) {
               Report.report(2, "Opening zip " + dir);
            }

            Object zip;
            if (dir.getName().endsWith(".jar")) {
               zip = new JarFile(dir);
            } else {
               zip = new ZipFile(dir);
            }

            this.zipCache.put(dir, zip);
            Enumeration i = ((ZipFile)zip).entries();

            while(i.hasMoreElements()) {
               ZipEntry ei = (ZipEntry)i.nextElement();
               String n = ei.getName();

               for(int index = n.indexOf(47); index >= 0; index = n.indexOf(47, index + 1)) {
                  this.packageCache.add(n.substring(0, index));
               }
            }

            return (ZipFile)zip;
         }

         this.zipCache.put(dir, not_found);
      }

      throw new FileNotFoundException(dir.getAbsolutePath());
   }

   ClassFile loadFromZip(File source, ZipFile zip, String entryName) throws IOException {
      if (Report.should_report((Collection)verbose, 2)) {
         Report.report(2, "Looking for " + entryName + " in " + zip.getName());
      }

      if (zip != null) {
         ZipEntry entry = zip.getEntry(entryName);
         if (entry != null) {
            if (Report.should_report((Collection)verbose, 3)) {
               Report.report(3, "found zip entry " + entry);
            }

            InputStream in = zip.getInputStream(entry);
            ClassFile c = this.loadFromStream(source, in, entryName);
            in.close();
            return c;
         }
      }

      return null;
   }

   ClassFile loadFromFile(String name, File dir) throws IOException {
      Set dirContents = (Set)this.dirContentsCache.get(dir);
      int firstSeparator;
      if (dirContents == null) {
         dirContents = new HashSet();
         this.dirContentsCache.put(dir, dirContents);
         if (dir.exists() && dir.isDirectory()) {
            String[] contents = dir.list();

            for(firstSeparator = 0; firstSeparator < contents.length; ++firstSeparator) {
               ((Set)dirContents).add(contents[firstSeparator]);
            }
         }
      }

      StringBuffer filenameSB = new StringBuffer(name.length() + 8);
      firstSeparator = -1;
      filenameSB.append(name);

      for(int i = 0; i < filenameSB.length(); ++i) {
         if (filenameSB.charAt(i) == '.') {
            filenameSB.setCharAt(i, File.separatorChar);
            if (firstSeparator == -1) {
               firstSeparator = i;
            }
         }
      }

      filenameSB.append(".class");
      String filename = filenameSB.toString();
      String firstPart = firstSeparator == -1 ? filename : filename.substring(0, firstSeparator);
      if (!((Set)dirContents).contains(firstPart)) {
         return null;
      } else {
         File file = new File(dir, filename);
         FileInputStream in = new FileInputStream(file);
         if (Report.should_report((Collection)verbose, 3)) {
            Report.report(3, "found " + file);
         }

         ClassFile c = this.loadFromStream(file, in, name);
         in.close();
         return c;
      }
   }

   ClassFile loadFromStream(File source, InputStream in, String name) throws IOException {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      byte[] buf = new byte[4096];
      boolean var6 = false;

      int n;
      do {
         n = in.read(buf);
         if (n >= 0) {
            out.write(buf, 0, n);
         }
      } while(n >= 0);

      byte[] bytecode = out.toByteArray();

      try {
         if (Report.should_report((Collection)verbose, 3)) {
            Report.report(3, "defining class " + name);
         }

         return this.extensionInfo.createClassFile(source, bytecode);
      } catch (ClassFormatError var9) {
         throw new IOException(var9.getMessage());
      }
   }

   static {
      verbose.add("loader");
   }
}
