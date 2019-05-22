package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Mapper;
import org.apache.tools.ant.types.PatternSet;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.types.resources.Union;
import org.apache.tools.ant.types.selectors.SelectorUtils;
import org.apache.tools.ant.util.FileNameMapper;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.IdentityMapper;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

public class Expand extends Task {
   private File dest;
   private File source;
   private boolean overwrite = true;
   private Mapper mapperElement = null;
   private Vector patternsets = new Vector();
   private Union resources = new Union();
   private boolean resourcesSpecified = false;
   private static final String NATIVE_ENCODING = "native-encoding";
   private String encoding = "UTF8";
   public static final String ERROR_MULTIPLE_MAPPERS = "Cannot define more than one mapper";
   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();

   public void execute() throws BuildException {
      if ("expand".equals(this.getTaskType())) {
         this.log("!! expand is deprecated. Use unzip instead. !!");
      }

      if (this.source == null && !this.resourcesSpecified) {
         throw new BuildException("src attribute and/or resources must be specified");
      } else if (this.dest == null) {
         throw new BuildException("Dest attribute must be specified");
      } else if (this.dest.exists() && !this.dest.isDirectory()) {
         throw new BuildException("Dest must be a directory.", this.getLocation());
      } else {
         if (this.source != null) {
            if (this.source.isDirectory()) {
               throw new BuildException("Src must not be a directory. Use nested filesets instead.", this.getLocation());
            }

            this.expandFile(FILE_UTILS, this.source, this.dest);
         }

         Iterator iter = this.resources.iterator();

         while(iter.hasNext()) {
            Resource r = (Resource)iter.next();
            if (r.isExists()) {
               if (r instanceof FileResource) {
                  this.expandFile(FILE_UTILS, ((FileResource)r).getFile(), this.dest);
               } else {
                  this.expandResource(r, this.dest);
               }
            }
         }

      }
   }

   protected void expandFile(FileUtils fileUtils, File srcF, File dir) {
      this.log("Expanding: " + srcF + " into " + dir, 2);
      ZipFile zf = null;
      FileNameMapper mapper = this.getMapper();

      try {
         zf = new ZipFile(srcF, this.encoding);
         Enumeration e = zf.getEntries();

         while(e.hasMoreElements()) {
            ZipEntry ze = (ZipEntry)e.nextElement();
            this.extractFile(fileUtils, srcF, dir, zf.getInputStream(ze), ze.getName(), new Date(ze.getTime()), ze.isDirectory(), mapper);
         }

         this.log("expand complete", 3);
      } catch (IOException var11) {
         throw new BuildException("Error while expanding " + srcF.getPath(), var11);
      } finally {
         ZipFile.closeQuietly(zf);
      }
   }

   protected void expandResource(Resource srcR, File dir) {
      throw new BuildException("only filesystem based resources are supported by this task.");
   }

   protected FileNameMapper getMapper() {
      FileNameMapper mapper = null;
      if (this.mapperElement != null) {
         mapper = this.mapperElement.getImplementation();
      } else {
         mapper = new IdentityMapper();
      }

      return (FileNameMapper)mapper;
   }

   protected void extractFile(FileUtils fileUtils, File srcF, File dir, InputStream compressedInputStream, String entryName, Date entryDate, boolean isDirectory, FileNameMapper mapper) throws IOException {
      int length;
      String fos;
      if (this.patternsets != null && this.patternsets.size() > 0) {
         String name = entryName.replace('/', File.separatorChar).replace('\\', File.separatorChar);
         boolean included = false;
         Set includePatterns = new HashSet();
         Set excludePatterns = new HashSet();
         length = 0;

         for(int size = this.patternsets.size(); length < size; ++length) {
            PatternSet p = (PatternSet)this.patternsets.elementAt(length);
            String[] incls = p.getIncludePatterns(this.getProject());
            if (incls == null || incls.length == 0) {
               incls = new String[]{"**"};
            }

            for(int w = 0; w < incls.length; ++w) {
               String pattern = incls[w].replace('/', File.separatorChar).replace('\\', File.separatorChar);
               if (pattern.endsWith(File.separator)) {
                  pattern = pattern + "**";
               }

               includePatterns.add(pattern);
            }

            String[] excls = p.getExcludePatterns(this.getProject());
            if (excls != null) {
               for(int w = 0; w < excls.length; ++w) {
                  String pattern = excls[w].replace('/', File.separatorChar).replace('\\', File.separatorChar);
                  if (pattern.endsWith(File.separator)) {
                     pattern = pattern + "**";
                  }

                  excludePatterns.add(pattern);
               }
            }
         }

         Iterator iter;
         for(iter = includePatterns.iterator(); !included && iter.hasNext(); included = SelectorUtils.matchPath(fos, name)) {
            fos = (String)iter.next();
         }

         for(iter = excludePatterns.iterator(); included && iter.hasNext(); included = !SelectorUtils.matchPath(fos, name)) {
            fos = (String)iter.next();
         }

         if (!included) {
            return;
         }
      }

      String[] mappedNames = mapper.mapFileName(entryName);
      if (mappedNames == null || mappedNames.length == 0) {
         mappedNames = new String[]{entryName};
      }

      File f = fileUtils.resolveFile(dir, mappedNames[0]);

      try {
         if (!this.overwrite && f.exists() && f.lastModified() >= entryDate.getTime()) {
            this.log("Skipping " + f + " as it is up-to-date", 4);
            return;
         }

         this.log("expanding " + entryName + " to " + f, 3);
         File dirF = f.getParentFile();
         if (dirF != null) {
            dirF.mkdirs();
         }

         if (isDirectory) {
            f.mkdirs();
         } else {
            byte[] buffer = new byte[1024];
            int length = false;
            fos = null;

            try {
               FileOutputStream fos = new FileOutputStream(f);

               while((length = compressedInputStream.read(buffer)) >= 0) {
                  fos.write(buffer, 0, length);
               }

               fos.close();
               fos = null;
            } finally {
               FileUtils.close((OutputStream)fos);
            }
         }

         fileUtils.setFileLastModified(f, entryDate.getTime());
      } catch (FileNotFoundException var24) {
         this.log("Unable to expand to file " + f.getPath(), 1);
      }

   }

   public void setDest(File d) {
      this.dest = d;
   }

   public void setSrc(File s) {
      this.source = s;
   }

   public void setOverwrite(boolean b) {
      this.overwrite = b;
   }

   public void addPatternset(PatternSet set) {
      this.patternsets.addElement(set);
   }

   public void addFileset(FileSet set) {
      this.add((ResourceCollection)set);
   }

   public void add(ResourceCollection rc) {
      this.resourcesSpecified = true;
      this.resources.add(rc);
   }

   public Mapper createMapper() throws BuildException {
      if (this.mapperElement != null) {
         throw new BuildException("Cannot define more than one mapper", this.getLocation());
      } else {
         this.mapperElement = new Mapper(this.getProject());
         return this.mapperElement;
      }
   }

   public void add(FileNameMapper fileNameMapper) {
      this.createMapper().add(fileNameMapper);
   }

   public void setEncoding(String encoding) {
      if ("native-encoding".equals(encoding)) {
         encoding = null;
      }

      this.encoding = encoding;
   }
}
