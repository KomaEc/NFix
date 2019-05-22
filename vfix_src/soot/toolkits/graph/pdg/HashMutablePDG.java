package soot.toolkits.graph.pdg;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import soot.Body;
import soot.SootClass;
import soot.toolkits.graph.Block;
import soot.toolkits.graph.BlockGraph;
import soot.toolkits.graph.DominatorNode;
import soot.toolkits.graph.DominatorTree;
import soot.toolkits.graph.HashMutableEdgeLabelledDirectedGraph;
import soot.toolkits.graph.UnitGraph;

public class HashMutablePDG extends HashMutableEdgeLabelledDirectedGraph<PDGNode, String> implements ProgramDependenceGraph {
   protected Body m_body = null;
   protected SootClass m_class = null;
   protected UnitGraph m_cfg = null;
   protected BlockGraph m_blockCFG = null;
   protected Hashtable<Object, PDGNode> m_obj2pdgNode = new Hashtable();
   protected List<Region> m_weakRegions = null;
   protected List<Region> m_strongRegions = null;
   protected PDGNode m_startNode = null;
   protected List<PDGRegion> m_pdgRegions = null;
   private RegionAnalysis m_regionAnalysis = null;
   private int m_strongRegionStartID;
   private static Hashtable<PDGNode, PDGRegion> node2Region = new Hashtable();

   public HashMutablePDG(UnitGraph cfg) {
      this.m_body = cfg.getBody();
      this.m_class = this.m_body.getMethod().getDeclaringClass();
      this.m_cfg = cfg;
      this.m_regionAnalysis = new RegionAnalysis(this.m_cfg, this.m_body.getMethod(), this.m_class);
      this.m_strongRegions = this.m_regionAnalysis.getRegions();
      this.m_weakRegions = this.cloneRegions(this.m_strongRegions);
      this.m_blockCFG = this.m_regionAnalysis.getBlockCFG();
      this.constructPDG();
      this.m_pdgRegions = computePDGRegions(this.m_startNode);

      IRegion r;
      for(r = (IRegion)this.m_pdgRegions.get(0); r.getParent() != null; r = r.getParent()) {
      }

      this.m_startNode.setNode(r);
   }

   public BlockGraph getBlockGraph() {
      return this.m_blockCFG;
   }

   protected void constructPDG() {
      Hashtable<Block, Region> block2region = this.m_regionAnalysis.getBlock2RegionMap();
      DominatorTree<Block> pdom = this.m_regionAnalysis.getPostDominatorTree();
      DominatorTree<Block> dom = this.m_regionAnalysis.getDominatorTree();
      List<Region> regions2process = new LinkedList();
      Region topLevelRegion = this.m_regionAnalysis.getTopLevelRegion();
      this.m_strongRegionStartID = this.m_weakRegions.size();
      PDGNode pdgnode = new PDGNode(topLevelRegion, PDGNode.Type.REGION);
      this.addNode(pdgnode);
      this.m_obj2pdgNode.put(topLevelRegion, pdgnode);
      this.m_startNode = pdgnode;
      topLevelRegion.setParent((IRegion)null);
      Set<Region> processedRegions = new HashSet();
      regions2process.add(topLevelRegion);

      while(!regions2process.isEmpty()) {
         Region r = (Region)regions2process.remove(0);
         processedRegions.add(r);
         pdgnode = (PDGNode)this.m_obj2pdgNode.get(r);
         List<Block> blocks = r.getBlocks();
         Hashtable<Region, List<Block>> toBeRemoved = new Hashtable();
         PDGNode prevPDGNodeInRegion = null;
         PDGNode curNodeInRegion = null;

         label165:
         for(Iterator itr = blocks.iterator(); itr.hasNext(); prevPDGNodeInRegion = curNodeInRegion) {
            Block a = (Block)itr.next();
            PDGNode pdgNodeOfA = null;
            if (!this.m_obj2pdgNode.containsKey(a)) {
               pdgNodeOfA = new PDGNode(a, PDGNode.Type.CFGNODE);
               this.addNode(pdgNodeOfA);
               this.m_obj2pdgNode.put(a, pdgNodeOfA);
            } else {
               pdgNodeOfA = (PDGNode)this.m_obj2pdgNode.get(a);
            }

            this.addEdge(pdgnode, pdgNodeOfA, "dependency");
            pdgnode.addDependent((PDGNode)pdgNodeOfA);
            curNodeInRegion = pdgNodeOfA;
            List<Block> bs = this.m_blockCFG.getSuccsOf(a);
            Iterator bItr = bs.iterator();

            label162:
            while(true) {
               ArrayList dependents;
               Block b;
               DominatorNode aDode;
               DominatorNode bDode;
               do {
                  if (!bItr.hasNext()) {
                     if (prevPDGNodeInRegion != null) {
                        this.addEdge(prevPDGNodeInRegion, curNodeInRegion, "controlflow");
                        ((PDGNode)prevPDGNodeInRegion).setNext((PDGNode)curNodeInRegion);
                        ((PDGNode)curNodeInRegion).setPrev((PDGNode)prevPDGNodeInRegion);
                     }
                     continue label165;
                  }

                  dependents = new ArrayList();
                  b = (Block)bItr.next();
                  if (b.equals(a)) {
                     throw new RuntimeException("PDG construction: A and B are not supposed to be the same node!");
                  }

                  aDode = pdom.getDode(a);
                  bDode = pdom.getDode(b);
               } while(pdom.isDominatorOf(bDode, aDode));

               DominatorNode<Block> aParentDode = aDode.getParent();

               for(DominatorNode dode = bDode; dode != aParentDode; dode = dode.getParent()) {
                  dependents.add((Block)dode.getGode());
                  if (dode.getParent() == null) {
                     break;
                  }
               }

               if (((PDGNode)pdgNodeOfA).getAttrib() != PDGNode.Attribute.CONDHEADER) {
                  PDGNode oldA = pdgNodeOfA;
                  pdgNodeOfA = new ConditionalPDGNode((PDGNode)pdgNodeOfA);
                  this.replaceInGraph((PDGNode)pdgNodeOfA, (PDGNode)oldA);
                  pdgnode.removeDependent((PDGNode)oldA);
                  this.m_obj2pdgNode.put(a, pdgNodeOfA);
                  pdgnode.addDependent((PDGNode)pdgNodeOfA);
                  ((PDGNode)pdgNodeOfA).setAttrib(PDGNode.Attribute.CONDHEADER);
                  curNodeInRegion = pdgNodeOfA;
               }

               List<Block> copyOfDependents = new ArrayList();
               copyOfDependents.addAll(dependents);
               Region regionOfB = (Region)block2region.get(b);
               PDGNode pdgnodeOfBRegion = null;
               if (!this.m_obj2pdgNode.containsKey(regionOfB)) {
                  pdgnodeOfBRegion = new PDGNode(regionOfB, PDGNode.Type.REGION);
                  this.addNode(pdgnodeOfBRegion);
                  this.m_obj2pdgNode.put(regionOfB, pdgnodeOfBRegion);
               } else {
                  pdgnodeOfBRegion = (PDGNode)this.m_obj2pdgNode.get(regionOfB);
               }

               regionOfB.setParent(r);
               r.addChildRegion(regionOfB);
               this.addEdge(pdgNodeOfA, pdgnodeOfBRegion, "dependency");
               ((PDGNode)pdgNodeOfA).addDependent(pdgnodeOfBRegion);
               if (!processedRegions.contains(regionOfB)) {
                  regions2process.add(regionOfB);
               }

               copyOfDependents.remove(b);
               copyOfDependents.removeAll(regionOfB.getBlocks());

               while(true) {
                  while(true) {
                     if (copyOfDependents.isEmpty()) {
                        continue label162;
                     }

                     Block depB = (Block)copyOfDependents.remove(0);
                     Region rdepB = (Region)block2region.get(depB);
                     PDGNode depBPDGNode = (PDGNode)this.m_obj2pdgNode.get(depB);
                     PDGNode predPDGofdepB;
                     if (depBPDGNode == null) {
                        predPDGofdepB = null;
                        if (!this.m_obj2pdgNode.containsKey(rdepB)) {
                           predPDGofdepB = new PDGNode(rdepB, PDGNode.Type.REGION);
                           this.addNode(predPDGofdepB);
                           this.m_obj2pdgNode.put(rdepB, predPDGofdepB);
                        } else {
                           predPDGofdepB = (PDGNode)this.m_obj2pdgNode.get(rdepB);
                        }

                        rdepB.setParent(regionOfB);
                        regionOfB.addChildRegion(rdepB);
                        this.addEdge(pdgnodeOfBRegion, predPDGofdepB, "dependency");
                        pdgnodeOfBRegion.addDependent(predPDGofdepB);
                        if (!processedRegions.contains(rdepB)) {
                           regions2process.add(rdepB);
                        }

                        copyOfDependents.removeAll(rdepB.getBlocks());
                     } else if (dependents.containsAll(rdepB.getBlocks())) {
                        predPDGofdepB = null;
                        if (!this.m_obj2pdgNode.containsKey(rdepB)) {
                           predPDGofdepB = new PDGNode(rdepB, PDGNode.Type.REGION);
                           this.addNode(predPDGofdepB);
                           this.m_obj2pdgNode.put(rdepB, predPDGofdepB);
                        } else {
                           predPDGofdepB = (PDGNode)this.m_obj2pdgNode.get(rdepB);
                        }

                        this.addEdge(pdgnodeOfBRegion, predPDGofdepB, "dependency");
                        pdgnodeOfBRegion.addDependent(predPDGofdepB);
                        if (!processedRegions.contains(rdepB)) {
                           regions2process.add(rdepB);
                        }

                        copyOfDependents.removeAll(rdepB.getBlocks());
                     } else {
                        predPDGofdepB = (PDGNode)this.getPredsOf(depBPDGNode).get(0);

                        assert this.m_obj2pdgNode.containsKey(rdepB);

                        PDGNode pdgnodeOfdepBRegion = (PDGNode)this.m_obj2pdgNode.get(rdepB);
                        if (predPDGofdepB != pdgnodeOfdepBRegion) {
                           this.addEdge(pdgnodeOfBRegion, predPDGofdepB, "dependency-back");
                           pdgnodeOfBRegion.addBackDependent(predPDGofdepB);
                        } else {
                           Region newRegion = new Region(this.m_strongRegionStartID++, topLevelRegion.getSootMethod(), topLevelRegion.getSootClass(), this.m_cfg);
                           newRegion.add(depB);
                           this.m_strongRegions.add(newRegion);
                           Object blocks2BRemoved;
                           if (toBeRemoved.contains(predPDGofdepB)) {
                              blocks2BRemoved = (List)toBeRemoved.get(predPDGofdepB);
                           } else {
                              blocks2BRemoved = new ArrayList();
                              toBeRemoved.put(rdepB, blocks2BRemoved);
                           }

                           ((List)blocks2BRemoved).add(depB);
                           PDGNode newpdgnode = new LoopedPDGNode(newRegion, PDGNode.Type.REGION, depBPDGNode);
                           this.addNode(newpdgnode);
                           this.m_obj2pdgNode.put(newRegion, newpdgnode);
                           newpdgnode.setAttrib(PDGNode.Attribute.LOOPHEADER);
                           depBPDGNode.setAttrib(PDGNode.Attribute.LOOPHEADER);
                           this.removeEdge(pdgnodeOfdepBRegion, depBPDGNode, "dependency");
                           pdgnodeOfdepBRegion.removeDependent(depBPDGNode);
                           this.addEdge(pdgnodeOfdepBRegion, newpdgnode, "dependency");
                           this.addEdge(newpdgnode, depBPDGNode, "dependency");
                           pdgnodeOfdepBRegion.addDependent(newpdgnode);
                           newpdgnode.addDependent(depBPDGNode);
                           PDGNode loopBodyPDGNode;
                           if (depB == a) {
                              loopBodyPDGNode = (PDGNode)this.getSuccsOf(depBPDGNode).get(0);
                              this.addEdge(depBPDGNode, newpdgnode, "dependency-back");
                              ((LoopedPDGNode)newpdgnode).setBody(loopBodyPDGNode);
                              depBPDGNode.addBackDependent(newpdgnode);
                              curNodeInRegion = newpdgnode;
                           } else {
                              pdgnodeOfBRegion.addBackDependent(newpdgnode);
                              this.addEdge(pdgnodeOfBRegion, newpdgnode, "dependency-back");
                              loopBodyPDGNode = null;
                              List<PDGNode> successors = this.getSuccsOf(depBPDGNode);
                              Iterator succItr = successors.iterator();

                              PDGNode prev;
                              while(succItr.hasNext()) {
                                 prev = (PDGNode)succItr.next();

                                 assert prev.getType() == PDGNode.Type.REGION;

                                 Region succR = (Region)prev.getNode();
                                 Block h = (Block)succR.getBlocks().get(0);
                                 DominatorNode<Block> hdode = dom.getDode(h);
                                 DominatorNode<Block> adode = dom.getDode(a);
                                 if (dom.isDominatorOf(hdode, adode)) {
                                    loopBodyPDGNode = prev;
                                    break;
                                 }
                              }

                              assert loopBodyPDGNode != null;

                              ((LoopedPDGNode)newpdgnode).setBody(loopBodyPDGNode);
                              prev = depBPDGNode.getPrev();
                              if (prev != null) {
                                 this.removeEdge(prev, depBPDGNode, "controlflow");
                                 this.addEdge(prev, newpdgnode, "controlflow");
                                 prev.setNext(newpdgnode);
                                 newpdgnode.setPrev(prev);
                                 depBPDGNode.setPrev((PDGNode)null);
                              }

                              PDGNode next = depBPDGNode.getNext();
                              if (next != null) {
                                 this.removeEdge(depBPDGNode, next, "controlflow");
                                 this.addEdge(newpdgnode, next, "controlflow");
                                 newpdgnode.setNext(next);
                                 next.setPrev(newpdgnode);
                                 depBPDGNode.setNext((PDGNode)null);
                              }
                           }
                        }
                     }
                  }
               }
            }
         }

         Enumeration itr1 = toBeRemoved.keys();

         while(itr1.hasMoreElements()) {
            Region region = (Region)itr1.nextElement();
            Iterator blockItr = ((List)toBeRemoved.get(region)).iterator();

            while(blockItr.hasNext()) {
               region.remove((Block)blockItr.next());
            }
         }
      }

   }

   private List<Region> cloneRegions(List<Region> weak) {
      List<Region> strong = new ArrayList();
      Iterator itr = weak.iterator();

      while(itr.hasNext()) {
         Region r = (Region)itr.next();
         strong.add((Region)r.clone());
      }

      return strong;
   }

   public UnitGraph getCFG() {
      return this.m_cfg;
   }

   public List<Region> getWeakRegions() {
      return this.m_weakRegions;
   }

   public List<Region> getStrongRegions() {
      return this.m_strongRegions;
   }

   public IRegion GetStartRegion() {
      return (IRegion)this.GetStartNode().getNode();
   }

   public PDGNode GetStartNode() {
      return this.m_startNode;
   }

   public static List<IRegion> getPreorderRegionList(IRegion r) {
      List<IRegion> list = new ArrayList();
      Queue<IRegion> toProcess = new LinkedList();
      toProcess.add(r);

      while(!toProcess.isEmpty()) {
         IRegion reg = (IRegion)toProcess.poll();
         list.add(reg);
         Iterator itr = reg.getChildRegions().iterator();

         while(itr.hasNext()) {
            toProcess.add((Region)itr.next());
         }
      }

      return list;
   }

   public static List<IRegion> getPostorderRegionList(IRegion r) {
      List<IRegion> list = new ArrayList();
      postorder(r, list);
      return list;
   }

   private static List<IRegion> postorder(IRegion r, List<IRegion> list) {
      if (!r.getChildRegions().isEmpty()) {
         Iterator itr = r.getChildRegions().iterator();

         while(itr.hasNext()) {
            postorder((IRegion)itr.next(), list);
         }
      }

      list.add(r);
      return list;
   }

   public List<PDGRegion> getPDGRegions() {
      return this.m_pdgRegions;
   }

   public static List<PDGRegion> getPostorderPDGRegionList(PDGNode r) {
      return computePDGRegions(r);
   }

   private static List<PDGRegion> computePDGRegions(PDGNode root) {
      List<PDGRegion> regions = new ArrayList();
      node2Region.clear();
      pdgpostorder(root, regions);
      return regions;
   }

   private static PDGRegion pdgpostorder(PDGNode node, List<PDGRegion> list) {
      if (node.getVisited()) {
         return null;
      } else {
         node.setVisited(true);
         PDGRegion region = null;
         if (!node2Region.containsKey(node)) {
            region = new PDGRegion(node);
            node2Region.put(node, region);
         } else {
            region = (PDGRegion)node2Region.get(node);
         }

         List<PDGNode> dependents = node.getDependents();
         if (!dependents.isEmpty()) {
            Iterator itr = dependents.iterator();

            label47:
            while(true) {
               while(true) {
                  PDGNode curNode;
                  do {
                     if (!itr.hasNext()) {
                        break label47;
                     }

                     curNode = (PDGNode)itr.next();
                  } while(curNode.getVisited());

                  region.addPDGNode(curNode);
                  if (curNode instanceof LoopedPDGNode) {
                     PDGNode body = ((LoopedPDGNode)curNode).getBody();
                     PDGRegion kid = pdgpostorder(body, list);
                     if (kid != null) {
                        kid.setParent(region);
                        region.addChildRegion(kid);
                        body.setNode(kid);
                     }
                  } else if (curNode instanceof ConditionalPDGNode) {
                     List<PDGNode> childs = curNode.getDependents();
                     Iterator condItr = childs.iterator();

                     while(condItr.hasNext()) {
                        PDGNode child = (PDGNode)condItr.next();
                        PDGRegion kid = pdgpostorder(child, list);
                        if (kid != null) {
                           kid.setParent(region);
                           region.addChildRegion(kid);
                           child.setNode(kid);
                        }
                     }
                  }
               }
            }
         }

         list.add(region);
         return region;
      }
   }

   public boolean dependentOn(PDGNode node1, PDGNode node2) {
      return node2.getDependents().contains(node1);
   }

   public List<PDGNode> getDependents(PDGNode node) {
      List<PDGNode> toReturn = new ArrayList();
      Iterator var3 = this.getSuccsOf(node).iterator();

      while(var3.hasNext()) {
         PDGNode succ = (PDGNode)var3.next();
         if (this.dependentOn(succ, node)) {
            toReturn.add(succ);
         }
      }

      return toReturn;
   }

   public PDGNode getPDGNode(Object cfgNode) {
      return cfgNode != null && cfgNode instanceof Block && this.m_obj2pdgNode.containsKey(cfgNode) ? (PDGNode)this.m_obj2pdgNode.get(cfgNode) : null;
   }

   private void replaceInGraph(PDGNode newnode, PDGNode oldnode) {
      this.addNode(newnode);
      HashMutableEdgeLabelledDirectedGraph graph = this.clone();
      List<PDGNode> succs = graph.getSuccsOf(oldnode);
      List<PDGNode> preds = graph.getPredsOf(oldnode);
      Iterator var6 = succs.iterator();

      PDGNode pred;
      List labels;
      Iterator labelItr;
      Object label;
      while(var6.hasNext()) {
         pred = (PDGNode)var6.next();
         labels = graph.getLabelsForEdges(oldnode, pred);
         labelItr = labels.iterator();

         while(labelItr.hasNext()) {
            label = labelItr.next();
            this.addEdge(newnode, pred, new String(label.toString()));
         }
      }

      var6 = preds.iterator();

      while(var6.hasNext()) {
         pred = (PDGNode)var6.next();
         labels = graph.getLabelsForEdges(pred, oldnode);
         labelItr = labels.iterator();

         while(labelItr.hasNext()) {
            label = labelItr.next();
            this.addEdge(pred, newnode, new String(label.toString()));
         }
      }

      this.removeNode(oldnode);
   }

   public void removeAllEdges(PDGNode from, PDGNode to) {
      if (this.containsAnyEdge(from, to)) {
         List<String> labels = new ArrayList(this.getLabelsForEdges(from, to));
         Iterator var4 = labels.iterator();

         while(var4.hasNext()) {
            String label = (String)var4.next();
            this.removeEdge(from, to, label);
         }

      }
   }

   public String toString() {
      String s = new String("\nProgram Dependence Graph for Method " + this.m_body.getMethod().getName());
      s = s + "\n*********CFG******** \n" + RegionAnalysis.CFGtoString(this.m_blockCFG, true);
      s = s + "\n*********PDG******** \n";
      List<PDGNode> processed = new ArrayList();
      Queue<PDGNode> nodes = new LinkedList();
      nodes.offer(this.m_startNode);

      while(nodes.peek() != null) {
         PDGNode node = (PDGNode)nodes.remove();
         processed.add(node);
         s = s + "\n Begin PDGNode: " + node;
         List<PDGNode> succs = this.getSuccsOf(node);
         s = s + "\n has " + succs.size() + " successors:\n";
         int i = 0;
         Iterator var7 = succs.iterator();

         while(var7.hasNext()) {
            PDGNode succ = (PDGNode)var7.next();
            List<String> labels = this.getLabelsForEdges(node, succ);
            s = s + i++;
            s = s + ": Edge's label: " + labels + " \n";
            s = s + "   Target: " + succ.toShortString();
            s = s + "\n";
            if (((String)labels.get(0)).equals("dependency") && !processed.contains(succ)) {
               nodes.offer(succ);
            }
         }

         s = s + "\n End PDGNode.";
      }

      return s;
   }
}
