package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.util.ClasspathUtils;

public abstract class DefBase extends AntlibDefinition {
   private ClassLoader createdLoader;
   private ClasspathUtils.Delegate cpDelegate;

   protected boolean hasCpDelegate() {
      return this.cpDelegate != null;
   }

   /** @deprecated */
   public void setReverseLoader(boolean reverseLoader) {
      this.getDelegate().setReverseLoader(reverseLoader);
      this.log("The reverseloader attribute is DEPRECATED. It will be removed", 1);
   }

   public Path getClasspath() {
      return this.getDelegate().getClasspath();
   }

   public boolean isReverseLoader() {
      return this.getDelegate().isReverseLoader();
   }

   public String getLoaderId() {
      return this.getDelegate().getClassLoadId();
   }

   public String getClasspathId() {
      return this.getDelegate().getClassLoadId();
   }

   public void setClasspath(Path classpath) {
      this.getDelegate().setClasspath(classpath);
   }

   public Path createClasspath() {
      return this.getDelegate().createClasspath();
   }

   public void setClasspathRef(Reference r) {
      this.getDelegate().setClasspathref(r);
   }

   public void setLoaderRef(Reference r) {
      this.getDelegate().setLoaderRef(r);
   }

   protected ClassLoader createLoader() {
      if (this.getAntlibClassLoader() != null && this.cpDelegate == null) {
         return this.getAntlibClassLoader();
      } else {
         if (this.createdLoader == null) {
            this.createdLoader = this.getDelegate().getClassLoader();
            ((AntClassLoader)this.createdLoader).addSystemPackageRoot("org.apache.tools.ant");
         }

         return this.createdLoader;
      }
   }

   public void init() throws BuildException {
      super.init();
   }

   private ClasspathUtils.Delegate getDelegate() {
      if (this.cpDelegate == null) {
         this.cpDelegate = ClasspathUtils.getDelegate(this);
      }

      return this.cpDelegate;
   }
}
