package org.apache.tools.ant.taskdefs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.ZipFileSet;
import org.apache.tools.ant.types.spi.Service;
import org.apache.tools.zip.JarMarker;
import org.apache.tools.zip.ZipExtraField;
import org.apache.tools.zip.ZipOutputStream;

public class Jar extends Zip {
   private static final String INDEX_NAME = "META-INF/INDEX.LIST";
   private static final String MANIFEST_NAME = "META-INF/MANIFEST.MF";
   private List serviceList = new ArrayList();
   private Manifest configuredManifest;
   private Manifest savedConfiguredManifest;
   private Manifest filesetManifest;
   private Manifest originalManifest;
   private Jar.FilesetManifestConfig filesetManifestConfig;
   private boolean mergeManifestsMain = true;
   private Manifest manifest;
   private String manifestEncoding;
   private File manifestFile;
   private boolean index = false;
   private boolean createEmpty = false;
   private Vector rootEntries;
   private Path indexJars;
   private static final ZipExtraField[] JAR_MARKER = new ZipExtraField[]{JarMarker.getInstance()};
   protected String emptyBehavior = "create";

   public Jar() {
      this.archiveType = "jar";
      this.emptyBehavior = "create";
      this.setEncoding("UTF8");
      this.rootEntries = new Vector();
   }

   public void setWhenempty(Zip.WhenEmpty we) {
      this.log("JARs are never empty, they contain at least a manifest file", 1);
   }

   public void setWhenmanifestonly(Zip.WhenEmpty we) {
      this.emptyBehavior = we.getValue();
   }

   /** @deprecated */
   public void setJarfile(File jarFile) {
      this.setDestFile(jarFile);
   }

   public void setIndex(boolean flag) {
      this.index = flag;
   }

   public void setManifestEncoding(String manifestEncoding) {
      this.manifestEncoding = manifestEncoding;
   }

   public void addConfiguredManifest(Manifest newManifest) throws ManifestException {
      if (this.configuredManifest == null) {
         this.configuredManifest = newManifest;
      } else {
         this.configuredManifest.merge(newManifest);
      }

      this.savedConfiguredManifest = this.configuredManifest;
   }

   public void setManifest(File manifestFile) {
      if (!manifestFile.exists()) {
         throw new BuildException("Manifest file: " + manifestFile + " does not exist.", this.getLocation());
      } else {
         this.manifestFile = manifestFile;
      }
   }

   private Manifest getManifest(File manifestFile) {
      Manifest newManifest = null;
      FileInputStream fis = null;
      InputStreamReader isr = null;

      try {
         fis = new FileInputStream(manifestFile);
         if (this.manifestEncoding == null) {
            isr = new InputStreamReader(fis);
         } else {
            isr = new InputStreamReader(fis, this.manifestEncoding);
         }

         newManifest = this.getManifest((Reader)isr);
      } catch (UnsupportedEncodingException var14) {
         throw new BuildException("Unsupported encoding while reading manifest: " + var14.getMessage(), var14);
      } catch (IOException var15) {
         throw new BuildException("Unable to read manifest file: " + manifestFile + " (" + var15.getMessage() + ")", var15);
      } finally {
         if (isr != null) {
            try {
               isr.close();
            } catch (IOException var13) {
            }
         }

      }

      return newManifest;
   }

   private Manifest getManifestFromJar(File jarFile) throws IOException {
      ZipFile zf = null;

      ZipEntry ze;
      try {
         zf = new ZipFile(jarFile);
         Enumeration e = zf.entries();

         while(e.hasMoreElements()) {
            ze = (ZipEntry)e.nextElement();
            if (ze.getName().equalsIgnoreCase("META-INF/MANIFEST.MF")) {
               InputStreamReader isr = new InputStreamReader(zf.getInputStream(ze), "UTF-8");
               Manifest var6 = this.getManifest((Reader)isr);
               return var6;
            }
         }

         ze = null;
      } finally {
         if (zf != null) {
            try {
               zf.close();
            } catch (IOException var14) {
            }
         }

      }

      return ze;
   }

   private Manifest getManifest(Reader r) {
      Manifest newManifest = null;

      try {
         newManifest = new Manifest(r);
         return newManifest;
      } catch (ManifestException var4) {
         this.log("Manifest is invalid: " + var4.getMessage(), 0);
         throw new BuildException("Invalid Manifest: " + this.manifestFile, var4, this.getLocation());
      } catch (IOException var5) {
         throw new BuildException("Unable to read manifest file (" + var5.getMessage() + ")", var5);
      }
   }

   public void setFilesetmanifest(Jar.FilesetManifestConfig config) {
      this.filesetManifestConfig = config;
      this.mergeManifestsMain = "merge".equals(config.getValue());
      if (this.filesetManifestConfig != null && !this.filesetManifestConfig.getValue().equals("skip")) {
         this.doubleFilePass = true;
      }

   }

   public void addMetainf(ZipFileSet fs) {
      fs.setPrefix("META-INF/");
      super.addFileset(fs);
   }

   public void addConfiguredIndexJars(Path p) {
      if (this.indexJars == null) {
         this.indexJars = new Path(this.getProject());
      }

      this.indexJars.append(p);
   }

   public void addConfiguredService(Service service) {
      service.check();
      this.serviceList.add(service);
   }

   private void writeServices(ZipOutputStream zOut) throws IOException {
      Iterator serviceIterator = this.serviceList.iterator();

      while(serviceIterator.hasNext()) {
         Service service = (Service)serviceIterator.next();
         super.zipFile(service.getAsStream(), zOut, "META-INF/service/" + service.getType(), System.currentTimeMillis(), (File)null, 33188);
      }

   }

   protected void initZipOutputStream(ZipOutputStream zOut) throws IOException, BuildException {
      if (!this.skipWriting) {
         Manifest jarManifest = this.createManifest();
         this.writeManifest(zOut, jarManifest);
         this.writeServices(zOut);
      }

   }

   private Manifest createManifest() throws BuildException {
      try {
         Manifest finalManifest = Manifest.getDefaultManifest();
         if (this.manifest == null && this.manifestFile != null) {
            this.manifest = this.getManifest(this.manifestFile);
         }

         if (this.isInUpdateMode()) {
            finalManifest.merge(this.originalManifest);
         }

         finalManifest.merge(this.filesetManifest);
         finalManifest.merge(this.configuredManifest);
         finalManifest.merge(this.manifest, !this.mergeManifestsMain);
         return finalManifest;
      } catch (ManifestException var2) {
         this.log("Manifest is invalid: " + var2.getMessage(), 0);
         throw new BuildException("Invalid Manifest", var2, this.getLocation());
      }
   }

   private void writeManifest(ZipOutputStream zOut, Manifest manifest) throws IOException {
      Enumeration e = manifest.getWarnings();

      while(e.hasMoreElements()) {
         this.log("Manifest warning: " + (String)e.nextElement(), 1);
      }

      this.zipDir((File)null, zOut, "META-INF/", 16877, JAR_MARKER);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      OutputStreamWriter osw = new OutputStreamWriter(baos, "UTF-8");
      PrintWriter writer = new PrintWriter(osw);
      manifest.write(writer);
      writer.flush();
      ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
      super.zipFile(bais, zOut, "META-INF/MANIFEST.MF", System.currentTimeMillis(), (File)null, 33188);
      super.initZipOutputStream(zOut);
   }

   protected void finalizeZipOutputStream(ZipOutputStream zOut) throws IOException, BuildException {
      if (this.index) {
         this.createIndexList(zOut);
      }

   }

   private void createIndexList(ZipOutputStream zOut) throws IOException {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      PrintWriter writer = new PrintWriter(new OutputStreamWriter(baos, "UTF8"));
      writer.println("JarIndex-Version: 1.0");
      writer.println();
      writer.println(this.zipFile.getName());
      this.writeIndexLikeList(new ArrayList(this.addedDirs.keySet()), this.rootEntries, writer);
      writer.println();
      if (this.indexJars != null) {
         Manifest mf = this.createManifest();
         Manifest.Attribute classpath = mf.getMainSection().getAttribute("Class-Path");
         String[] cpEntries = null;
         int i;
         if (classpath != null && classpath.getValue() != null) {
            StringTokenizer tok = new StringTokenizer(classpath.getValue(), " ");
            cpEntries = new String[tok.countTokens()];

            for(i = 0; tok.hasMoreTokens(); cpEntries[i++] = tok.nextToken()) {
            }
         }

         String[] indexJarEntries = this.indexJars.list();

         for(i = 0; i < indexJarEntries.length; ++i) {
            String name = findJarName(indexJarEntries[i], cpEntries);
            if (name != null) {
               ArrayList dirs = new ArrayList();
               ArrayList files = new ArrayList();
               grabFilesAndDirs(indexJarEntries[i], dirs, files);
               if (dirs.size() + files.size() > 0) {
                  writer.println(name);
                  this.writeIndexLikeList(dirs, files, writer);
                  writer.println();
               }
            }
         }
      }

      writer.flush();
      ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
      super.zipFile(bais, zOut, "META-INF/INDEX.LIST", System.currentTimeMillis(), (File)null, 33188);
   }

   protected void zipFile(InputStream is, ZipOutputStream zOut, String vPath, long lastModified, File fromArchive, int mode) throws IOException {
      if ("META-INF/MANIFEST.MF".equalsIgnoreCase(vPath)) {
         if (!this.doubleFilePass || this.doubleFilePass && this.skipWriting) {
            this.filesetManifest(fromArchive, is);
         }
      } else if ("META-INF/INDEX.LIST".equalsIgnoreCase(vPath) && this.index) {
         this.log("Warning: selected " + this.archiveType + " files include a META-INF/INDEX.LIST which will" + " be replaced by a newly generated one.", 1);
      } else {
         if (this.index && vPath.indexOf("/") == -1) {
            this.rootEntries.addElement(vPath);
         }

         super.zipFile(is, zOut, vPath, lastModified, fromArchive, mode);
      }

   }

   private void filesetManifest(File file, InputStream is) throws IOException {
      if (this.manifestFile != null && this.manifestFile.equals(file)) {
         this.log("Found manifest " + file, 3);

         try {
            if (is != null) {
               InputStreamReader isr;
               if (this.manifestEncoding == null) {
                  isr = new InputStreamReader(is);
               } else {
                  isr = new InputStreamReader(is, this.manifestEncoding);
               }

               this.manifest = this.getManifest((Reader)isr);
            } else {
               this.manifest = this.getManifest(file);
            }
         } catch (UnsupportedEncodingException var7) {
            throw new BuildException("Unsupported encoding while reading manifest: " + var7.getMessage(), var7);
         }
      } else if (this.filesetManifestConfig != null && !this.filesetManifestConfig.getValue().equals("skip")) {
         this.log("Found manifest to merge in file " + file, 3);

         try {
            Manifest newManifest = null;
            if (is != null) {
               InputStreamReader isr;
               if (this.manifestEncoding == null) {
                  isr = new InputStreamReader(is);
               } else {
                  isr = new InputStreamReader(is, this.manifestEncoding);
               }

               newManifest = this.getManifest((Reader)isr);
            } else {
               newManifest = this.getManifest(file);
            }

            if (this.filesetManifest == null) {
               this.filesetManifest = newManifest;
            } else {
               this.filesetManifest.merge(newManifest);
            }
         } catch (UnsupportedEncodingException var5) {
            throw new BuildException("Unsupported encoding while reading manifest: " + var5.getMessage(), var5);
         } catch (ManifestException var6) {
            this.log("Manifest in file " + file + " is invalid: " + var6.getMessage(), 0);
            throw new BuildException("Invalid Manifest", var6, this.getLocation());
         }
      }

   }

   protected Zip.ArchiveState getResourcesToAdd(ResourceCollection[] rcs, File zipFile, boolean needsUpdate) throws BuildException {
      if (zipFile.exists()) {
         try {
            this.originalManifest = this.getManifestFromJar(zipFile);
            if (this.originalManifest == null) {
               this.log("Updating jar since the current jar has no manifest", 3);
               needsUpdate = true;
            } else {
               Manifest mf = this.createManifest();
               if (!mf.equals(this.originalManifest)) {
                  this.log("Updating jar since jar manifest has changed", 3);
                  needsUpdate = true;
               }
            }
         } catch (Throwable var5) {
            this.log("error while reading original manifest in file: " + zipFile.toString() + var5.getMessage(), 1);
            needsUpdate = true;
         }
      } else {
         needsUpdate = true;
      }

      this.createEmpty = needsUpdate;
      return super.getResourcesToAdd(rcs, zipFile, needsUpdate);
   }

   protected boolean createEmptyZip(File zipFile) throws BuildException {
      if (!this.createEmpty) {
         return true;
      } else if (this.emptyBehavior.equals("skip")) {
         this.log("Warning: skipping " + this.archiveType + " archive " + zipFile + " because no files were included.", 1);
         return true;
      } else if (this.emptyBehavior.equals("fail")) {
         throw new BuildException("Cannot create " + this.archiveType + " archive " + zipFile + ": no files were included.", this.getLocation());
      } else {
         ZipOutputStream zOut = null;

         try {
            this.log("Building MANIFEST-only jar: " + this.getDestFile().getAbsolutePath());
            zOut = new ZipOutputStream(new FileOutputStream(this.getDestFile()));
            zOut.setEncoding(this.getEncoding());
            if (this.isCompress()) {
               zOut.setMethod(8);
            } else {
               zOut.setMethod(0);
            }

            this.initZipOutputStream(zOut);
            this.finalizeZipOutputStream(zOut);
         } catch (IOException var11) {
            throw new BuildException("Could not create almost empty JAR archive (" + var11.getMessage() + ")", var11, this.getLocation());
         } finally {
            try {
               if (zOut != null) {
                  zOut.close();
               }
            } catch (IOException var10) {
            }

            this.createEmpty = false;
         }

         return true;
      }
   }

   protected void cleanUp() {
      super.cleanUp();
      if (!this.doubleFilePass || this.doubleFilePass && !this.skipWriting) {
         this.manifest = null;
         this.configuredManifest = this.savedConfiguredManifest;
         this.filesetManifest = null;
         this.originalManifest = null;
      }

      this.rootEntries.removeAllElements();
   }

   public void reset() {
      super.reset();
      this.emptyBehavior = "create";
      this.configuredManifest = null;
      this.filesetManifestConfig = null;
      this.mergeManifestsMain = false;
      this.manifestFile = null;
      this.index = false;
   }

   protected final void writeIndexLikeList(List dirs, List files, PrintWriter writer) throws IOException {
      Collections.sort(dirs);
      Collections.sort(files);
      Iterator iter = dirs.iterator();

      while(iter.hasNext()) {
         String dir = (String)iter.next();
         dir = dir.replace('\\', '/');
         if (dir.startsWith("./")) {
            dir = dir.substring(2);
         }

         while(dir.startsWith("/")) {
            dir = dir.substring(1);
         }

         int pos = dir.lastIndexOf(47);
         if (pos != -1) {
            dir = dir.substring(0, pos);
         }

         if (!dir.startsWith("META-INF")) {
            writer.println(dir);
         }
      }

      iter = files.iterator();

      while(iter.hasNext()) {
         writer.println(iter.next());
      }

   }

   protected static final String findJarName(String fileName, String[] classpath) {
      if (classpath == null) {
         return (new File(fileName)).getName();
      } else {
         fileName = fileName.replace(File.separatorChar, '/');
         TreeMap matches = new TreeMap(new Comparator() {
            public int compare(Object o1, Object o2) {
               return o1 instanceof String && o2 instanceof String ? ((String)o2).length() - ((String)o1).length() : 0;
            }
         });

         for(int i = 0; i < classpath.length; ++i) {
            if (fileName.endsWith(classpath[i])) {
               matches.put(classpath[i], classpath[i]);
            } else {
               int slash = classpath[i].indexOf("/");

               for(String candidate = classpath[i]; slash > -1; slash = candidate.indexOf("/")) {
                  candidate = candidate.substring(slash + 1);
                  if (fileName.endsWith(candidate)) {
                     matches.put(candidate, classpath[i]);
                     break;
                  }
               }
            }
         }

         return matches.size() == 0 ? null : (String)matches.get(matches.firstKey());
      }
   }

   protected static final void grabFilesAndDirs(String file, List dirs, List files) throws IOException {
      org.apache.tools.zip.ZipFile zf = null;

      try {
         zf = new org.apache.tools.zip.ZipFile(file, "utf-8");
         Enumeration entries = zf.getEntries();
         HashSet dirSet = new HashSet();

         while(entries.hasMoreElements()) {
            org.apache.tools.zip.ZipEntry ze = (org.apache.tools.zip.ZipEntry)entries.nextElement();
            String name = ze.getName();
            if (!name.startsWith("META-INF/")) {
               if (ze.isDirectory()) {
                  dirSet.add(name);
               } else if (name.indexOf("/") == -1) {
                  files.add(name);
               } else {
                  dirSet.add(name.substring(0, name.lastIndexOf("/") + 1));
               }
            }
         }

         dirs.addAll(dirSet);
      } finally {
         if (zf != null) {
            zf.close();
         }

      }

   }

   public static class FilesetManifestConfig extends EnumeratedAttribute {
      public String[] getValues() {
         return new String[]{"skip", "merge", "mergewithoutmain"};
      }
   }
}
