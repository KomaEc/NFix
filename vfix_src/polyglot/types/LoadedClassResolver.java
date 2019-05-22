package polyglot.types;

import java.io.InvalidClassException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import polyglot.main.Report;
import polyglot.main.Version;
import polyglot.types.reflect.ClassFile;
import polyglot.types.reflect.ClassFileLoader;
import polyglot.types.reflect.ClassPathLoader;
import polyglot.util.CollectionUtil;
import polyglot.util.TypeEncoder;

public class LoadedClassResolver extends ClassResolver implements TopLevelResolver {
   protected static final int NOT_COMPATIBLE = -1;
   protected static final int MINOR_NOT_COMPATIBLE = 1;
   protected static final int COMPATIBLE = 0;
   TypeSystem ts;
   TypeEncoder te;
   ClassPathLoader loader;
   Version version;
   Set nocache;
   boolean allowRawClasses;
   static final Collection report_topics = CollectionUtil.list("types", "resolver", "loader");

   public LoadedClassResolver(TypeSystem ts, String classpath, ClassFileLoader loader, Version version, boolean allowRawClasses) {
      this.ts = ts;
      this.te = new TypeEncoder(ts);
      this.loader = new ClassPathLoader(classpath, loader);
      this.version = version;
      this.nocache = new HashSet();
      this.allowRawClasses = allowRawClasses;
   }

   public boolean packageExists(String name) {
      return this.loader.packageExists(name);
   }

   protected ClassFile loadFile(String name) {
      if (this.nocache.contains(name)) {
         return null;
      } else {
         try {
            ClassFile clazz = this.loader.loadClass(name);
            if (clazz != null) {
               if (Report.should_report((Collection)report_topics, 4)) {
                  Report.report(4, "Class " + name + " found in classpath " + this.loader.classpath());
               }

               return clazz;
            }

            if (Report.should_report((Collection)report_topics, 4)) {
               Report.report(4, "Class " + name + " not found in classpath " + this.loader.classpath());
            }
         } catch (ClassFormatError var3) {
            if (Report.should_report((Collection)report_topics, 4)) {
               Report.report(4, "Class " + name + " format error");
            }
         }

         this.nocache.add(name);
         return null;
      }
   }

   public Named find(String name) throws SemanticException {
      if (Report.should_report((Collection)report_topics, 3)) {
         Report.report(3, "LoadedCR.find(" + name + ")");
      }

      ClassFile clazz = this.loadFile(name);
      if (clazz == null) {
         throw new NoClassException(name);
      } else if (clazz.encodedClassType(this.version.name()) != null) {
         if (Report.should_report((Collection)report_topics, 4)) {
            Report.report(4, "Using encoded class type for " + name);
         }

         return this.getEncodedType(clazz, name);
      } else if (this.allowRawClasses) {
         if (Report.should_report((Collection)report_topics, 4)) {
            Report.report(4, "Using raw class file for " + name);
         }

         return clazz.type(this.ts);
      } else {
         throw new SemanticException("Unable to find a suitable definition of \"" + name + "\". A class file was found," + " but it did not contain appropriate information for this" + " language extension. If the source for this file is written" + " in the language extension, try recompiling the source code.");
      }
   }

   protected ClassType getEncodedType(ClassFile clazz, String name) throws SemanticException {
      int comp = this.checkCompilerVersion(clazz.compilerVersion(this.version.name()));
      if (comp == -1) {
         throw new SemanticException("Unable to find a suitable definition of " + clazz.name() + ". Try recompiling or obtaining " + " a newer version of the class file.");
      } else {
         ClassType dt;
         try {
            dt = (ClassType)this.te.decode(clazz.encodedClassType(this.version.name()));
         } catch (InvalidClassException var7) {
            throw new BadSerializationException(clazz.name());
         }

         ((CachingResolver)this.ts.systemResolver()).addNamed(name, dt);
         if (Report.should_report((Collection)report_topics, 2)) {
            Report.report(2, "Returning serialized ClassType for " + clazz.name() + ".");
         }

         return dt;
      }
   }

   protected int checkCompilerVersion(String clazzVersion) {
      if (clazzVersion == null) {
         return -1;
      } else {
         StringTokenizer st = new StringTokenizer(clazzVersion, ".");

         try {
            int v = Integer.parseInt(st.nextToken());
            Version version = this.version;
            if (v != version.major()) {
               return -1;
            } else {
               v = Integer.parseInt(st.nextToken());
               return v != version.minor() ? 1 : 0;
            }
         } catch (NumberFormatException var5) {
            return -1;
         }
      }
   }
}
