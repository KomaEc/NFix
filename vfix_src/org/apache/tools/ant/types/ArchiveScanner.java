package org.apache.tools.ant.types;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.types.resources.FileResourceIterator;

public abstract class ArchiveScanner extends DirectoryScanner {
   protected File srcFile;
   private Resource src;
   private Resource lastScannedResource;
   private TreeMap fileEntries = new TreeMap();
   private TreeMap dirEntries = new TreeMap();
   private TreeMap matchFileEntries = new TreeMap();
   private TreeMap matchDirEntries = new TreeMap();
   private String encoding;

   public void scan() {
      if (this.src != null) {
         super.scan();
      }
   }

   public void setSrc(File srcFile) {
      this.setSrc((Resource)(new FileResource(srcFile)));
   }

   public void setSrc(Resource src) {
      this.src = src;
      if (src instanceof FileResource) {
         this.srcFile = ((FileResource)src).getFile();
      }

   }

   public void setEncoding(String encoding) {
      this.encoding = encoding;
   }

   public String[] getIncludedFiles() {
      if (this.src == null) {
         return super.getIncludedFiles();
      } else {
         this.scanme();
         Set s = this.matchFileEntries.keySet();
         return (String[])((String[])s.toArray(new String[s.size()]));
      }
   }

   public int getIncludedFilesCount() {
      if (this.src == null) {
         return super.getIncludedFilesCount();
      } else {
         this.scanme();
         return this.matchFileEntries.size();
      }
   }

   public String[] getIncludedDirectories() {
      if (this.src == null) {
         return super.getIncludedDirectories();
      } else {
         this.scanme();
         Set s = this.matchDirEntries.keySet();
         return (String[])((String[])s.toArray(new String[s.size()]));
      }
   }

   public int getIncludedDirsCount() {
      if (this.src == null) {
         return super.getIncludedDirsCount();
      } else {
         this.scanme();
         return this.matchDirEntries.size();
      }
   }

   Iterator getResourceFiles() {
      if (this.src == null) {
         return new FileResourceIterator(this.getBasedir(), this.getIncludedFiles());
      } else {
         this.scanme();
         return this.matchFileEntries.values().iterator();
      }
   }

   Iterator getResourceDirectories() {
      if (this.src == null) {
         return new FileResourceIterator(this.getBasedir(), this.getIncludedDirectories());
      } else {
         this.scanme();
         return this.matchDirEntries.values().iterator();
      }
   }

   public void init() {
      if (this.includes == null) {
         this.includes = new String[1];
         this.includes[0] = "**";
      }

      if (this.excludes == null) {
         this.excludes = new String[0];
      }

   }

   public boolean match(String path) {
      String vpath = path.replace('/', File.separatorChar).replace('\\', File.separatorChar);
      return this.isIncluded(vpath) && !this.isExcluded(vpath);
   }

   public Resource getResource(String name) {
      if (this.src == null) {
         return super.getResource(name);
      } else if (name.equals("")) {
         return new Resource("", true, Long.MAX_VALUE, true);
      } else {
         this.scanme();
         if (this.fileEntries.containsKey(name)) {
            return (Resource)this.fileEntries.get(name);
         } else {
            name = trimSeparator(name);
            return this.dirEntries.containsKey(name) ? (Resource)this.dirEntries.get(name) : new Resource(name);
         }
      }
   }

   protected abstract void fillMapsFromArchive(Resource var1, String var2, Map var3, Map var4, Map var5, Map var6);

   private void scanme() {
      Resource thisresource = new Resource(this.src.getName(), this.src.isExists(), this.src.getLastModified());
      if (this.lastScannedResource == null || !this.lastScannedResource.getName().equals(thisresource.getName()) || this.lastScannedResource.getLastModified() != thisresource.getLastModified()) {
         this.init();
         this.fileEntries.clear();
         this.dirEntries.clear();
         this.matchFileEntries.clear();
         this.matchDirEntries.clear();
         this.fillMapsFromArchive(this.src, this.encoding, this.fileEntries, this.matchFileEntries, this.dirEntries, this.matchDirEntries);
         this.lastScannedResource = thisresource;
      }
   }

   protected static final String trimSeparator(String s) {
      return s.endsWith("/") ? s.substring(0, s.length() - 1) : s;
   }
}
