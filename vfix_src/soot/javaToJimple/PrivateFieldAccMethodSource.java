package soot.javaToJimple;

import java.util.Iterator;
import soot.Body;
import soot.Local;
import soot.MethodSource;
import soot.Scene;
import soot.SootClass;
import soot.SootFieldRef;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.Jimple;
import soot.jimple.ParameterRef;
import soot.jimple.Stmt;

public class PrivateFieldAccMethodSource implements MethodSource {
   private final Type fieldType;
   private final String fieldName;
   private final boolean isStatic;
   private final SootClass classToInvoke;

   public PrivateFieldAccMethodSource(Type fieldType, String fieldName, boolean isStatic, SootClass classToInvoke) {
      this.fieldType = fieldType;
      this.fieldName = fieldName;
      this.isStatic = isStatic;
      this.classToInvoke = classToInvoke;
   }

   public Body getBody(SootMethod sootMethod, String phaseName) {
      Body body = Jimple.v().newBody(sootMethod);
      LocalGenerator lg = new LocalGenerator(body);
      Local fieldBase = null;

      Local paramLocal;
      ParameterRef fieldRef;
      for(Iterator paramIt = sootMethod.getParameterTypes().iterator(); paramIt.hasNext(); fieldBase = paramLocal) {
         Type sootType = (Type)paramIt.next();
         paramLocal = lg.generateLocal(sootType);
         fieldRef = Jimple.v().newParameterRef(sootType, 0);
         Stmt stmt = Jimple.v().newIdentityStmt(paramLocal, fieldRef);
         body.getUnits().add((Unit)stmt);
      }

      Local fieldLocal = lg.generateLocal(this.fieldType);
      SootFieldRef field = Scene.v().makeFieldRef(this.classToInvoke, this.fieldName, this.fieldType, this.isStatic);
      fieldRef = null;
      Object fieldRef;
      if (this.isStatic) {
         fieldRef = Jimple.v().newStaticFieldRef(field);
      } else {
         fieldRef = Jimple.v().newInstanceFieldRef(fieldBase, field);
      }

      AssignStmt assign = Jimple.v().newAssignStmt(fieldLocal, (Value)fieldRef);
      body.getUnits().add((Unit)assign);
      Stmt retStmt = Jimple.v().newReturnStmt(fieldLocal);
      body.getUnits().add((Unit)retStmt);
      return body;
   }
}
