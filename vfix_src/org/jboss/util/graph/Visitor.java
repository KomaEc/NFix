package org.jboss.util.graph;

public interface Visitor<T> {
   void visit(Graph<T> var1, Vertex<T> var2);
}
