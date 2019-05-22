package polyglot.ext.jl;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import polyglot.ast.NodeFactory;
import polyglot.ext.jl.ast.NodeFactory_c;
import polyglot.ext.jl.parse.Grm;
import polyglot.ext.jl.parse.Lexer_c;
import polyglot.ext.jl.types.TypeSystem_c;
import polyglot.frontend.AbstractExtensionInfo;
import polyglot.frontend.BarrierPass;
import polyglot.frontend.CupParser;
import polyglot.frontend.EmptyPass;
import polyglot.frontend.FileSource;
import polyglot.frontend.GlobalBarrierPass;
import polyglot.frontend.Job;
import polyglot.frontend.JobExt;
import polyglot.frontend.OutputPass;
import polyglot.frontend.Parser;
import polyglot.frontend.ParserPass;
import polyglot.frontend.Pass;
import polyglot.frontend.VisitorPass;
import polyglot.lex.Lexer;
import polyglot.parse.BaseParser;
import polyglot.types.LoadedClassResolver;
import polyglot.types.SemanticException;
import polyglot.types.SourceClassResolver;
import polyglot.types.TypeSystem;
import polyglot.util.ErrorQueue;
import polyglot.util.InternalCompilerError;
import polyglot.visit.AddMemberVisitor;
import polyglot.visit.AmbiguityRemover;
import polyglot.visit.ClassSerializer;
import polyglot.visit.ConstructorCallChecker;
import polyglot.visit.ExceptionChecker;
import polyglot.visit.ExitChecker;
import polyglot.visit.FwdReferenceChecker;
import polyglot.visit.InitChecker;
import polyglot.visit.ReachChecker;
import polyglot.visit.Translator;
import polyglot.visit.TypeBuilder;
import polyglot.visit.TypeChecker;

public class ExtensionInfo extends AbstractExtensionInfo {
   protected void initTypeSystem() {
      try {
         LoadedClassResolver lr = new SourceClassResolver(this.compiler, this, this.getOptions().constructFullClasspath(), this.compiler.loader(), true);
         this.ts.initialize(lr, this);
      } catch (SemanticException var2) {
         throw new InternalCompilerError("Unable to initialize type system: " + var2.getMessage());
      }
   }

   public String defaultFileExtension() {
      return "jl";
   }

   public String compilerName() {
      return "jlc";
   }

   public polyglot.main.Version version() {
      return new Version();
   }

   protected TypeSystem createTypeSystem() {
      return new TypeSystem_c();
   }

   protected NodeFactory createNodeFactory() {
      return new NodeFactory_c();
   }

   public JobExt jobExt() {
      return null;
   }

   public Parser parser(Reader reader, FileSource source, ErrorQueue eq) {
      Lexer lexer = new Lexer_c(reader, source.name(), eq);
      BaseParser parser = new Grm(lexer, this.ts, this.nf, eq);
      return new CupParser(parser, source, eq);
   }

   public List passes(Job job) {
      ArrayList l = new ArrayList(15);
      l.add(new ParserPass(Pass.PARSE, this.compiler, job));
      l.add(new VisitorPass(Pass.BUILD_TYPES, job, new TypeBuilder(job, this.ts, this.nf)));
      l.add(new GlobalBarrierPass(Pass.BUILD_TYPES_ALL, job));
      l.add(new VisitorPass(Pass.CLEAN_SUPER, job, new AmbiguityRemover(job, this.ts, this.nf, AmbiguityRemover.SUPER)));
      l.add(new BarrierPass(Pass.CLEAN_SUPER_ALL, job));
      l.add(new VisitorPass(Pass.CLEAN_SIGS, job, new AmbiguityRemover(job, this.ts, this.nf, AmbiguityRemover.SIGNATURES)));
      l.add(new VisitorPass(Pass.ADD_MEMBERS, job, new AddMemberVisitor(job, this.ts, this.nf)));
      l.add(new GlobalBarrierPass(Pass.ADD_MEMBERS_ALL, job));
      l.add(new VisitorPass(Pass.DISAM, job, new AmbiguityRemover(job, this.ts, this.nf, AmbiguityRemover.ALL)));
      l.add(new BarrierPass(Pass.DISAM_ALL, job));
      l.add(new VisitorPass(Pass.TYPE_CHECK, job, new TypeChecker(job, this.ts, this.nf)));
      l.add(new VisitorPass(Pass.REACH_CHECK, job, new ReachChecker(job, this.ts, this.nf)));
      l.add(new VisitorPass(Pass.EXC_CHECK, job, new ExceptionChecker(job, this.ts, this.nf)));
      l.add(new VisitorPass(Pass.EXIT_CHECK, job, new ExitChecker(job, this.ts, this.nf)));
      l.add(new VisitorPass(Pass.INIT_CHECK, job, new InitChecker(job, this.ts, this.nf)));
      l.add(new VisitorPass(Pass.CONSTRUCTOR_CHECK, job, new ConstructorCallChecker(job, this.ts, this.nf)));
      l.add(new VisitorPass(Pass.FWD_REF_CHECK, job, new FwdReferenceChecker(job, this.ts, this.nf)));
      l.add(new EmptyPass(Pass.PRE_OUTPUT_ALL));
      if (this.compiler.serializeClassInfo()) {
         l.add(new VisitorPass(Pass.SERIALIZE, job, new ClassSerializer(this.ts, this.nf, job.source().lastModified(), this.compiler.errorQueue(), this.version())));
      }

      l.add(new OutputPass(Pass.OUTPUT, job, new Translator(job, this.ts, this.nf, this.targetFactory())));
      return l;
   }

   static {
      new Topics();
   }
}
