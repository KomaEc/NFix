package soot.jimple.spark.internal;

import soot.AnySubType;
import soot.ArrayType;
import soot.RefType;
import soot.SootMethod;
import soot.Type;
import soot.TypeSwitch;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.ArrayElement;
import soot.jimple.spark.pag.FieldRefNode;
import soot.jimple.spark.pag.LocalVarNode;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.pag.VarNode;

public class SparkLibraryHelper extends TypeSwitch {
   private PAG pag;
   private Node node;
   private SootMethod method;

   public SparkLibraryHelper(PAG pag, Node node, SootMethod method) {
      this.pag = pag;
      this.node = node;
      this.method = method;
   }

   public void caseRefType(RefType t) {
      VarNode local = this.pag.makeLocalVarNode(new Object(), t, this.method);
      AllocNode alloc = this.pag.makeAllocNode(new Object(), AnySubType.v(t), this.method);
      this.pag.addAllocEdge(alloc, local);
      this.pag.addEdge(local, this.node);
   }

   public void caseArrayType(ArrayType type) {
      Node array = this.node;

      for(Object t = type; t instanceof ArrayType; t = ((ArrayType)t).getElementType()) {
         ArrayType at = (ArrayType)t;
         if (at.baseType instanceof RefType) {
            LocalVarNode localArray = this.pag.makeLocalVarNode(new Object(), (Type)t, this.method);
            this.pag.addEdge(localArray, (Node)array);
            AllocNode newArray = this.pag.makeAllocNode(new Object(), at, this.method);
            this.pag.addEdge(newArray, localArray);
            FieldRefNode arrayRef = this.pag.makeFieldRefNode(localArray, ArrayElement.v());
            LocalVarNode local = this.pag.makeLocalVarNode(new Object(), at.getElementType(), this.method);
            this.pag.addEdge(local, arrayRef);
            array = local;
            if (at.numDimensions == 1) {
               AllocNode alloc = this.pag.makeAllocNode(new Object(), AnySubType.v((RefType)at.baseType), this.method);
               this.pag.addEdge(alloc, local);
            }
         }
      }

   }
}
