package polyglot.visit;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import polyglot.ast.Import;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.ast.SourceCollection;
import polyglot.ast.SourceFile;
import polyglot.ast.TopLevelDecl;
import polyglot.frontend.Job;
import polyglot.frontend.TargetFactory;
import polyglot.main.Options;
import polyglot.types.ClassType;
import polyglot.types.Context;
import polyglot.types.Package;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.Copy;
import polyglot.util.InternalCompilerError;

public class Translator extends PrettyPrinter implements Copy {
   protected Job job;
   protected NodeFactory nf;
   protected TargetFactory tf;
   protected TypeSystem ts;
   protected Context context;
   protected ClassType outerClass = null;
   private static HashMap createdFiles = new HashMap();

   public static HashMap getFileNames() {
      return createdFiles;
   }

   public Translator(Job job, TypeSystem ts, NodeFactory nf, TargetFactory tf) {
      this.job = job;
      this.nf = nf;
      this.tf = tf;
      this.ts = ts;
      this.context = job.context();
      if (this.context == null) {
         this.context = ts.createContext();
      }

   }

   public Job job() {
      return this.job;
   }

   public Translator context(Context c) {
      if (c == this.context) {
         return this;
      } else {
         Translator tr = (Translator)this.copy();
         tr.context = c;
         return tr;
      }
   }

   public Object copy() {
      try {
         return super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new InternalCompilerError("Java clone() weirdness.");
      }
   }

   public HeaderTranslator headerContext(Context c) {
      HeaderTranslator ht = new HeaderTranslator(this);
      ht.context = c;
      return ht;
   }

   public ClassType outerClass() {
      return this.outerClass;
   }

   public void setOuterClass(ClassType ct) {
      this.outerClass = ct;
   }

   public TypeSystem typeSystem() {
      return this.ts;
   }

   public Context context() {
      return this.context;
   }

   public NodeFactory nodeFactory() {
      return this.nf;
   }

   public TargetFactory targetFactory() {
      return this.tf;
   }

   public void print(Node parent, Node child, CodeWriter w) {
      Translator tr;
      Context c;
      if (parent != null) {
         c = parent.del().enterScope(child, this.context);
         tr = this.context(c);
      } else {
         c = child.del().enterScope(this.context);
         tr = this.context(c);
      }

      child.del().translate(w, tr);
      if (parent != null) {
         parent.addDecls(this.context);
      }

   }

   public boolean translate(Node ast) {
      if (ast instanceof SourceFile) {
         SourceFile sfn = (SourceFile)ast;
         return this.translateSource(sfn);
      } else if (!(ast instanceof SourceCollection)) {
         throw new InternalCompilerError("AST root must be a SourceFile; found a " + ast.getClass().getName());
      } else {
         SourceCollection sc = (SourceCollection)ast;
         boolean okay = true;

         SourceFile sfn;
         for(Iterator i = sc.sources().iterator(); i.hasNext(); okay &= this.translateSource(sfn)) {
            sfn = (SourceFile)i.next();
         }

         return okay;
      }
   }

   protected boolean translateSource(SourceFile sfn) {
      TypeSystem ts = this.typeSystem();
      NodeFactory nf = this.nodeFactory();
      TargetFactory tf = this.tf;
      int outputWidth = this.job.compiler().outputWidth();
      Collection outputFiles = this.job.compiler().outputFiles();
      List exports = this.exports(sfn);

      try {
         Writer headerWriter = null;
         CodeWriter wH = null;
         String pkg = "";
         if (sfn.package_() != null) {
            Package p = sfn.package_().package_();
            pkg = p.toString();
         }

         Context c = sfn.del().enterScope(this.context);
         TopLevelDecl first = null;
         File of;
         if (exports.size() == 0) {
            of = tf.outputFile(pkg, sfn.source());
         } else {
            first = (TopLevelDecl)exports.get(0);
            of = tf.outputFile(pkg, first.name(), sfn.source());
         }

         String opfPath = of.getPath();
         if (!opfPath.endsWith("$")) {
            outputFiles.add(of.getPath());
         }

         Writer ofw = tf.outputWriter(of);
         CodeWriter w = new CodeWriter(ofw, outputWidth);
         createdFiles.put(of.getPath(), (Object)null);
         if (Options.global.cppBackend()) {
            File headerFile = new File(tf.headerNameForFileName(of.getPath()));
            headerWriter = tf.outputWriter(headerFile);
            wH = new CodeWriter(headerWriter, outputWidth);
            String className = null;
            if (!exports.isEmpty()) {
               first = (TopLevelDecl)exports.get(0);
               className = first.name();
            } else {
               String name = sfn.source().name();
               className = name.substring(0, name.lastIndexOf(46));
            }

            this.writeHFileHeader(sfn, className, wH);
         }

         this.writeHeader(sfn, w);
         Iterator i = sfn.decls().iterator();

         while(i.hasNext()) {
            TopLevelDecl decl = (TopLevelDecl)i.next();
            if (decl.flags().isPublic() && decl != first && !Options.global.cppBackend()) {
               w.flush();
               ofw.close();
               of = tf.outputFile(pkg, decl.name(), sfn.source());
               outputFiles.add(of.getPath());
               ofw = tf.outputWriter(of);
               w = new CodeWriter(ofw, outputWidth);
               this.writeHeader(sfn, w);
            }

            decl.del().translate(w, this.context(c));
            if (Options.global.cppBackend()) {
               decl.del().translate(wH, this.headerContext(c));
            }

            if (i.hasNext()) {
               w.newline(0);
            }
         }

         this.writeFooter(sfn, w);
         if (Options.global.cppBackend()) {
            this.writeHFileFooter(sfn, wH);
            wH.flush();
            headerWriter.close();
         }

         w.flush();
         ofw.close();
         return true;
      } catch (IOException var20) {
         this.job.compiler().errorQueue().enqueue(2, "I/O error while translating: " + var20.getMessage());
         return false;
      }
   }

   public static String macroEscape(String s) {
      String out = "_";

      for(int i = 0; i < s.length(); ++i) {
         char c = s.charAt(i);
         if (c != '.' && c != ':') {
            out = out + c;
         } else {
            out = out + "_";
         }
      }

      return out;
   }

   public static String cScope(String s) {
      String out = "";

      for(int i = 0; i < s.length(); ++i) {
         char c = s.charAt(i);
         if (c == '.') {
            out = out + "::";
         } else {
            out = out + c;
         }
      }

      return out;
   }

   protected void writeHFileHeader(SourceFile sfn, String className, CodeWriter w) {
      String pkg = null;
      if (sfn.package_() != null) {
         Package p = sfn.package_().package_();
         pkg = p.fullName();
      }

      if (pkg == null || pkg.equals("")) {
         pkg = "jmatch_primary";
      }

      String macroName = "_" + macroEscape(pkg) + "_" + macroEscape(className) + "_H";
      w.write("#ifndef " + macroName);
      w.newline(0);
      w.write("#define " + macroName);
      w.newline(0);
      if (sfn.package_() != null) {
         sfn.package_().del().translate(w, this);
      } else {
         w.write("namespace " + cScope(pkg) + " {");
      }

      w.newline(0);
      w.write("using namespace jmatch_primary;");
      w.newline(0);
      w.write("using namespace java::lang;");
      w.newline(0);
      Iterator i = sfn.imports().iterator();

      while(i.hasNext()) {
         Import imp = (Import)i.next();
         imp.del().translate(w, this);
         w.newline(0);
      }

   }

   protected void writeHFileFooter(SourceFile sfn, CodeWriter w) {
      int packageDepth = 0;
      if (null != sfn.package_()) {
         Package p = sfn.package_().package_();
         String pkgName = p.toString();
         if (pkgName.length() > 0) {
            ++packageDepth;
         }

         int i;
         for(i = 0; i < pkgName.length(); ++i) {
            if (pkgName.charAt(i) == '.') {
               ++packageDepth;
            }
         }

         w.write("/* closing namespace */");
         w.newline(0);

         for(i = 0; i < packageDepth; ++i) {
            w.write("}");
         }

         w.newline(0);
         w.newline(0);
      }

      if (packageDepth == 0) {
         w.newline(0);
         w.write("} /* namespace */");
         w.newline(0);
         w.newline(0);
      }

      w.write("#endif");
      w.newline(0);
      w.newline(0);
   }

   protected void writeFooter(SourceFile sfn, CodeWriter w) {
      if (Options.global.cppBackend()) {
         int packageDepth = 0;
         if (null != sfn.package_()) {
            Package p = sfn.package_().package_();
            String pkgName = p.toString();
            if (pkgName.length() > 0) {
               ++packageDepth;
            }

            int i;
            for(i = 0; i < pkgName.length(); ++i) {
               if (pkgName.charAt(i) == '.') {
                  ++packageDepth;
               }
            }

            w.write("/* closing namespace */");
            w.newline(0);

            for(i = 0; i < packageDepth; ++i) {
               w.write("}");
            }

            w.newline(0);
            w.newline(0);
         }

         if (packageDepth == 0) {
            w.newline(0);
            w.write("} /* namespace */");
            w.newline(0);
            w.newline(0);
         }
      }

   }

   protected void writeHeader(SourceFile sfn, CodeWriter w) {
      if (Options.global.cppBackend()) {
         String pkg = "";
         if (sfn.package_() != null) {
            Package p = sfn.package_().package_();
            pkg = p.toString() + ".";
         }

         int i = false;
         int dots = 0;

         int i;
         for(i = 0; i < pkg.length(); ++i) {
            if (pkg.charAt(i) == '.') {
               ++dots;
            }
         }

         w.write("#include\"");

         for(i = 0; i < dots; ++i) {
            w.write("../");
         }

         w.write("mainproj.h\"");
         w.newline(0);
         if (null != sfn.package_()) {
            sfn.package_().del().translate(w, this);
            w.newline(0);
            w.newline(0);
         } else {
            w.write("namespace jmatch_primary {");
            w.newline(0);
            w.newline(0);
         }

         w.write("using namespace jmatch_primary;");
         w.newline(0);
         w.write("using namespace java::lang;");
         w.newline(0);
         Iterator it = sfn.imports().iterator();

         while(it.hasNext()) {
            Import imp = (Import)it.next();
            imp.del().translate(w, this);
            w.newline(0);
         }
      } else {
         if (sfn.package_() != null) {
            w.write("package ");
            sfn.package_().del().translate(w, this);
            w.write(";");
            w.newline(0);
            w.newline(0);
         }

         boolean newline = false;

         for(Iterator i = sfn.imports().iterator(); i.hasNext(); newline = true) {
            Import imp = (Import)i.next();
            imp.del().translate(w, this);
         }

         if (newline) {
            w.newline(0);
         }
      }

   }

   protected List exports(SourceFile sfn) {
      List exports = new LinkedList();
      Iterator i = sfn.decls().iterator();

      while(i.hasNext()) {
         TopLevelDecl decl = (TopLevelDecl)i.next();
         if (decl.flags().isPublic()) {
            exports.add(decl);
         }
      }

      return exports;
   }

   public String toString() {
      return "Translator";
   }
}
