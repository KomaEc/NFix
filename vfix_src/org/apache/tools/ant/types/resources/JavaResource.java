package org.apache.tools.ant.types.resources;

import java.io.IOException;
import java.io.InputStream;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.util.FileUtils;

public class JavaResource extends Resource {
   private Path classpath;
   private Reference loader;
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$types$resources$JavaResource;

   public JavaResource() {
   }

   public JavaResource(String name, Path path) {
      this.setName(name);
      this.classpath = path;
   }

   public void setClasspath(Path classpath) {
      this.checkAttributesAllowed();
      if (this.classpath == null) {
         this.classpath = classpath;
      } else {
         this.classpath.append(classpath);
      }

   }

   public Path createClasspath() {
      this.checkChildrenAllowed();
      if (this.classpath == null) {
         this.classpath = new Path(this.getProject());
      }

      return this.classpath.createPath();
   }

   public void setClasspathRef(Reference r) {
      this.checkAttributesAllowed();
      this.createClasspath().setRefid(r);
   }

   public Path getClasspath() {
      return this.isReference() ? ((JavaResource)this.getCheckedRef()).getClasspath() : this.classpath;
   }

   public void setLoaderRef(Reference r) {
      this.checkAttributesAllowed();
      this.loader = r;
   }

   public void setRefid(Reference r) {
      if (this.loader == null && this.classpath == null) {
         super.setRefid(r);
      } else {
         throw this.tooManyAttributes();
      }
   }

   public boolean isExists() {
      InputStream is = null;

      boolean var3;
      try {
         boolean var2 = this.isReference() ? ((Resource)this.getCheckedRef()).isExists() : (is = this.getInputStream()) != null;
         return var2;
      } catch (IOException var7) {
         var3 = false;
      } finally {
         FileUtils.close(is);
      }

      return var3;
   }

   public InputStream getInputStream() throws IOException {
      if (this.isReference()) {
         return ((Resource)this.getCheckedRef()).getInputStream();
      } else {
         ClassLoader cl = null;
         if (this.loader != null) {
            cl = (ClassLoader)this.loader.getReferencedObject();
         }

         if (cl == null) {
            if (this.getClasspath() != null) {
               cl = this.getProject().createClassLoader(this.classpath);
            } else {
               cl = (class$org$apache$tools$ant$types$resources$JavaResource == null ? (class$org$apache$tools$ant$types$resources$JavaResource = class$("org.apache.tools.ant.types.resources.JavaResource")) : class$org$apache$tools$ant$types$resources$JavaResource).getClassLoader();
            }

            if (this.loader != null && cl != null) {
               this.getProject().addReference(this.loader.getRefId(), cl);
            }
         }

         return cl == null ? ClassLoader.getSystemResourceAsStream(this.getName()) : ((ClassLoader)cl).getResourceAsStream(this.getName());
      }
   }

   public int compareTo(Object another) {
      if (this.isReference()) {
         return ((Comparable)this.getCheckedRef()).compareTo(another);
      } else if (another.getClass().equals(this.getClass())) {
         JavaResource otherjr = (JavaResource)another;
         if (!this.getName().equals(otherjr.getName())) {
            return this.getName().compareTo(otherjr.getName());
         } else if (this.loader != otherjr.loader) {
            if (this.loader == null) {
               return -1;
            } else {
               return otherjr.loader == null ? 1 : this.loader.getRefId().compareTo(otherjr.loader.getRefId());
            }
         } else {
            Path p = this.getClasspath();
            Path op = otherjr.getClasspath();
            if (p != op) {
               if (p == null) {
                  return -1;
               } else {
                  return op == null ? 1 : p.toString().compareTo(op.toString());
               }
            } else {
               return 0;
            }
         }
      } else {
         return super.compareTo(another);
      }
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
