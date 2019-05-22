package org.apache.tools.ant.types;

import java.util.Properties;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.util.CompositeMapper;
import org.apache.tools.ant.util.ContainerMapper;
import org.apache.tools.ant.util.FileNameMapper;

public class Mapper extends DataType implements Cloneable {
   protected Mapper.MapperType type = null;
   protected String classname = null;
   protected Path classpath = null;
   protected String from = null;
   protected String to = null;
   private ContainerMapper container = null;

   public Mapper(Project p) {
      this.setProject(p);
   }

   public void setType(Mapper.MapperType type) {
      if (this.isReference()) {
         throw this.tooManyAttributes();
      } else {
         this.type = type;
      }
   }

   public void addConfigured(FileNameMapper fileNameMapper) {
      this.add(fileNameMapper);
   }

   public void add(FileNameMapper fileNameMapper) {
      if (this.isReference()) {
         throw this.noChildrenAllowed();
      } else {
         if (this.container == null) {
            if (this.type == null && this.classname == null) {
               this.container = new CompositeMapper();
            } else {
               FileNameMapper m = this.getImplementation();
               if (!(m instanceof ContainerMapper)) {
                  throw new BuildException(m + " mapper implementation does not support nested mappers!");
               }

               this.container = (ContainerMapper)m;
            }
         }

         this.container.add(fileNameMapper);
      }
   }

   public void addConfiguredMapper(Mapper mapper) {
      this.add(mapper.getImplementation());
   }

   public void setClassname(String classname) {
      if (this.isReference()) {
         throw this.tooManyAttributes();
      } else {
         this.classname = classname;
      }
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

   public void setClasspathRef(Reference ref) {
      if (this.isReference()) {
         throw this.tooManyAttributes();
      } else {
         this.createClasspath().setRefid(ref);
      }
   }

   public void setFrom(String from) {
      if (this.isReference()) {
         throw this.tooManyAttributes();
      } else {
         this.from = from;
      }
   }

   public void setTo(String to) {
      if (this.isReference()) {
         throw this.tooManyAttributes();
      } else {
         this.to = to;
      }
   }

   public void setRefid(Reference r) throws BuildException {
      if (this.type == null && this.from == null && this.to == null) {
         super.setRefid(r);
      } else {
         throw this.tooManyAttributes();
      }
   }

   public FileNameMapper getImplementation() throws BuildException {
      if (this.isReference()) {
         return this.getRef().getImplementation();
      } else if (this.type == null && this.classname == null && this.container == null) {
         throw new BuildException("nested mapper or one of the attributes type or classname is required");
      } else if (this.container != null) {
         return this.container;
      } else if (this.type != null && this.classname != null) {
         throw new BuildException("must not specify both type and classname attribute");
      } else {
         try {
            FileNameMapper m = (FileNameMapper)((FileNameMapper)this.getImplementationClass().newInstance());
            Project p = this.getProject();
            if (p != null) {
               p.setProjectReference(m);
            }

            m.setFrom(this.from);
            m.setTo(this.to);
            return m;
         } catch (BuildException var3) {
            throw var3;
         } catch (Throwable var4) {
            throw new BuildException(var4);
         }
      }
   }

   protected Class getImplementationClass() throws ClassNotFoundException {
      String cName = this.classname;
      if (this.type != null) {
         cName = this.type.getImplementation();
      }

      ClassLoader loader = this.classpath == null ? this.getClass().getClassLoader() : this.getProject().createClassLoader(this.classpath);
      return Class.forName(cName, true, (ClassLoader)loader);
   }

   protected Mapper getRef() {
      return (Mapper)this.getCheckedRef();
   }

   public static class MapperType extends EnumeratedAttribute {
      private Properties implementations = new Properties();

      public MapperType() {
         this.implementations.put("identity", "org.apache.tools.ant.util.IdentityMapper");
         this.implementations.put("flatten", "org.apache.tools.ant.util.FlatFileNameMapper");
         this.implementations.put("glob", "org.apache.tools.ant.util.GlobPatternMapper");
         this.implementations.put("merge", "org.apache.tools.ant.util.MergingMapper");
         this.implementations.put("regexp", "org.apache.tools.ant.util.RegexpPatternMapper");
         this.implementations.put("package", "org.apache.tools.ant.util.PackageNameMapper");
         this.implementations.put("unpackage", "org.apache.tools.ant.util.UnPackageNameMapper");
      }

      public String[] getValues() {
         return new String[]{"identity", "flatten", "glob", "merge", "regexp", "package", "unpackage"};
      }

      public String getImplementation() {
         return this.implementations.getProperty(this.getValue());
      }
   }
}
