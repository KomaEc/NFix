package polyglot.frontend;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import polyglot.types.reflect.ClassFileLoader;
import polyglot.util.ErrorLimitError;
import polyglot.util.ErrorQueue;
import polyglot.util.InternalCompilerError;
import polyglot.util.StdErrorQueue;

public class Compiler {
   private ExtensionInfo extensionInfo;
   private List allExtensions;
   private ErrorQueue eq;
   private ClassFileLoader loader;
   private Collection outputFiles;
   // $FF: synthetic field
   static Class class$polyglot$frontend$Compiler;

   public Compiler(ExtensionInfo extensionInfo) {
      this(extensionInfo, new StdErrorQueue(System.err, extensionInfo.getOptions().error_count, extensionInfo.compilerName()));
   }

   public Compiler(ExtensionInfo extensionInfo, ErrorQueue eq) {
      this.outputFiles = new HashSet();
      this.extensionInfo = extensionInfo;
      this.eq = eq;
      this.allExtensions = new ArrayList(2);
      this.loader = new ClassFileLoader(extensionInfo);
      extensionInfo.initCompiler(this);
   }

   public Collection outputFiles() {
      return this.outputFiles;
   }

   public boolean compile(Collection sources) {
      boolean okay = false;

      try {
         try {
            SourceLoader source_loader = this.sourceExtension().sourceLoader();
            Iterator i = sources.iterator();

            while(i.hasNext()) {
               String sourceName = (String)i.next();
               FileSource source = source_loader.fileSource(sourceName);
               source.setUserSpecified(true);
               this.sourceExtension().addJob(source);
            }

            okay = this.sourceExtension().runToCompletion();
         } catch (FileNotFoundException var8) {
            this.eq.enqueue(2, "Cannot find source file \"" + var8.getMessage() + "\".");
         } catch (IOException var9) {
            this.eq.enqueue(2, var9.getMessage());
         } catch (InternalCompilerError var10) {
            InternalCompilerError e = var10;

            try {
               this.eq.enqueue(1, e.message(), e.position());
            } catch (ErrorLimitError var7) {
            }

            this.eq.flush();
            throw var10;
         } catch (RuntimeException var11) {
            this.eq.flush();
            throw var11;
         }
      } catch (ErrorLimitError var12) {
      }

      this.eq.flush();
      Iterator i = this.allExtensions.iterator();

      while(i.hasNext()) {
         ExtensionInfo ext = (ExtensionInfo)i.next();
         ext.getStats().report();
      }

      return okay;
   }

   public ClassFileLoader loader() {
      return this.loader;
   }

   public boolean useFullyQualifiedNames() {
      return this.extensionInfo.getOptions().fully_qualified_names;
   }

   public void addExtension(ExtensionInfo ext) {
      this.allExtensions.add(ext);
   }

   public List allExtensions() {
      return this.allExtensions;
   }

   public ExtensionInfo sourceExtension() {
      return this.extensionInfo;
   }

   public int outputWidth() {
      return this.extensionInfo.getOptions().output_width;
   }

   public boolean serializeClassInfo() {
      return this.extensionInfo.getOptions().serialize_type_info;
   }

   public ErrorQueue errorQueue() {
      return this.eq;
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   static {
      try {
         ClassLoader loader = (class$polyglot$frontend$Compiler == null ? (class$polyglot$frontend$Compiler = class$("polyglot.frontend.Compiler")) : class$polyglot$frontend$Compiler).getClassLoader();
         loader.loadClass("polyglot.util.StdErrorQueue");
      } catch (ClassNotFoundException var1) {
         throw new InternalCompilerError(var1.getMessage());
      }
   }
}
