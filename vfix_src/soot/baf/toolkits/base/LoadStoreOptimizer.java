package soot.baf.toolkits.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Local;
import soot.PhaseOptions;
import soot.Singletons;
import soot.Trap;
import soot.Unit;
import soot.baf.AddInst;
import soot.baf.AndInst;
import soot.baf.ArrayReadInst;
import soot.baf.ArrayWriteInst;
import soot.baf.Baf;
import soot.baf.Dup1Inst;
import soot.baf.DupInst;
import soot.baf.EnterMonitorInst;
import soot.baf.ExitMonitorInst;
import soot.baf.FieldArgInst;
import soot.baf.FieldGetInst;
import soot.baf.FieldPutInst;
import soot.baf.IdentityInst;
import soot.baf.IncInst;
import soot.baf.Inst;
import soot.baf.LoadInst;
import soot.baf.MethodArgInst;
import soot.baf.MulInst;
import soot.baf.NewInst;
import soot.baf.OrInst;
import soot.baf.PushInst;
import soot.baf.StaticGetInst;
import soot.baf.StaticPutInst;
import soot.baf.StoreInst;
import soot.baf.XorInst;
import soot.options.Options;
import soot.toolkits.graph.Block;
import soot.toolkits.graph.BlockGraph;
import soot.toolkits.graph.ZonedBlockGraph;
import soot.toolkits.scalar.LocalDefs;
import soot.toolkits.scalar.LocalUses;
import soot.toolkits.scalar.UnitValueBoxPair;
import soot.util.Chain;

public class LoadStoreOptimizer extends BodyTransformer {
   private static final Logger logger = LoggerFactory.getLogger(LoadStoreOptimizer.class);
   boolean debug = false;
   private static final int FAILURE = 0;
   private static final int SUCCESS = 1;
   private static final int MAKE_DUP = 2;
   private static final int MAKE_DUP1_X1 = 3;
   private static final int SPECIAL_SUCCESS = 4;
   private static final int HAS_CHANGED = 5;
   private static final int SPECIAL_SUCCESS2 = 6;
   private static final int STORE_LOAD_ELIMINATION = 0;
   private static final int STORE_LOAD_LOAD_ELIMINATION = -1;
   private Map<String, String> gOptions;

   public LoadStoreOptimizer(Singletons.Global g) {
   }

   public static LoadStoreOptimizer v() {
      return G.v().soot_baf_toolkits_base_LoadStoreOptimizer();
   }

   protected void internalTransform(Body body, String phaseName, Map<String, String> options) {
      this.gOptions = options;
      LoadStoreOptimizer.Instance instance = new LoadStoreOptimizer.Instance();
      instance.mBody = body;
      instance.mUnits = body.getUnits();
      this.debug = PhaseOptions.getBoolean(this.gOptions, "debug");
      if (Options.v().verbose()) {
         logger.debug("[" + body.getMethod().getName() + "] Performing LoadStore optimizations...");
      }

      if (this.debug) {
         logger.debug("\n\nOptimizing Method: " + body.getMethod().getName());
      }

      instance.go();
   }

   class Instance {
      private Chain<Unit> mUnits;
      private Body mBody;
      private LocalDefs mLocalDefs;
      private LocalUses mLocalUses;
      private Map<Unit, Block> mUnitToBlockMap;
      private boolean mPass2 = false;

      void go() {
         if (!this.mUnits.isEmpty()) {
            this.buildUnitToBlockMap();
            this.computeLocalDefsAndLocalUsesInfo();
            if (LoadStoreOptimizer.this.debug) {
               LoadStoreOptimizer.logger.debug("Calling optimizeLoadStore(1)\n");
            }

            this.optimizeLoadStores();
            if (PhaseOptions.getBoolean(LoadStoreOptimizer.this.gOptions, "inter")) {
               if (LoadStoreOptimizer.this.debug) {
                  LoadStoreOptimizer.logger.debug("Calling doInterBlockOptimizations");
               }

               this.doInterBlockOptimizations();
            }

            if (PhaseOptions.getBoolean(LoadStoreOptimizer.this.gOptions, "sl2") || PhaseOptions.getBoolean(LoadStoreOptimizer.this.gOptions, "sll2")) {
               this.mPass2 = true;
               if (LoadStoreOptimizer.this.debug) {
                  LoadStoreOptimizer.logger.debug("Calling optimizeLoadStore(2)");
               }

               this.optimizeLoadStores();
            }
         }

      }

      private void buildUnitToBlockMap() {
         BlockGraph blockGraph = new ZonedBlockGraph(this.mBody);
         if (LoadStoreOptimizer.this.debug) {
            LoadStoreOptimizer.logger.debug("Method " + this.mBody.getMethod().getName() + " Block Graph: ");
            LoadStoreOptimizer.logger.debug("" + blockGraph);
         }

         List<Block> blocks = blockGraph.getBlocks();
         this.mUnitToBlockMap = new HashMap();
         Iterator blockIt = blocks.iterator();

         while(blockIt.hasNext()) {
            Block block = (Block)blockIt.next();
            Iterator unitIt = block.iterator();

            while(unitIt.hasNext()) {
               Unit unit = (Unit)unitIt.next();
               this.mUnitToBlockMap.put(unit, block);
            }
         }

      }

      private List<Unit> buildStoreList() {
         List<Unit> storeList = new ArrayList();
         Iterator var2 = this.mUnits.iterator();

         while(var2.hasNext()) {
            Unit unit = (Unit)var2.next();
            if (unit instanceof StoreInst) {
               storeList.add(unit);
            }
         }

         return storeList;
      }

      private void computeLocalDefsAndLocalUsesInfo() {
         this.mLocalDefs = LocalDefs.Factory.newLocalDefs(this.mBody);
         this.mLocalUses = LocalUses.Factory.newLocalUses(this.mBody, this.mLocalDefs);
      }

      private void optimizeLoadStores() {
         List<Unit> storeList = this.buildStoreList();
         boolean hasChanged = true;
         boolean hasChangedFlag = false;

         label121:
         while(hasChanged) {
            hasChanged = false;
            Iterator unitIt = storeList.iterator();

            while(true) {
               while(true) {
                  Unit unit;
                  List uses;
                  Unit secondLoad;
                  Block block;
                  Unit firstLoad;
                  do {
                     label101:
                     do {
                        while(true) {
                           while(true) {
                              label73:
                              while(true) {
                                 do {
                                    if (!unitIt.hasNext()) {
                                       continue label121;
                                    }

                                    unit = (Unit)unitIt.next();
                                    uses = this.mLocalUses.getUsesOf(unit);
                                 } while(uses.size() >= 3);

                                 Iterator useItx = uses.iterator();

                                 while(useItx.hasNext()) {
                                    UnitValueBoxPair pairx = (UnitValueBoxPair)useItx.next();
                                    secondLoad = pairx.getUnit();
                                    if (!(secondLoad instanceof LoadInst)) {
                                       continue label73;
                                    }

                                    List<Unit> defs = this.mLocalDefs.getDefsOfAt((Local)pairx.getValueBox().getValue(), secondLoad);
                                    if (defs.size() > 1 || defs.get(0) != unit) {
                                       continue label73;
                                    }
                                 }

                                 block = (Block)this.mUnitToBlockMap.get(unit);
                                 Iterator useIt = uses.iterator();

                                 while(useIt.hasNext()) {
                                    UnitValueBoxPair pair = (UnitValueBoxPair)useIt.next();
                                    Block useBlock = (Block)this.mUnitToBlockMap.get(pair.getUnit());
                                    if (useBlock != block) {
                                       continue label73;
                                    }
                                 }

                                 switch(uses.size()) {
                                 case 0:
                                 default:
                                    break;
                                 case 1:
                                    if (!PhaseOptions.getBoolean(LoadStoreOptimizer.this.gOptions, "sl") || this.mPass2 && !PhaseOptions.getBoolean(LoadStoreOptimizer.this.gOptions, "sl2")) {
                                       break;
                                    }

                                    firstLoad = ((UnitValueBoxPair)uses.get(0)).getUnit();
                                    block = (Block)this.mUnitToBlockMap.get(unit);
                                    int test = this.stackIndependent(unit, firstLoad, block, 0);
                                    if (test != 1 && test != 4) {
                                       break;
                                    }

                                    block.remove(unit);
                                    block.remove(firstLoad);
                                    unitIt.remove();
                                    hasChanged = true;
                                    hasChangedFlag = false;
                                    if (LoadStoreOptimizer.this.debug) {
                                       LoadStoreOptimizer.logger.debug("Store/Load elimination occurred case1.");
                                    }
                                    break;
                                 case 2:
                                    continue label101;
                                 }
                              }
                           }
                        }
                     } while(!PhaseOptions.getBoolean(LoadStoreOptimizer.this.gOptions, "sll"));
                  } while(this.mPass2 && !PhaseOptions.getBoolean(LoadStoreOptimizer.this.gOptions, "sll2"));

                  firstLoad = ((UnitValueBoxPair)uses.get(0)).getUnit();
                  secondLoad = ((UnitValueBoxPair)uses.get(1)).getUnit();
                  block = (Block)this.mUnitToBlockMap.get(unit);
                  if (this.mUnits.follows(firstLoad, secondLoad)) {
                     Unit temp = secondLoad;
                     secondLoad = firstLoad;
                     firstLoad = temp;
                  }

                  int result = this.stackIndependent(unit, firstLoad, block, 0);
                  if (result == 1) {
                     block.remove(firstLoad);
                     block.insertAfter(firstLoad, unit);
                     int res = this.stackIndependent(unit, secondLoad, block, -1);
                     if (res == 2) {
                        Dup1Inst dup = Baf.v().newDup1Inst(((LoadInst)secondLoad).getOpType());
                        dup.addAllTagsOf(unit);
                        this.replaceUnit(unit, dup);
                        unitIt.remove();
                        block.remove(firstLoad);
                        block.remove(secondLoad);
                        hasChanged = true;
                        hasChangedFlag = false;
                     }
                  } else if ((result == 4 || result == 5 || result == 6) && !hasChangedFlag) {
                     hasChangedFlag = true;
                     hasChanged = true;
                  }
               }
            }
         }

      }

      private boolean isRequiredByFollowingUnits(Unit from, Unit to) {
         Iterator<Unit> it = this.mUnits.iterator(from, to);
         int stackHeight = 0;
         boolean res = false;
         if (from != to) {
            it.next();

            while(it.hasNext()) {
               Unit currentInst = (Unit)it.next();
               if (currentInst == to) {
                  break;
               }

               stackHeight -= ((Inst)currentInst).getInCount();
               if (stackHeight < 0) {
                  res = true;
                  break;
               }

               stackHeight += ((Inst)currentInst).getOutCount();
            }
         }

         return res;
      }

      private int pushStoreToLoad(Unit from, Unit to, Block block) {
         Unit storePred = block.getPredOf(from);
         if (storePred != null && ((Inst)storePred).getOutCount() == 1) {
            Set<Unit> unitsToMove = new HashSet();
            unitsToMove.add(storePred);
            unitsToMove.add(from);
            int h = ((Inst)storePred).getInCount();
            Unit currentUnit = storePred;
            if (h != 0) {
               for(currentUnit = block.getPredOf(storePred); currentUnit != null; currentUnit = block.getPredOf(currentUnit)) {
                  h -= ((Inst)currentUnit).getOutCount();
                  if (h < 0) {
                     if (LoadStoreOptimizer.this.debug) {
                        LoadStoreOptimizer.logger.debug("xxx: negative");
                     }

                     return 0;
                  }

                  h += ((Inst)currentUnit).getInCount();
                  unitsToMove.add(currentUnit);
                  if (h == 0) {
                     break;
                  }
               }
            }

            if (currentUnit == null) {
               if (LoadStoreOptimizer.this.debug) {
                  LoadStoreOptimizer.logger.debug("xxx: null");
               }

               return 0;
            } else {
               Unit uu = from;

               while(true) {
                  uu = block.getSuccOf(uu);
                  Unit nu;
                  if (uu == to) {
                     for(Unit unitToMove = currentUnit; unitToMove != from; unitToMove = nu) {
                        nu = block.getSuccOf(unitToMove);
                        if (LoadStoreOptimizer.this.debug) {
                           LoadStoreOptimizer.logger.debug("moving " + unitToMove);
                        }

                        block.remove(unitToMove);
                        block.insertBefore(unitToMove, to);
                     }

                     block.remove(from);
                     block.insertBefore(from, to);
                     if (LoadStoreOptimizer.this.debug) {
                        LoadStoreOptimizer.logger.debug("xxx1success pushing forward stuff.");
                     }

                     return 4;
                  }

                  Iterator it2 = unitsToMove.iterator();

                  while(it2.hasNext()) {
                     nu = (Unit)it2.next();
                     if (LoadStoreOptimizer.this.debug) {
                        LoadStoreOptimizer.logger.debug("xxxspecial;success pushing forward stuff.");
                     }

                     if (!this.canMoveUnitOver(nu, uu)) {
                        if (LoadStoreOptimizer.this.debug) {
                           LoadStoreOptimizer.logger.debug("xxx: cant move over faillure" + nu);
                        }

                        return 0;
                     }

                     if (LoadStoreOptimizer.this.debug) {
                        LoadStoreOptimizer.logger.debug("can move" + nu + " over " + uu);
                     }
                  }
               }
            }
         } else {
            return 0;
         }
      }

      private int stackIndependent(Unit from, Unit to, Block block, int aContext) {
         if (LoadStoreOptimizer.this.debug) {
            LoadStoreOptimizer.logger.debug("Trying: " + from + "/" + to + " in block  " + block.getIndexInMethod() + ":");
            LoadStoreOptimizer.logger.debug("context:" + (aContext == 0 ? "STORE_LOAD_ELIMINATION" : "STORE_LOAD_LOAD_ELIMINATION"));
         }

         int minStackHeightAttained = 0;
         int stackHeight = 0;
         Iterator<Unit> it = this.mUnits.iterator(this.mUnits.getSuccOf(from));
         int res = 0;
         Unit currentInst = (Unit)it.next();
         if (aContext == -1) {
            currentInst = (Unit)it.next();
         }

         while(currentInst != to && it.hasNext()) {
            stackHeight -= ((Inst)currentInst).getInCount();
            if (stackHeight < minStackHeightAttained) {
               minStackHeightAttained = stackHeight;
            }

            stackHeight += ((Inst)currentInst).getOutCount();
            currentInst = (Unit)it.next();
         }

         if (LoadStoreOptimizer.this.debug) {
            LoadStoreOptimizer.logger.debug("nshv = " + stackHeight);
            LoadStoreOptimizer.logger.debug("mshv = " + minStackHeightAttained);
         }

         boolean hasChanged = true;

         while(true) {
            while(hasChanged) {
               hasChanged = false;
               if (aContext == -1) {
                  if (stackHeight == 0 && minStackHeightAttained == 0) {
                     if (LoadStoreOptimizer.this.debug) {
                        LoadStoreOptimizer.logger.debug("xxx: succ: -1, makedup ");
                     }

                     return 2;
                  }

                  if (stackHeight == -1 && minStackHeightAttained == -1) {
                     if (LoadStoreOptimizer.this.debug) {
                        LoadStoreOptimizer.logger.debug("xxx: succ: -1, makedup , -1");
                     }

                     return 2;
                  }

                  if (stackHeight == -2 && minStackHeightAttained == -2) {
                     if (LoadStoreOptimizer.this.debug) {
                        LoadStoreOptimizer.logger.debug("xxx: succ -1 , make dupx1 ");
                     }

                     return 3;
                  }

                  if (minStackHeightAttained < -2) {
                     if (LoadStoreOptimizer.this.debug) {
                        LoadStoreOptimizer.logger.debug("xxx: failled due: minStackHeightAttained < -2 ");
                     }

                     return 0;
                  }
               }

               if (aContext == 0) {
                  if (stackHeight == 0 && minStackHeightAttained == 0) {
                     if (LoadStoreOptimizer.this.debug) {
                        LoadStoreOptimizer.logger.debug("xxx: success due: 0, SUCCESS ");
                     }

                     return 1;
                  }

                  if (minStackHeightAttained < 0) {
                     return this.pushStoreToLoad(from, to, block);
                  }
               }

               it = this.mUnits.iterator(this.mUnits.getSuccOf(from), to);
               Unit unitToMove = null;
               Unit u = (Unit)it.next();
               if (aContext == -1) {
                  u = (Unit)it.next();
               }

               for(int currentH = 0; u != to; u = (Unit)it.next()) {
                  if (((Inst)u).getNetCount() == 1) {
                     if (!(u instanceof LoadInst) && !(u instanceof PushInst) && !(u instanceof NewInst) && !(u instanceof StaticGetInst) && !(u instanceof Dup1Inst)) {
                        if (LoadStoreOptimizer.this.debug) {
                           LoadStoreOptimizer.logger.debug("1003:(LoadStoreOptimizer@stackIndependent): found unknown unit w/ getNetCount == 1" + u);
                        }
                     } else if (!this.isRequiredByFollowingUnits(u, to)) {
                        unitToMove = u;
                     }
                  }

                  if (unitToMove != null) {
                     int flag = 0;
                     if (this.tryToMoveUnit(unitToMove, block, from, to, flag)) {
                        if (stackHeight > -2 && minStackHeightAttained == -2) {
                           return 5;
                        }

                        --stackHeight;
                        if (stackHeight < minStackHeightAttained) {
                           minStackHeightAttained = stackHeight;
                        }

                        hasChanged = true;
                        break;
                     }
                  }

                  currentH += ((Inst)u).getNetCount();
                  unitToMove = null;
               }
            }

            if (this.isCommutativeBinOp(block.getSuccOf(to))) {
               if (aContext == 0 && stackHeight == 1 && minStackHeightAttained == 0) {
                  if (LoadStoreOptimizer.this.debug) {
                     LoadStoreOptimizer.logger.debug("xxx: commutative ");
                  }

                  return 4;
               }

               if (((Inst)to).getOutCount() == 1 && ((Inst)to).getInCount() == 0 && ((Inst)this.mUnits.getPredOf(to)).getOutCount() == 1 && ((Inst)this.mUnits.getPredOf(to)).getInCount() == 0) {
                  Object toPred = this.mUnits.getPredOf(to);
                  block.remove((Unit)toPred);
                  block.insertAfter((Unit)toPred, to);
                  return 5;
               }

               return 0;
            }

            if (aContext == 0) {
               return this.pushStoreToLoad(from, to, block);
            }

            return res;
         }
      }

      private boolean isNonLocalReadOrWrite(Unit aUnit) {
         return aUnit instanceof FieldArgInst || aUnit instanceof ArrayReadInst || aUnit instanceof ArrayWriteInst;
      }

      private boolean canMoveUnitOver(Unit aUnitToMove, Unit aUnitToGoOver) {
         if (aUnitToGoOver instanceof MethodArgInst && aUnitToMove instanceof MethodArgInst || aUnitToGoOver instanceof MethodArgInst && this.isNonLocalReadOrWrite(aUnitToMove) || this.isNonLocalReadOrWrite(aUnitToGoOver) && aUnitToMove instanceof MethodArgInst || aUnitToGoOver instanceof ArrayReadInst && aUnitToMove instanceof ArrayWriteInst || aUnitToGoOver instanceof ArrayWriteInst && aUnitToMove instanceof ArrayReadInst || aUnitToGoOver instanceof ArrayWriteInst && aUnitToMove instanceof ArrayWriteInst || aUnitToGoOver instanceof FieldPutInst && aUnitToMove instanceof FieldGetInst && ((FieldArgInst)aUnitToGoOver).getField() == ((FieldArgInst)aUnitToMove).getField() || aUnitToGoOver instanceof FieldGetInst && aUnitToMove instanceof FieldPutInst && ((FieldArgInst)aUnitToGoOver).getField() == ((FieldArgInst)aUnitToMove).getField() || aUnitToGoOver instanceof FieldPutInst && aUnitToMove instanceof FieldPutInst && ((FieldArgInst)aUnitToGoOver).getField() == ((FieldArgInst)aUnitToMove).getField() || aUnitToGoOver instanceof StaticPutInst && aUnitToMove instanceof StaticGetInst && ((FieldArgInst)aUnitToGoOver).getField() == ((FieldArgInst)aUnitToMove).getField() || aUnitToGoOver instanceof StaticGetInst && aUnitToMove instanceof StaticPutInst && ((FieldArgInst)aUnitToGoOver).getField() == ((FieldArgInst)aUnitToMove).getField() || aUnitToGoOver instanceof StaticPutInst && aUnitToMove instanceof StaticPutInst && ((FieldArgInst)aUnitToGoOver).getField() == ((FieldArgInst)aUnitToMove).getField()) {
            return false;
         } else if (!(aUnitToGoOver instanceof EnterMonitorInst) && !(aUnitToGoOver instanceof ExitMonitorInst)) {
            if (!(aUnitToMove instanceof EnterMonitorInst) && !(aUnitToGoOver instanceof ExitMonitorInst)) {
               if (!(aUnitToGoOver instanceof IdentityInst) && !(aUnitToMove instanceof IdentityInst)) {
                  if (aUnitToMove instanceof LoadInst) {
                     if (aUnitToGoOver instanceof StoreInst) {
                        if (((StoreInst)aUnitToGoOver).getLocal() == ((LoadInst)aUnitToMove).getLocal()) {
                           return false;
                        }
                     } else if (aUnitToGoOver instanceof IncInst && ((IncInst)aUnitToGoOver).getLocal() == ((LoadInst)aUnitToMove).getLocal()) {
                        return false;
                     }
                  }

                  if (aUnitToMove instanceof StoreInst) {
                     if (aUnitToGoOver instanceof LoadInst) {
                        if (((LoadInst)aUnitToGoOver).getLocal() == ((StoreInst)aUnitToMove).getLocal()) {
                           return false;
                        }
                     } else if (aUnitToGoOver instanceof IncInst && ((IncInst)aUnitToGoOver).getLocal() == ((StoreInst)aUnitToMove).getLocal()) {
                        return false;
                     }
                  }

                  if (aUnitToMove instanceof IncInst) {
                     if (aUnitToGoOver instanceof StoreInst) {
                        if (((StoreInst)aUnitToGoOver).getLocal() == ((IncInst)aUnitToMove).getLocal()) {
                           return false;
                        }
                     } else if (aUnitToGoOver instanceof LoadInst && ((LoadInst)aUnitToGoOver).getLocal() == ((IncInst)aUnitToMove).getLocal()) {
                        return false;
                     }
                  }

                  return true;
               } else {
                  return false;
               }
            } else {
               return false;
            }
         } else {
            return false;
         }
      }

      private boolean tryToMoveUnit(Unit unitToMove, Block block, Unit from, Unit to, int flag) {
         int h = 0;
         Unit current = unitToMove;
         boolean reachedStore = false;
         boolean reorderingOccurred = false;
         if (LoadStoreOptimizer.this.debug) {
            LoadStoreOptimizer.logger.debug("[tryToMoveUnit]: trying to move:" + unitToMove);
         }

         if (unitToMove == null) {
            return false;
         } else {
            while(current != block.getHead()) {
               current = (Unit)this.mUnits.getPredOf(current);
               if (!this.canMoveUnitOver(current, unitToMove)) {
                  return false;
               }

               if (current == from) {
                  reachedStore = true;
               }

               h -= ((Inst)current).getOutCount();
               if (h < 0) {
                  if (LoadStoreOptimizer.this.debug) {
                     LoadStoreOptimizer.logger.debug("1006:(LoadStoreOptimizer@stackIndependent): Stack went negative while trying to reorder code.");
                  }

                  if (flag == 1 && current instanceof DupInst) {
                     block.remove(unitToMove);
                     block.insertAfter(unitToMove, current);
                  }

                  return false;
               }

               h += ((Inst)current).getInCount();
               if (h == 0 && reachedStore && !this.isRequiredByFollowingUnits(unitToMove, to)) {
                  if (LoadStoreOptimizer.this.debug) {
                     LoadStoreOptimizer.logger.debug("10077:(LoadStoreOptimizer@stackIndependent): reordering bytecode move: " + unitToMove + "before: " + current);
                  }

                  block.remove(unitToMove);
                  block.insertBefore(unitToMove, current);
                  reorderingOccurred = true;
                  break;
               }
            }

            if (reorderingOccurred) {
               if (LoadStoreOptimizer.this.debug) {
                  LoadStoreOptimizer.logger.debug("reordering occured");
               }

               return true;
            } else {
               if (LoadStoreOptimizer.this.debug) {
                  LoadStoreOptimizer.logger.debug("1008:(LoadStoreOptimizer@stackIndependent):failled to find a new slot for unit to move");
               }

               return false;
            }
         }
      }

      private void replaceUnit(Unit aToReplace1, Unit aToReplace2, Unit aReplacement) {
         Block block = (Block)this.mUnitToBlockMap.get(aToReplace1);
         if (aToReplace2 != null) {
            block.insertAfter(aReplacement, aToReplace2);
            block.remove(aToReplace2);
         } else {
            block.insertAfter(aReplacement, aToReplace1);
         }

         block.remove(aToReplace1);
         this.mUnitToBlockMap.put(aReplacement, block);
      }

      private void replaceUnit(Unit aToReplace, Unit aReplacement) {
         this.replaceUnit(aToReplace, (Unit)null, aReplacement);
      }

      private boolean isExceptionHandlerBlock(Block aBlock) {
         Unit blockHead = aBlock.getHead();
         Iterator var3 = this.mBody.getTraps().iterator();

         Trap trap;
         do {
            if (!var3.hasNext()) {
               return false;
            }

            trap = (Trap)var3.next();
         } while(trap.getHandlerUnit() != blockHead);

         return true;
      }

      private int getDeltaStackHeightFromTo(Unit aFrom, Unit aTo) {
         Iterator<Unit> it = this.mUnits.iterator(aFrom, aTo);

         int h;
         for(h = 0; it.hasNext(); h += ((Inst)it.next()).getNetCount()) {
         }

         return h;
      }

      private void doInterBlockOptimizations() {
         boolean hasChanged = true;

         label163:
         while(hasChanged) {
            hasChanged = false;
            List<Unit> tempList = new ArrayList();
            tempList.addAll(this.mUnits);
            Iterator it = tempList.iterator();

            while(true) {
               Unit u;
               Block loadBlock;
               Unit def1;
               Block defBlock;
               do {
                  List uses;
                  do {
                     do {
                        do {
                           List defs;
                           do {
                              do {
                                 while(true) {
                                    do {
                                       if (!it.hasNext()) {
                                          continue label163;
                                       }

                                       u = (Unit)it.next();
                                    } while(!(u instanceof LoadInst));

                                    if (LoadStoreOptimizer.this.debug) {
                                       LoadStoreOptimizer.logger.debug("inter trying: " + u);
                                    }

                                    loadBlock = (Block)this.mUnitToBlockMap.get(u);
                                    defs = this.mLocalDefs.getDefsOfAt(((LoadInst)u).getLocal(), u);
                                    if (defs.size() == 1) {
                                       defBlock = (Block)this.mUnitToBlockMap.get(defs.get(0));
                                       break;
                                    }

                                    if (defs.size() == 2) {
                                       Unit def0 = (Unit)defs.get(0);
                                       def1 = (Unit)defs.get(1);
                                       Block defBlock0 = (Block)this.mUnitToBlockMap.get(def0);
                                       Block defBlock1 = (Block)this.mUnitToBlockMap.get(def1);
                                       if (defBlock0 != loadBlock && defBlock1 != loadBlock && defBlock0 != defBlock1 && !this.isExceptionHandlerBlock(loadBlock)) {
                                          if (this.mLocalUses.getUsesOf(def0).size() == 1 && this.mLocalUses.getUsesOf(def1).size() == 1) {
                                             List<Block> def0Succs = defBlock0.getSuccs();
                                             List<Block> def1Succs = defBlock1.getSuccs();
                                             if (def0Succs.size() == 1 && def1Succs.size() == 1) {
                                                if (def0Succs.get(0) == loadBlock && def1Succs.get(0) == loadBlock) {
                                                   if (loadBlock.getPreds().size() != 2) {
                                                      if (LoadStoreOptimizer.this.debug) {
                                                         LoadStoreOptimizer.logger.debug("failed: inter2");
                                                      }
                                                   } else if ((def0 == defBlock0.getTail() || this.getDeltaStackHeightFromTo(defBlock0.getSuccOf(def0), defBlock0.getTail()) == 0) && (def1 == defBlock1.getTail() || this.getDeltaStackHeightFromTo(defBlock1.getSuccOf(def1), defBlock1.getTail()) == 0)) {
                                                      defBlock0.remove(def0);
                                                      defBlock1.remove(def1);
                                                      loadBlock.insertBefore(def0, loadBlock.getHead());
                                                      this.mUnitToBlockMap.put(def0, loadBlock);
                                                      hasChanged = true;
                                                      if (LoadStoreOptimizer.this.debug) {
                                                         LoadStoreOptimizer.logger.debug("inter-block opti2 occurred " + def0);
                                                      }
                                                   } else if (LoadStoreOptimizer.this.debug) {
                                                      LoadStoreOptimizer.logger.debug("failed: inter1");
                                                   }
                                                } else if (LoadStoreOptimizer.this.debug) {
                                                   LoadStoreOptimizer.logger.debug("failed: inter3");
                                                }
                                             } else if (LoadStoreOptimizer.this.debug) {
                                                LoadStoreOptimizer.logger.debug("failed: inter4");
                                             }
                                          } else if (LoadStoreOptimizer.this.debug) {
                                             LoadStoreOptimizer.logger.debug("failed: inter5");
                                          }
                                       } else if (LoadStoreOptimizer.this.debug) {
                                          LoadStoreOptimizer.logger.debug("failed: inter6");
                                       }
                                    }
                                 }
                              } while(defBlock == loadBlock);
                           } while(this.isExceptionHandlerBlock(loadBlock));

                           def1 = (Unit)defs.get(0);
                        } while(!(def1 instanceof StoreInst));

                        uses = this.mLocalUses.getUsesOf(def1);
                     } while(uses.size() != 1);
                  } while(!this.allSuccesorsOfAreThePredecessorsOf(defBlock, loadBlock));
               } while(this.getDeltaStackHeightFromTo(defBlock.getSuccOf(def1), defBlock.getTail()) != 0);

               Iterator<Block> it2 = defBlock.getSuccs().iterator();
               boolean res = true;

               label158: {
                  Block b;
                  do {
                     if (!it2.hasNext()) {
                        break label158;
                     }

                     b = (Block)it2.next();
                     if (this.getDeltaStackHeightFromTo(b.getHead(), b.getTail()) != 0) {
                        res = false;
                        break label158;
                     }
                  } while(b.getPreds().size() == 1 && b.getSuccs().size() == 1);

                  res = false;
               }

               if (LoadStoreOptimizer.this.debug) {
                  LoadStoreOptimizer.logger.debug("" + defBlock.toString() + loadBlock.toString());
               }

               if (res) {
                  defBlock.remove(def1);
                  this.mUnitToBlockMap.put(def1, loadBlock);
                  loadBlock.insertBefore(def1, loadBlock.getHead());
                  hasChanged = true;
                  if (LoadStoreOptimizer.this.debug) {
                     LoadStoreOptimizer.logger.debug("inter-block opti occurred " + def1 + " " + u);
                  }

                  if (LoadStoreOptimizer.this.debug) {
                     LoadStoreOptimizer.logger.debug("" + defBlock.toString() + loadBlock.toString());
                  }
               }
            }
         }

      }

      private boolean allSuccesorsOfAreThePredecessorsOf(Block aFirstBlock, Block aSecondBlock) {
         int size = aFirstBlock.getSuccs().size();
         Iterator<Block> it = aFirstBlock.getSuccs().iterator();
         List preds = aSecondBlock.getPreds();

         do {
            if (!it.hasNext()) {
               if (size == preds.size()) {
                  return true;
               }

               return false;
            }
         } while(preds.contains(it.next()));

         return false;
      }

      private boolean isCommutativeBinOp(Unit aUnit) {
         if (aUnit == null) {
            return false;
         } else {
            return aUnit instanceof AddInst || aUnit instanceof MulInst || aUnit instanceof AndInst || aUnit instanceof OrInst || aUnit instanceof XorInst;
         }
      }

      void propagateBackwardsIndependentHunk() {
         List<Unit> tempList = new ArrayList();
         tempList.addAll(this.mUnits);
         Iterator it = tempList.iterator();

         while(true) {
            Unit u;
            Block block;
            Unit succ;
            do {
               do {
                  if (!it.hasNext()) {
                     return;
                  }

                  u = (Unit)it.next();
               } while(!(u instanceof NewInst));

               block = (Block)this.mUnitToBlockMap.get(u);
               succ = block.getSuccOf(u);
            } while(!(succ instanceof StoreInst));

            Unit currentUnit = u;

            Unit candidate;
            for(candidate = null; currentUnit != block.getHead(); candidate = currentUnit) {
               currentUnit = block.getPredOf(currentUnit);
               if (!this.canMoveUnitOver(currentUnit, succ)) {
                  break;
               }
            }

            if (candidate != null) {
               block.remove(u);
               block.remove(succ);
               block.insertBefore(u, candidate);
               block.insertBefore(succ, candidate);
            }
         }
      }
   }
}
