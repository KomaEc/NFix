package soot.jbco.bafTransformations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.IntType;
import soot.Local;
import soot.LongType;
import soot.PatchingChain;
import soot.PrimType;
import soot.ShortType;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.baf.Baf;
import soot.baf.IdentityInst;
import soot.baf.IncInst;
import soot.baf.LoadInst;
import soot.baf.StoreInst;
import soot.jbco.IJbcoTransform;
import soot.jbco.Main;
import soot.jbco.util.Rand;
import soot.jimple.IntConstant;
import soot.jimple.LongConstant;
import soot.jimple.ParameterRef;
import soot.util.Chain;

public class LocalsToBitField extends BodyTransformer implements IJbcoTransform {
   int replaced = 0;
   int locals = 0;
   public static String[] dependancies = new String[]{"jtp.jbco_jl", "bb.jbco_plvb", "bb.jbco_ful", "bb.lp"};
   public static String name = "bb.jbco_plvb";

   public String[] getDependencies() {
      return dependancies;
   }

   public String getName() {
      return name;
   }

   public void outputSummary() {
      out.println("Local fields inserted into bitfield: " + this.replaced);
      out.println("Original number of locals: " + this.locals);
   }

   protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
      int weight = Main.getWeight(phaseName, b.getMethod().getSignature());
      if (weight != 0) {
         Chain<Local> bLocals = b.getLocals();
         PatchingChain<Unit> u = b.getUnits();
         Unit first = null;
         List<Value> params = new ArrayList();
         Iterator uit = u.iterator();

         while(uit.hasNext()) {
            Unit unit = (Unit)uit.next();
            if (unit instanceof IdentityInst) {
               IdentityInst ii = (IdentityInst)unit;
               if (ii.getRightOpBox().getValue() instanceof ParameterRef) {
                  Value v = ii.getLeftOp();
                  if (v instanceof Local) {
                     params.add(v);
                     first = unit;
                  }
               }
            }
         }

         Map<Local, Local> bafToJLocals = new HashMap();
         Iterator jlocIt = ((List)Main.methods2JLocals.get(b.getMethod())).iterator();

         while(true) {
            while(jlocIt.hasNext()) {
               Local jl = (Local)jlocIt.next();
               Iterator blocIt = bLocals.iterator();

               while(blocIt.hasNext()) {
                  Local bl = (Local)blocIt.next();
                  if (bl.getName().equals(jl.getName())) {
                     bafToJLocals.put(bl, jl);
                     break;
                  }
               }
            }

            List<Local> booleans = new ArrayList();
            List<Local> bytes = new ArrayList();
            List<Local> chars = new ArrayList();
            List<Local> ints = new ArrayList();
            Map<Local, Integer> sizes = new HashMap();
            Iterator blocs = bLocals.iterator();

            while(blocs.hasNext()) {
               Local bl = (Local)blocs.next();
               if (!params.contains(bl)) {
                  ++this.locals;
                  Local jlocal = (Local)bafToJLocals.get(bl);
                  if (jlocal != null) {
                     Type t = jlocal.getType();
                     if (t instanceof PrimType && !(t instanceof DoubleType) && !(t instanceof LongType) && Rand.getInt(10) <= weight) {
                        if (t instanceof BooleanType) {
                           booleans.add(bl);
                           sizes.put(bl, 1);
                        } else if (t instanceof ByteType) {
                           bytes.add(bl);
                           sizes.put(bl, 8);
                        } else if (t instanceof CharType) {
                           chars.add(bl);
                           sizes.put(bl, 16);
                        } else if (t instanceof IntType) {
                           ints.add(bl);
                           sizes.put(bl, 32);
                        }
                     }
                  }
               }
            }

            int count = 0;
            Map<Local, Local> bafToNewLocs = new HashMap();
            int total = booleans.size() + bytes.size() * 8 + chars.size() * 16 + ints.size() * 32;

            HashMap newLocs;
            int index;
            for(newLocs = new HashMap(); total >= 32 && booleans.size() + bytes.size() + chars.size() + ints.size() > 2; total = booleans.size() + bytes.size() * 8 + chars.size() * 16 + ints.size() * 32) {
               Local nloc = Baf.v().newLocal("newDumby" + count++, LongType.v());
               Map<Local, Integer> nlocMap = new HashMap();
               boolean done = false;
               int index = 0;

               while(index < 64 && !done) {
                  int max = 64 - index;
                  max = max > 31 ? 4 : (max > 15 ? 3 : (max > 7 ? 2 : 1));
                  index = Rand.getInt(max);
                  Local l;
                  switch(index) {
                  case 3:
                     if (ints.size() > 0) {
                        l = (Local)ints.remove(Rand.getInt(ints.size()));
                        nlocMap.put(l, index);
                        index += 32;
                        bafToNewLocs.put(l, nloc);
                        index = this.getNewIndex(index, ints, chars, bytes, booleans);
                        break;
                     }
                  case 2:
                     if (chars.size() > 0) {
                        l = (Local)chars.remove(Rand.getInt(chars.size()));
                        nlocMap.put(l, index);
                        index += 16;
                        bafToNewLocs.put(l, nloc);
                        index = this.getNewIndex(index, ints, chars, bytes, booleans);
                        break;
                     }
                  case 1:
                     if (bytes.size() > 0) {
                        l = (Local)bytes.remove(Rand.getInt(bytes.size()));
                        nlocMap.put(l, index);
                        index += 8;
                        bafToNewLocs.put(l, nloc);
                        index = this.getNewIndex(index, ints, chars, bytes, booleans);
                        break;
                     }
                  case 0:
                     if (booleans.size() > 0) {
                        l = (Local)booleans.remove(Rand.getInt(booleans.size()));
                        nlocMap.put(l, index++);
                        bafToNewLocs.put(l, nloc);
                        index = this.getNewIndex(index, ints, chars, bytes, booleans);
                     }
                  }

                  if (index == index) {
                     done = true;
                  }
               }

               newLocs.put(nloc, nlocMap);
               bLocals.add(nloc);
               if (first != null) {
                  u.insertAfter((Unit)Baf.v().newStoreInst(LongType.v(), nloc), (Unit)first);
                  u.insertAfter((Unit)Baf.v().newPushInst(LongConstant.v(0L)), (Unit)first);
               } else {
                  u.addFirst((Unit)Baf.v().newStoreInst(LongType.v(), nloc));
                  u.addFirst((Unit)Baf.v().newPushInst(LongConstant.v(0L)));
               }
            }

            if (bafToNewLocs.size() == 0) {
               return;
            }

            Iterator it = u.snapshotIterator();

            while(it.hasNext()) {
               Unit unit = (Unit)it.next();
               int size;
               long longmask;
               Local nloc;
               Local bafLoc;
               int index;
               if (unit instanceof StoreInst) {
                  StoreInst si = (StoreInst)unit;
                  bafLoc = si.getLocal();
                  nloc = (Local)bafToNewLocs.get(bafLoc);
                  if (nloc != null) {
                     Local jloc = (Local)bafToJLocals.get(bafLoc);
                     index = (Integer)((Map)newLocs.get(nloc)).get(bafLoc);
                     size = (Integer)sizes.get(bafLoc);
                     longmask = ~((size == 1 ? 1L : (size == 8 ? 255L : (size == 16 ? 65535L : 4294967295L))) << index);
                     u.insertBefore((Unit)Baf.v().newPrimitiveCastInst(jloc.getType(), LongType.v()), (Unit)unit);
                     if (index > 0) {
                        u.insertBefore((Unit)Baf.v().newPushInst(IntConstant.v(index)), (Unit)unit);
                        u.insertBefore((Unit)Baf.v().newShlInst(LongType.v()), (Unit)unit);
                     }

                     u.insertBefore((Unit)Baf.v().newPushInst(LongConstant.v(~longmask)), (Unit)unit);
                     u.insertBefore((Unit)Baf.v().newAndInst(LongType.v()), (Unit)unit);
                     u.insertBefore((Unit)Baf.v().newLoadInst(LongType.v(), nloc), (Unit)unit);
                     u.insertBefore((Unit)Baf.v().newPushInst(LongConstant.v(longmask)), (Unit)unit);
                     u.insertBefore((Unit)Baf.v().newAndInst(LongType.v()), (Unit)unit);
                     u.insertBefore((Unit)Baf.v().newXorInst(LongType.v()), (Unit)unit);
                     u.insertBefore((Unit)Baf.v().newStoreInst(LongType.v(), nloc), (Unit)unit);
                     u.remove(unit);
                  }
               } else if (unit instanceof LoadInst) {
                  LoadInst li = (LoadInst)unit;
                  bafLoc = li.getLocal();
                  nloc = (Local)bafToNewLocs.get(bafLoc);
                  if (nloc != null) {
                     index = (Integer)((Map)newLocs.get(nloc)).get(bafLoc);
                     index = (Integer)sizes.get(bafLoc);
                     long longmask = (index == 1 ? 1L : (index == 8 ? 255L : (index == 16 ? 65535L : 4294967295L))) << index;
                     u.insertBefore((Unit)Baf.v().newLoadInst(LongType.v(), nloc), (Unit)unit);
                     u.insertBefore((Unit)Baf.v().newPushInst(LongConstant.v(longmask)), (Unit)unit);
                     u.insertBefore((Unit)Baf.v().newAndInst(LongType.v()), (Unit)unit);
                     if (index > 0) {
                        u.insertBefore((Unit)Baf.v().newPushInst(IntConstant.v(index)), (Unit)unit);
                        u.insertBefore((Unit)Baf.v().newShrInst(LongType.v()), (Unit)unit);
                     }

                     Type origType = ((Local)bafToJLocals.get(bafLoc)).getType();
                     Type t = this.getType(origType);
                     u.insertBefore((Unit)Baf.v().newPrimitiveCastInst(LongType.v(), t), (Unit)unit);
                     if (!(origType instanceof IntType) && !(origType instanceof BooleanType)) {
                        u.insertBefore((Unit)Baf.v().newPrimitiveCastInst(t, origType), (Unit)unit);
                     }

                     u.remove(unit);
                  }
               } else if (unit instanceof IncInst) {
                  IncInst ii = (IncInst)unit;
                  bafLoc = ii.getLocal();
                  nloc = (Local)bafToNewLocs.get(bafLoc);
                  if (nloc != null) {
                     Type jlocType = this.getType(((Local)bafToJLocals.get(bafLoc)).getType());
                     index = (Integer)((Map)newLocs.get(nloc)).get(bafLoc);
                     size = (Integer)sizes.get(bafLoc);
                     longmask = (size == 1 ? 1L : (size == 8 ? 255L : (size == 16 ? 65535L : 4294967295L))) << index;
                     u.insertBefore((Unit)Baf.v().newPushInst(ii.getConstant()), (Unit)unit);
                     u.insertBefore((Unit)Baf.v().newLoadInst(LongType.v(), nloc), (Unit)unit);
                     u.insertBefore((Unit)Baf.v().newPushInst(LongConstant.v(longmask)), (Unit)unit);
                     u.insertBefore((Unit)Baf.v().newAndInst(LongType.v()), (Unit)unit);
                     if (index > 0) {
                        u.insertBefore((Unit)Baf.v().newPushInst(IntConstant.v(index)), (Unit)unit);
                        u.insertBefore((Unit)Baf.v().newShrInst(LongType.v()), (Unit)unit);
                     }

                     u.insertBefore((Unit)Baf.v().newPrimitiveCastInst(LongType.v(), ii.getConstant().getType()), (Unit)unit);
                     u.insertBefore((Unit)Baf.v().newAddInst(ii.getConstant().getType()), (Unit)unit);
                     u.insertBefore((Unit)Baf.v().newPrimitiveCastInst(jlocType, LongType.v()), (Unit)unit);
                     if (index > 0) {
                        u.insertBefore((Unit)Baf.v().newPushInst(IntConstant.v(index)), (Unit)unit);
                        u.insertBefore((Unit)Baf.v().newShlInst(LongType.v()), (Unit)unit);
                     }

                     longmask = ~longmask;
                     u.insertBefore((Unit)Baf.v().newLoadInst(LongType.v(), nloc), (Unit)unit);
                     u.insertBefore((Unit)Baf.v().newPushInst(LongConstant.v(longmask)), (Unit)unit);
                     u.insertBefore((Unit)Baf.v().newAndInst(LongType.v()), (Unit)unit);
                     u.insertBefore((Unit)Baf.v().newXorInst(LongType.v()), (Unit)unit);
                     u.insertBefore((Unit)Baf.v().newStoreInst(LongType.v(), nloc), (Unit)unit);
                     u.remove(unit);
                  }
               }
            }

            Iterator localIterator = bLocals.snapshotIterator();

            while(localIterator.hasNext()) {
               Local l = (Local)localIterator.next();
               if (bafToNewLocs.containsKey(l)) {
                  bLocals.remove(l);
                  ++this.replaced;
               }
            }

            return;
         }
      }
   }

   private Type getType(Type t) {
      return (Type)(!(t instanceof BooleanType) && !(t instanceof CharType) && !(t instanceof ShortType) && !(t instanceof ByteType) ? t : IntType.v());
   }

   private int getNewIndex(int index, List<Local> ints, List<Local> chars, List<Local> bytes, List<Local> booleans) {
      int max = 0;
      if (booleans.size() > 0 && index < 63) {
         max = 64;
      } else if (bytes.size() > 0 && index < 56) {
         max = 57;
      } else if (chars.size() > 0 && index < 48) {
         max = 49;
      } else if (ints.size() > 0 && index < 32) {
         max = 33;
      }

      if (max != 0) {
         int rand = Rand.getInt(4);
         int max = max - index;
         if (max > rand) {
            max = rand;
         } else if (max != 1) {
            max = Rand.getInt(max);
         }

         index += max;
      }

      return index;
   }
}
