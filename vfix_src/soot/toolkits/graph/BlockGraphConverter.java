package soot.toolkits.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import soot.Body;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;

public class BlockGraphConverter {
   public static void addStartStopNodesTo(BlockGraph graph) {
      List<Block> tails = graph.getHeads();
      List blocks;
      DummyBlock tail;
      if (tails.size() != 0 && (tails.size() != 1 || !(tails.get(0) instanceof DummyBlock))) {
         blocks = graph.getBlocks();
         tail = new DummyBlock(graph.getBody(), 0);
         tail.makeHeadBlock(tails);
         graph.mHeads = Collections.singletonList(tail);
         Iterator var4 = blocks.iterator();

         while(var4.hasNext()) {
            Block block = (Block)var4.next();
            block.setIndexInMethod(block.getIndexInMethod() + 1);
         }

         List<Block> newBlocks = new ArrayList();
         newBlocks.add(tail);
         newBlocks.addAll(blocks);
         graph.mBlocks = newBlocks;
      }

      tails = graph.getTails();
      if (tails.size() != 0 && (tails.size() != 1 || !(tails.get(0) instanceof DummyBlock))) {
         blocks = graph.getBlocks();
         tail = new DummyBlock(graph.getBody(), blocks.size());
         tail.makeTailBlock(tails);
         graph.mTails = Collections.singletonList(tail);
         blocks.add(tail);
      }

   }

   public static void reverse(BlockGraph graph) {
      Iterator blocksIt = graph.getBlocks().iterator();

      while(blocksIt.hasNext()) {
         Block block = (Block)blocksIt.next();
         List<Block> succs = block.getSuccs();
         List<Block> preds = block.getPreds();
         block.setSuccs(preds);
         block.setPreds(succs);
      }

      List<Block> heads = graph.getHeads();
      List<Block> tails = graph.getTails();
      graph.mHeads = new ArrayList(tails);
      graph.mTails = new ArrayList(heads);
   }

   public static void main(String[] args) {
      Scene.v().loadClassAndSupport(args[0]);
      SootClass sc = Scene.v().getSootClass(args[0]);
      SootMethod sm = sc.getMethod(args[1]);
      Body b = sm.retrieveActiveBody();
      CompleteBlockGraph cfg = new CompleteBlockGraph(b);
      System.out.println(cfg);
      addStartStopNodesTo(cfg);
      System.out.println(cfg);
      reverse(cfg);
      System.out.println(cfg);
   }
}
