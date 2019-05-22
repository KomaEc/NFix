package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.filters.ChainableReader;
import org.apache.tools.ant.types.FilterChain;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.RedirectorElement;
import org.apache.tools.ant.types.resources.FileResource;

public class VerifyJar extends AbstractJarSignerTask {
   public static final String ERROR_NO_FILE = "Not found :";
   private static final String VERIFIED_TEXT = "jar verified.";
   private boolean certificates = false;
   private VerifyJar.BufferingOutputFilter outputCache = new VerifyJar.BufferingOutputFilter();
   public static final String ERROR_NO_VERIFY = "Failed to verify ";

   public void setCertificates(boolean certificates) {
      this.certificates = certificates;
   }

   public void execute() throws BuildException {
      boolean hasJar = this.jar != null;
      if (!hasJar && !this.hasResources()) {
         throw new BuildException("jar must be set through jar attribute or nested filesets");
      } else {
         this.beginExecution();
         RedirectorElement redirector = this.getRedirector();
         redirector.setAlwaysLog(true);
         FilterChain outputFilterChain = redirector.createOutputFilterChain();
         outputFilterChain.add(this.outputCache);

         try {
            Path sources = this.createUnifiedSourcePath();
            Iterator iter = sources.iterator();

            while(iter.hasNext()) {
               FileResource fr = (FileResource)iter.next();
               this.verifyOneJar(fr.getFile());
            }
         } finally {
            this.endExecution();
         }

      }
   }

   private void verifyOneJar(File jar) {
      if (!jar.exists()) {
         throw new BuildException("Not found :" + jar);
      } else {
         ExecTask cmd = this.createJarSigner();
         this.setCommonOptions(cmd);
         this.bindToKeystore(cmd);
         this.addValue(cmd, "-verify");
         if (this.certificates) {
            this.addValue(cmd, "-certs");
         }

         this.addValue(cmd, jar.getPath());
         this.log("Verifying JAR: " + jar.getAbsolutePath());
         this.outputCache.clear();
         BuildException ex = null;

         try {
            cmd.execute();
         } catch (BuildException var5) {
            ex = var5;
         }

         String results = this.outputCache.toString();
         if (ex != null) {
            if (results.indexOf("zip file closed") < 0) {
               throw ex;
            }

            this.log("You are running jarsigner against a JVM with a known bug that manifests as an IllegalStateException.", 1);
         }

         if (results.indexOf("jar verified.") < 0) {
            throw new BuildException("Failed to verify " + jar);
         }
      }
   }

   private static class BufferingOutputFilterReader extends Reader {
      private Reader next;
      private StringBuffer buffer = new StringBuffer();

      public BufferingOutputFilterReader(Reader next) {
         this.next = next;
      }

      public int read(char[] cbuf, int off, int len) throws IOException {
         int result = this.next.read(cbuf, off, len);
         this.buffer.append(cbuf, off, len);
         return result;
      }

      public void close() throws IOException {
         this.next.close();
      }

      public String toString() {
         return this.buffer.toString();
      }

      public void clear() {
         this.buffer = new StringBuffer();
      }
   }

   private static class BufferingOutputFilter implements ChainableReader {
      private VerifyJar.BufferingOutputFilterReader buffer;

      private BufferingOutputFilter() {
      }

      public Reader chain(Reader rdr) {
         this.buffer = new VerifyJar.BufferingOutputFilterReader(rdr);
         return this.buffer;
      }

      public String toString() {
         return this.buffer.toString();
      }

      public void clear() {
         if (this.buffer != null) {
            this.buffer.clear();
         }

      }

      // $FF: synthetic method
      BufferingOutputFilter(Object x0) {
         this();
      }
   }
}
