package org.jboss.util.graph;

public interface DFSVisitor<T> {
   void visit(Graph<T> var1, Vertex<T> var2);

   void visit(Graph<T> var1, Vertex<T> var2, Edge<T> var3);
}
