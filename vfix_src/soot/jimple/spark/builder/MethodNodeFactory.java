package soot.jimple.spark.builder;

import java.util.Iterator;
import soot.ArrayType;
import soot.Local;
import soot.RefLikeType;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootFieldRef;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Type;
import soot.Value;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.CastExpr;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.ClassConstant;
import soot.jimple.Expr;
import soot.jimple.IdentityRef;
import soot.jimple.IdentityStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InvokeExpr;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.jimple.NullConstant;
import soot.jimple.ParameterRef;
import soot.jimple.ReturnStmt;
import soot.jimple.StaticFieldRef;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.ThisRef;
import soot.jimple.ThrowStmt;
import soot.jimple.VirtualInvokeExpr;
import soot.jimple.spark.internal.ClientAccessibilityOracle;
import soot.jimple.spark.internal.SparkLibraryHelper;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.ArrayElement;
import soot.jimple.spark.pag.MethodPAG;
import soot.jimple.spark.pag.NewInstanceNode;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.pag.Parm;
import soot.jimple.spark.pag.VarNode;
import soot.shimple.AbstractShimpleValueSwitch;
import soot.shimple.PhiExpr;
import soot.toolkits.scalar.Pair;

public class MethodNodeFactory extends AbstractShimpleValueSwitch {
   protected final PAG pag;
   protected final MethodPAG mpag;
   protected SootMethod method;
   protected ClientAccessibilityOracle accessibilityOracle = Scene.v().getClientAccessibilityOracle();

   public MethodNodeFactory(PAG pag, MethodPAG mpag) {
      this.pag = pag;
      this.mpag = mpag;
      this.setCurrentMethod(mpag.getMethod());
   }

   private void setCurrentMethod(SootMethod m) {
      this.method = m;
      if (!m.isStatic()) {
         SootClass c = m.getDeclaringClass();
         if (c == null) {
            throw new RuntimeException("Method " + m + " has no declaring class");
         }

         this.caseThis();
      }

      for(int i = 0; i < m.getParameterCount(); ++i) {
         if (m.getParameterType(i) instanceof RefLikeType) {
            this.caseParm(i);
         }
      }

      Type retType = m.getReturnType();
      if (retType instanceof RefLikeType) {
         this.caseRet();
      }

   }

   public Node getNode(Value v) {
      v.apply(this);
      return this.getNode();
   }

   public final void handleStmt(Stmt s) {
      if (s.containsInvokeExpr()) {
         if (!this.pag.getCGOpts().types_for_invoke()) {
            return;
         }

         InvokeExpr iexpr = s.getInvokeExpr();
         if (iexpr instanceof VirtualInvokeExpr) {
            if (!this.isReflectionNewInstance(iexpr)) {
               return;
            }
         } else if (!(iexpr instanceof StaticInvokeExpr)) {
            return;
         }
      }

      s.apply(new AbstractStmtSwitch() {
         public final void caseAssignStmt(AssignStmt as) {
            Value l = as.getLeftOp();
            Value r = as.getRightOp();
            if (l.getType() instanceof RefLikeType) {
               assert r.getType() instanceof RefLikeType : "Type mismatch in assignment " + as + " in method " + MethodNodeFactory.this.method.getSignature();

               l.apply(MethodNodeFactory.this);
               Node dest = MethodNodeFactory.this.getNode();
               r.apply(MethodNodeFactory.this);
               Node src = MethodNodeFactory.this.getNode();
               if (l instanceof InstanceFieldRef) {
                  ((InstanceFieldRef)l).getBase().apply(MethodNodeFactory.this);
                  MethodNodeFactory.this.pag.addDereference((VarNode)MethodNodeFactory.this.getNode());
               }

               if (r instanceof InstanceFieldRef) {
                  ((InstanceFieldRef)r).getBase().apply(MethodNodeFactory.this);
                  MethodNodeFactory.this.pag.addDereference((VarNode)MethodNodeFactory.this.getNode());
               } else if (r instanceof StaticFieldRef) {
                  StaticFieldRef sfr = (StaticFieldRef)r;
                  SootFieldRef s = sfr.getFieldRef();
                  if (MethodNodeFactory.this.pag.getOpts().empties_as_allocs()) {
                     if (s.declaringClass().getName().equals("java.util.Collections")) {
                        if (s.name().equals("EMPTY_SET")) {
                           src = MethodNodeFactory.this.pag.makeAllocNode(RefType.v("java.util.HashSet"), RefType.v("java.util.HashSet"), MethodNodeFactory.this.method);
                        } else if (s.name().equals("EMPTY_MAP")) {
                           src = MethodNodeFactory.this.pag.makeAllocNode(RefType.v("java.util.HashMap"), RefType.v("java.util.HashMap"), MethodNodeFactory.this.method);
                        } else if (s.name().equals("EMPTY_LIST")) {
                           src = MethodNodeFactory.this.pag.makeAllocNode(RefType.v("java.util.LinkedList"), RefType.v("java.util.LinkedList"), MethodNodeFactory.this.method);
                        }
                     } else if (s.declaringClass().getName().equals("java.util.Hashtable")) {
                        if (s.name().equals("emptyIterator")) {
                           src = MethodNodeFactory.this.pag.makeAllocNode(RefType.v("java.util.Hashtable$EmptyIterator"), RefType.v("java.util.Hashtable$EmptyIterator"), MethodNodeFactory.this.method);
                        } else if (s.name().equals("emptyEnumerator")) {
                           src = MethodNodeFactory.this.pag.makeAllocNode(RefType.v("java.util.Hashtable$EmptyEnumerator"), RefType.v("java.util.Hashtable$EmptyEnumerator"), MethodNodeFactory.this.method);
                        }
                     }
                  }
               }

               MethodNodeFactory.this.mpag.addInternalEdge((Node)src, dest);
            }
         }

         public final void caseReturnStmt(ReturnStmt rs) {
            if (rs.getOp().getType() instanceof RefLikeType) {
               rs.getOp().apply(MethodNodeFactory.this);
               Node retNode = MethodNodeFactory.this.getNode();
               MethodNodeFactory.this.mpag.addInternalEdge(retNode, MethodNodeFactory.this.caseRet());
            }
         }

         public final void caseIdentityStmt(IdentityStmt is) {
            if (is.getLeftOp().getType() instanceof RefLikeType) {
               Value leftOp = is.getLeftOp();
               Value rightOp = is.getRightOp();
               leftOp.apply(MethodNodeFactory.this);
               Node dest = MethodNodeFactory.this.getNode();
               rightOp.apply(MethodNodeFactory.this);
               Node src = MethodNodeFactory.this.getNode();
               MethodNodeFactory.this.mpag.addInternalEdge(src, dest);
               int libOption = MethodNodeFactory.this.pag.getCGOpts().library();
               if (libOption != 1 && MethodNodeFactory.this.accessibilityOracle.isAccessible(MethodNodeFactory.this.method) && rightOp instanceof IdentityRef) {
                  Type rt = rightOp.getType();
                  rt.apply(new SparkLibraryHelper(MethodNodeFactory.this.pag, src, MethodNodeFactory.this.method));
               }

            }
         }

         public final void caseThrowStmt(ThrowStmt ts) {
            ts.getOp().apply(MethodNodeFactory.this);
            MethodNodeFactory.this.mpag.addOutEdge(MethodNodeFactory.this.getNode(), MethodNodeFactory.this.pag.nodeFactory().caseThrow());
         }
      });
   }

   private boolean isReflectionNewInstance(InvokeExpr iexpr) {
      if (iexpr instanceof VirtualInvokeExpr) {
         VirtualInvokeExpr vie = (VirtualInvokeExpr)iexpr;
         if (vie.getBase().getType() instanceof RefType) {
            RefType rt = (RefType)vie.getBase().getType();
            if (rt.getSootClass().getName().equals("java.lang.Class") && vie.getMethodRef().name().equals("newInstance") && vie.getMethodRef().parameterTypes().size() == 0) {
               return true;
            }
         }
      }

      return false;
   }

   public final Node getNode() {
      return (Node)this.getResult();
   }

   public final Node caseThis() {
      VarNode ret = this.pag.makeLocalVarNode(new Pair(this.method, "THIS_NODE"), this.method.getDeclaringClass().getType(), this.method);
      ret.setInterProcTarget();
      return ret;
   }

   public final Node caseParm(int index) {
      VarNode ret = this.pag.makeLocalVarNode(new Pair(this.method, new Integer(index)), this.method.getParameterType(index), this.method);
      ret.setInterProcTarget();
      return ret;
   }

   public final void casePhiExpr(PhiExpr e) {
      Pair<Expr, String> phiPair = new Pair(e, "PHI_NODE");
      Node phiNode = this.pag.makeLocalVarNode(phiPair, e.getType(), this.method);
      Iterator var4 = e.getValues().iterator();

      while(var4.hasNext()) {
         Value op = (Value)var4.next();
         op.apply(this);
         Node opNode = this.getNode();
         this.mpag.addInternalEdge(opNode, phiNode);
      }

      this.setResult(phiNode);
   }

   public final Node caseRet() {
      VarNode ret = this.pag.makeLocalVarNode(Parm.v(this.method, -2), this.method.getReturnType(), this.method);
      ret.setInterProcSource();
      return ret;
   }

   public final Node caseArray(VarNode base) {
      return this.pag.makeFieldRefNode(base, ArrayElement.v());
   }

   public final void caseArrayRef(ArrayRef ar) {
      this.caseLocal((Local)ar.getBase());
      this.setResult(this.caseArray((VarNode)this.getNode()));
   }

   public final void caseCastExpr(CastExpr ce) {
      Pair<Expr, String> castPair = new Pair(ce, "CAST_NODE");
      ce.getOp().apply(this);
      Node opNode = this.getNode();
      Node castNode = this.pag.makeLocalVarNode(castPair, ce.getCastType(), this.method);
      this.mpag.addInternalEdge(opNode, castNode);
      this.setResult(castNode);
   }

   public final void caseCaughtExceptionRef(CaughtExceptionRef cer) {
      this.setResult(this.pag.nodeFactory().caseThrow());
   }

   public final void caseInstanceFieldRef(InstanceFieldRef ifr) {
      if (!this.pag.getOpts().field_based() && !this.pag.getOpts().vta()) {
         this.setResult(this.pag.makeLocalFieldRefNode(ifr.getBase(), ifr.getBase().getType(), ifr.getField(), this.method));
      } else {
         this.setResult(this.pag.makeGlobalVarNode(ifr.getField(), ifr.getField().getType()));
      }

   }

   public final void caseLocal(Local l) {
      this.setResult(this.pag.makeLocalVarNode(l, l.getType(), this.method));
   }

   public final void caseNewArrayExpr(NewArrayExpr nae) {
      this.setResult(this.pag.makeAllocNode(nae, nae.getType(), this.method));
   }

   private boolean isStringBuffer(Type t) {
      if (!(t instanceof RefType)) {
         return false;
      } else {
         RefType rt = (RefType)t;
         String s = rt.toString();
         if (s.equals("java.lang.StringBuffer")) {
            return true;
         } else {
            return s.equals("java.lang.StringBuilder");
         }
      }
   }

   public final void caseNewExpr(NewExpr ne) {
      if (this.pag.getOpts().merge_stringbuffer() && this.isStringBuffer(ne.getType())) {
         this.setResult(this.pag.makeAllocNode(ne.getType(), ne.getType(), (SootMethod)null));
      } else {
         this.setResult(this.pag.makeAllocNode(ne, ne.getType(), this.method));
      }

   }

   public final void caseNewMultiArrayExpr(NewMultiArrayExpr nmae) {
      ArrayType type = (ArrayType)nmae.getType();
      AllocNode prevAn = this.pag.makeAllocNode(new Pair(nmae, new Integer(type.numDimensions)), type, this.method);
      VarNode prevVn = this.pag.makeLocalVarNode(prevAn, prevAn.getType(), this.method);
      this.mpag.addInternalEdge(prevAn, prevVn);
      this.setResult(prevAn);

      while(true) {
         Type t = type.getElementType();
         if (!(t instanceof ArrayType)) {
            return;
         }

         type = (ArrayType)t;
         AllocNode an = this.pag.makeAllocNode(new Pair(nmae, new Integer(type.numDimensions)), type, this.method);
         VarNode vn = this.pag.makeLocalVarNode(an, an.getType(), this.method);
         this.mpag.addInternalEdge(an, vn);
         this.mpag.addInternalEdge(vn, this.pag.makeFieldRefNode(prevVn, ArrayElement.v()));
         prevVn = vn;
      }
   }

   public final void caseParameterRef(ParameterRef pr) {
      this.setResult(this.caseParm(pr.getIndex()));
   }

   public final void caseStaticFieldRef(StaticFieldRef sfr) {
      this.setResult(this.pag.makeGlobalVarNode(sfr.getField(), sfr.getField().getType()));
   }

   public final void caseStringConstant(StringConstant sc) {
      AllocNode stringConstant;
      if (!this.pag.getOpts().string_constants() && !Scene.v().containsClass(sc.value) && (sc.value.length() <= 0 || sc.value.charAt(0) != '[')) {
         stringConstant = this.pag.makeAllocNode("STRING_NODE", RefType.v("java.lang.String"), (SootMethod)null);
      } else {
         stringConstant = this.pag.makeStringConstantNode(sc.value);
      }

      VarNode stringConstantLocal = this.pag.makeGlobalVarNode(stringConstant, RefType.v("java.lang.String"));
      this.pag.addEdge(stringConstant, stringConstantLocal);
      this.setResult(stringConstantLocal);
   }

   public final void caseThisRef(ThisRef tr) {
      this.setResult(this.caseThis());
   }

   public final void caseNullConstant(NullConstant nr) {
      this.setResult((Object)null);
   }

   public final void caseClassConstant(ClassConstant cc) {
      AllocNode classConstant = this.pag.makeClassConstantNode(cc);
      VarNode classConstantLocal = this.pag.makeGlobalVarNode(classConstant, RefType.v("java.lang.Class"));
      this.pag.addEdge(classConstant, classConstantLocal);
      this.setResult(classConstantLocal);
   }

   public final void defaultCase(Object v) {
      throw new RuntimeException("failed to handle " + v);
   }

   public void caseStaticInvokeExpr(StaticInvokeExpr v) {
      SootMethodRef ref = v.getMethodRef();
      if (v.getArgCount() == 1 && v.getArg(0) instanceof StringConstant && ref.name().equals("forName") && ref.declaringClass().getName().equals("java.lang.Class") && ref.parameterTypes().size() == 1) {
         StringConstant classNameConst = (StringConstant)v.getArg(0);
         this.caseClassConstant(ClassConstant.v("L" + classNameConst.value.replaceAll("\\.", "/") + ";"));
      }

   }

   public void caseVirtualInvokeExpr(VirtualInvokeExpr v) {
      if (this.isReflectionNewInstance(v)) {
         NewInstanceNode newInstanceNode = this.pag.makeNewInstanceNode(v, Scene.v().getObjectType(), this.method);
         v.getBase().apply(this);
         Node srcNode = this.getNode();
         this.mpag.addInternalEdge(srcNode, newInstanceNode);
         this.setResult(newInstanceNode);
      } else {
         throw new RuntimeException("Unhandled case of VirtualInvokeExpr");
      }
   }
}
