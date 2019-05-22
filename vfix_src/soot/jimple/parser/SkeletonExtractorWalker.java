package soot.jimple.parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.SootResolver;
import soot.Type;
import soot.jimple.parser.node.AFile;
import soot.jimple.parser.node.AMethodMember;
import soot.jimple.parser.node.AThrowsClause;
import soot.jimple.parser.node.PModifier;

public class SkeletonExtractorWalker extends Walker {
   public SkeletonExtractorWalker(SootResolver aResolver, SootClass aSootClass) {
      super(aSootClass, aResolver);
   }

   public SkeletonExtractorWalker(SootResolver aResolver) {
      super(aResolver);
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
      if (this.mSootClass == null) {
         this.mSootClass = new SootClass(className);
         this.mSootClass.setResolvingLevel(2);
      } else if (!className.equals(this.mSootClass.getName())) {
         throw new RuntimeException("expected:  " + className + ", but got: " + this.mSootClass.getName());
      }

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

   public void outAFile(AFile node) {
      List implementsList = null;
      String superClass = null;
      String classType = null;
      if (node.getImplementsClause() != null) {
         implementsList = (List)this.mProductions.removeLast();
      }

      if (node.getExtendsClause() != null) {
         superClass = (String)this.mProductions.removeLast();
      }

      classType = (String)this.mProductions.removeLast();
      int modifierFlags = this.processModifiers(node.getModifier());
      if (classType.equals("interface")) {
         modifierFlags |= 512;
      }

      this.mSootClass.setModifiers(modifierFlags);
      if (superClass != null) {
         this.mSootClass.setSuperclass(this.mResolver.makeClassRef(superClass));
      }

      if (implementsList != null) {
         Iterator implIt = implementsList.iterator();

         while(implIt.hasNext()) {
            SootClass interfaceClass = this.mResolver.makeClassRef((String)implIt.next());
            this.mSootClass.addInterface(interfaceClass);
         }
      }

      this.mProductions.addLast(this.mSootClass);
   }

   public void caseAMethodMember(AMethodMember node) {
      this.inAMethodMember(node);
      Object[] temp = node.getModifier().toArray();
      Object[] var3 = temp;
      int var4 = temp.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Object element = var3[var5];
         ((PModifier)element).apply(this);
      }

      if (node.getType() != null) {
         node.getType().apply(this);
      }

      if (node.getName() != null) {
         node.getName().apply(this);
      }

      if (node.getLParen() != null) {
         node.getLParen().apply(this);
      }

      if (node.getParameterList() != null) {
         node.getParameterList().apply(this);
      }

      if (node.getRParen() != null) {
         node.getRParen().apply(this);
      }

      if (node.getThrowsClause() != null) {
         node.getThrowsClause().apply(this);
      }

      this.outAMethodMember(node);
   }

   public void outAMethodMember(AMethodMember node) {
      int modifier = false;
      List parameterList = null;
      List<SootClass> throwsClause = null;
      if (node.getThrowsClause() != null) {
         throwsClause = (List)this.mProductions.removeLast();
      }

      if (node.getParameterList() != null) {
         parameterList = (List)this.mProductions.removeLast();
      } else {
         parameterList = new ArrayList();
      }

      Object o = this.mProductions.removeLast();
      String name = (String)o;
      Type type = (Type)this.mProductions.removeLast();
      int modifier = this.processModifiers(node.getModifier());
      SootMethod method;
      if (throwsClause != null) {
         method = Scene.v().makeSootMethod(name, (List)parameterList, type, modifier, throwsClause);
      } else {
         method = Scene.v().makeSootMethod(name, (List)parameterList, type, modifier);
      }

      this.mSootClass.addMethod(method);
   }

   public void outAThrowsClause(AThrowsClause node) {
      List l = (List)this.mProductions.removeLast();
      Iterator it = l.iterator();
      ArrayList exceptionClasses = new ArrayList(l.size());

      while(it.hasNext()) {
         String className = (String)it.next();
         exceptionClasses.add(this.mResolver.makeClassRef(className));
      }

      this.mProductions.addLast(exceptionClasses);
   }
}
