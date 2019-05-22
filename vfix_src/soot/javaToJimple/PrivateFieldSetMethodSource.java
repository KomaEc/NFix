package soot.javaToJimple;

import java.util.Iterator;
import soot.Body;
import soot.Local;
import soot.MethodSource;
import soot.Scene;
import soot.SootFieldRef;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.Jimple;
import soot.jimple.ParameterRef;
import soot.jimple.Stmt;

public class PrivateFieldSetMethodSource implements MethodSource {
   private final Type fieldType;
   private final String fieldName;
   private final boolean isStatic;

   public PrivateFieldSetMethodSource(Type fieldType, String fieldName, boolean isStatic) {
      this.fieldType = fieldType;
      this.fieldName = fieldName;
      this.isStatic = isStatic;
   }

   public Body getBody(SootMethod sootMethod, String phaseName) {
      Body body = Jimple.v().newBody(sootMethod);
      LocalGenerator lg = new LocalGenerator(body);
      Local fieldBase = null;
      Local assignLocal = null;
      int paramCounter = 0;

      Local paramLocal;
      for(Iterator paramIt = sootMethod.getParameterTypes().iterator(); paramIt.hasNext(); ++paramCounter) {
         Type sootType = (Type)paramIt.next();
         paramLocal = lg.generateLocal(sootType);
         ParameterRef paramRef = Jimple.v().newParameterRef(sootType, paramCounter);
         Stmt stmt = Jimple.v().newIdentityStmt(paramLocal, paramRef);
         body.getUnits().add((Unit)stmt);
         if (paramCounter == 0) {
            fieldBase = paramLocal;
         }

         assignLocal = paramLocal;
      }

      SootFieldRef field = Scene.v().makeFieldRef(sootMethod.getDeclaringClass(), this.fieldName, this.fieldType, this.isStatic);
      paramLocal = null;
      Object fieldRef;
      if (this.isStatic) {
         fieldRef = Jimple.v().newStaticFieldRef(field);
      } else {
         fieldRef = Jimple.v().newInstanceFieldRef(fieldBase, field);
      }

      AssignStmt assign = Jimple.v().newAssignStmt((Value)fieldRef, assignLocal);
      body.getUnits().add((Unit)assign);
      Stmt retStmt = Jimple.v().newReturnStmt(assignLocal);
      body.getUnits().add((Unit)retStmt);
      return body;
   }
}
