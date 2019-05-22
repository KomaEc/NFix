package corg.vfix.sa.vfg;

import java.util.ArrayList;
import java.util.Iterator;

public class NodeType {
   public static final int SRC = 0;
   public static final int TRANS = 1;
   public static final int SINK = 2;
   public static final int NONE = -1;

   public static void setType(VFG vfg) {
      vfg.setDegree();
      ArrayList<VFGNode> nodes = vfg.getNodes();
      Iterator var3 = nodes.iterator();

      while(var3.hasNext()) {
         VFGNode node = (VFGNode)var3.next();
         node.setNodeType(getTypeByDegree(node));
      }

      vfg.setType();
   }

   private static int getTypeByDegree(VFGNode vfgnode) {
      if (vfgnode.getInDegree() == 0 && vfgnode.getOutDegree() > 0) {
         return 0;
      } else if (vfgnode.getInDegree() >= 0 && vfgnode.getOutDegree() == 0) {
         return 2;
      } else {
         return vfgnode.getInDegree() > 0 && vfgnode.getOutDegree() > 0 ? 1 : -1;
      }
   }

   public static String typeToString(int type) {
      switch(type) {
      case 0:
         return "SRC";
      case 1:
         return "TRANS";
      case 2:
         return "SINK";
      default:
         return "NONE";
      }
   }
}
