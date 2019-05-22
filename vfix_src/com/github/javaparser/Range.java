package com.github.javaparser;

public class Range {
   public final Position begin;
   public final Position end;

   public Range(Position begin, Position end) {
      if (begin == null) {
         throw new IllegalArgumentException("begin can't be null");
      } else if (end == null) {
         throw new IllegalArgumentException("end can't be null");
      } else {
         this.begin = begin;
         this.end = end;
      }
   }

   public static Range range(Position begin, Position end) {
      return new Range(begin, end);
   }

   public static Range range(int beginLine, int beginColumn, int endLine, int endColumn) {
      return new Range(Position.pos(beginLine, beginColumn), Position.pos(endLine, endColumn));
   }

   public Range withBeginColumn(int column) {
      return range(this.begin.withColumn(column), this.end);
   }

   public Range withBeginLine(int line) {
      return range(this.begin.withLine(line), this.end);
   }

   public Range withEndColumn(int column) {
      return range(this.begin, this.end.withColumn(column));
   }

   public Range withEndLine(int line) {
      return range(this.begin, this.end.withLine(line));
   }

   public Range withBegin(Position begin) {
      return range(begin, this.end);
   }

   public Range withEnd(Position end) {
      return range(this.begin, end);
   }

   public boolean contains(Range other) {
      return (this.begin.isBefore(other.begin) || this.begin.equals(other.begin)) && (this.end.isAfter(other.end) || this.end.equals(other.end));
   }

   public boolean strictlyContains(Range other) {
      return this.begin.isBefore(other.begin) && this.end.isAfter(other.end);
   }

   public boolean isBefore(Position position) {
      return this.end.isBefore(position);
   }

   public boolean isAfter(Position position) {
      return this.begin.isAfter(position);
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         Range range = (Range)o;
         return this.begin.equals(range.begin) && this.end.equals(range.end);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return 31 * this.begin.hashCode() + this.end.hashCode();
   }

   public String toString() {
      return this.begin + "-" + this.end;
   }
}
