package soot.JastAddJ;

import java.util.AbstractSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Set;

public class ASTNode$State {
   public boolean IN_CIRCLE = false;
   public int CIRCLE_INDEX = 1;
   public boolean CHANGE = false;
   public boolean RESET_CYCLE = false;
   public static final int REWRITE_CHANGE = 1;
   public static final int REWRITE_NOCHANGE = 2;
   public static final int REWRITE_INTERRUPT = 3;
   public int boundariesCrossed = 0;
   private int[] stack = new int[64];
   private int pos = 0;
   public Options options = new Options();
   public int replacePos = 0;
   protected int duringImplicitConstructor = 0;
   protected int duringBoundNames = 0;
   protected int duringNameResolution = 0;
   protected int duringSyntacticClassification = 0;
   protected int duringAnonymousClasses = 0;
   protected int duringVariableDeclarationTransformation = 0;
   protected int duringLiterals = 0;
   protected int duringDU = 0;
   protected int duringAnnotations = 0;
   protected int duringEnums = 0;
   protected int duringGenericTypeVariables = 0;

   private void ensureSize(int size) {
      if (size >= this.stack.length) {
         int[] newStack = new int[this.stack.length * 2];
         System.arraycopy(this.stack, 0, newStack, 0, this.stack.length);
         this.stack = newStack;
      }
   }

   public void push(int i) {
      this.ensureSize(this.pos + 1);
      this.stack[this.pos++] = i;
   }

   public int pop() {
      return this.stack[--this.pos];
   }

   public int peek() {
      return this.stack[this.pos - 1];
   }

   public void reset() {
      this.IN_CIRCLE = false;
      this.CIRCLE_INDEX = 1;
      this.CHANGE = false;
      this.boundariesCrossed = 0;
      if (this.duringImplicitConstructor != 0) {
         System.out.println("Warning: resetting duringImplicitConstructor");
         this.duringImplicitConstructor = 0;
      }

      if (this.duringBoundNames != 0) {
         System.out.println("Warning: resetting duringBoundNames");
         this.duringBoundNames = 0;
      }

      if (this.duringNameResolution != 0) {
         System.out.println("Warning: resetting duringNameResolution");
         this.duringNameResolution = 0;
      }

      if (this.duringSyntacticClassification != 0) {
         System.out.println("Warning: resetting duringSyntacticClassification");
         this.duringSyntacticClassification = 0;
      }

      if (this.duringAnonymousClasses != 0) {
         System.out.println("Warning: resetting duringAnonymousClasses");
         this.duringAnonymousClasses = 0;
      }

      if (this.duringVariableDeclarationTransformation != 0) {
         System.out.println("Warning: resetting duringVariableDeclarationTransformation");
         this.duringVariableDeclarationTransformation = 0;
      }

      if (this.duringLiterals != 0) {
         System.out.println("Warning: resetting duringLiterals");
         this.duringLiterals = 0;
      }

      if (this.duringDU != 0) {
         System.out.println("Warning: resetting duringDU");
         this.duringDU = 0;
      }

      if (this.duringAnnotations != 0) {
         System.out.println("Warning: resetting duringAnnotations");
         this.duringAnnotations = 0;
      }

      if (this.duringEnums != 0) {
         System.out.println("Warning: resetting duringEnums");
         this.duringEnums = 0;
      }

      if (this.duringGenericTypeVariables != 0) {
         System.out.println("Warning: resetting duringGenericTypeVariables");
         this.duringGenericTypeVariables = 0;
      }

   }

   static class IdentityHashSet extends AbstractSet implements Set {
      private IdentityHashMap map;
      private static final Object PRESENT = new Object();

      public IdentityHashSet(int initialCapacity) {
         this.map = new IdentityHashMap(initialCapacity);
      }

      public Iterator iterator() {
         return this.map.keySet().iterator();
      }

      public int size() {
         return this.map.size();
      }

      public boolean isEmpty() {
         return this.map.isEmpty();
      }

      public boolean contains(Object o) {
         return this.map.containsKey(o);
      }

      public boolean add(Object o) {
         return this.map.put(o, PRESENT) == null;
      }

      public boolean remove(Object o) {
         return this.map.remove(o) == PRESENT;
      }

      public void clear() {
         this.map.clear();
      }
   }

   public static class CircularValue {
      Object value;
      int visited = -1;
   }
}
