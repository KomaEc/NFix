package soot.jimple.spark.ondemand.pautil;

import java.util.HashSet;
import java.util.Set;
import soot.AnySubType;
import soot.ArrayType;
import soot.RefType;
import soot.Scene;
import soot.SootMethod;
import soot.Type;
import soot.jimple.spark.internal.TypeManager;
import soot.jimple.spark.ondemand.genericutil.ImmutableStack;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.pag.VarNode;
import soot.jimple.spark.sets.P2SetVisitor;
import soot.jimple.spark.sets.PointsToSetInternal;
import soot.jimple.toolkits.callgraph.VirtualCalls;
import soot.util.NumberedString;

public interface AllocationSiteHandler {
   boolean handleAllocationSite(AllocNode var1, ImmutableStack<Integer> var2);

   void resetState();

   boolean shouldHandle(VarNode var1);

   public static class VirtualCallHandler implements AllocationSiteHandler {
      public PAG pag;
      public Type receiverType;
      public NumberedString methodStr;
      public Set<SootMethod> possibleMethods = new HashSet();

      public VirtualCallHandler(PAG pag, Type receiverType, NumberedString methodStr) {
         this.pag = pag;
         this.receiverType = receiverType;
         this.methodStr = methodStr;
      }

      public boolean handleAllocationSite(AllocNode allocNode, ImmutableStack<Integer> callStack) {
         Type type = allocNode.getType();
         if (!this.pag.getTypeManager().castNeverFails((Type)type, this.receiverType)) {
            return false;
         } else if (type instanceof AnySubType) {
            AnySubType any = (AnySubType)type;
            RefType refType = any.getBase();
            return this.pag.getTypeManager().getFastHierarchy().canStoreType(this.receiverType, refType) || this.pag.getTypeManager().getFastHierarchy().canStoreType(refType, this.receiverType);
         } else {
            if (type instanceof ArrayType) {
               type = Scene.v().getSootClass("java.lang.Object").getType();
            }

            RefType refType = (RefType)type;
            SootMethod targetMethod = null;
            targetMethod = VirtualCalls.v().resolveNonSpecial(refType, this.methodStr);
            if (!this.possibleMethods.contains(targetMethod)) {
               this.possibleMethods.add(targetMethod);
               if (this.possibleMethods.size() > 1) {
                  return true;
               }
            }

            return false;
         }
      }

      public void resetState() {
         this.possibleMethods.clear();
      }

      public boolean shouldHandle(VarNode dst) {
         return false;
      }
   }

   public static class CastCheckHandler implements AllocationSiteHandler {
      private Type type;
      private TypeManager manager;
      private boolean castFailed = false;

      public boolean handleAllocationSite(AllocNode allocNode, ImmutableStack<Integer> callStack) {
         this.castFailed = !this.manager.castNeverFails(allocNode.getType(), this.type);
         return this.castFailed;
      }

      public void setManager(TypeManager manager) {
         this.manager = manager;
      }

      public void setType(Type type) {
         this.type = type;
      }

      public void resetState() {
         throw new RuntimeException();
      }

      public boolean shouldHandle(VarNode dst) {
         P2SetVisitor v = new P2SetVisitor() {
            public void visit(Node n) {
               if (!this.returnValue) {
                  this.returnValue = !CastCheckHandler.this.manager.castNeverFails(n.getType(), CastCheckHandler.this.type);
               }

            }
         };
         dst.getP2Set().forall(v);
         return v.getReturnValue();
      }
   }

   public static class PointsToSetHandler implements AllocationSiteHandler {
      private PointsToSetInternal p2set;

      public boolean handleAllocationSite(AllocNode allocNode, ImmutableStack<Integer> callStack) {
         this.p2set.add(allocNode);
         return false;
      }

      public PointsToSetInternal getP2set() {
         return this.p2set;
      }

      public void setP2set(PointsToSetInternal p2set) {
         this.p2set = p2set;
      }

      public void resetState() {
         throw new RuntimeException();
      }

      public boolean shouldHandle(VarNode dst) {
         return false;
      }
   }
}
