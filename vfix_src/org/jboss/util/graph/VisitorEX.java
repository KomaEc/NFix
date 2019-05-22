package org.jboss.util.graph;

public interface VisitorEX<T, E extends Exception> {
   void visit(Graph<T> var1, Vertex<T> var2) throws E;
}
