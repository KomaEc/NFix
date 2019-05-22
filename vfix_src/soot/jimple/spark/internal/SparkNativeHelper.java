package soot.jimple.spark.internal;

import soot.G;
import soot.RefType;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.ArrayElement;
import soot.jimple.spark.pag.FieldRefNode;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.pag.VarNode;
import soot.jimple.toolkits.pointer.representations.AbstractObject;
import soot.jimple.toolkits.pointer.representations.ReferenceVariable;
import soot.jimple.toolkits.pointer.util.NativeHelper;
import soot.toolkits.scalar.Pair;

public class SparkNativeHelper extends NativeHelper {
   protected PAG pag;

   public SparkNativeHelper(PAG pag) {
      this.pag = pag;
   }

   protected void assignImpl(ReferenceVariable lhs, ReferenceVariable rhs) {
      this.pag.addEdge((Node)rhs, (Node)lhs);
   }

   protected void assignObjectToImpl(ReferenceVariable lhs, AbstractObject obj) {
      AllocNode objNode = this.pag.makeAllocNode(new Pair("AbstractObject", obj.getType()), obj.getType(), (SootMethod)null);
      Object var;
      if (lhs instanceof FieldRefNode) {
         var = this.pag.makeGlobalVarNode(objNode, objNode.getType());
         this.pag.addEdge((Node)lhs, (Node)var);
      } else {
         var = (VarNode)lhs;
      }

      this.pag.addEdge(objNode, (Node)var);
   }

   protected void throwExceptionImpl(AbstractObject obj) {
      AllocNode objNode = this.pag.makeAllocNode(new Pair("AbstractObject", obj.getType()), obj.getType(), (SootMethod)null);
      this.pag.addEdge(objNode, this.pag.nodeFactory().caseThrow());
   }

   protected ReferenceVariable arrayElementOfImpl(ReferenceVariable base) {
      Object l;
      if (base instanceof VarNode) {
         l = (VarNode)base;
      } else {
         FieldRefNode b = (FieldRefNode)base;
         l = this.pag.makeGlobalVarNode(b, b.getType());
         this.pag.addEdge(b, (Node)l);
      }

      return this.pag.makeFieldRefNode((VarNode)l, ArrayElement.v());
   }

   protected ReferenceVariable cloneObjectImpl(ReferenceVariable source) {
      return source;
   }

   protected ReferenceVariable newInstanceOfImpl(ReferenceVariable cls) {
      return this.pag.nodeFactory().caseNewInstance((VarNode)cls);
   }

   protected ReferenceVariable staticFieldImpl(String className, String fieldName) {
      SootClass c = RefType.v(className).getSootClass();
      SootField f = c.getFieldByName(fieldName);
      return this.pag.makeGlobalVarNode(f, f.getType());
   }

   protected ReferenceVariable tempFieldImpl(String fieldsig) {
      return this.pag.makeGlobalVarNode(new Pair("tempField", fieldsig), RefType.v("java.lang.Object"));
   }

   protected ReferenceVariable tempVariableImpl() {
      return this.pag.makeGlobalVarNode(new Pair("TempVar", new Integer(++G.v().SparkNativeHelper_tempVar)), RefType.v("java.lang.Object"));
   }

   protected ReferenceVariable tempLocalVariableImpl(SootMethod method) {
      return this.pag.makeLocalVarNode(new Pair("TempVar", new Integer(++G.v().SparkNativeHelper_tempVar)), RefType.v("java.lang.Object"), method);
   }
}
