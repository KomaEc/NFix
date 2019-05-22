package soot.jimple.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.util.HashMap;
import java.util.Set;
import soot.Body;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.SootResolver;
import soot.jimple.JimpleBody;
import soot.jimple.parser.lexer.Lexer;
import soot.jimple.parser.lexer.LexerException;
import soot.jimple.parser.node.Start;
import soot.jimple.parser.parser.Parser;
import soot.jimple.parser.parser.ParserException;

public class JimpleAST {
   private Start mTree = null;
   private HashMap<SootMethod, JimpleBody> methodToParsedBodyMap = null;

   public JimpleAST(InputStream aJIS) throws ParserException, LexerException, IOException {
      Parser p = new Parser(new Lexer(new PushbackReader(new BufferedReader(new InputStreamReader(aJIS)), 1024)));
      this.mTree = p.parse();
   }

   public SootClass createSootClass() {
      Walker w = new Walker(SootResolver.v());
      this.mTree.apply(w);
      return w.getSootClass();
   }

   public void getSkeleton(SootClass sc) {
      Walker w = new SkeletonExtractorWalker(SootResolver.v(), sc);
      this.mTree.apply(w);
   }

   public Body getBody(SootMethod m) {
      if (this.methodToParsedBodyMap == null) {
         synchronized(this) {
            if (this.methodToParsedBodyMap == null) {
               this.stashBodiesForClass(m.getDeclaringClass());
            }
         }
      }

      return (Body)this.methodToParsedBodyMap.get(m);
   }

   public Set<String> getCstPool() {
      CstPoolExtractor cpe = new CstPoolExtractor(this.mTree);
      return cpe.getCstPool();
   }

   public SootResolver getResolver() {
      return SootResolver.v();
   }

   private void stashBodiesForClass(SootClass sc) {
      HashMap<SootMethod, JimpleBody> methodToBodyMap = new HashMap();
      Walker w = new BodyExtractorWalker(sc, SootResolver.v(), methodToBodyMap);
      boolean oldPhantomValue = Scene.v().getPhantomRefs();
      Scene.v().setPhantomRefs(true);
      this.mTree.apply(w);
      Scene.v().setPhantomRefs(oldPhantomValue);
      this.methodToParsedBodyMap = methodToBodyMap;
   }
}
