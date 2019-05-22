package soot.jimple.spark.pag;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import soot.ArrayType;
import soot.Body;
import soot.Context;
import soot.EntryPoints;
import soot.G;
import soot.RefLikeType;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.VoidType;
import soot.jimple.Stmt;
import soot.jimple.spark.builder.MethodNodeFactory;
import soot.jimple.spark.internal.SparkLibraryHelper;
import soot.util.NumberedString;
import soot.util.queue.ChunkedQueue;
import soot.util.queue.QueueReader;

public final class MethodPAG {
   private PAG pag;
   private Set<Context> addedContexts;
   private final ChunkedQueue<Node> internalEdges = new ChunkedQueue();
   private final ChunkedQueue<Node> inEdges = new ChunkedQueue();
   private final ChunkedQueue<Node> outEdges = new ChunkedQueue();
   private final QueueReader<Node> internalReader;
   private final QueueReader<Node> inReader;
   private final QueueReader<Node> outReader;
   SootMethod method;
   protected MethodNodeFactory nodeFactory;
   protected boolean hasBeenAdded;
   protected boolean hasBeenBuilt;
   private static final String mainSubSignature = SootMethod.getSubSignature("main", Collections.singletonList(ArrayType.v(RefType.v("java.lang.String"), 1)), VoidType.v());
   protected final NumberedString sigCanonicalize;

   public PAG pag() {
      return this.pag;
   }

   protected MethodPAG(PAG pag, SootMethod m) {
      this.internalReader = this.internalEdges.reader();
      this.inReader = this.inEdges.reader();
      this.outReader = this.outEdges.reader();
      this.hasBeenAdded = false;
      this.hasBeenBuilt = false;
      this.sigCanonicalize = Scene.v().getSubSigNumberer().findOrAdd("java.lang.String canonicalize(java.lang.String)");
      this.pag = pag;
      this.method = m;
      this.nodeFactory = new MethodNodeFactory(pag, this);
   }

   public void addToPAG(Context varNodeParameter) {
      if (!this.hasBeenBuilt) {
         throw new RuntimeException(String.format("No PAG built for context %s and method %s", varNodeParameter, this.method));
      } else {
         if (varNodeParameter == null) {
            if (this.hasBeenAdded) {
               return;
            }

            this.hasBeenAdded = true;
         } else {
            if (this.addedContexts == null) {
               this.addedContexts = new HashSet();
            }

            if (!this.addedContexts.add(varNodeParameter)) {
               return;
            }
         }

         QueueReader reader = this.internalReader.clone();

         Node src;
         Node dst;
         while(reader.hasNext()) {
            src = (Node)reader.next();
            src = this.parameterize(src, varNodeParameter);
            dst = (Node)reader.next();
            dst = this.parameterize(dst, varNodeParameter);
            this.pag.addEdge(src, dst);
         }

         reader = this.inReader.clone();

         while(reader.hasNext()) {
            src = (Node)reader.next();
            dst = (Node)reader.next();
            dst = this.parameterize(dst, varNodeParameter);
            this.pag.addEdge(src, dst);
         }

         reader = this.outReader.clone();

         while(reader.hasNext()) {
            src = (Node)reader.next();
            src = this.parameterize(src, varNodeParameter);
            dst = (Node)reader.next();
            this.pag.addEdge(src, dst);
         }

      }
   }

   public void addInternalEdge(Node src, Node dst) {
      if (src != null) {
         this.internalEdges.add(src);
         this.internalEdges.add(dst);
         if (this.hasBeenAdded) {
            this.pag.addEdge(src, dst);
         }

      }
   }

   public void addInEdge(Node src, Node dst) {
      if (src != null) {
         this.inEdges.add(src);
         this.inEdges.add(dst);
         if (this.hasBeenAdded) {
            this.pag.addEdge(src, dst);
         }

      }
   }

   public void addOutEdge(Node src, Node dst) {
      if (src != null) {
         this.outEdges.add(src);
         this.outEdges.add(dst);
         if (this.hasBeenAdded) {
            this.pag.addEdge(src, dst);
         }

      }
   }

   public SootMethod getMethod() {
      return this.method;
   }

   public MethodNodeFactory nodeFactory() {
      return this.nodeFactory;
   }

   public static MethodPAG v(PAG pag, SootMethod m) {
      MethodPAG ret = (MethodPAG)G.v().MethodPAG_methodToPag.get(m);
      if (ret == null) {
         ret = new MethodPAG(pag, m);
         G.v().MethodPAG_methodToPag.put(m, ret);
      }

      return ret;
   }

   public void build() {
      if (!this.hasBeenBuilt) {
         this.hasBeenBuilt = true;
         if (this.method.isNative()) {
            if (this.pag().getOpts().simulate_natives()) {
               this.buildNative();
            }
         } else if (this.method.isConcrete() && !this.method.isPhantom()) {
            this.buildNormal();
         }

         this.addMiscEdges();
      }
   }

   protected VarNode parameterize(LocalVarNode vn, Context varNodeParameter) {
      SootMethod m = vn.getMethod();
      if (m != this.method && m != null) {
         throw new RuntimeException("VarNode " + vn + " with method " + m + " parameterized in method " + this.method);
      } else {
         return this.pag().makeContextVarNode(vn, varNodeParameter);
      }
   }

   protected FieldRefNode parameterize(FieldRefNode frn, Context varNodeParameter) {
      return this.pag().makeFieldRefNode((VarNode)this.parameterize((Node)frn.getBase(), varNodeParameter), frn.getField());
   }

   public Node parameterize(Node n, Context varNodeParameter) {
      if (varNodeParameter == null) {
         return n;
      } else if (n instanceof LocalVarNode) {
         return this.parameterize((LocalVarNode)n, varNodeParameter);
      } else {
         return (Node)(n instanceof FieldRefNode ? this.parameterize((FieldRefNode)n, varNodeParameter) : n);
      }
   }

   protected void buildNormal() {
      Body b = this.method.retrieveActiveBody();
      Iterator var2 = b.getUnits().iterator();

      while(var2.hasNext()) {
         Unit u = (Unit)var2.next();
         this.nodeFactory.handleStmt((Stmt)u);
      }

   }

   protected void buildNative() {
      ValNode thisNode = null;
      ValNode retNode = null;
      if (!this.method.isStatic()) {
         thisNode = (ValNode)this.nodeFactory.caseThis();
      }

      if (this.method.getReturnType() instanceof RefLikeType) {
         retNode = (ValNode)this.nodeFactory.caseRet();
         if (this.pag.getCGOpts().library() != 1) {
            Type retType = this.method.getReturnType();
            retType.apply(new SparkLibraryHelper(this.pag, retNode, this.method));
         }
      }

      ValNode[] args = new ValNode[this.method.getParameterCount()];

      for(int i = 0; i < this.method.getParameterCount(); ++i) {
         if (this.method.getParameterType(i) instanceof RefLikeType) {
            args[i] = (ValNode)this.nodeFactory.caseParm(i);
         }
      }

      this.pag.nativeMethodDriver.process(this.method, thisNode, retNode, args);
   }

   protected void addMiscEdges() {
      String signature = this.method.getSignature();
      if (this.method.getSubSignature().equals(mainSubSignature)) {
         this.addInEdge(this.pag().nodeFactory().caseArgv(), this.nodeFactory.caseParm(0));
      } else if (signature.equals("<java.lang.Thread: void <init>(java.lang.ThreadGroup,java.lang.String)>")) {
         this.addInEdge(this.pag().nodeFactory().caseMainThread(), this.nodeFactory.caseThis());
         this.addInEdge(this.pag().nodeFactory().caseMainThreadGroup(), this.nodeFactory.caseParm(0));
      } else if (signature.equals("<java.lang.ref.Finalizer: void <init>(java.lang.Object)>")) {
         this.addInEdge(this.nodeFactory.caseThis(), this.pag().nodeFactory().caseFinalizeQueue());
      } else if (signature.equals("<java.lang.ref.Finalizer: void runFinalizer()>")) {
         this.addInEdge(this.pag.nodeFactory().caseFinalizeQueue(), this.nodeFactory.caseThis());
      } else if (signature.equals("<java.lang.ref.Finalizer: void access$100(java.lang.Object)>")) {
         this.addInEdge(this.pag.nodeFactory().caseFinalizeQueue(), this.nodeFactory.caseParm(0));
      } else if (signature.equals("<java.lang.ClassLoader: void <init>()>")) {
         this.addInEdge(this.pag.nodeFactory().caseDefaultClassLoader(), this.nodeFactory.caseThis());
      } else if (signature.equals("<java.lang.Thread: void exit()>")) {
         this.addInEdge(this.pag.nodeFactory().caseMainThread(), this.nodeFactory.caseThis());
      } else if (signature.equals("<java.security.PrivilegedActionException: void <init>(java.lang.Exception)>")) {
         this.addInEdge(this.pag.nodeFactory().caseThrow(), this.nodeFactory.caseParm(0));
         this.addInEdge(this.pag.nodeFactory().casePrivilegedActionException(), this.nodeFactory.caseThis());
      }

      if (this.method.getNumberedSubSignature().equals(this.sigCanonicalize)) {
         for(SootClass cl = this.method.getDeclaringClass(); cl != null; cl = cl.getSuperclassUnsafe()) {
            if (cl.equals(Scene.v().getSootClass("java.io.FileSystem"))) {
               this.addInEdge(this.pag.nodeFactory().caseCanonicalPath(), this.nodeFactory.caseRet());
            }
         }
      }

      boolean isImplicit = false;
      Iterator var3 = EntryPoints.v().implicit().iterator();

      while(var3.hasNext()) {
         SootMethod implicitMethod = (SootMethod)var3.next();
         if (implicitMethod.getNumberedSubSignature().equals(this.method.getNumberedSubSignature())) {
            isImplicit = true;
            break;
         }
      }

      if (isImplicit) {
         SootClass c = this.method.getDeclaringClass();

         while(true) {
            if (c.getName().equals("java.lang.ClassLoader")) {
               if (!this.method.getName().equals("<init>")) {
                  this.addInEdge(this.pag().nodeFactory().caseDefaultClassLoader(), this.nodeFactory.caseThis());
                  this.addInEdge(this.pag().nodeFactory().caseMainClassNameString(), this.nodeFactory.caseParm(0));
               }
               break;
            }

            if (!c.hasSuperclass()) {
               break;
            }

            c = c.getSuperclass();
         }
      }

   }
}
