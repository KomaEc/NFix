package polyglot.visit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import polyglot.ast.Node;
import polyglot.ast.Term;
import polyglot.types.Type;
import polyglot.util.CollectionUtil;
import polyglot.util.IdentityKey;

public class FlowGraph {
   protected Map peerMap;
   protected Term root;
   protected boolean forward;
   public static final FlowGraph.EdgeKey EDGE_KEY_TRUE = new FlowGraph.EdgeKey("true");
   public static final FlowGraph.EdgeKey EDGE_KEY_FALSE = new FlowGraph.EdgeKey("false");
   public static final FlowGraph.EdgeKey EDGE_KEY_OTHER = new FlowGraph.EdgeKey("");

   FlowGraph(Term root, boolean forward) {
      this.root = root;
      this.forward = forward;
      this.peerMap = new HashMap();
   }

   public Term startNode() {
      return this.forward ? this.root.entry() : this.root;
   }

   public Term finishNode() {
      return this.forward ? this.root : this.root.entry();
   }

   public Term entryNode() {
      return this.root.entry();
   }

   public Term exitNode() {
      return this.root;
   }

   public Term root() {
      return this.root;
   }

   public boolean forward() {
      return this.forward;
   }

   public Collection pathMaps() {
      return this.peerMap.values();
   }

   public Map pathMap(Node n) {
      return (Map)this.peerMap.get(new IdentityKey(n));
   }

   public Collection peers() {
      Collection c = new ArrayList();
      Iterator i = this.peerMap.values().iterator();

      while(i.hasNext()) {
         Map m = (Map)i.next();
         Iterator j = m.values().iterator();

         while(j.hasNext()) {
            c.add(j.next());
         }
      }

      return c;
   }

   public FlowGraph.Peer peer(Term n, DataFlow df) {
      return this.peer(n, Collections.EMPTY_LIST, df);
   }

   public Collection peers(Term n) {
      IdentityKey k = new IdentityKey(n);
      Map pathMap = (Map)this.peerMap.get(k);
      return (Collection)(pathMap == null ? Collections.EMPTY_LIST : pathMap.values());
   }

   public FlowGraph.Peer peer(Term n, List path_to_finally, DataFlow df) {
      IdentityKey k = new IdentityKey(n);
      Map pathMap = (Map)this.peerMap.get(k);
      if (pathMap == null) {
         pathMap = new HashMap();
         this.peerMap.put(k, pathMap);
      }

      FlowGraph.ListKey lk = new FlowGraph.ListKey(path_to_finally);
      FlowGraph.Peer p = (FlowGraph.Peer)((Map)pathMap).get(lk);
      if (p == null) {
         p = new FlowGraph.Peer(n, path_to_finally);
         ((Map)pathMap).put(lk, p);
      }

      return p;
   }

   public String toString() {
      StringBuffer sb = new StringBuffer();
      Set todo = new HashSet(this.peers());
      LinkedList queue = new LinkedList(this.peers(this.startNode()));

      while(!queue.isEmpty()) {
         FlowGraph.Peer p = (FlowGraph.Peer)queue.removeFirst();
         ((Set)todo).remove(p);
         sb.append(p.node + " (" + p.node.position() + ")\n");
         Iterator i = p.succs.iterator();

         while(i.hasNext()) {
            FlowGraph.Edge e = (FlowGraph.Edge)i.next();
            FlowGraph.Peer q = e.getTarget();
            sb.append("    -> " + q.node + " (" + q.node.position() + ")\n");
            if (((Set)todo).contains(q) && !queue.contains(q)) {
               queue.addLast(q);
            }
         }

         if (queue.isEmpty() && !((Set)todo).isEmpty()) {
            sb.append("\n\n***UNREACHABLE***\n");
            queue.addAll((Collection)todo);
            todo = Collections.EMPTY_SET;
         }
      }

      return sb.toString();
   }

   protected static class ListKey {
      protected List list;

      ListKey(List list) {
         this.list = list;
      }

      public int hashCode() {
         return this.list.hashCode();
      }

      public boolean equals(Object other) {
         if (other instanceof FlowGraph.ListKey) {
            FlowGraph.ListKey k = (FlowGraph.ListKey)other;
            return CollectionUtil.equals(this.list, k.list);
         } else {
            return false;
         }
      }
   }

   public static class Peer {
      protected DataFlow.Item inItem;
      protected Map outItems;
      protected Term node;
      protected List succs;
      protected List preds;
      protected List path_to_finally;
      private Set succEdgeKeys;

      public Peer(Term node, List path_to_finally) {
         this.node = node;
         this.path_to_finally = path_to_finally;
         this.inItem = null;
         this.outItems = null;
         this.succs = new ArrayList();
         this.preds = new ArrayList();
         this.succEdgeKeys = null;
      }

      public List succs() {
         return this.succs;
      }

      public List preds() {
         return this.preds;
      }

      public Term node() {
         return this.node;
      }

      public DataFlow.Item inItem() {
         return this.inItem;
      }

      public DataFlow.Item outItem(FlowGraph.EdgeKey key) {
         return (DataFlow.Item)this.outItems.get(key);
      }

      public String toString() {
         return this.node + "[" + this.hashCode() + ": " + this.path_to_finally + "]";
      }

      public Set succEdgeKeys() {
         if (this.succEdgeKeys == null) {
            this.succEdgeKeys = new HashSet();
            Iterator iter = this.succs.iterator();

            while(iter.hasNext()) {
               FlowGraph.Edge e = (FlowGraph.Edge)iter.next();
               this.succEdgeKeys.add(e.getKey());
            }

            if (this.succEdgeKeys.isEmpty()) {
               this.succEdgeKeys.add(FlowGraph.EDGE_KEY_OTHER);
            }
         }

         return this.succEdgeKeys;
      }
   }

   public static class Edge {
      protected FlowGraph.EdgeKey key;
      protected FlowGraph.Peer target;

      protected Edge(FlowGraph.EdgeKey key, FlowGraph.Peer target) {
         this.key = key;
         this.target = target;
      }

      public FlowGraph.EdgeKey getKey() {
         return this.key;
      }

      public FlowGraph.Peer getTarget() {
         return this.target;
      }

      public String toString() {
         return "(" + this.key + ")" + this.target;
      }
   }

   public static class ExceptionEdgeKey extends FlowGraph.EdgeKey {
      public ExceptionEdgeKey(Type t) {
         super(t);
      }

      public Type type() {
         return (Type)this.o;
      }

      public String toString() {
         return this.type().isClass() ? this.type().toClass().name() : this.type().toString();
      }
   }

   public static class EdgeKey {
      protected Object o;

      protected EdgeKey(Object o) {
         this.o = o;
      }

      public int hashCode() {
         return this.o.hashCode();
      }

      public boolean equals(Object other) {
         return other instanceof FlowGraph.EdgeKey && ((FlowGraph.EdgeKey)other).o.equals(this.o);
      }

      public String toString() {
         return this.o.toString();
      }
   }
}
