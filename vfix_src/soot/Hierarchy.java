package soot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.jimple.SpecialInvokeExpr;
import soot.util.ArraySet;
import soot.util.Chain;

public class Hierarchy {
   protected Map<SootClass, List<SootClass>> classToSubclasses;
   protected Map<SootClass, List<SootClass>> interfaceToSubinterfaces;
   protected Map<SootClass, List<SootClass>> interfaceToSuperinterfaces;
   protected Map<SootClass, List<SootClass>> classToDirSubclasses;
   protected Map<SootClass, List<SootClass>> interfaceToDirSubinterfaces;
   protected Map<SootClass, List<SootClass>> interfaceToDirSuperinterfaces;
   protected Map<SootClass, List<SootClass>> interfaceToDirImplementers;
   int state;
   Scene sc = Scene.v();

   public Hierarchy() {
      this.state = this.sc.getState();
      Chain<SootClass> allClasses = this.sc.getClasses();
      this.classToSubclasses = new HashMap(allClasses.size() * 2 + 1, 0.7F);
      this.interfaceToSubinterfaces = new HashMap(allClasses.size() * 2 + 1, 0.7F);
      this.interfaceToSuperinterfaces = new HashMap(allClasses.size() * 2 + 1, 0.7F);
      this.classToDirSubclasses = new HashMap(allClasses.size() * 2 + 1, 0.7F);
      this.interfaceToDirSubinterfaces = new HashMap(allClasses.size() * 2 + 1, 0.7F);
      this.interfaceToDirSuperinterfaces = new HashMap(allClasses.size() * 2 + 1, 0.7F);
      this.interfaceToDirImplementers = new HashMap(allClasses.size() * 2 + 1, 0.7F);
      this.initializeHierarchy(allClasses);
   }

   protected void initializeHierarchy(Chain<SootClass> allClasses) {
      Iterator var2 = allClasses.iterator();

      SootClass c;
      while(var2.hasNext()) {
         c = (SootClass)var2.next();
         if (c.resolvingLevel() >= 1) {
            if (c.isInterface()) {
               this.interfaceToDirSubinterfaces.put(c, new ArrayList());
               this.interfaceToDirSuperinterfaces.put(c, new ArrayList());
               this.interfaceToDirImplementers.put(c, new ArrayList());
            } else {
               this.classToDirSubclasses.put(c, new ArrayList());
            }
         }
      }

      var2 = allClasses.iterator();

      while(true) {
         while(true) {
            List l;
            do {
               do {
                  if (!var2.hasNext()) {
                     var2 = allClasses.iterator();

                     while(true) {
                        do {
                           do {
                              if (!var2.hasNext()) {
                                 return;
                              }

                              c = (SootClass)var2.next();
                           } while(c.resolvingLevel() < 1);
                        } while(!c.isInterface());

                        l = (List)this.interfaceToDirImplementers.get(c);
                        Set<SootClass> s = new ArraySet();
                        Iterator var9 = l.iterator();

                        while(var9.hasNext()) {
                           SootClass c0 = (SootClass)var9.next();
                           if (c.resolvingLevel() >= 1) {
                              s.addAll(this.getSubclassesOfIncluding(c0));
                           }
                        }

                        l.clear();
                        l.addAll(s);
                     }
                  }

                  c = (SootClass)var2.next();
               } while(c.resolvingLevel() < 1);
            } while(!c.hasSuperclass());

            Iterator var5;
            SootClass i;
            if (c.isInterface()) {
               l = (List)this.interfaceToDirSuperinterfaces.get(c);
               var5 = c.getInterfaces().iterator();

               while(var5.hasNext()) {
                  i = (SootClass)var5.next();
                  if (c.resolvingLevel() >= 1) {
                     List<SootClass> l = (List)this.interfaceToDirSubinterfaces.get(i);
                     if (l != null) {
                        l.add(c);
                     }

                     if (l != null) {
                        l.add(i);
                     }
                  }
               }
            } else {
               l = (List)this.classToDirSubclasses.get(c.getSuperclass());
               if (l != null) {
                  l.add(c);
               }

               var5 = c.getInterfaces().iterator();

               while(var5.hasNext()) {
                  i = (SootClass)var5.next();
                  if (c.resolvingLevel() >= 1) {
                     l = (List)this.interfaceToDirImplementers.get(i);
                     if (l != null) {
                        l.add(c);
                     }
                  }
               }
            }
         }
      }
   }

   protected void checkState() {
      if (this.state != this.sc.getState()) {
         throw new ConcurrentModificationException("Scene changed for Hierarchy!");
      }
   }

   public List<SootClass> getSubclassesOfIncluding(SootClass c) {
      c.checkLevel(1);
      if (c.isInterface()) {
         throw new RuntimeException("class needed!");
      } else {
         List<SootClass> l = new ArrayList();
         l.addAll(this.getSubclassesOf(c));
         l.add(c);
         return Collections.unmodifiableList(l);
      }
   }

   public List<SootClass> getSubclassesOf(SootClass c) {
      c.checkLevel(1);
      if (c.isInterface()) {
         throw new RuntimeException("class needed!");
      } else {
         this.checkState();
         if (this.classToSubclasses.get(c) != null) {
            return (List)this.classToSubclasses.get(c);
         } else {
            List<SootClass> l = new ArrayList();
            Iterator var3 = ((List)this.classToDirSubclasses.get(c)).iterator();

            while(var3.hasNext()) {
               SootClass cls = (SootClass)var3.next();
               if (cls.resolvingLevel() >= 1) {
                  l.addAll(this.getSubclassesOfIncluding(cls));
               }
            }

            List<SootClass> l = Collections.unmodifiableList(l);
            this.classToSubclasses.put(c, l);
            return l;
         }
      }
   }

   public List<SootClass> getSuperclassesOfIncluding(SootClass sootClass) {
      List<SootClass> superclasses = this.getSuperclassesOf(sootClass);
      List<SootClass> result = new ArrayList(superclasses.size() + 1);
      result.add(sootClass);
      result.addAll(superclasses);
      return Collections.unmodifiableList(result);
   }

   public List<SootClass> getSuperclassesOf(SootClass sootClass) {
      sootClass.checkLevel(1);
      if (sootClass.isInterface()) {
         throw new IllegalArgumentException(sootClass.getName() + " is an interface, but class is expected");
      } else {
         this.checkState();
         List<SootClass> superclasses = new ArrayList();

         for(SootClass current = sootClass; current.hasSuperclass(); current = current.getSuperclass()) {
            superclasses.add(current.getSuperclass());
         }

         return Collections.unmodifiableList(superclasses);
      }
   }

   public List<SootClass> getSubinterfacesOfIncluding(SootClass sootClass) {
      List<SootClass> result = new ArrayList(this.getSubinterfacesOf(sootClass));
      result.add(sootClass);
      return Collections.unmodifiableList(result);
   }

   public List<SootClass> getSubinterfacesOf(SootClass sootClass) {
      sootClass.checkLevel(1);
      if (!sootClass.isInterface()) {
         throw new IllegalArgumentException(sootClass.getName() + " is a class, but interface is expected");
      } else {
         this.checkState();
         if (this.interfaceToSubinterfaces.get(sootClass) != null) {
            return (List)this.interfaceToSubinterfaces.get(sootClass);
         } else {
            List<SootClass> result = new ArrayList();
            Iterator var3 = ((List)this.interfaceToDirSubinterfaces.get(sootClass)).iterator();

            while(var3.hasNext()) {
               SootClass si = (SootClass)var3.next();
               result.addAll(this.getSubinterfacesOfIncluding(si));
            }

            List<SootClass> unmodifiableResult = Collections.unmodifiableList(result);
            this.interfaceToSubinterfaces.put(sootClass, unmodifiableResult);
            return unmodifiableResult;
         }
      }
   }

   public List<SootClass> getSuperinterfacesOfIncluding(SootClass c) {
      c.checkLevel(1);
      if (!c.isInterface()) {
         throw new RuntimeException("interface needed!");
      } else {
         List<SootClass> l = new ArrayList();
         l.addAll(this.getSuperinterfacesOf(c));
         l.add(c);
         return Collections.unmodifiableList(l);
      }
   }

   public List<SootClass> getSuperinterfacesOf(SootClass c) {
      c.checkLevel(1);
      if (!c.isInterface()) {
         throw new RuntimeException("interface needed!");
      } else {
         this.checkState();
         List<SootClass> cached = (List)this.interfaceToSuperinterfaces.get(c);
         if (cached != null) {
            return cached;
         } else {
            List<SootClass> l = new ArrayList();
            Iterator var4 = ((List)this.interfaceToDirSuperinterfaces.get(c)).iterator();

            while(var4.hasNext()) {
               SootClass si = (SootClass)var4.next();
               l.addAll(this.getSuperinterfacesOfIncluding(si));
            }

            this.interfaceToSuperinterfaces.put(c, Collections.unmodifiableList(l));
            return Collections.unmodifiableList(l);
         }
      }
   }

   public List<SootClass> getDirectSuperclassesOf(SootClass c) {
      throw new RuntimeException("Not implemented yet!");
   }

   public List<SootClass> getDirectSubclassesOf(SootClass c) {
      c.checkLevel(1);
      if (c.isInterface()) {
         throw new RuntimeException("class needed!");
      } else {
         this.checkState();
         return Collections.unmodifiableList((List)this.classToDirSubclasses.get(c));
      }
   }

   public List<SootClass> getDirectSubclassesOfIncluding(SootClass c) {
      c.checkLevel(1);
      if (c.isInterface()) {
         throw new RuntimeException("class needed!");
      } else {
         this.checkState();
         List<SootClass> l = new ArrayList();
         l.addAll((Collection)this.classToDirSubclasses.get(c));
         l.add(c);
         return Collections.unmodifiableList(l);
      }
   }

   public List<SootClass> getDirectSuperinterfacesOf(SootClass c) {
      throw new RuntimeException("Not implemented yet!");
   }

   public List<SootClass> getDirectSubinterfacesOf(SootClass c) {
      c.checkLevel(1);
      if (!c.isInterface()) {
         throw new RuntimeException("interface needed!");
      } else {
         this.checkState();
         return (List)this.interfaceToDirSubinterfaces.get(c);
      }
   }

   public List<SootClass> getDirectSubinterfacesOfIncluding(SootClass c) {
      c.checkLevel(1);
      if (!c.isInterface()) {
         throw new RuntimeException("interface needed!");
      } else {
         this.checkState();
         List<SootClass> l = new ArrayList();
         l.addAll((Collection)this.interfaceToDirSubinterfaces.get(c));
         l.add(c);
         return Collections.unmodifiableList(l);
      }
   }

   public List<SootClass> getDirectImplementersOf(SootClass i) {
      i.checkLevel(1);
      if (!i.isInterface()) {
         throw new RuntimeException("interface needed; got " + i);
      } else {
         this.checkState();
         return Collections.unmodifiableList((List)this.interfaceToDirImplementers.get(i));
      }
   }

   public List<SootClass> getImplementersOf(SootClass i) {
      i.checkLevel(1);
      if (!i.isInterface()) {
         throw new RuntimeException("interface needed; got " + i);
      } else {
         this.checkState();
         ArraySet<SootClass> set = new ArraySet();
         Iterator var3 = this.getSubinterfacesOfIncluding(i).iterator();

         while(var3.hasNext()) {
            SootClass c = (SootClass)var3.next();
            set.addAll(this.getDirectImplementersOf(c));
         }

         ArrayList<SootClass> l = new ArrayList();
         l.addAll(set);
         return Collections.unmodifiableList(l);
      }
   }

   public boolean isClassSubclassOf(SootClass child, SootClass possibleParent) {
      child.checkLevel(1);
      possibleParent.checkLevel(1);
      List<SootClass> parentClasses = this.getSuperclassesOf(child);
      if (parentClasses.contains(possibleParent)) {
         return true;
      } else {
         Iterator var4 = parentClasses.iterator();

         SootClass sc;
         do {
            if (!var4.hasNext()) {
               return false;
            }

            sc = (SootClass)var4.next();
         } while(!sc.isPhantom());

         return true;
      }
   }

   public boolean isClassSubclassOfIncluding(SootClass child, SootClass possibleParent) {
      child.checkLevel(1);
      possibleParent.checkLevel(1);
      List<SootClass> parentClasses = this.getSuperclassesOfIncluding(child);
      if (parentClasses.contains(possibleParent)) {
         return true;
      } else {
         Iterator var4 = parentClasses.iterator();

         SootClass sc;
         do {
            if (!var4.hasNext()) {
               return false;
            }

            sc = (SootClass)var4.next();
         } while(!sc.isPhantom());

         return true;
      }
   }

   public boolean isClassDirectSubclassOf(SootClass c, SootClass c2) {
      throw new RuntimeException("Not implemented yet!");
   }

   public boolean isClassSuperclassOf(SootClass parent, SootClass possibleChild) {
      parent.checkLevel(1);
      possibleChild.checkLevel(1);
      return this.getSubclassesOf(parent).contains(possibleChild);
   }

   public boolean isClassSuperclassOfIncluding(SootClass parent, SootClass possibleChild) {
      parent.checkLevel(1);
      possibleChild.checkLevel(1);
      return this.getSubclassesOfIncluding(parent).contains(possibleChild);
   }

   public boolean isInterfaceSubinterfaceOf(SootClass child, SootClass possibleParent) {
      child.checkLevel(1);
      possibleParent.checkLevel(1);
      return this.getSubinterfacesOf(possibleParent).contains(child);
   }

   public boolean isInterfaceDirectSubinterfaceOf(SootClass child, SootClass possibleParent) {
      child.checkLevel(1);
      possibleParent.checkLevel(1);
      return this.getDirectSubinterfacesOf(possibleParent).contains(child);
   }

   public boolean isInterfaceSuperinterfaceOf(SootClass parent, SootClass possibleChild) {
      parent.checkLevel(1);
      possibleChild.checkLevel(1);
      return this.getSuperinterfacesOf(possibleChild).contains(parent);
   }

   public boolean isInterfaceDirectSuperinterfaceOf(SootClass parent, SootClass possibleChild) {
      parent.checkLevel(1);
      possibleChild.checkLevel(1);
      return this.getDirectSuperinterfacesOf(possibleChild).contains(parent);
   }

   public SootClass getLeastCommonSuperclassOf(SootClass c1, SootClass c2) {
      c1.checkLevel(1);
      c2.checkLevel(1);
      throw new RuntimeException("Not implemented yet!");
   }

   public boolean isVisible(SootClass from, SootClass check) {
      if (check.isPublic()) {
         return true;
      } else {
         return !check.isProtected() && !check.isPrivate() ? from.getJavaPackageName().equals(check.getJavaPackageName()) : false;
      }
   }

   public boolean isVisible(SootClass from, ClassMember m) {
      from.checkLevel(1);
      m.getDeclaringClass().checkLevel(1);
      if (!this.isVisible(from, m.getDeclaringClass())) {
         return false;
      } else if (m.isPublic()) {
         return true;
      } else if (m.isPrivate()) {
         return from.equals(m.getDeclaringClass());
      } else if (!m.isProtected()) {
         return from.getJavaPackageName().equals(m.getDeclaringClass().getJavaPackageName());
      } else {
         return this.isClassSubclassOfIncluding(from, m.getDeclaringClass()) || from.getJavaPackageName().equals(m.getDeclaringClass().getJavaPackageName());
      }
   }

   public SootMethod resolveConcreteDispatch(SootClass concreteType, SootMethod m) {
      concreteType.checkLevel(1);
      m.getDeclaringClass().checkLevel(1);
      this.checkState();
      if (concreteType.isInterface()) {
         throw new RuntimeException("class needed!");
      } else {
         String methodSig = m.getSubSignature();
         Iterator var4 = this.getSuperclassesOfIncluding(concreteType).iterator();

         SootClass c;
         SootMethod sm;
         do {
            if (!var4.hasNext()) {
               throw new RuntimeException("could not resolve concrete dispatch!\nType: " + concreteType + "\nMethod: " + m);
            }

            c = (SootClass)var4.next();
            sm = c.getMethodUnsafe(methodSig);
         } while(sm == null || !this.isVisible(c, (ClassMember)m));

         return sm;
      }
   }

   public List<SootMethod> resolveConcreteDispatch(List<Type> classes, SootMethod m) {
      m.getDeclaringClass().checkLevel(1);
      this.checkState();
      Set<SootMethod> s = new ArraySet();
      Iterator var4 = classes.iterator();

      while(var4.hasNext()) {
         Type cls = (Type)var4.next();
         if (cls instanceof RefType) {
            s.add(this.resolveConcreteDispatch(((RefType)cls).getSootClass(), m));
         } else {
            if (!(cls instanceof ArrayType)) {
               throw new RuntimeException("Unable to resolve concrete dispatch of type " + cls);
            }

            s.add(this.resolveConcreteDispatch(RefType.v("java.lang.Object").getSootClass(), m));
         }
      }

      return Collections.unmodifiableList(new ArrayList(s));
   }

   public List<SootMethod> resolveAbstractDispatch(SootClass c, SootMethod m) {
      c.checkLevel(1);
      m.getDeclaringClass().checkLevel(1);
      this.checkState();
      Set<SootMethod> s = new ArraySet();
      Object classesIt;
      if (c.isInterface()) {
         Set<SootClass> classes = new HashSet();
         Iterator var6 = this.getImplementersOf(c).iterator();

         while(var6.hasNext()) {
            SootClass sootClass = (SootClass)var6.next();
            classes.addAll(this.getSubclassesOfIncluding(sootClass));
         }

         classesIt = classes;
      } else {
         classesIt = this.getSubclassesOfIncluding(c);
      }

      Iterator var8 = ((Collection)classesIt).iterator();

      while(var8.hasNext()) {
         SootClass cl = (SootClass)var8.next();
         if (!Modifier.isAbstract(cl.getModifiers())) {
            s.add(this.resolveConcreteDispatch(cl, m));
         }
      }

      return Collections.unmodifiableList(new ArrayList(s));
   }

   public List<SootMethod> resolveAbstractDispatch(List<SootClass> classes, SootMethod m) {
      m.getDeclaringClass().checkLevel(1);
      Set<SootMethod> s = new ArraySet();
      Iterator var4 = classes.iterator();

      while(var4.hasNext()) {
         SootClass sootClass = (SootClass)var4.next();
         s.addAll(this.resolveAbstractDispatch(sootClass, m));
      }

      return Collections.unmodifiableList(new ArrayList(s));
   }

   public SootMethod resolveSpecialDispatch(SpecialInvokeExpr ie, SootMethod container) {
      container.getDeclaringClass().checkLevel(1);
      SootMethod target = ie.getMethod();
      target.getDeclaringClass().checkLevel(1);
      if (!"<init>".equals(target.getName()) && !target.isPrivate()) {
         return this.isClassSubclassOf(target.getDeclaringClass(), container.getDeclaringClass()) ? this.resolveConcreteDispatch(container.getDeclaringClass(), target) : target;
      } else {
         return target;
      }
   }
}
