package com.github.javaparser.ast.nodeTypes;

import com.github.javaparser.Position;
import com.github.javaparser.Range;
import com.github.javaparser.ast.Node;
import java.util.Optional;

public interface NodeWithRange<N> {
   Optional<Range> getRange();

   N setRange(Range range);

   default Optional<Position> getBegin() {
      return this.getRange().map((r) -> {
         return r.begin;
      });
   }

   default Optional<Position> getEnd() {
      return this.getRange().map((r) -> {
         return r.end;
      });
   }

   default boolean containsWithin(Node other) {
      return this.getRange().isPresent() && other.getRange().isPresent() ? ((Range)this.getRange().get()).contains((Range)other.getRange().get()) : false;
   }

   /** @deprecated */
   @Deprecated
   default boolean isPositionedAfter(Position position) {
      return (Boolean)this.getRange().map((r) -> {
         return r.isAfter(position);
      }).orElse(false);
   }

   /** @deprecated */
   @Deprecated
   default boolean isPositionedBefore(Position position) {
      return (Boolean)this.getRange().map((r) -> {
         return r.isBefore(position);
      }).orElse(false);
   }
}
