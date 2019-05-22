package soot.jimple.spark.builder;

import java.util.Iterator;
import soot.AnySubType;
import soot.ArrayType;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.ArrayElement;
import soot.jimple.spark.pag.ContextVarNode;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.pag.VarNode;
import soot.toolkits.scalar.Pair;

public class GlobalNodeFactory {
   protected PAG pag;

   public GlobalNodeFactory(PAG pag) {
      this.pag = pag;
   }

   public final Node caseDefaultClassLoader() {
      AllocNode a = this.pag.makeAllocNode("DEFAULT_CLASS_LOADER", AnySubType.v(RefType.v("java.lang.ClassLoader")), (SootMethod)null);
      VarNode v = this.pag.makeGlobalVarNode("DEFAULT_CLASS_LOADER_LOCAL", RefType.v("java.lang.ClassLoader"));
      this.pag.addEdge(a, v);
      return v;
   }

   public final Node caseMainClassNameString() {
      AllocNode a = this.pag.makeAllocNode("MAIN_CLASS_NAME_STRING", RefType.v("java.lang.String"), (SootMethod)null);
      VarNode v = this.pag.makeGlobalVarNode("MAIN_CLASS_NAME_STRING_LOCAL", RefType.v("java.lang.String"));
      this.pag.addEdge(a, v);
      return v;
   }

   public final Node caseMainThreadGroup() {
      AllocNode threadGroupNode = this.pag.makeAllocNode("MAIN_THREAD_GROUP_NODE", RefType.v("java.lang.ThreadGroup"), (SootMethod)null);
      VarNode threadGroupNodeLocal = this.pag.makeGlobalVarNode("MAIN_THREAD_GROUP_NODE_LOCAL", RefType.v("java.lang.ThreadGroup"));
      this.pag.addEdge(threadGroupNode, threadGroupNodeLocal);
      return threadGroupNodeLocal;
   }

   public final Node casePrivilegedActionException() {
      AllocNode a = this.pag.makeAllocNode("PRIVILEGED_ACTION_EXCEPTION", AnySubType.v(RefType.v("java.security.PrivilegedActionException")), (SootMethod)null);
      VarNode v = this.pag.makeGlobalVarNode("PRIVILEGED_ACTION_EXCEPTION_LOCAL", RefType.v("java.security.PrivilegedActionException"));
      this.pag.addEdge(a, v);
      return v;
   }

   public final Node caseCanonicalPath() {
      AllocNode a = this.pag.makeAllocNode("CANONICAL_PATH", RefType.v("java.lang.String"), (SootMethod)null);
      VarNode v = this.pag.makeGlobalVarNode("CANONICAL_PATH_LOCAL", RefType.v("java.lang.String"));
      this.pag.addEdge(a, v);
      return v;
   }

   public final Node caseMainThread() {
      AllocNode threadNode = this.pag.makeAllocNode("MAIN_THREAD_NODE", RefType.v("java.lang.Thread"), (SootMethod)null);
      VarNode threadNodeLocal = this.pag.makeGlobalVarNode("MAIN_THREAD_NODE_LOCAL", RefType.v("java.lang.Thread"));
      this.pag.addEdge(threadNode, threadNodeLocal);
      return threadNodeLocal;
   }

   public final Node caseFinalizeQueue() {
      return this.pag.makeGlobalVarNode("FINALIZE_QUEUE", RefType.v("java.lang.Object"));
   }

   public final Node caseArgv() {
      AllocNode argv = this.pag.makeAllocNode("STRING_ARRAY_NODE", ArrayType.v(RefType.v("java.lang.String"), 1), (SootMethod)null);
      VarNode sanl = this.pag.makeGlobalVarNode("STRING_ARRAY_NODE_LOCAL", ArrayType.v(RefType.v("java.lang.String"), 1));
      AllocNode stringNode = this.pag.makeAllocNode("STRING_NODE", RefType.v("java.lang.String"), (SootMethod)null);
      VarNode stringNodeLocal = this.pag.makeGlobalVarNode("STRING_NODE_LOCAL", RefType.v("java.lang.String"));
      this.pag.addEdge(argv, sanl);
      this.pag.addEdge(stringNode, stringNodeLocal);
      this.pag.addEdge(stringNodeLocal, this.pag.makeFieldRefNode(sanl, ArrayElement.v()));
      return sanl;
   }

   public final Node caseNewInstance(VarNode cls) {
      if (cls instanceof ContextVarNode) {
         cls = this.pag.findLocalVarNode(((VarNode)cls).getVariable());
      }

      VarNode local = this.pag.makeGlobalVarNode(cls, RefType.v("java.lang.Object"));
      Iterator var3 = Scene.v().dynamicClasses().iterator();

      while(var3.hasNext()) {
         SootClass cl = (SootClass)var3.next();
         AllocNode site = this.pag.makeAllocNode(new Pair(cls, cl), cl.getType(), (SootMethod)null);
         this.pag.addEdge(site, local);
      }

      return local;
   }

   public Node caseThrow() {
      VarNode ret = this.pag.makeGlobalVarNode("EXCEPTION_NODE", RefType.v("java.lang.Throwable"));
      ret.setInterProcTarget();
      ret.setInterProcSource();
      return ret;
   }
}
