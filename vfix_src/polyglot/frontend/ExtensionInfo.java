package polyglot.frontend;

import java.io.File;
import java.io.Reader;
import java.util.List;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.main.Options;
import polyglot.main.Version;
import polyglot.types.Context;
import polyglot.types.TypeSystem;
import polyglot.types.reflect.ClassFile;
import polyglot.util.ErrorQueue;

public interface ExtensionInfo {
   String compilerName();

   Version version();

   Options getOptions();

   Stats getStats();

   void initCompiler(Compiler var1);

   Compiler compiler();

   String[] fileExtensions();

   String[] defaultFileExtensions();

   String defaultFileExtension();

   TypeSystem typeSystem();

   NodeFactory nodeFactory();

   SourceLoader sourceLoader();

   void addDependencyToCurrentJob(Source var1);

   SourceJob addJob(Source var1);

   SourceJob addJob(Source var1, Node var2);

   Job spawnJob(Context var1, Node var2, Job var3, Pass.ID var4, Pass.ID var5);

   boolean runToCompletion();

   boolean runToPass(Job var1, Pass.ID var2) throws CyclicDependencyException;

   boolean runAllPasses(Job var1);

   boolean readSource(FileSource var1);

   TargetFactory targetFactory();

   Parser parser(Reader var1, FileSource var2, ErrorQueue var3);

   List passes(Job var1);

   List passes(Job var1, Pass.ID var2, Pass.ID var3);

   void beforePass(List var1, Pass.ID var2, Pass var3);

   void beforePass(List var1, Pass.ID var2, List var3);

   void afterPass(List var1, Pass.ID var2, Pass var3);

   void afterPass(List var1, Pass.ID var2, List var3);

   void replacePass(List var1, Pass.ID var2, Pass var3);

   void replacePass(List var1, Pass.ID var2, List var3);

   void removePass(List var1, Pass.ID var2);

   ClassFile createClassFile(File var1, byte[] var2);
}
