package soot.jimple.toolkits.typing.fast;

import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import soot.ArrayType;
import soot.FloatType;
import soot.IntType;
import soot.IntegerType;
import soot.NullType;
import soot.PrimType;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.Type;

public class BytecodeHierarchy implements IHierarchy {
   private static Collection<BytecodeHierarchy.AncestryTreeNode> buildAncestryTree(RefType root) {
      if (root.getSootClass().isPhantom()) {
         return Collections.emptyList();
      } else {
         LinkedList<BytecodeHierarchy.AncestryTreeNode> leafs = new LinkedList();
         leafs.add(new BytecodeHierarchy.AncestryTreeNode((BytecodeHierarchy.AncestryTreeNode)null, root));
         LinkedList<BytecodeHierarchy.AncestryTreeNode> r = new LinkedList();
         RefType objectType = RefType.v("java.lang.Object");

         while(true) {
            while(!leafs.isEmpty()) {
               BytecodeHierarchy.AncestryTreeNode node = (BytecodeHierarchy.AncestryTreeNode)leafs.remove();
               if (TypeResolver.typesEqual(node.type, objectType)) {
                  r.add(node);
               } else {
                  SootClass sc = node.type.getSootClass();
                  Iterator var6 = sc.getInterfaces().iterator();

                  while(var6.hasNext()) {
                     SootClass i = (SootClass)var6.next();
                     leafs.add(new BytecodeHierarchy.AncestryTreeNode(node, i.getType()));
                  }

                  if ((!sc.isInterface() || sc.getInterfaceCount() == 0) && !sc.isPhantom()) {
                     leafs.add(new BytecodeHierarchy.AncestryTreeNode(node, sc.getSuperclass().getType()));
                  }
               }
            }

            return r;
         }
      }
   }

   private static RefType leastCommonNode(BytecodeHierarchy.AncestryTreeNode a, BytecodeHierarchy.AncestryTreeNode b) {
      RefType r;
      for(r = null; a != null && b != null && TypeResolver.typesEqual(a.type, b.type); b = b.next) {
         r = a.type;
         a = a.next;
      }

      return r;
   }

   public Collection<Type> lcas(Type a, Type b) {
      return lcas_(a, b);
   }

   public static Collection<Type> lcas_(Type a, Type b) {
      if (TypeResolver.typesEqual(a, b)) {
         return Collections.singletonList(a);
      } else if (a instanceof BottomType) {
         return Collections.singletonList(b);
      } else if (b instanceof BottomType) {
         return Collections.singletonList(a);
      } else if (a instanceof IntegerType && b instanceof IntegerType) {
         return Collections.singletonList(IntType.v());
      } else if (a instanceof IntegerType && b instanceof FloatType) {
         return Collections.singletonList(FloatType.v());
      } else if (b instanceof IntegerType && a instanceof FloatType) {
         return Collections.singletonList(FloatType.v());
      } else if (!(a instanceof PrimType) && !(b instanceof PrimType)) {
         if (a instanceof NullType) {
            return Collections.singletonList(b);
         } else if (b instanceof NullType) {
            return Collections.singletonList(a);
         } else {
            Type rt;
            if (a instanceof ArrayType && b instanceof ArrayType) {
               rt = ((ArrayType)a).getElementType();
               Type etb = ((ArrayType)b).getElementType();
               Object ts;
               if (!(rt instanceof PrimType) && !(etb instanceof PrimType)) {
                  ts = lcas_(rt, etb);
               } else {
                  ts = Collections.emptyList();
               }

               LinkedList<Type> r = new LinkedList();
               if (((Collection)ts).isEmpty()) {
                  r.add(RefType.v("java.lang.Object"));
                  r.add(RefType.v("java.io.Serializable"));
                  r.add(RefType.v("java.lang.Cloneable"));
               } else {
                  Iterator var18 = ((Collection)ts).iterator();

                  while(var18.hasNext()) {
                     Type t = (Type)var18.next();
                     r.add(t.makeArrayType());
                  }
               }

               return r;
            } else if (!(a instanceof ArrayType) && !(b instanceof ArrayType)) {
               Collection<BytecodeHierarchy.AncestryTreeNode> treea = buildAncestryTree((RefType)a);
               Collection<BytecodeHierarchy.AncestryTreeNode> treeb = buildAncestryTree((RefType)b);
               LinkedList<Type> r = new LinkedList();
               Iterator var5 = treea.iterator();

               while(var5.hasNext()) {
                  BytecodeHierarchy.AncestryTreeNode nodea = (BytecodeHierarchy.AncestryTreeNode)var5.next();
                  Iterator var7 = treeb.iterator();

                  while(var7.hasNext()) {
                     BytecodeHierarchy.AncestryTreeNode nodeb = (BytecodeHierarchy.AncestryTreeNode)var7.next();
                     RefType t = leastCommonNode(nodea, nodeb);
                     boolean least = true;
                     ListIterator i = r.listIterator();

                     while(i.hasNext()) {
                        Type t_ = (Type)i.next();
                        if (ancestor_(t, t_)) {
                           least = false;
                           break;
                        }

                        if (ancestor_(t_, t)) {
                           i.remove();
                        }
                     }

                     if (least) {
                        r.add(t);
                     }
                  }
               }

               if (r.isEmpty()) {
                  r.add(RefType.v("java.lang.Object"));
               }

               return r;
            } else {
               if (a instanceof ArrayType) {
                  rt = b;
               } else {
                  rt = a;
               }

               LinkedList<Type> r = new LinkedList();
               if (!TypeResolver.typesEqual(RefType.v("java.lang.Object"), rt)) {
                  if (ancestor_(RefType.v("java.io.Serializable"), rt)) {
                     r.add(RefType.v("java.io.Serializable"));
                  }

                  if (ancestor_(RefType.v("java.lang.Cloneable"), rt)) {
                     r.add(RefType.v("java.lang.Cloneable"));
                  }
               }

               if (r.isEmpty()) {
                  r.add(RefType.v("java.lang.Object"));
               }

               return r;
            }
         }
      } else {
         return Collections.emptyList();
      }
   }

   public boolean ancestor(Type ancestor, Type child) {
      return ancestor_(ancestor, child);
   }

   public static boolean ancestor_(Type ancestor, Type child) {
      if (TypeResolver.typesEqual(ancestor, child)) {
         return true;
      } else if (child instanceof BottomType) {
         return true;
      } else if (ancestor instanceof BottomType) {
         return false;
      } else if (ancestor instanceof IntegerType && child instanceof IntegerType) {
         return true;
      } else if (!(ancestor instanceof PrimType) && !(child instanceof PrimType)) {
         if (child instanceof NullType) {
            return true;
         } else {
            return ancestor instanceof NullType ? false : Scene.v().getOrMakeFastHierarchy().canStoreType(child, ancestor);
         }
      } else {
         return false;
      }
   }

   private static Deque<RefType> superclassPath(RefType t, RefType anchor) {
      Deque<RefType> r = new LinkedList();
      r.addFirst(t);
      if (t.getSootClass().isPhantom() && anchor != null) {
         r.addFirst(anchor);
         return r;
      } else {
         SootClass sc = t.getSootClass();

         while(sc.hasSuperclass()) {
            sc = sc.getSuperclass();
            r.addFirst(sc.getType());
            if (sc.isPhantom() && anchor != null) {
               r.addFirst(anchor);
               break;
            }
         }

         return r;
      }
   }

   public static RefType lcsc(RefType a, RefType b) {
      if (a == b) {
         return a;
      } else {
         Deque<RefType> pathA = superclassPath(a, (RefType)null);
         Deque<RefType> pathB = superclassPath(b, (RefType)null);
         RefType r = null;

         while(!pathA.isEmpty() && !pathB.isEmpty() && TypeResolver.typesEqual((Type)pathA.getFirst(), (Type)pathB.getFirst())) {
            r = (RefType)pathA.removeFirst();
            pathB.removeFirst();
         }

         return r;
      }
   }

   public static RefType lcsc(RefType a, RefType b, RefType anchor) {
      if (a == b) {
         return a;
      } else {
         Deque<RefType> pathA = superclassPath(a, anchor);
         Deque<RefType> pathB = superclassPath(b, anchor);
         RefType r = null;

         while(!pathA.isEmpty() && !pathB.isEmpty() && TypeResolver.typesEqual((Type)pathA.getFirst(), (Type)pathB.getFirst())) {
            r = (RefType)pathA.removeFirst();
            pathB.removeFirst();
         }

         return r;
      }
   }

   private static class AncestryTreeNode {
      public final BytecodeHierarchy.AncestryTreeNode next;
      public final RefType type;

      public AncestryTreeNode(BytecodeHierarchy.AncestryTreeNode next, RefType type) {
         this.next = next;
         this.type = type;
      }
   }
}
