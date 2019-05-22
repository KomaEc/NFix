package org.apache.velocity.runtime.resource.loader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.io.UnicodeInputStream;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.util.StringUtils;

public class FileResourceLoader extends ResourceLoader {
   private List paths = new ArrayList();
   private Map templatePaths = Collections.synchronizedMap(new HashMap());
   private boolean unicode = false;

   public void init(ExtendedProperties configuration) {
      if (this.log.isTraceEnabled()) {
         this.log.trace("FileResourceLoader : initialization starting.");
      }

      this.paths.addAll(configuration.getVector("path"));
      this.unicode = configuration.getBoolean("unicode", false);
      if (this.log.isDebugEnabled()) {
         this.log.debug("Do unicode file recognition:  " + this.unicode);
      }

      StringUtils.trimStrings(this.paths);
      if (this.log.isInfoEnabled()) {
         int sz = this.paths.size();

         for(int i = 0; i < sz; ++i) {
            this.log.info("FileResourceLoader : adding path '" + (String)this.paths.get(i) + "'");
         }

         this.log.trace("FileResourceLoader : initialization complete.");
      }

   }

   public InputStream getResourceStream(String templateName) throws ResourceNotFoundException {
      if (org.apache.commons.lang.StringUtils.isEmpty(templateName)) {
         throw new ResourceNotFoundException("Need to specify a file name or file path!");
      } else {
         String template = StringUtils.normalizePath(templateName);
         if (template != null && template.length() != 0) {
            int size = this.paths.size();

            for(int i = 0; i < size; ++i) {
               String path = (String)this.paths.get(i);
               InputStream inputStream = null;

               try {
                  inputStream = this.findTemplate(path, template);
               } catch (IOException var8) {
                  this.log.error("While loading Template " + template + ": ", var8);
               }

               if (inputStream != null) {
                  this.templatePaths.put(templateName, path);
                  return inputStream;
               }
            }

            throw new ResourceNotFoundException("FileResourceLoader : cannot find " + template);
         } else {
            String msg = "File resource error : argument " + template + " contains .. and may be trying to access " + "content outside of template root.  Rejected.";
            this.log.error("FileResourceLoader : " + msg);
            throw new ResourceNotFoundException(msg);
         }
      }
   }

   private InputStream findTemplate(String path, String template) throws IOException {
      try {
         File file = this.getFile(path, template);
         if (file.canRead()) {
            FileInputStream fis = null;

            try {
               fis = new FileInputStream(file.getAbsolutePath());
               if (this.unicode) {
                  UnicodeInputStream uis = null;

                  try {
                     uis = new UnicodeInputStream(fis, true);
                     if (this.log.isDebugEnabled()) {
                        this.log.debug("File Encoding for " + file + " is: " + uis.getEncodingFromStream());
                     }

                     return new BufferedInputStream(uis);
                  } catch (IOException var7) {
                     this.closeQuiet(uis);
                     throw var7;
                  }
               } else {
                  return new BufferedInputStream(fis);
               }
            } catch (IOException var8) {
               this.closeQuiet(fis);
               throw var8;
            }
         } else {
            return null;
         }
      } catch (FileNotFoundException var9) {
         return null;
      }
   }

   private void closeQuiet(InputStream is) {
      if (is != null) {
         try {
            is.close();
         } catch (IOException var3) {
         }
      }

   }

   public boolean isSourceModified(Resource resource) {
      boolean modified = true;
      String fileName = resource.getName();
      String path = (String)this.templatePaths.get(fileName);
      File currentFile = null;

      for(int i = 0; currentFile == null && i < this.paths.size(); ++i) {
         String testPath = (String)this.paths.get(i);
         File testFile = this.getFile(testPath, fileName);
         if (testFile.canRead()) {
            currentFile = testFile;
         }
      }

      File file = this.getFile(path, fileName);
      if (currentFile != null && file.exists() && currentFile.equals(file) && file.canRead()) {
         modified = file.lastModified() != resource.getLastModified();
      }

      return modified;
   }

   public long getLastModified(Resource resource) {
      String path = (String)this.templatePaths.get(resource.getName());
      File file = this.getFile(path, resource.getName());
      return file.canRead() ? file.lastModified() : 0L;
   }

   private File getFile(String path, String template) {
      File file = null;
      if ("".equals(path)) {
         file = new File(template);
      } else {
         if (template.startsWith("/")) {
            template = template.substring(1);
         }

         file = new File(path, template);
      }

      return file;
   }
}
