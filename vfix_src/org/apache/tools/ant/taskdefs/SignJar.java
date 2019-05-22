package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.condition.IsSigned;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.util.FileNameMapper;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.IdentityMapper;

public class SignJar extends AbstractJarSignerTask {
   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
   protected String sigfile;
   protected File signedjar;
   protected boolean internalsf;
   protected boolean sectionsonly;
   private boolean preserveLastModified;
   protected boolean lazy;
   protected File destDir;
   private FileNameMapper mapper;
   protected String tsaurl;
   protected String tsacert;
   public static final String ERROR_TODIR_AND_SIGNEDJAR = "'destdir' and 'signedjar' cannot both be set";
   public static final String ERROR_TOO_MANY_MAPPERS = "Too many mappers";
   public static final String ERROR_SIGNEDJAR_AND_PATHS = "You cannot specify the signed JAR when using paths or filesets";
   public static final String ERROR_BAD_MAP = "Cannot map source file to anything sensible: ";
   public static final String ERROR_MAPPER_WITHOUT_DEST = "The destDir attribute is required if a mapper is set";
   public static final String ERROR_NO_ALIAS = "alias attribute must be set";
   public static final String ERROR_NO_STOREPASS = "storepass attribute must be set";

   public void setSigfile(String sigfile) {
      this.sigfile = sigfile;
   }

   public void setSignedjar(File signedjar) {
      this.signedjar = signedjar;
   }

   public void setInternalsf(boolean internalsf) {
      this.internalsf = internalsf;
   }

   public void setSectionsonly(boolean sectionsonly) {
      this.sectionsonly = sectionsonly;
   }

   public void setLazy(boolean lazy) {
      this.lazy = lazy;
   }

   public void setDestDir(File destDir) {
      this.destDir = destDir;
   }

   public void add(FileNameMapper newMapper) {
      if (this.mapper != null) {
         throw new BuildException("Too many mappers");
      } else {
         this.mapper = newMapper;
      }
   }

   public FileNameMapper getMapper() {
      return this.mapper;
   }

   public String getTsaurl() {
      return this.tsaurl;
   }

   public void setTsaurl(String tsaurl) {
      this.tsaurl = tsaurl;
   }

   public String getTsacert() {
      return this.tsacert;
   }

   public void setTsacert(String tsacert) {
      this.tsacert = tsacert;
   }

   public void execute() throws BuildException {
      boolean hasJar = this.jar != null;
      boolean hasSignedJar = this.signedjar != null;
      boolean hasDestDir = this.destDir != null;
      boolean hasMapper = this.mapper != null;
      if (!hasJar && !this.hasResources()) {
         throw new BuildException("jar must be set through jar attribute or nested filesets");
      } else if (null == this.alias) {
         throw new BuildException("alias attribute must be set");
      } else if (null == this.storepass) {
         throw new BuildException("storepass attribute must be set");
      } else if (hasDestDir && hasSignedJar) {
         throw new BuildException("'destdir' and 'signedjar' cannot both be set");
      } else if (this.hasResources() && hasSignedJar) {
         throw new BuildException("You cannot specify the signed JAR when using paths or filesets");
      } else if (!hasDestDir && hasMapper) {
         throw new BuildException("The destDir attribute is required if a mapper is set");
      } else {
         this.beginExecution();

         try {
            if (hasJar && hasSignedJar) {
               this.signOneJar(this.jar, this.signedjar);
               return;
            }

            Path sources = this.createUnifiedSourcePath();
            Object destMapper;
            if (hasMapper) {
               destMapper = this.mapper;
            } else {
               destMapper = new IdentityMapper();
            }

            Iterator iter = sources.iterator();

            while(iter.hasNext()) {
               FileResource fr = (FileResource)iter.next();
               File toDir = hasDestDir ? this.destDir : fr.getBaseDir();
               String[] destFilenames = ((FileNameMapper)destMapper).mapFileName(fr.getName());
               if (destFilenames == null || destFilenames.length != 1) {
                  throw new BuildException("Cannot map source file to anything sensible: " + fr.getFile());
               }

               File destFile = new File(toDir, destFilenames[0]);
               this.signOneJar(fr.getFile(), destFile);
            }
         } finally {
            this.endExecution();
         }

      }
   }

   private void signOneJar(File jarSource, File jarTarget) throws BuildException {
      File targetFile = jarTarget;
      if (jarTarget == null) {
         targetFile = jarSource;
      }

      if (!this.isUpToDate(jarSource, targetFile)) {
         long lastModified = jarSource.lastModified();
         ExecTask cmd = this.createJarSigner();
         this.setCommonOptions(cmd);
         this.bindToKeystore(cmd);
         if (null != this.sigfile) {
            this.addValue(cmd, "-sigfile");
            String value = this.sigfile;
            this.addValue(cmd, value);
         }

         if (null != targetFile && !jarSource.equals(targetFile)) {
            this.addValue(cmd, "-signedjar");
            this.addValue(cmd, targetFile.getPath());
         }

         if (this.internalsf) {
            this.addValue(cmd, "-internalsf");
         }

         if (this.sectionsonly) {
            this.addValue(cmd, "-sectionsonly");
         }

         this.addTimestampAuthorityCommands(cmd);
         this.addValue(cmd, jarSource.getPath());
         this.addValue(cmd, this.alias);
         this.log("Signing JAR: " + jarSource.getAbsolutePath() + " to " + targetFile.getAbsolutePath() + " as " + this.alias);
         cmd.execute();
         if (this.preserveLastModified) {
            targetFile.setLastModified(lastModified);
         }

      }
   }

   private void addTimestampAuthorityCommands(ExecTask cmd) {
      if (this.tsaurl != null) {
         this.addValue(cmd, "-tsa");
         this.addValue(cmd, this.tsaurl);
      }

      if (this.tsacert != null) {
         this.addValue(cmd, "-tsacert");
         this.addValue(cmd, this.tsacert);
      }

   }

   protected boolean isUpToDate(File jarFile, File signedjarFile) {
      if (null != jarFile && jarFile.exists()) {
         File destFile = signedjarFile;
         if (signedjarFile == null) {
            destFile = jarFile;
         }

         if (jarFile.equals(destFile)) {
            return this.lazy ? this.isSigned(jarFile) : false;
         } else {
            return FILE_UTILS.isUpToDate(jarFile, destFile);
         }
      } else {
         return false;
      }
   }

   protected boolean isSigned(File file) {
      try {
         return IsSigned.isSigned(file, this.alias);
      } catch (IOException var3) {
         this.log(var3.toString(), 3);
         return false;
      }
   }

   public void setPreserveLastModified(boolean preserveLastModified) {
      this.preserveLastModified = preserveLastModified;
   }
}
