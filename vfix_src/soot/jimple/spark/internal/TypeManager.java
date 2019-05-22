package soot.jimple.spark.internal;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.AnySubType;
import soot.ArrayType;
import soot.FastHierarchy;
import soot.NullType;
import soot.RefLikeType;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.Type;
import soot.TypeSwitch;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.util.ArrayNumberer;
import soot.util.BitVector;
import soot.util.LargeNumberedMap;
import soot.util.queue.QueueReader;

public final class TypeManager {
   private static final Logger logger = LoggerFactory.getLogger(TypeManager.class);
   private Map<SootClass, List<AllocNode>> class2allocs = new HashMap(1024);
   private List<AllocNode> anySubtypeAllocs = new LinkedList();
   private LargeNumberedMap<Type, BitVector> typeMask = null;
   protected FastHierarchy fh = null;
   protected PAG pag;
   protected QueueReader<AllocNode> allocNodeListener = null;

   public TypeManager(PAG pag) {
      this.pag = pag;
   }

   public static boolean isUnresolved(Type type) {
      if (type instanceof ArrayType) {
         ArrayType at = (ArrayType)type;
         type = at.getArrayElementType();
      }

      if (!(type instanceof RefType)) {
         return false;
      } else {
         RefType rt = (RefType)type;
         if (!rt.hasSootClass()) {
            return true;
         } else {
            SootClass cl = rt.getSootClass();
            return cl.resolvingLevel() < 1;
         }
      }
   }

   public final BitVector get(Type type) {
      if (type == null) {
         return null;
      } else {
         label83:
         while(this.allocNodeListener.hasNext()) {
            AllocNode n = (AllocNode)this.allocNodeListener.next();
            Iterator var3 = Scene.v().getTypeNumberer().iterator();

            while(true) {
               while(true) {
                  Type t;
                  do {
                     do {
                        do {
                           do {
                              if (!var3.hasNext()) {
                                 continue label83;
                              }

                              t = (Type)var3.next();
                           } while(!(t instanceof RefLikeType));
                        } while(t instanceof AnySubType);
                     } while(isUnresolved(t));
                  } while(!this.castNeverFails(n.getType(), t));

                  BitVector mask = (BitVector)this.typeMask.get(t);
                  if (mask == null) {
                     this.typeMask.put(t, mask = new BitVector());
                     Iterator var6 = this.pag.getAllocNodeNumberer().iterator();

                     while(var6.hasNext()) {
                        AllocNode an = (AllocNode)var6.next();
                        if (this.castNeverFails(an.getType(), t)) {
                           mask.set(an.getNumber());
                        }
                     }
                  } else {
                     mask.set(n.getNumber());
                  }
               }
            }
         }

         BitVector ret = (BitVector)this.typeMask.get(type);
         if (ret == null && this.fh != null) {
            SootClass curClass = ((RefType)type).getSootClass();
            if (type instanceof RefType && curClass.isPhantom()) {
               return new BitVector();
            } else {
               do {
                  if (!curClass.hasSuperclass()) {
                     throw new RuntimeException("Type mask not found for type " + type);
                  }

                  curClass = curClass.getSuperclass();
               } while(!(type instanceof RefType) || !curClass.isPhantom());

               return new BitVector();
            }
         } else {
            return ret;
         }
      }
   }

   public final void clearTypeMask() {
      this.typeMask = null;
   }

   public final void makeTypeMask() {
      RefType.v("java.lang.Class");
      this.typeMask = new LargeNumberedMap(Scene.v().getTypeNumberer());
      if (this.fh != null) {
         int numTypes = Scene.v().getTypeNumberer().size();
         if (this.pag.getOpts().verbose()) {
            logger.debug("Total types: " + numTypes);
         }

         this.initClass2allocs();
         this.makeClassTypeMask(Scene.v().getSootClass("java.lang.Object"));
         BitVector visitedTypes = new BitVector();
         Iterator it = this.typeMask.keyIterator();

         while(it.hasNext()) {
            Type t = (Type)it.next();
            visitedTypes.set(t.getNumber());
         }

         ArrayNumberer<AllocNode> allocNodes = this.pag.getAllocNodeNumberer();
         Iterator var10 = Scene.v().getTypeNumberer().iterator();

         while(true) {
            while(true) {
               Type t;
               do {
                  do {
                     do {
                        if (!var10.hasNext()) {
                           this.allocNodeListener = this.pag.allocNodeListener();
                           return;
                        }

                        t = (Type)var10.next();
                     } while(!(t instanceof RefLikeType));
                  } while(t instanceof AnySubType);
               } while(isUnresolved(t));

               if (t instanceof RefType && !t.equals(RefType.v("java.lang.Object")) && !t.equals(RefType.v("java.io.Serializable")) && !t.equals(RefType.v("java.lang.Cloneable"))) {
                  SootClass sc = ((RefType)t).getSootClass();
                  if (sc.isInterface()) {
                     this.makeMaskOfInterface(sc);
                  }

                  if (!visitedTypes.get(t.getNumber()) && !((RefType)t).getSootClass().isPhantom()) {
                     this.makeClassTypeMask(((RefType)t).getSootClass());
                  }
               } else {
                  BitVector mask = new BitVector(allocNodes.size());
                  Iterator var7 = allocNodes.iterator();

                  while(var7.hasNext()) {
                     Node n = (Node)var7.next();
                     if (this.castNeverFails(n.getType(), t)) {
                        mask.set(n.getNumber());
                     }
                  }

                  this.typeMask.put(t, mask);
               }
            }
         }
      }
   }

   public final boolean castNeverFails(Type src, Type dst) {
      if (this.fh == null) {
         return true;
      } else if (dst == null) {
         return true;
      } else if (dst == src) {
         return true;
      } else if (src == null) {
         return false;
      } else if (dst.equals(src)) {
         return true;
      } else if (src instanceof NullType) {
         return true;
      } else if (src instanceof AnySubType) {
         return true;
      } else if (dst instanceof NullType) {
         return false;
      } else if (dst instanceof AnySubType) {
         throw new RuntimeException("oops src=" + src + " dst=" + dst);
      } else {
         return this.fh.canStoreType(src, dst);
      }
   }

   public void setFastHierarchy(FastHierarchy fh) {
      this.fh = fh;
   }

   public FastHierarchy getFastHierarchy() {
      return this.fh;
   }

   private void initClass2allocs() {
      Iterator var1 = this.pag.getAllocNodeNumberer().iterator();

      while(var1.hasNext()) {
         AllocNode an = (AllocNode)var1.next();
         this.addAllocNode(an);
      }

   }

   private final void addAllocNode(final AllocNode alloc) {
      alloc.getType().apply(new TypeSwitch() {
         public final void caseRefType(RefType t) {
            SootClass cl = t.getSootClass();
            Object list;
            if ((list = (List)TypeManager.this.class2allocs.get(cl)) == null) {
               list = new LinkedList();
               TypeManager.this.class2allocs.put(cl, list);
            }

            ((List)list).add(alloc);
         }

         public final void caseAnySubType(AnySubType t) {
            TypeManager.this.anySubtypeAllocs.add(alloc);
         }
      });
   }

   private final BitVector makeClassTypeMask(SootClass clazz) {
      BitVector cachedMask = (BitVector)this.typeMask.get(clazz.getType());
      if (cachedMask != null) {
         return cachedMask;
      } else {
         int nBits = this.pag.getAllocNodeNumberer().size();
         BitVector mask = new BitVector(nBits);
         List<AllocNode> allocs = null;
         if (clazz.isConcrete()) {
            allocs = (List)this.class2allocs.get(clazz);
         }

         if (allocs != null) {
            Iterator var5 = allocs.iterator();

            while(var5.hasNext()) {
               AllocNode an = (AllocNode)var5.next();
               mask.set(an.getNumber());
            }
         }

         Collection<SootClass> subclasses = this.fh.getSubclassesOf(clazz);
         Iterator var10;
         if (subclasses == Collections.EMPTY_LIST) {
            var10 = this.anySubtypeAllocs.iterator();

            while(var10.hasNext()) {
               AllocNode an = (AllocNode)var10.next();
               mask.set(an.getNumber());
            }

            this.typeMask.put(clazz.getType(), mask);
            return mask;
         } else {
            var10 = subclasses.iterator();

            while(var10.hasNext()) {
               SootClass subcl = (SootClass)var10.next();
               mask.or(this.makeClassTypeMask(subcl));
            }

            this.typeMask.put(clazz.getType(), mask);
            return mask;
         }
      }
   }

   private final BitVector makeMaskOfInterface(SootClass interf) {
      if (!interf.isInterface()) {
         throw new RuntimeException();
      } else {
         BitVector ret = new BitVector(this.pag.getAllocNodeNumberer().size());
         this.typeMask.put(interf.getType(), ret);
         Collection<SootClass> implementers = this.fh.getAllImplementersOfInterface(interf);

         Iterator var4;
         BitVector other;
         for(var4 = implementers.iterator(); var4.hasNext(); ret.or(other)) {
            SootClass impl = (SootClass)var4.next();
            other = (BitVector)this.typeMask.get(impl.getType());
            if (other == null) {
               other = this.makeClassTypeMask(impl);
            }
         }

         if (implementers.size() == 0) {
            var4 = this.anySubtypeAllocs.iterator();

            while(var4.hasNext()) {
               AllocNode an = (AllocNode)var4.next();
               ret.set(an.getNumber());
            }
         }

         return ret;
      }
   }
}
