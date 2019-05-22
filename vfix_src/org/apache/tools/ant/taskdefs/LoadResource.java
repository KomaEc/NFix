package org.apache.tools.ant.taskdefs;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.filters.util.ChainReaderHelper;
import org.apache.tools.ant.types.FilterChain;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.util.FileUtils;

public class LoadResource extends Task {
   private Resource src;
   private boolean failOnError = true;
   private boolean quiet = false;
   private String encoding = null;
   private String property = null;
   private final Vector filterChains = new Vector();

   public final void setEncoding(String encoding) {
      this.encoding = encoding;
   }

   public final void setProperty(String property) {
      this.property = property;
   }

   public final void setFailonerror(boolean fail) {
      this.failOnError = fail;
   }

   public void setQuiet(boolean quiet) {
      this.quiet = quiet;
      if (quiet) {
         this.failOnError = false;
      }

   }

   public final void execute() throws BuildException {
      if (this.src == null) {
         throw new BuildException("source resource not defined");
      } else if (this.property == null) {
         throw new BuildException("output property not defined");
      } else if (this.quiet && this.failOnError) {
         throw new BuildException("quiet and failonerror cannot both be set to true");
      } else if (!this.src.isExists()) {
         String message = this.src + " doesn't exist";
         if (this.failOnError) {
            throw new BuildException(message);
         } else {
            this.log(message, this.quiet ? 1 : 0);
         }
      } else {
         InputStream is = null;
         BufferedInputStream bis = null;
         Reader instream = null;
         this.log("loading " + this.src + " into property " + this.property, 3);

         try {
            long len = this.src.getSize();
            this.log("resource size = " + (len != -1L ? String.valueOf(len) : "unknown"), 4);
            int size = (int)len;
            is = this.src.getInputStream();
            bis = new BufferedInputStream(is);
            if (this.encoding == null) {
               instream = new InputStreamReader(bis);
            } else {
               instream = new InputStreamReader(bis, this.encoding);
            }

            String text = "";
            if (size != 0) {
               ChainReaderHelper crh = new ChainReaderHelper();
               if (len != -1L) {
                  crh.setBufferSize(size);
               }

               crh.setPrimaryReader(instream);
               crh.setFilterChains(this.filterChains);
               crh.setProject(this.getProject());
               Reader instream = crh.getAssembledReader();
               text = crh.readFully(instream);
            }

            if (text != null && text.length() > 0) {
               this.getProject().setNewProperty(this.property, text);
               this.log("loaded " + text.length() + " characters", 3);
               this.log(this.property + " := " + text, 4);
            }
         } catch (IOException var15) {
            String message = "Unable to load resource: " + var15.toString();
            if (this.failOnError) {
               throw new BuildException(message, var15, this.getLocation());
            }

            this.log(message, this.quiet ? 3 : 0);
         } catch (BuildException var16) {
            if (this.failOnError) {
               throw var16;
            }

            this.log(var16.getMessage(), this.quiet ? 3 : 0);
         } finally {
            FileUtils.close(is);
         }

      }
   }

   public final void addFilterChain(FilterChain filter) {
      this.filterChains.addElement(filter);
   }

   public void addConfigured(ResourceCollection a) {
      if (a.size() != 1) {
         throw new BuildException("only single argument resource collections are supported");
      } else {
         this.src = (Resource)a.iterator().next();
      }
   }
}
