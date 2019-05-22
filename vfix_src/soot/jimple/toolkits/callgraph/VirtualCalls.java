package soot.jimple.toolkits.callgraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import soot.AnySubType;
import soot.ArrayType;
import soot.FastHierarchy;
import soot.G;
import soot.NullType;
import soot.PhaseOptions;
import soot.RefType;
import soot.Scene;
import soot.Singletons;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.jimple.SpecialInvokeExpr;
import soot.options.CGOptions;
import soot.toolkits.scalar.Pair;
import soot.util.Chain;
import soot.util.HashMultiMap;
import soot.util.LargeNumberedMap;
import soot.util.MultiMap;
import soot.util.NumberedString;
import soot.util.SmallNumberedMap;
import soot.util.queue.ChunkedQueue;

public class VirtualCalls {
   private CGOptions options = new CGOptions(PhaseOptions.v().getPhaseOptions("cg"));
   private final LargeNumberedMap<Type, SmallNumberedMap<SootMethod>> typeToVtbl = new LargeNumberedMap(Scene.v().getTypeNumberer());
   protected MultiMap<Type, Type> baseToSubTypes = new HashMultiMap();
   protected MultiMap<Pair<Type, NumberedString>, Pair<Type, NumberedString>> baseToPossibleSubTypes = new HashMultiMap();
   public final NumberedString sigClinit = Scene.v().getSubSigNumberer().findOrAdd("void <clinit>()");
   public final NumberedString sigStart = Scene.v().getSubSigNumberer().findOrAdd("void start()");
   public final NumberedString sigRun = Scene.v().getSubSigNumberer().findOrAdd("void run()");

   public VirtualCalls(Singletons.Global g) {
   }

   public static VirtualCalls v() {
      return G.v().soot_jimple_toolkits_callgraph_VirtualCalls();
   }

   public SootMethod resolveSpecial(SpecialInvokeExpr iie, NumberedString subSig, SootMethod container) {
      return this.resolveSpecial(iie, subSig, container, false);
   }

   public SootMethod resolveSpecial(SpecialInvokeExpr iie, NumberedString subSig, SootMethod container, boolean appOnly) {
      SootMethod target = iie.getMethod();
      return Scene.v().getOrMakeFastHierarchy().canStoreType(container.getDeclaringClass().getType(), target.getDeclaringClass().getType()) && container.getDeclaringClass().getType() != target.getDeclaringClass().getType() && !target.getName().equals("<init>") && subSig != this.sigClinit ? this.resolveNonSpecial(container.getDeclaringClass().getSuperclass().getType(), subSig, appOnly) : target;
   }

   public SootMethod resolveNonSpecial(RefType t, NumberedString subSig) {
      return this.resolveNonSpecial(t, subSig, false);
   }

   public SootMethod resolveNonSpecial(RefType t, NumberedString subSig, boolean appOnly) {
      SmallNumberedMap<SootMethod> vtbl = (SmallNumberedMap)this.typeToVtbl.get(t);
      if (vtbl == null) {
         this.typeToVtbl.put(t, vtbl = new SmallNumberedMap());
      }

      SootMethod ret = (SootMethod)vtbl.get(subSig);
      if (ret != null) {
         return ret;
      } else {
         SootClass cls = t.getSootClass();
         if (appOnly && cls.isLibraryClass()) {
            return null;
         } else {
            SootMethod m = cls.getMethodUnsafe(subSig);
            if (m != null) {
               if (m.isConcrete() || m.isNative() || m.isPhantom()) {
                  ret = m;
               }
            } else {
               SootClass c = cls.getSuperclassUnsafe();
               if (c != null) {
                  ret = this.resolveNonSpecial(c.getType(), subSig);
               }
            }

            vtbl.put(subSig, ret);
            return ret;
         }
      }
   }

   public void resolve(Type t, Type declaredType, NumberedString subSig, SootMethod container, ChunkedQueue<SootMethod> targets) {
      this.resolve(t, declaredType, (Type)null, subSig, container, targets);
   }

   public void resolve(Type t, Type declaredType, NumberedString subSig, SootMethod container, ChunkedQueue<SootMethod> targets, boolean appOnly) {
      this.resolve(t, declaredType, (Type)null, subSig, container, targets, appOnly);
   }

   public void resolve(Type t, Type declaredType, Type sigType, NumberedString subSig, SootMethod container, ChunkedQueue<SootMethod> targets) {
      this.resolve(t, declaredType, sigType, subSig, container, targets, false);
   }

   public void resolve(Type t, Type declaredType, Type sigType, NumberedString subSig, SootMethod container, ChunkedQueue<SootMethod> targets, boolean appOnly) {
      if (declaredType instanceof ArrayType) {
         declaredType = RefType.v("java.lang.Object");
      }

      if (sigType instanceof ArrayType) {
         sigType = RefType.v("java.lang.Object");
      }

      if (t instanceof ArrayType) {
         t = RefType.v("java.lang.Object");
      }

      FastHierarchy fastHierachy = Scene.v().getOrMakeFastHierarchy();
      if (declaredType == null || fastHierachy.canStoreType((Type)t, (Type)declaredType)) {
         if (sigType == null || fastHierachy.canStoreType((Type)t, (Type)sigType)) {
            if (t instanceof RefType) {
               SootMethod target = this.resolveNonSpecial((RefType)t, subSig, appOnly);
               if (target != null) {
                  targets.add(target);
               }
            } else if (t instanceof AnySubType) {
               RefType base = ((AnySubType)t).getBase();
               if (this.options.library() == 3 && base.getSootClass().isInterface()) {
                  this.resolveLibrarySignature((Type)declaredType, (Type)sigType, subSig, container, targets, appOnly, base);
               } else {
                  this.resolveAnySubType((Type)declaredType, (Type)sigType, subSig, container, targets, appOnly, base);
               }
            } else if (!(t instanceof NullType)) {
               throw new RuntimeException("oops " + t);
            }

         }
      }
   }

   protected void resolveAnySubType(Type declaredType, Type sigType, NumberedString subSig, SootMethod container, ChunkedQueue<SootMethod> targets, boolean appOnly, RefType base) {
      FastHierarchy fastHierachy = Scene.v().getOrMakeFastHierarchy();
      Set<Type> subTypes = this.baseToSubTypes.get(base);
      if (subTypes != null && !subTypes.isEmpty()) {
         Iterator var17 = subTypes.iterator();

         while(var17.hasNext()) {
            Type st = (Type)var17.next();
            this.resolve(st, declaredType, sigType, subSig, container, targets, appOnly);
         }

      } else {
         Set<Type> newSubTypes = new HashSet();
         newSubTypes.add(base);
         LinkedList<SootClass> worklist = new LinkedList();
         HashSet<SootClass> workset = new HashSet();
         FastHierarchy fh = fastHierachy;
         SootClass cl = base.getSootClass();
         if (workset.add(cl)) {
            worklist.add(cl);
         }

         while(true) {
            while(!worklist.isEmpty()) {
               cl = (SootClass)worklist.removeFirst();
               Iterator cIt;
               SootClass c;
               if (cl.isInterface()) {
                  cIt = fh.getAllImplementersOfInterface(cl).iterator();

                  while(cIt.hasNext()) {
                     c = (SootClass)cIt.next();
                     if (workset.add(c)) {
                        worklist.add(c);
                     }
                  }
               } else {
                  if (cl.isConcrete()) {
                     this.resolve(cl.getType(), declaredType, sigType, subSig, container, targets, appOnly);
                     newSubTypes.add(cl.getType());
                  }

                  cIt = fh.getSubclassesOf(cl).iterator();

                  while(cIt.hasNext()) {
                     c = (SootClass)cIt.next();
                     if (workset.add(c)) {
                        worklist.add(c);
                     }
                  }
               }
            }

            this.baseToSubTypes.putAll(base, newSubTypes);
            return;
         }
      }
   }

   protected void resolveLibrarySignature(Type declaredType, Type sigType, NumberedString subSig, SootMethod container, ChunkedQueue<SootMethod> targets, boolean appOnly, RefType base) {
      FastHierarchy fastHierachy = Scene.v().getOrMakeFastHierarchy();

      assert declaredType instanceof RefType;

      Pair<Type, NumberedString> pair = new Pair(base, subSig);
      Set<Pair<Type, NumberedString>> types = this.baseToPossibleSubTypes.get(pair);
      if (types != null) {
         Iterator var25 = types.iterator();

         while(var25.hasNext()) {
            Pair<Type, NumberedString> tuple = (Pair)var25.next();
            Type st = (Type)tuple.getO1();
            if (!fastHierachy.canStoreType(st, declaredType)) {
               this.resolve(st, st, sigType, subSig, container, targets, appOnly);
            } else {
               this.resolve(st, declaredType, sigType, subSig, container, targets, appOnly);
            }
         }

      } else {
         Set<Pair<Type, NumberedString>> types = new HashSet();
         String[] split = subSig.getString().replaceAll("(.*) (.*)\\((.*)\\)", "$1;$2;$3").split(";");
         Type declaredReturnType = Scene.v().getType(split[0]);
         String declaredName = split[1];
         List<Type> declaredParamTypes = new ArrayList();
         if (split.length == 3) {
            String[] var15 = split[2].split(",");
            int var16 = var15.length;

            for(int var17 = 0; var17 < var16; ++var17) {
               String type = var15[var17];
               declaredParamTypes.add(Scene.v().getType(type));
            }
         }

         Chain<SootClass> classes = Scene.v().getClasses();
         Iterator var29 = classes.iterator();

         label87:
         while(var29.hasNext()) {
            SootClass sc = (SootClass)var29.next();
            Iterator var31 = sc.getMethods().iterator();

            while(true) {
               SootMethod sm;
               List paramTypes;
               do {
                  do {
                     do {
                        do {
                           if (!var31.hasNext()) {
                              continue label87;
                           }

                           sm = (SootMethod)var31.next();
                        } while(!sm.isConcrete() && !sm.isNative());
                     } while(!sm.getName().equals(declaredName));
                  } while(!fastHierachy.canStoreType(sm.getReturnType(), declaredReturnType));

                  paramTypes = sm.getParameterTypes();
               } while(declaredParamTypes.size() != paramTypes.size());

               boolean check = true;

               for(int i = 0; i < paramTypes.size(); ++i) {
                  if (!fastHierachy.canStoreType((Type)declaredParamTypes.get(i), (Type)paramTypes.get(i))) {
                     check = false;
                     break;
                  }
               }

               if (check) {
                  Type st = sc.getType();
                  if (!fastHierachy.canStoreType(st, declaredType)) {
                     if (!sc.isFinal()) {
                        NumberedString newSubSig = sm.getNumberedSubSignature();
                        this.resolve(st, st, sigType, newSubSig, container, targets, appOnly);
                        types.add(new Pair(st, newSubSig));
                     }
                  } else {
                     this.resolve(st, declaredType, sigType, subSig, container, targets, appOnly);
                     types.add(new Pair(st, subSig));
                  }
               }
            }
         }

         this.baseToPossibleSubTypes.putAll(pair, types);
      }
   }
}
