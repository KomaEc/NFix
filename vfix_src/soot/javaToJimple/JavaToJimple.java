package soot.javaToJimple;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import polyglot.ast.Node;
import polyglot.frontend.Compiler;
import polyglot.frontend.ExtensionInfo;
import polyglot.frontend.FileSource;
import polyglot.frontend.Job;
import polyglot.frontend.Pass;
import polyglot.frontend.SourceJob;
import polyglot.frontend.SourceLoader;
import polyglot.frontend.VisitorPass;
import polyglot.main.Options;
import soot.CompilationDeathException;

public class JavaToJimple {
   public static final Pass.ID CAST_INSERTION = new Pass.ID("cast-insertion");
   public static final Pass.ID STRICTFP_PROP = new Pass.ID("strictfp-prop");
   public static final Pass.ID ANON_CONSTR_FINDER = new Pass.ID("anon-constr-finder");
   public static final Pass.ID SAVE_AST = new Pass.ID("save-ast");

   public ExtensionInfo initExtInfo(String fileName, List<String> sourceLocations) {
      Set<String> source = new HashSet();
      ExtensionInfo extInfo = new soot.javaToJimple.jj.ExtensionInfo() {
         public List passes(Job job) {
            List passes = super.passes(job);
            this.beforePass(passes, Pass.EXIT_CHECK, new VisitorPass(JavaToJimple.CAST_INSERTION, job, new CastInsertionVisitor(job, this.ts, this.nf)));
            this.beforePass(passes, Pass.EXIT_CHECK, new VisitorPass(JavaToJimple.STRICTFP_PROP, job, new StrictFPPropagator(false)));
            this.beforePass(passes, Pass.EXIT_CHECK, new VisitorPass(JavaToJimple.ANON_CONSTR_FINDER, job, new AnonConstructorFinder(job, this.ts, this.nf)));
            this.afterPass(passes, Pass.PRE_OUTPUT_ALL, new SaveASTVisitor(JavaToJimple.SAVE_AST, job, this));
            this.removePass(passes, Pass.OUTPUT);
            return passes;
         }
      };
      Options options = extInfo.getOptions();
      options.assertions = true;
      options.source_path = new LinkedList();
      Iterator it = sourceLocations.iterator();

      while(it.hasNext()) {
         Object next = it.next();
         options.source_path.add(new File(next.toString()));
      }

      options.source_ext = new String[]{"java"};
      options.serialize_type_info = false;
      source.add(fileName);
      options.source_path.add((new File(fileName)).getParentFile());
      Options.global = options;
      return extInfo;
   }

   public Node compile(Compiler compiler, String fileName, ExtensionInfo extInfo) {
      SourceLoader var4 = compiler.sourceExtension().sourceLoader();

      try {
         FileSource source = new FileSource(new File(fileName));
         SourceJob job = null;
         if (compiler.sourceExtension() instanceof soot.javaToJimple.jj.ExtensionInfo) {
            soot.javaToJimple.jj.ExtensionInfo jjInfo = (soot.javaToJimple.jj.ExtensionInfo)compiler.sourceExtension();
            if (jjInfo.sourceJobMap() != null) {
               job = (SourceJob)jjInfo.sourceJobMap().get(source);
            }
         }

         if (job == null) {
            job = compiler.sourceExtension().addJob(source);
         }

         boolean result = false;
         result = compiler.sourceExtension().runToCompletion();
         if (!result) {
            throw new CompilationDeathException(0, "Could not compile");
         } else {
            Node node = job.ast();
            return node;
         }
      } catch (IOException var9) {
         return null;
      }
   }
}
