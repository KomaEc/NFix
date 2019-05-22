package org.apache.tools.ant.types;

import java.util.Vector;
import org.apache.tools.ant.BuildException;

public final class AntFilterReader extends DataType implements Cloneable {
   private String className;
   private final Vector parameters = new Vector();
   private Path classpath;

   public void setClassName(String className) {
      this.className = className;
   }

   public String getClassName() {
      return this.className;
   }

   public void addParam(Parameter param) {
      this.parameters.addElement(param);
   }

   public void setClasspath(Path classpath) {
      if (this.isReference()) {
         throw this.tooManyAttributes();
      } else {
         if (this.classpath == null) {
            this.classpath = classpath;
         } else {
            this.classpath.append(classpath);
         }

      }
   }

   public Path createClasspath() {
      if (this.isReference()) {
         throw this.noChildrenAllowed();
      } else {
         if (this.classpath == null) {
            this.classpath = new Path(this.getProject());
         }

         return this.classpath.createPath();
      }
   }

   public Path getClasspath() {
      return this.classpath;
   }

   public void setClasspathRef(Reference r) {
      if (this.isReference()) {
         throw this.tooManyAttributes();
      } else {
         this.createClasspath().setRefid(r);
      }
   }

   public Parameter[] getParams() {
      Parameter[] params = new Parameter[this.parameters.size()];
      this.parameters.copyInto(params);
      return params;
   }

   public void setRefid(Reference r) throws BuildException {
      if (this.parameters.isEmpty() && this.className == null && this.classpath == null) {
         Object o = r.getReferencedObject(this.getProject());
         if (!(o instanceof AntFilterReader)) {
            String msg = r.getRefId() + " doesn't refer to a FilterReader";
            throw new BuildException(msg);
         } else {
            AntFilterReader afr = (AntFilterReader)o;
            this.setClassName(afr.getClassName());
            this.setClasspath(afr.getClasspath());
            Parameter[] p = afr.getParams();
            if (p != null) {
               for(int i = 0; i < p.length; ++i) {
                  this.addParam(p[i]);
               }
            }

            super.setRefid(r);
         }
      } else {
         throw this.tooManyAttributes();
      }
   }
}
