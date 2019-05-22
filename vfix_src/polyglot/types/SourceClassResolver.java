package polyglot.types;

import java.util.Collection;
import polyglot.frontend.Compiler;
import polyglot.frontend.ExtensionInfo;
import polyglot.frontend.FileSource;
import polyglot.main.Report;
import polyglot.types.reflect.ClassFile;
import polyglot.types.reflect.ClassFileLoader;

public class SourceClassResolver extends LoadedClassResolver {
   Compiler compiler;
   ExtensionInfo ext;

   public SourceClassResolver(Compiler compiler, ExtensionInfo ext, String classpath, ClassFileLoader loader, boolean allowRawClasses) {
      super(ext.typeSystem(), classpath, loader, ext.version(), allowRawClasses);
      this.compiler = compiler;
      this.ext = ext;
   }

   public boolean packageExists(String name) {
      return super.packageExists(name);
   }

   public Named find(String name) throws SemanticException {
      if (Report.should_report((Collection)report_topics, 3)) {
         Report.report(3, "SourceCR.find(" + name + ")");
      }

      ClassFile clazz = null;
      ClassFile encodedClazz = null;
      FileSource source = null;
      clazz = this.loadFile(name);
      if (clazz != null && clazz.encodedClassType(this.version.name()) != null) {
         if (Report.should_report((Collection)report_topics, 4)) {
            Report.report(4, "Class " + name + " has encoded type info");
         }

         encodedClazz = clazz;
      }

      if (clazz != null && !this.allowRawClasses) {
         clazz = null;
      }

      source = this.ext.sourceLoader().classSource(name);
      if (Report.should_report((Collection)report_topics, 4)) {
         if (source == null) {
            Report.report(4, "Class " + name + " not found in source file");
         } else {
            Report.report(4, "Class " + name + " found in source " + source);
         }
      }

      if (encodedClazz != null) {
         if (Report.should_report((Collection)report_topics, 4)) {
            Report.report(4, "Not using raw class file for " + name);
         }

         clazz = null;
      }

      long classModTime;
      long sourceModTime;
      if (clazz != null && source != null) {
         classModTime = clazz.rawSourceLastModified();
         sourceModTime = source.lastModified().getTime();
         if (classModTime < sourceModTime) {
            if (Report.should_report((Collection)report_topics, 3)) {
               Report.report(3, "Source file version is newer than compiled for " + name + ".");
            }

            clazz = null;
         } else {
            source = null;
         }
      }

      if (encodedClazz != null && source != null) {
         classModTime = encodedClazz.sourceLastModified(this.version.name());
         sourceModTime = source.lastModified().getTime();
         int comp = this.checkCompilerVersion(encodedClazz.compilerVersion(this.version.name()));
         if (classModTime < sourceModTime) {
            if (Report.should_report((Collection)report_topics, 3)) {
               Report.report(3, "Source file version is newer than compiled for " + name + ".");
            }

            encodedClazz = null;
         } else if (comp != 0) {
            if (Report.should_report((Collection)report_topics, 3)) {
               Report.report(3, "Incompatible source file version for " + name + ".");
            }

            encodedClazz = null;
         }
      }

      if (encodedClazz != null) {
         if (Report.should_report((Collection)report_topics, 4)) {
            Report.report(4, "Using encoded class type for " + name);
         }

         try {
            return this.getEncodedType(encodedClazz, name);
         } catch (BadSerializationException var10) {
            throw var10;
         } catch (SemanticException var11) {
            if (Report.should_report((Collection)report_topics, 4)) {
               Report.report(4, "Could not load encoded class " + name);
            }

            encodedClazz = null;
         }
      }

      if (clazz != null) {
         if (Report.should_report((Collection)report_topics, 4)) {
            Report.report(4, "Using raw class file for " + name);
         }

         return clazz.type(this.ts);
      } else if (source != null) {
         if (Report.should_report((Collection)report_topics, 4)) {
            Report.report(4, "Using source file for " + name);
         }

         return this.getTypeFromSource(source, name);
      } else if (clazz != null && !this.allowRawClasses) {
         throw new SemanticException("Class \"" + name + "\" not found." + " A class file was found, but it did not contain appropriate" + " information for the Polyglot-based compiler " + this.ext.compilerName() + ". Try using " + this.ext.compilerName() + " to recompile the source code.");
      } else {
         throw new NoClassException(name);
      }
   }

   protected Named getTypeFromSource(FileSource source, String name) throws SemanticException {
      this.ext.readSource(source);
      return this.ts.parsedResolver().find(name);
   }
}
