package soot.jimple.parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.SootClass;
import soot.SootMethod;
import soot.SootResolver;
import soot.Type;
import soot.jimple.JimpleBody;
import soot.jimple.parser.node.AFieldMember;
import soot.jimple.parser.node.AFile;
import soot.jimple.parser.node.AFullMethodBody;
import soot.jimple.parser.node.AMethodMember;
import soot.jimple.parser.node.PModifier;
import soot.options.Options;

public class BodyExtractorWalker extends Walker {
   private static final Logger logger = LoggerFactory.getLogger(BodyExtractorWalker.class);
   Map<SootMethod, JimpleBody> methodToParsedBodyMap;

   public BodyExtractorWalker(SootClass sc, SootResolver resolver, Map<SootMethod, JimpleBody> methodToParsedBodyMap) {
      super(sc, resolver);
      this.methodToParsedBodyMap = methodToParsedBodyMap;
   }

   public void caseAFile(AFile node) {
      this.inAFile(node);
      Object[] temp = node.getModifier().toArray();
      Object[] var3 = temp;
      int var4 = temp.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Object element = var3[var5];
         ((PModifier)element).apply(this);
      }

      if (node.getFileType() != null) {
         node.getFileType().apply(this);
      }

      if (node.getClassName() != null) {
         node.getClassName().apply(this);
      }

      String className = (String)this.mProductions.removeLast();
      if (!className.equals(this.mSootClass.getName())) {
         throw new RuntimeException("expected:  " + className + ", but got: " + this.mSootClass.getName());
      } else {
         if (node.getExtendsClause() != null) {
            node.getExtendsClause().apply(this);
         }

         if (node.getImplementsClause() != null) {
            node.getImplementsClause().apply(this);
         }

         if (node.getFileBody() != null) {
            node.getFileBody().apply(this);
         }

         this.outAFile(node);
      }
   }

   public void outAFile(AFile node) {
      if (node.getImplementsClause() != null) {
         this.mProductions.removeLast();
      }

      if (node.getExtendsClause() != null) {
         this.mProductions.removeLast();
      }

      this.mProductions.removeLast();
      this.mProductions.addLast(this.mSootClass);
   }

   public void outAFieldMember(AFieldMember node) {
      this.mProductions.removeLast();
      this.mProductions.removeLast();
   }

   public void outAMethodMember(AMethodMember node) {
      List<Type> parameterList = new ArrayList();
      List throwsClause = null;
      JimpleBody methodBody = null;
      if (node.getMethodBody() instanceof AFullMethodBody) {
         methodBody = (JimpleBody)this.mProductions.removeLast();
      }

      if (node.getThrowsClause() != null) {
         throwsClause = (List)this.mProductions.removeLast();
      }

      if (node.getParameterList() != null) {
         parameterList = (List)this.mProductions.removeLast();
      }

      String name = (String)this.mProductions.removeLast();
      Type type = (Type)this.mProductions.removeLast();
      SootMethod sm = this.mSootClass.getMethodUnsafe(SootMethod.getSubSignature(name, (List)parameterList, type));
      if (sm != null) {
         if (Options.v().verbose()) {
            logger.debug("[Jimple parser] " + SootMethod.getSubSignature(name, (List)parameterList, type));
         }
      } else {
         logger.debug("[!!! Couldn't parse !!] " + SootMethod.getSubSignature(name, (List)parameterList, type));
         logger.debug("[!] Methods in class are:");
         Iterator var8 = this.mSootClass.getMethods().iterator();

         while(var8.hasNext()) {
            SootMethod next = (SootMethod)var8.next();
            logger.debug("" + next.getSubSignature());
         }
      }

      if (sm.isConcrete() && methodBody != null) {
         if (Options.v().verbose()) {
            logger.debug("[Parsed] " + sm.getDeclaration());
         }

         methodBody.setMethod(sm);
         this.methodToParsedBodyMap.put(sm, methodBody);
      } else if (node.getMethodBody() instanceof AFullMethodBody) {
         if (sm.isPhantom() && Options.v().verbose()) {
            logger.debug("[jimple parser] phantom method!");
         }

         throw new RuntimeException("Impossible: !concrete => ! instanceof " + sm.getName());
      }

   }
}
