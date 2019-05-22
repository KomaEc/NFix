package org.apache.tools.ant.taskdefs;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.filters.util.ChainReaderHelper;
import org.apache.tools.ant.types.FilterChain;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.types.resources.JavaResource;
import org.apache.tools.ant.util.FileUtils;

public class LoadProperties extends Task {
   private Resource src = null;
   private final Vector filterChains = new Vector();
   private String encoding = null;

   public final void setSrcFile(File srcFile) {
      this.addConfigured(new FileResource(srcFile));
   }

   public void setResource(String resource) {
      this.assertSrcIsJavaResource();
      ((JavaResource)this.src).setName(resource);
   }

   public final void setEncoding(String encoding) {
      this.encoding = encoding;
   }

   public void setClasspath(Path classpath) {
      this.assertSrcIsJavaResource();
      ((JavaResource)this.src).setClasspath(classpath);
   }

   public Path createClasspath() {
      this.assertSrcIsJavaResource();
      return ((JavaResource)this.src).createClasspath();
   }

   public void setClasspathRef(Reference r) {
      this.assertSrcIsJavaResource();
      ((JavaResource)this.src).setClasspathRef(r);
   }

   public Path getClasspath() {
      this.assertSrcIsJavaResource();
      return ((JavaResource)this.src).getClasspath();
   }

   public final void execute() throws BuildException {
      if (this.src == null) {
         throw new BuildException("A source resource is required.");
      } else if (!this.src.isExists()) {
         if (this.src instanceof JavaResource) {
            this.log("Unable to find resource " + this.src, 1);
         } else {
            throw new BuildException("Source resource does not exist: " + this.src);
         }
      } else {
         BufferedInputStream bis = null;
         Reader instream = null;
         ByteArrayInputStream tis = null;

         try {
            String text;
            try {
               bis = new BufferedInputStream(this.src.getInputStream());
               if (this.encoding == null) {
                  instream = new InputStreamReader(bis);
               } else {
                  instream = new InputStreamReader(bis, this.encoding);
               }

               ChainReaderHelper crh = new ChainReaderHelper();
               crh.setPrimaryReader(instream);
               crh.setFilterChains(this.filterChains);
               crh.setProject(this.getProject());
               Reader instream = crh.getAssembledReader();
               text = crh.readFully(instream);
               if (text != null) {
                  if (!text.endsWith("\n")) {
                     text = text + "\n";
                  }

                  if (this.encoding == null) {
                     tis = new ByteArrayInputStream(text.getBytes());
                  } else {
                     tis = new ByteArrayInputStream(text.getBytes(this.encoding));
                  }

                  Properties props = new Properties();
                  props.load(tis);
                  Property propertyTask = new Property();
                  propertyTask.bindToOwner(this);
                  propertyTask.addProperties(props);
               }
            } catch (IOException var11) {
               text = "Unable to load file: " + var11.toString();
               throw new BuildException(text, var11, this.getLocation());
            }
         } finally {
            FileUtils.close((InputStream)bis);
            FileUtils.close((InputStream)tis);
         }

      }
   }

   public final void addFilterChain(FilterChain filter) {
      this.filterChains.addElement(filter);
   }

   public void addConfigured(ResourceCollection a) {
      if (this.src != null) {
         throw new BuildException("only a single source is supported");
      } else if (a.size() != 1) {
         throw new BuildException("only single argument resource collections are supported");
      } else {
         this.src = (Resource)a.iterator().next();
      }
   }

   private void assertSrcIsJavaResource() {
      if (this.src == null) {
         this.src = new JavaResource();
         this.src.setProject(this.getProject());
      } else if (!(this.src instanceof JavaResource)) {
         throw new BuildException("expected a java resource as source");
      }

   }
}
