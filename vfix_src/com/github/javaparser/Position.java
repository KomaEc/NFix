package com.github.javaparser;

import com.github.javaparser.utils.Utils;

public class Position implements Comparable<Position> {
   public final int line;
   public final int column;
   public static final Position HOME = new Position(1, 1);

   public Position(int line, int column) {
      if (line < -2) {
         throw new IllegalArgumentException("Can't position at line " + line);
      } else if (column < -1) {
         throw new IllegalArgumentException("Can't position at column " + column);
      } else {
         this.line = line;
         this.column = column;
      }
   }

   public static Position pos(int line, int column) {
      return new Position(line, column);
   }

   public Position withColumn(int column) {
      return new Position(this.line, column);
   }

   public Position withLine(int line) {
      return new Position(line, this.column);
   }

   public boolean valid() {
      return this.line > 0 && this.column > 0;
   }

   public boolean invalid() {
      return !this.valid();
   }

   public Position orIfInvalid(Position anotherPosition) {
      Utils.assertNotNull(anotherPosition);
      return !this.valid() && !anotherPosition.invalid() ? anotherPosition : this;
   }

   public boolean isAfter(Position position) {
      Utils.assertNotNull(position);
      if (position.line == -1) {
         return true;
      } else if (this.line > position.line) {
         return true;
      } else if (this.line == position.line) {
         return this.column > position.column;
      } else {
         return false;
      }
   }

   public boolean isBefore(Position position) {
      Utils.assertNotNull(position);
      if (position.line == -2) {
         return true;
      } else if (this.line < position.line) {
         return true;
      } else if (this.line == position.line) {
         return this.column < position.column;
      } else {
         return false;
      }
   }

   public int compareTo(Position o) {
      Utils.assertNotNull(o);
      if (this.isBefore(o)) {
         return -1;
      } else {
         return this.isAfter(o) ? 1 : 0;
      }
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         Position position = (Position)o;
         return this.line == position.line && this.column == position.column;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return 31 * this.line + this.column;
   }

   public String toString() {
      return "(line " + this.line + ",col " + this.column + ")";
   }
}
