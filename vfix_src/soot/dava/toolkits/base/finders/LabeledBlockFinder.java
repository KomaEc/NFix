package soot.dava.toolkits.base.finders;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;
import soot.G;
import soot.Singletons;
import soot.dava.Dava;
import soot.dava.DavaBody;
import soot.dava.RetriggerAnalysisException;
import soot.dava.internal.SET.SETBasicBlock;
import soot.dava.internal.SET.SETLabeledBlockNode;
import soot.dava.internal.SET.SETNode;
import soot.dava.internal.SET.SETStatementSequenceNode;
import soot.dava.internal.SET.SETTryNode;
import soot.dava.internal.SET.SETUnconditionalWhileNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.internal.asg.AugmentedStmtGraph;
import soot.util.IterableSet;

public class LabeledBlockFinder implements FactFinder {
   private final HashMap<SETNode, Integer> orderNumber = new HashMap();

   public LabeledBlockFinder(Singletons.Global g) {
   }

   public static LabeledBlockFinder v() {
      return G.v().soot_dava_toolkits_base_finders_LabeledBlockFinder();
   }

   public void find(DavaBody body, AugmentedStmtGraph asg, SETNode SET) throws RetriggerAnalysisException {
      Dava.v().log("LabeledBlockFinder::find()");
      Iterator bit = SET.get_Body().iterator();

      while(bit.hasNext()) {
         SET.find_SmallestSETNode((AugmentedStmt)bit.next());
      }

      SET.find_LabeledBlocks(this);
   }

   public void perform_ChildOrder(SETNode SETParent) {
      Dava.v().log("LabeledBlockFinder::perform_ChildOrder()");
      if (!(SETParent instanceof SETStatementSequenceNode)) {
         Iterator sbit = SETParent.get_SubBodies().iterator();

         while(sbit.hasNext()) {
            IterableSet body = (IterableSet)sbit.next();
            IterableSet children = (IterableSet)SETParent.get_Body2ChildChain().get(body);
            HashSet<SETBasicBlock> touchSet = new HashSet();
            IterableSet childOrdering = new IterableSet();
            LinkedList worklist = new LinkedList();
            List<SETBasicBlock> SETBasicBlocks = null;
            SETNode startSETNode;
            Iterator it;
            Iterator sit;
            if (SETParent instanceof SETUnconditionalWhileNode) {
               for(startSETNode = ((SETUnconditionalWhileNode)SETParent).get_CharacterizingStmt().myNode; !children.contains(startSETNode); startSETNode = startSETNode.get_Parent()) {
               }

               this.build_Connectivity(SETParent, body, startSETNode);
               worklist.add(SETBasicBlock.get_SETBasicBlock(startSETNode));
            } else if (!(SETParent instanceof SETTryNode)) {
               this.build_Connectivity(SETParent, body, (SETNode)null);
               Iterator cit = children.iterator();

               while(cit.hasNext()) {
                  SETNode child = (SETNode)cit.next();
                  if (child.get_Predecessors().isEmpty()) {
                     worklist.add(SETBasicBlock.get_SETBasicBlock(child));
                  }
               }
            } else {
               startSETNode = null;
               it = body.iterator();

               label88:
               while(it.hasNext()) {
                  AugmentedStmt as = (AugmentedStmt)it.next();
                  sit = as.cpreds.iterator();

                  while(sit.hasNext()) {
                     if (!body.contains(sit.next())) {
                        startSETNode = as.myNode;
                        break label88;
                     }
                  }
               }

               if (startSETNode == null) {
                  startSETNode = ((SETTryNode)SETParent).get_EntryStmt().myNode;
               }

               while(!children.contains(startSETNode)) {
                  startSETNode = startSETNode.get_Parent();
               }

               this.build_Connectivity(SETParent, body, startSETNode);
               worklist.add(SETBasicBlock.get_SETBasicBlock(startSETNode));
            }

            label123:
            while(!worklist.isEmpty()) {
               SETBasicBlock sbb = (SETBasicBlock)worklist.removeFirst();
               it = sbb.get_Body().iterator();

               while(it.hasNext()) {
                  childOrdering.addLast(it.next());
               }

               touchSet.add(sbb);
               TreeSet sortedSuccessors = new TreeSet();
               sit = sbb.get_Successors().iterator();

               while(true) {
                  label113:
                  while(true) {
                     SETBasicBlock ssbb;
                     do {
                        if (!sit.hasNext()) {
                           sit = sortedSuccessors.iterator();

                           while(sit.hasNext()) {
                              worklist.addFirst(sit.next());
                           }
                           continue label123;
                        }

                        ssbb = (SETBasicBlock)sit.next();
                     } while(touchSet.contains(ssbb));

                     Iterator psit = ssbb.get_Predecessors().iterator();

                     while(psit.hasNext()) {
                        if (!touchSet.contains(psit.next())) {
                           continue label113;
                        }
                     }

                     sortedSuccessors.add(ssbb);
                  }
               }
            }

            int count = 0;
            it = childOrdering.iterator();

            while(it.hasNext()) {
               this.orderNumber.put((SETNode)it.next(), new Integer(count++));
            }

            children.clear();
            children.addAll(childOrdering);
         }

      }
   }

   private List<SETBasicBlock> build_Connectivity(SETNode SETParent, IterableSet body, SETNode startSETNode) {
      Dava.v().log("LabeledBlockFinder::build_Connectivity()");
      IterableSet children = (IterableSet)SETParent.get_Body2ChildChain().get(body);
      Iterator it = body.iterator();

      Iterator cit;
      SETNode dstNode;
      label122:
      while(it.hasNext()) {
         AugmentedStmt as = (AugmentedStmt)it.next();
         cit = as.csuccs.iterator();

         while(true) {
            AugmentedStmt sas;
            do {
               if (!cit.hasNext()) {
                  continue label122;
               }

               sas = (AugmentedStmt)cit.next();
            } while(!body.contains(sas));

            SETNode srcNode = as.myNode;

            for(dstNode = sas.myNode; !children.contains(srcNode); srcNode = srcNode.get_Parent()) {
            }

            while(!children.contains(dstNode)) {
               dstNode = dstNode.get_Parent();
            }

            if (srcNode != dstNode) {
               if (!srcNode.get_Successors().contains(dstNode)) {
                  srcNode.get_Successors().add(dstNode);
               }

               if (!dstNode.get_Predecessors().contains(srcNode)) {
                  dstNode.get_Predecessors().add(srcNode);
               }
            }
         }
      }

      Dava.v().log("LabeledBlockFinder::build_Connectivity() - built connectivity");
      LinkedList<SETBasicBlock> basicBlockList = new LinkedList();
      cit = children.iterator();

      while(true) {
         SETNode child;
         SETBasicBlock basicBlock;
         do {
            if (!cit.hasNext()) {
               Dava.v().log("LabeledBlockFinder::build_Connectivity() - created basic blocks");
               Iterator bblit = basicBlockList.iterator();

               while(bblit.hasNext()) {
                  basicBlock = (SETBasicBlock)bblit.next();
                  dstNode = basicBlock.get_EntryNode();
                  Iterator pit = dstNode.get_Predecessors().iterator();

                  while(pit.hasNext()) {
                     SETNode psn = (SETNode)pit.next();
                     SETBasicBlock psbb = SETBasicBlock.get_SETBasicBlock(psn);
                     if (!basicBlock.get_Predecessors().contains(psbb)) {
                        basicBlock.get_Predecessors().add(psbb);
                     }

                     if (!psbb.get_Successors().contains(basicBlock)) {
                        psbb.get_Successors().add(basicBlock);
                     }
                  }
               }

               Dava.v().log("LabeledBlockFinder::build_Connectivity() - done");
               return basicBlockList;
            }

            child = (SETNode)cit.next();
         } while(SETBasicBlock.get_SETBasicBlock(child) != null);

         for(basicBlock = new SETBasicBlock(); child.get_Predecessors().size() == 1 && (startSETNode == null || child != startSETNode); child = dstNode) {
            dstNode = (SETNode)child.get_Predecessors().getFirst();
            if (SETBasicBlock.get_SETBasicBlock(dstNode) != null || dstNode.get_Successors().size() != 1) {
               break;
            }
         }

         basicBlock.add(child);

         while(child.get_Successors().size() == 1) {
            child = (SETNode)child.get_Successors().getFirst();
            if (SETBasicBlock.get_SETBasicBlock(child) != null || child.get_Predecessors().size() != 1) {
               break;
            }

            basicBlock.add(child);
         }

         basicBlockList.add(basicBlock);
      }
   }

   public void find_LabeledBlocks(SETNode SETParent) {
      Dava.v().log("LabeledBlockFinder::find_LabeledBlocks()");
      Iterator sbit = SETParent.get_SubBodies().iterator();

      label99:
      while(true) {
         IterableSet curBody;
         IterableSet children;
         Iterator it;
         do {
            if (!sbit.hasNext()) {
               return;
            }

            curBody = (IterableSet)sbit.next();
            children = (IterableSet)SETParent.get_Body2ChildChain().get(curBody);
            it = children.snapshotIterator();
         } while(!it.hasNext());

         SETNode curNode = (SETNode)it.next();
         SETNode prevNode = null;

         while(true) {
            SETNode minNode;
            boolean build;
            label79:
            do {
               if (!it.hasNext()) {
                  continue label99;
               }

               prevNode = curNode;
               curNode = (SETNode)it.next();
               AugmentedStmt entryStmt = curNode.get_EntryStmt();
               minNode = null;
               build = false;
               Iterator pit = entryStmt.cpreds.iterator();

               while(true) {
                  SETNode srcNode;
                  do {
                     do {
                        do {
                           AugmentedStmt pas;
                           do {
                              if (!pit.hasNext()) {
                                 continue label79;
                              }

                              pas = (AugmentedStmt)pit.next();
                           } while(!curBody.contains(pas));

                           for(srcNode = pas.myNode; !children.contains(srcNode); srcNode = srcNode.get_Parent()) {
                           }
                        } while(srcNode == curNode);
                     } while(srcNode == prevNode);

                     build = true;
                  } while(minNode != null && (Integer)this.orderNumber.get(srcNode) >= (Integer)this.orderNumber.get(minNode));

                  minNode = srcNode;
               }
            } while(!build);

            IterableSet labeledBlockBody = new IterableSet();
            Iterator cit = children.iterator(minNode);

            while(cit.hasNext()) {
               SETNode child = (SETNode)cit.next();
               if (child == curNode) {
                  break;
               }

               labeledBlockBody.addAll(child.get_Body());
            }

            SETLabeledBlockNode slbn = new SETLabeledBlockNode(labeledBlockBody);
            this.orderNumber.put(slbn, this.orderNumber.get(minNode));
            cit = children.snapshotIterator(minNode);

            while(cit.hasNext()) {
               SETNode child = (SETNode)cit.next();
               if (child == curNode) {
                  break;
               }

               SETParent.remove_Child(child, children);
               slbn.add_Child(child, (IterableSet)slbn.get_Body2ChildChain().get(slbn.get_SubBodies().get(0)));
            }

            SETParent.insert_ChildBefore(slbn, curNode, children);
         }
      }
   }
}
