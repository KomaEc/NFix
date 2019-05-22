package soot.javaToJimple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import polyglot.ast.Block;
import polyglot.ast.FieldDecl;
import polyglot.types.ClassType;
import soot.Local;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootFieldRef;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Type;
import soot.Unit;
import soot.VoidType;
import soot.jimple.AssignStmt;
import soot.jimple.FieldRef;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InvokeExpr;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.ParameterRef;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.Stmt;
import soot.jimple.ThisRef;
import soot.tagkit.EnclosingTag;
import soot.tagkit.QualifyingTag;

public class AnonInitBodyBuilder extends JimpleBodyBuilder {
   public JimpleBody createBody(SootMethod sootMethod) {
      this.body = Jimple.v().newBody(sootMethod);
      this.lg = new LocalGenerator(this.body);
      AnonClassInitMethodSource acims = (AnonClassInitMethodSource)this.body.getMethod().getSource();
      ArrayList<SootField> fields = acims.getFinalsList();
      boolean inStaticMethod = acims.inStaticMethod();
      boolean isSubType = acims.isSubType();
      Type superOuterType = acims.superOuterType();
      Type thisOuterType = acims.thisOuterType();
      ArrayList<FieldDecl> fieldInits = acims.getFieldInits();
      Type outerClassType = acims.outerClassType();
      ClassType polyglotType = acims.polyglotType();
      ClassType anonType = acims.anonType();
      boolean hasOuterRef = ((AnonClassInitMethodSource)this.body.getMethod().getSource()).hasOuterRef();
      boolean hasQualifier = ((AnonClassInitMethodSource)this.body.getMethod().getSource()).hasQualifier();
      RefType type = sootMethod.getDeclaringClass().getType();
      this.specialThisLocal = Jimple.v().newLocal("this", type);
      this.body.getLocals().add(this.specialThisLocal);
      ThisRef thisRef = Jimple.v().newThisRef(type);
      Stmt thisStmt = Jimple.v().newIdentityStmt(this.specialThisLocal, thisRef);
      this.body.getUnits().add((Unit)thisStmt);
      ArrayList invokeList = new ArrayList();
      ArrayList invokeTypeList = new ArrayList();
      int numParams = sootMethod.getParameterCount();
      int numFinals = 0;
      if (fields != null) {
         numFinals = fields.size();
      }

      int startFinals = numParams - numFinals;
      ArrayList paramsForFinals = new ArrayList();
      Local outerLocal = null;
      Local qualifierLocal = null;
      Iterator fIt = sootMethod.getParameterTypes().iterator();

      for(int counter = 0; fIt.hasNext(); ++counter) {
         Type fType = (Type)fIt.next();
         Local local = Jimple.v().newLocal("r" + counter, fType);
         this.body.getLocals().add(local);
         ParameterRef paramRef = Jimple.v().newParameterRef(fType, counter);
         Stmt stmt = Jimple.v().newIdentityStmt(local, paramRef);
         int realArgs = 0;
         if (hasOuterRef && counter == 0) {
            outerLocal = local;
            realArgs = 1;
            stmt.addTag(new EnclosingTag());
         }

         if (hasOuterRef && hasQualifier && counter == 1) {
            realArgs = 2;
            invokeList.add(local);
            stmt.addTag(new QualifyingTag());
         } else if (!hasOuterRef && hasQualifier && counter == 0) {
            realArgs = 1;
            invokeList.add(local);
            stmt.addTag(new QualifyingTag());
         }

         if (counter >= realArgs && counter < startFinals) {
            invokeTypeList.add(fType);
            invokeList.add(local);
         } else if (counter >= startFinals) {
            paramsForFinals.add(local);
         }

         this.body.getUnits().add((Unit)stmt);
      }

      SootClass superClass = sootMethod.getDeclaringClass().getSuperclass();
      if (this.needsOuterClassRef(polyglotType)) {
         invokeTypeList.add(0, superOuterType);
      }

      SootMethodRef callMethod = Scene.v().makeMethodRef(sootMethod.getDeclaringClass().getSuperclass(), "<init>", invokeTypeList, VoidType.v(), false);
      if (!hasQualifier && this.needsOuterClassRef(polyglotType)) {
         if (isSubType) {
            invokeList.add(0, outerLocal);
         } else {
            invokeList.add(0, Util.getThisGivenOuter(superOuterType, new HashMap(), this.body, new LocalGenerator(this.body), outerLocal));
         }
      }

      InvokeExpr invoke = Jimple.v().newSpecialInvokeExpr(this.specialThisLocal, callMethod, (List)invokeList);
      Stmt invokeStmt = Jimple.v().newInvokeStmt(invoke);
      this.body.getUnits().add((Unit)invokeStmt);
      if (!inStaticMethod && this.needsOuterClassRef(anonType)) {
         SootFieldRef field = Scene.v().makeFieldRef(sootMethod.getDeclaringClass(), "this$0", outerClassType, false);
         InstanceFieldRef ref = Jimple.v().newInstanceFieldRef(this.specialThisLocal, field);
         AssignStmt assign = Jimple.v().newAssignStmt(ref, outerLocal);
         this.body.getUnits().add((Unit)assign);
      }

      if (fields != null) {
         Iterator finalsIt = paramsForFinals.iterator();
         Iterator fieldsIt = fields.iterator();

         while(finalsIt.hasNext() && fieldsIt.hasNext()) {
            Local pLocal = (Local)finalsIt.next();
            SootField pField = (SootField)fieldsIt.next();
            FieldRef pRef = Jimple.v().newInstanceFieldRef(this.specialThisLocal, pField.makeRef());
            AssignStmt pAssign = Jimple.v().newAssignStmt(pRef, pLocal);
            this.body.getUnits().add((Unit)pAssign);
         }
      }

      if (fieldInits != null) {
         this.handleFieldInits(fieldInits);
      }

      ArrayList<Block> staticBlocks = ((AnonClassInitMethodSource)this.body.getMethod().getSource()).getInitializerBlocks();
      if (staticBlocks != null) {
         this.handleStaticBlocks(staticBlocks);
      }

      ReturnVoidStmt retStmt = Jimple.v().newReturnVoidStmt();
      this.body.getUnits().add((Unit)retStmt);
      return this.body;
   }
}
