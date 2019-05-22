package soot;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import soot.jimple.SpecialInvokeExpr;
import soot.util.ConcurrentHashMultiMap;
import soot.util.MultiMap;

public class FastHierarchy {
   protected MultiMap<SootClass, SootClass> classToSubclasses = new ConcurrentHashMultiMap();
   protected MultiMap<SootClass, SootClass> interfaceToSubinterfaces = new ConcurrentHashMultiMap();
   protected MultiMap<SootClass, SootClass> interfaceToImplementers = new ConcurrentHashMultiMap();
   protected MultiMap<SootClass, SootClass> interfaceToAllSubinterfaces = new ConcurrentHashMultiMap();
   protected MultiMap<SootClass, SootClass> interfaceToAllImplementers = new ConcurrentHashMultiMap();
   protected Map<SootClass, FastHierarchy.Interval> classToInterval = new HashMap();
   protected Scene sc = Scene.v();

   protected int dfsVisit(int start, SootClass c) {
      FastHierarchy.Interval r = new FastHierarchy.Interval();
      r.lower = start++;
      Collection<SootClass> col = this.classToSubclasses.get(c);
      if (col != null) {
         Iterator var5 = col.iterator();

         while(var5.hasNext()) {
            SootClass sc = (SootClass)var5.next();
            if (!sc.isInterface()) {
               start = this.dfsVisit(start, sc);
            }
         }
      }

      r.upper = start++;
      if (c.isInterface()) {
         throw new RuntimeException("Attempt to dfs visit interface " + c);
      } else {
         if (!this.classToInterval.containsKey(c)) {
            this.classToInterval.put(c, r);
         }

         return start;
      }
   }

   public FastHierarchy() {
      Iterator phantomClassIt = this.sc.getClasses().getElementsUnsorted().iterator();

      while(true) {
         SootClass cl;
         do {
            if (!phantomClassIt.hasNext()) {
               this.dfsVisit(0, this.sc.getSootClass("java.lang.Object"));
               phantomClassIt = this.sc.getPhantomClasses().snapshotIterator();

               while(phantomClassIt.hasNext()) {
                  cl = (SootClass)phantomClassIt.next();
                  if (!cl.isInterface()) {
                     this.dfsVisit(0, cl);
                  }
               }

               return;
            }

            cl = (SootClass)phantomClassIt.next();
         } while(cl.resolvingLevel() < 1);

         if (!cl.isInterface()) {
            SootClass superClass = cl.getSuperclassUnsafe();
            if (superClass != null) {
               this.classToSubclasses.put(superClass, cl);
            }
         }

         Iterator var5 = cl.getInterfaces().iterator();

         while(var5.hasNext()) {
            SootClass supercl = (SootClass)var5.next();
            if (cl.isInterface()) {
               this.interfaceToSubinterfaces.put(supercl, cl);
            } else {
               this.interfaceToImplementers.put(supercl, cl);
            }
         }
      }
   }

   public boolean isSubclass(SootClass child, SootClass parent) {
      child.checkLevel(1);
      parent.checkLevel(1);
      FastHierarchy.Interval parentInterval = (FastHierarchy.Interval)this.classToInterval.get(parent);
      FastHierarchy.Interval childInterval = (FastHierarchy.Interval)this.classToInterval.get(child);
      return parentInterval != null && childInterval != null && parentInterval.isSubrange(childInterval);
   }

   public Set<SootClass> getAllImplementersOfInterface(SootClass parent) {
      parent.checkLevel(1);
      if (!this.interfaceToAllImplementers.containsKey(parent)) {
         Iterator var2 = this.getAllSubinterfaces(parent).iterator();

         while(var2.hasNext()) {
            SootClass subinterface = (SootClass)var2.next();
            if (subinterface != parent) {
               this.interfaceToAllImplementers.putAll(parent, this.getAllImplementersOfInterface(subinterface));
            }
         }

         this.interfaceToAllImplementers.putAll(parent, this.interfaceToImplementers.get(parent));
      }

      return this.interfaceToAllImplementers.get(parent);
   }

   public Set<SootClass> getAllSubinterfaces(SootClass parent) {
      parent.checkLevel(1);
      if (!parent.isInterface()) {
         return Collections.emptySet();
      } else {
         if (!this.interfaceToAllSubinterfaces.containsKey(parent)) {
            this.interfaceToAllSubinterfaces.put(parent, parent);
            Iterator var2 = this.interfaceToSubinterfaces.get(parent).iterator();

            while(var2.hasNext()) {
               SootClass si = (SootClass)var2.next();
               this.interfaceToAllSubinterfaces.putAll(parent, this.getAllSubinterfaces(si));
            }
         }

         return this.interfaceToAllSubinterfaces.get(parent);
      }
   }

   public boolean canStoreType(Type child, Type parent) {
      if (child.equals(parent)) {
         return true;
      } else if (parent instanceof NullType) {
         return false;
      } else if (child instanceof NullType) {
         return parent instanceof RefLikeType;
      } else if (child instanceof RefType) {
         if (parent.equals(this.sc.getObjectType())) {
            return true;
         } else {
            return parent instanceof RefType ? this.canStoreClass(((RefType)child).getSootClass(), ((RefType)parent).getSootClass()) : false;
         }
      } else if (child instanceof AnySubType) {
         if (!(parent instanceof RefLikeType)) {
            throw new RuntimeException("Unhandled type " + parent);
         } else if (!(parent instanceof ArrayType)) {
            SootClass base = ((AnySubType)child).getBase().getSootClass();
            SootClass parentClass = ((RefType)parent).getSootClass();
            ArrayDeque<SootClass> worklist = new ArrayDeque();
            if (base.isInterface()) {
               worklist.addAll(this.getAllImplementersOfInterface(base));
            } else {
               worklist.add(base);
            }

            HashSet workset = new HashSet();

            while(true) {
               SootClass cl = (SootClass)worklist.poll();
               if (cl == null) {
                  return false;
               }

               if (workset.add(cl)) {
                  if (cl.isConcrete() && this.canStoreClass(cl, parentClass)) {
                     return true;
                  }

                  worklist.addAll(this.getSubclassesOf(cl));
               }
            }
         } else {
            Type base = ((AnySubType)child).getBase();
            return base.equals(this.sc.getObjectType()) || base.equals(RefType.v("java.io.Serializable")) || base.equals(RefType.v("java.lang.Cloneable"));
         }
      } else if (child instanceof ArrayType) {
         ArrayType achild = (ArrayType)child;
         if (!(parent instanceof RefType)) {
            if (!(parent instanceof ArrayType)) {
               return false;
            } else {
               ArrayType aparent = (ArrayType)parent;
               if (achild.numDimensions == aparent.numDimensions) {
                  if (achild.baseType.equals(aparent.baseType)) {
                     return true;
                  } else if (!(achild.baseType instanceof RefType)) {
                     return false;
                  } else {
                     return !(aparent.baseType instanceof RefType) ? false : this.canStoreType(achild.baseType, aparent.baseType);
                  }
               } else if (achild.numDimensions > aparent.numDimensions) {
                  if (aparent.baseType.equals(this.sc.getObjectType())) {
                     return true;
                  } else if (aparent.baseType.equals(RefType.v("java.io.Serializable"))) {
                     return true;
                  } else {
                     return aparent.baseType.equals(RefType.v("java.lang.Cloneable"));
                  }
               } else {
                  return false;
               }
            }
         } else {
            return parent.equals(this.sc.getObjectType()) || parent.equals(RefType.v("java.io.Serializable")) || parent.equals(RefType.v("java.lang.Cloneable"));
         }
      } else {
         return false;
      }
   }

   protected boolean canStoreClass(SootClass child, SootClass parent) {
      parent.checkLevel(1);
      child.checkLevel(1);
      FastHierarchy.Interval parentInterval = (FastHierarchy.Interval)this.classToInterval.get(parent);
      FastHierarchy.Interval childInterval = (FastHierarchy.Interval)this.classToInterval.get(child);
      if (parentInterval != null && childInterval != null) {
         return parentInterval.isSubrange(childInterval);
      } else if (childInterval == null) {
         return parentInterval != null ? parent.equals(this.sc.getObjectType().getSootClass()) : this.getAllSubinterfaces(parent).contains(child);
      } else {
         Set<SootClass> impl = this.getAllImplementersOfInterface(parent);
         Iterator it = impl.iterator();

         do {
            if (!it.hasNext()) {
               return false;
            }

            parentInterval = (FastHierarchy.Interval)this.classToInterval.get(it.next());
         } while(parentInterval == null || !parentInterval.isSubrange(childInterval));

         return true;
      }
   }

   public Collection<SootMethod> resolveConcreteDispatchWithoutFailing(Collection<Type> concreteTypes, SootMethod m, RefType declaredTypeOfBase) {
      Set<SootMethod> ret = new HashSet();
      SootClass declaringClass = declaredTypeOfBase.getSootClass();
      declaringClass.checkLevel(1);
      Iterator var6 = concreteTypes.iterator();

      while(var6.hasNext()) {
         Type t = (Type)var6.next();
         SootClass c;
         SootMethod concreteM;
         if (t instanceof AnySubType) {
            HashSet<SootClass> s = new HashSet();
            s.add(declaringClass);

            while(!s.isEmpty()) {
               c = (SootClass)s.iterator().next();
               s.remove(c);
               if (!c.isInterface() && !c.isAbstract() && this.canStoreClass(c, declaringClass)) {
                  concreteM = this.resolveConcreteDispatch(c, m);
                  if (concreteM != null) {
                     ret.add(concreteM);
                  }
               }

               if (this.classToSubclasses.containsKey(c)) {
                  s.addAll(this.classToSubclasses.get(c));
               }

               if (this.interfaceToSubinterfaces.containsKey(c)) {
                  s.addAll(this.interfaceToSubinterfaces.get(c));
               }

               if (this.interfaceToImplementers.containsKey(c)) {
                  s.addAll(this.interfaceToImplementers.get(c));
               }
            }

            return ret;
         }

         RefType concreteType;
         if (t instanceof RefType) {
            concreteType = (RefType)t;
            c = concreteType.getSootClass();
            if (this.canStoreClass(c, declaringClass)) {
               concreteM = null;

               try {
                  concreteM = this.resolveConcreteDispatch(c, m);
               } catch (Exception var13) {
                  concreteM = null;
               }

               if (concreteM != null) {
                  ret.add(concreteM);
               }
            }
         } else {
            if (!(t instanceof ArrayType)) {
               throw new RuntimeException("Unrecognized reaching type " + t);
            }

            concreteType = null;

            SootMethod concreteM;
            try {
               concreteM = this.resolveConcreteDispatch(RefType.v("java.lang.Object").getSootClass(), m);
            } catch (Exception var12) {
               concreteM = null;
            }

            if (concreteM != null) {
               ret.add(concreteM);
            }
         }
      }

      return ret;
   }

   public Collection<SootMethod> resolveConcreteDispatch(Collection<Type> concreteTypes, SootMethod m, RefType declaredTypeOfBase) {
      Set<SootMethod> ret = new HashSet();
      SootClass declaringClass = declaredTypeOfBase.getSootClass();
      declaringClass.checkLevel(1);
      Iterator var6 = concreteTypes.iterator();

      while(var6.hasNext()) {
         Type t = (Type)var6.next();
         SootClass c;
         SootMethod concreteM;
         if (t instanceof AnySubType) {
            HashSet<SootClass> s = new HashSet();
            s.add(declaringClass);

            while(!s.isEmpty()) {
               c = (SootClass)s.iterator().next();
               s.remove(c);
               if (!c.isInterface() && !c.isAbstract() && this.canStoreClass(c, declaringClass)) {
                  concreteM = this.resolveConcreteDispatch(c, m);
                  if (concreteM != null) {
                     ret.add(concreteM);
                  }
               }

               if (this.classToSubclasses.containsKey(c)) {
                  s.addAll(this.classToSubclasses.get(c));
               }

               if (this.interfaceToSubinterfaces.containsKey(c)) {
                  s.addAll(this.interfaceToSubinterfaces.get(c));
               }

               if (this.interfaceToImplementers.containsKey(c)) {
                  s.addAll(this.interfaceToImplementers.get(c));
               }
            }

            return ret;
         }

         if (t instanceof RefType) {
            RefType concreteType = (RefType)t;
            c = concreteType.getSootClass();
            if (this.canStoreClass(c, declaringClass)) {
               concreteM = this.resolveConcreteDispatch(c, m);
               if (concreteM != null) {
                  ret.add(concreteM);
               }
            }
         } else {
            if (!(t instanceof ArrayType)) {
               throw new RuntimeException("Unrecognized reaching type " + t);
            }

            SootMethod concreteM = this.resolveConcreteDispatch(RefType.v("java.lang.Object").getSootClass(), m);
            if (concreteM != null) {
               ret.add(concreteM);
            }
         }
      }

      return ret;
   }

   private boolean isVisible(SootClass from, SootMethod m) {
      from.checkLevel(1);
      if (m.isPublic()) {
         return true;
      } else if (m.isPrivate()) {
         return from.equals(m.getDeclaringClass());
      } else {
         return m.isProtected() ? this.canStoreClass(from, m.getDeclaringClass()) : from.getJavaPackageName().equals(m.getDeclaringClass().getJavaPackageName());
      }
   }

   public Set<SootMethod> resolveAbstractDispatch(SootClass abstractType, SootMethod m) {
      String methodSig = m.getSubSignature();
      HashSet<SootClass> resolved = new HashSet();
      HashSet<SootMethod> ret = new HashSet();
      ArrayDeque<SootClass> worklist = new ArrayDeque();
      worklist.add(abstractType);

      while(true) {
         while(true) {
            SootClass concreteType = (SootClass)worklist.poll();
            if (concreteType == null) {
               return ret;
            }

            SootClass savedConcreteType = concreteType;
            if (concreteType.isInterface()) {
               worklist.addAll(this.getAllImplementersOfInterface(concreteType));
            } else {
               Collection<SootClass> c = this.classToSubclasses.get(concreteType);
               if (c != null) {
                  worklist.addAll(c);
               }

               if (!concreteType.isAbstract()) {
                  while(!resolved.contains(concreteType)) {
                     resolved.add(concreteType);
                     SootMethod method = concreteType.getMethodUnsafe(methodSig);
                     if (method != null && this.isVisible(concreteType, m)) {
                        if (method.isAbstract()) {
                           throw new RuntimeException("abstract dispatch resolved to abstract method!\nAbstract Type: " + abstractType + "\nConcrete Type: " + savedConcreteType + "\nMethod: " + m);
                        }

                        ret.add(method);
                        break;
                     }

                     SootClass superClass = concreteType.getSuperclassUnsafe();
                     if (superClass == null) {
                        if (!concreteType.isPhantom()) {
                           throw new RuntimeException("could not resolve abstract dispatch!\nAbstract Type: " + abstractType + "\nConcrete Type: " + savedConcreteType + "\nMethod: " + m);
                        }
                        break;
                     }

                     concreteType = superClass;
                  }
               }
            }
         }
      }
   }

   public SootMethod resolveConcreteDispatch(SootClass concreteType, SootMethod m) {
      concreteType.checkLevel(1);
      if (concreteType.isInterface()) {
         throw new RuntimeException("A concrete type cannot be an interface: " + concreteType);
      } else {
         String methodSig = m.getSubSignature();

         do {
            SootMethod method = concreteType.getMethodUnsafe(methodSig);
            if (method != null && this.isVisible(concreteType, m)) {
               if (method.isAbstract()) {
                  throw new RuntimeException("Error: Method call resolves to abstract method!");
               }

               return method;
            }

            concreteType = concreteType.getSuperclassUnsafe();
         } while(concreteType != null);

         return null;
      }
   }

   public SootMethod resolveSpecialDispatch(SpecialInvokeExpr ie, SootMethod container) {
      SootMethod target = ie.getMethod();
      if (!target.getName().equals("<init>") && !target.isPrivate()) {
         return this.isSubclass(target.getDeclaringClass(), container.getDeclaringClass()) ? this.resolveConcreteDispatch(container.getDeclaringClass(), target) : target;
      } else {
         return target;
      }
   }

   public Collection<SootClass> getSubclassesOf(SootClass c) {
      c.checkLevel(1);
      Collection<SootClass> ret = this.classToSubclasses.get(c);
      return (Collection)(ret == null ? Collections.emptyList() : ret);
   }

   protected class Interval {
      int lower;
      int upper;

      public boolean isSubrange(FastHierarchy.Interval potentialSubrange) {
         if (potentialSubrange == null) {
            return false;
         } else if (this.lower > potentialSubrange.lower) {
            return false;
         } else {
            return this.upper >= potentialSubrange.upper;
         }
      }
   }
}
