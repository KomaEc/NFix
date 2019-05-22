package groovy.lang;

import groovy.security.GroovyCodeSourcePermission;
import groovy.util.CharsetToolkit;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessController;
import java.security.CodeSource;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.cert.Certificate;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;

public class GroovyCodeSource {
   private CodeSource codeSource;
   private String name;
   private String scriptText;
   Certificate[] certs;
   private boolean cachable;
   private File file;

   public GroovyCodeSource(String script, String name, String codeBase) {
      this.name = name;
      this.scriptText = script;
      this.codeSource = createCodeSource(codeBase);
      this.cachable = true;
   }

   public GroovyCodeSource(Reader reader, String name, String codeBase) {
      this.name = name;
      this.codeSource = createCodeSource(codeBase);

      try {
         this.scriptText = DefaultGroovyMethods.getText(reader);
      } catch (IOException var5) {
         throw new RuntimeException("Impossible to read the text content from that reader, for script: " + name + " with codeBase: " + codeBase, var5);
      }
   }

   /** @deprecated */
   @Deprecated
   public GroovyCodeSource(InputStream inputStream, String name, String codeBase) {
      this.name = name;
      this.codeSource = createCodeSource(codeBase);

      try {
         this.scriptText = DefaultGroovyMethods.getText(inputStream);
      } catch (IOException var5) {
         throw new RuntimeException("Impossible to read the text content from that input stream, for script: " + name + " with codeBase: " + codeBase, var5);
      }
   }

   public GroovyCodeSource(final File infile, final String encoding) throws IOException {
      final File file = new File(infile.getCanonicalPath());
      if (!file.exists()) {
         throw new FileNotFoundException(file.toString() + " (" + file.getAbsolutePath() + ")");
      } else if (file.isDirectory()) {
         throw new IllegalArgumentException(file.toString() + " (" + file.getAbsolutePath() + ") is a directory not a Groovy source file.");
      } else {
         try {
            if (!file.canRead()) {
               throw new RuntimeException(file.toString() + " can not be read. Check the read permission of the file \"" + file.toString() + "\" (" + file.getAbsolutePath() + ").");
            }
         } catch (SecurityException var7) {
            throw var7;
         }

         this.file = file;
         this.cachable = true;

         try {
            Object[] info = (Object[])AccessController.doPrivileged(new PrivilegedExceptionAction<Object[]>() {
               public Object[] run() throws IOException {
                  if (encoding != null) {
                     GroovyCodeSource.this.scriptText = DefaultGroovyMethods.getText(infile, encoding);
                  } else {
                     GroovyCodeSource.this.scriptText = DefaultGroovyMethods.getText(infile);
                  }

                  Object[] info = new Object[2];
                  URL url = file.toURI().toURL();
                  info[0] = url.toExternalForm();
                  info[1] = new CodeSource(url, (Certificate[])null);
                  return info;
               }
            });
            this.name = (String)info[0];
            this.codeSource = (CodeSource)info[1];
         } catch (PrivilegedActionException var6) {
            Throwable cause = var6.getCause();
            if (cause != null && cause instanceof IOException) {
               throw (IOException)cause;
            } else {
               throw new RuntimeException("Could not construct CodeSource for file: " + file, cause);
            }
         }
      }
   }

   public GroovyCodeSource(File infile) throws IOException {
      this(infile, CharsetToolkit.getDefaultSystemCharset().name());
   }

   public GroovyCodeSource(URL url) throws IOException {
      if (url == null) {
         throw new RuntimeException("Could not construct a GroovyCodeSource from a null URL");
      } else {
         this.name = url.toExternalForm();
         this.codeSource = new CodeSource(url, (Certificate[])null);

         try {
            String contentEncoding = url.openConnection().getContentEncoding();
            if (contentEncoding != null) {
               this.scriptText = DefaultGroovyMethods.getText(url, contentEncoding);
            } else {
               this.scriptText = DefaultGroovyMethods.getText(url);
            }

         } catch (IOException var3) {
            throw new RuntimeException("Impossible to read the text content from " + this.name, var3);
         }
      }
   }

   CodeSource getCodeSource() {
      return this.codeSource;
   }

   /** @deprecated */
   @Deprecated
   public InputStream getInputStream() {
      Object ioe;
      if (this.file == null) {
         try {
            return new ByteArrayInputStream(this.scriptText.getBytes("UTF-8"));
         } catch (UnsupportedEncodingException var3) {
            ioe = var3;
         }
      } else {
         try {
            return new FileInputStream(this.file);
         } catch (FileNotFoundException var4) {
            ioe = var4;
         }
      }

      String errorMsg = "Impossible to read the bytes from the associated script: " + this.scriptText + " with name: " + this.name;
      if (ioe != null) {
         throw new RuntimeException(errorMsg, (Throwable)ioe);
      } else {
         throw new RuntimeException(errorMsg);
      }
   }

   public String getScriptText() {
      return this.scriptText;
   }

   public String getName() {
      return this.name;
   }

   public File getFile() {
      return this.file;
   }

   public void setCachable(boolean b) {
      this.cachable = b;
   }

   public boolean isCachable() {
      return this.cachable;
   }

   private static CodeSource createCodeSource(String codeBase) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new GroovyCodeSourcePermission(codeBase));
      }

      try {
         return new CodeSource(new URL("file", "", codeBase), (Certificate[])null);
      } catch (MalformedURLException var3) {
         throw new RuntimeException("A CodeSource file URL cannot be constructed from the supplied codeBase: " + codeBase);
      }
   }
}
