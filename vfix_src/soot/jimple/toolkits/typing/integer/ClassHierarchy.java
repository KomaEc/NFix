package soot.jimple.toolkits.typing.integer;

import java.util.HashMap;
import java.util.Map;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.G;
import soot.IntType;
import soot.PrimType;
import soot.RefType;
import soot.ShortType;
import soot.Singletons;
import soot.Type;

public class ClassHierarchy {
   public final TypeNode BOOLEAN = new TypeNode(0, BooleanType.v());
   public final TypeNode BYTE = new TypeNode(1, ByteType.v());
   public final TypeNode SHORT = new TypeNode(2, ShortType.v());
   public final TypeNode CHAR = new TypeNode(3, CharType.v());
   public final TypeNode INT = new TypeNode(4, IntType.v());
   public final TypeNode TOP = new TypeNode(5, (Type)null);
   public final TypeNode R0_1 = new TypeNode(6, (Type)null);
   public final TypeNode R0_127 = new TypeNode(7, (Type)null);
   public final TypeNode R0_32767 = new TypeNode(8, (Type)null);
   private final boolean[][] ancestors_1 = new boolean[][]{{false, false, false, false, false, true, false, false, false}, {false, false, true, false, true, true, false, false, false}, {false, false, false, false, true, true, false, false, false}, {false, false, false, false, true, true, false, false, false}, {false, false, false, false, false, true, false, false, false}, {false, false, false, false, false, false, false, false, false}, {true, true, true, true, true, true, false, true, true}, {false, true, true, true, true, true, false, false, true}, {false, false, true, true, true, true, false, false, false}};
   private final boolean[][] ancestors_2 = new boolean[][]{{false, true, true, true, true, false, false, true, true}, {false, false, true, false, true, false, false, false, false}, {false, false, false, false, true, false, false, false, false}, {false, false, false, false, true, false, false, false, false}, {false, false, false, false, false, false, false, false, false}, new boolean[0], new boolean[0], {false, true, true, true, true, false, false, false, true}, {false, false, true, true, true, false, false, false, false}};
   private final boolean[][] descendants_1 = new boolean[][]{{false, false, false, false, false, false, true, false, false}, {false, false, false, false, false, false, true, true, false}, {false, true, false, false, false, false, true, true, true}, {false, false, false, false, false, false, true, true, true}, {false, true, true, true, false, false, true, true, true}, {true, true, true, true, true, false, true, true, true}, {false, false, false, false, false, false, false, false, false}, {false, false, false, false, false, false, true, false, false}, {false, false, false, false, false, false, true, true, false}};
   private final boolean[][] descendants_2 = new boolean[][]{{false, false, false, false, false, false, false, false, false}, {true, false, false, false, false, false, false, true, false}, {true, true, false, false, false, false, false, true, true}, {true, false, false, false, false, false, false, true, true}, {true, true, true, true, false, false, false, true, true}, new boolean[0], new boolean[0], {true, false, false, false, false, false, false, false, false}, {true, false, false, false, false, false, false, true, false}};
   private final TypeNode[][] lca_1;
   private final TypeNode[][] lca_2;
   private final TypeNode[][] gcd_1;
   private final TypeNode[][] gcd_2;
   private final Map<Type, TypeNode> typeNodeMap;

   public ClassHierarchy(Singletons.Global g) {
      this.lca_1 = new TypeNode[][]{{this.BOOLEAN, this.TOP, this.TOP, this.TOP, this.TOP, this.TOP, this.BOOLEAN, this.TOP, this.TOP}, {this.TOP, this.BYTE, this.SHORT, this.INT, this.INT, this.TOP, this.BYTE, this.BYTE, this.SHORT}, {this.TOP, this.SHORT, this.SHORT, this.INT, this.INT, this.TOP, this.SHORT, this.SHORT, this.SHORT}, {this.TOP, this.INT, this.INT, this.CHAR, this.INT, this.TOP, this.CHAR, this.CHAR, this.CHAR}, {this.TOP, this.INT, this.INT, this.INT, this.INT, this.TOP, this.INT, this.INT, this.INT}, {this.TOP, this.TOP, this.TOP, this.TOP, this.TOP, this.TOP, this.TOP, this.TOP, this.TOP}, {this.BOOLEAN, this.BYTE, this.SHORT, this.CHAR, this.INT, this.TOP, this.R0_1, this.R0_127, this.R0_32767}, {this.TOP, this.BYTE, this.SHORT, this.CHAR, this.INT, this.TOP, this.R0_127, this.R0_127, this.R0_32767}, {this.TOP, this.SHORT, this.SHORT, this.CHAR, this.INT, this.TOP, this.R0_32767, this.R0_32767, this.R0_32767}};
      this.lca_2 = new TypeNode[][]{{this.BOOLEAN, this.BYTE, this.SHORT, this.CHAR, this.INT, null, null, this.R0_127, this.R0_32767}, {this.BYTE, this.BYTE, this.SHORT, this.INT, this.INT, null, null, this.BYTE, this.SHORT}, {this.SHORT, this.SHORT, this.SHORT, this.INT, this.INT, null, null, this.SHORT, this.SHORT}, {this.CHAR, this.INT, this.INT, this.CHAR, this.INT, null, null, this.CHAR, this.CHAR}, {this.INT, this.INT, this.INT, this.INT, this.INT, null, null, this.INT, this.INT}, new TypeNode[0], new TypeNode[0], {this.R0_127, this.BYTE, this.SHORT, this.CHAR, this.INT, null, null, this.R0_127, this.R0_32767}, {this.R0_32767, this.SHORT, this.SHORT, this.CHAR, this.INT, null, null, this.R0_32767, this.R0_32767}};
      this.gcd_1 = new TypeNode[][]{{this.BOOLEAN, this.R0_1, this.R0_1, this.R0_1, this.R0_1, this.BOOLEAN, this.R0_1, this.R0_1, this.R0_1}, {this.R0_1, this.BYTE, this.BYTE, this.R0_127, this.BYTE, this.BYTE, this.R0_1, this.R0_127, this.R0_127}, {this.R0_1, this.BYTE, this.SHORT, this.R0_32767, this.SHORT, this.SHORT, this.R0_1, this.R0_127, this.R0_32767}, {this.R0_1, this.R0_127, this.R0_32767, this.CHAR, this.CHAR, this.CHAR, this.R0_1, this.R0_127, this.R0_32767}, {this.R0_1, this.BYTE, this.SHORT, this.CHAR, this.INT, this.INT, this.R0_1, this.R0_127, this.R0_32767}, {this.BOOLEAN, this.BYTE, this.SHORT, this.CHAR, this.INT, this.TOP, this.R0_1, this.R0_127, this.R0_32767}, {this.R0_1, this.R0_1, this.R0_1, this.R0_1, this.R0_1, this.R0_1, this.R0_1, this.R0_1, this.R0_1}, {this.R0_1, this.R0_127, this.R0_127, this.R0_127, this.R0_127, this.R0_127, this.R0_1, this.R0_127, this.R0_127}, {this.R0_1, this.R0_127, this.R0_32767, this.R0_32767, this.R0_32767, this.R0_32767, this.R0_1, this.R0_127, this.R0_32767}};
      this.gcd_2 = new TypeNode[][]{{this.BOOLEAN, this.BOOLEAN, this.BOOLEAN, this.BOOLEAN, this.BOOLEAN, null, null, this.BOOLEAN, this.BOOLEAN}, {this.BOOLEAN, this.BYTE, this.BYTE, this.R0_127, this.BYTE, null, null, this.R0_127, this.R0_127}, {this.BOOLEAN, this.BYTE, this.SHORT, this.R0_32767, this.SHORT, null, null, this.R0_127, this.R0_32767}, {this.BOOLEAN, this.R0_127, this.R0_32767, this.CHAR, this.CHAR, null, null, this.R0_127, this.R0_32767}, {this.BOOLEAN, this.BYTE, this.SHORT, this.CHAR, this.INT, null, null, this.R0_127, this.R0_32767}, new TypeNode[0], new TypeNode[0], {this.BOOLEAN, this.R0_127, this.R0_127, this.R0_127, this.R0_127, null, null, this.R0_127, this.R0_127}, {this.BOOLEAN, this.R0_127, this.R0_32767, this.R0_32767, this.R0_32767, null, null, this.R0_127, this.R0_32767}};
      this.typeNodeMap = new HashMap();
      this.typeNodeMap.put(BooleanType.v(), this.BOOLEAN);
      this.typeNodeMap.put(ByteType.v(), this.BYTE);
      this.typeNodeMap.put(ShortType.v(), this.SHORT);
      this.typeNodeMap.put(CharType.v(), this.CHAR);
      this.typeNodeMap.put(IntType.v(), this.INT);
   }

   public static ClassHierarchy v() {
      return G.v().soot_jimple_toolkits_typing_integer_ClassHierarchy();
   }

   public TypeNode typeNode(Type type) {
      if (type != null && (type instanceof PrimType || type instanceof RefType)) {
         TypeNode typeNode = (TypeNode)this.typeNodeMap.get(type);
         if (typeNode == null) {
            throw new InternalTypingException();
         } else {
            return typeNode;
         }
      } else {
         throw new InternalTypingException(type);
      }
   }

   public boolean hasAncestor_1(int t1, int t2) {
      return this.ancestors_1[t1][t2];
   }

   public boolean hasAncestor_2(int t1, int t2) {
      return this.ancestors_2[t1][t2];
   }

   public boolean hasDescendant_1(int t1, int t2) {
      return this.descendants_1[t1][t2];
   }

   public boolean hasDescendant_2(int t1, int t2) {
      return this.descendants_2[t1][t2];
   }

   public TypeNode lca_1(int t1, int t2) {
      return this.lca_1[t1][t2];
   }

   private int convert(int n) {
      switch(n) {
      case 5:
         return 4;
      case 6:
         return 0;
      default:
         return n;
      }
   }

   public TypeNode lca_2(int t1, int t2) {
      return this.lca_2[this.convert(t1)][this.convert(t2)];
   }

   public TypeNode gcd_1(int t1, int t2) {
      return this.gcd_1[t1][t2];
   }

   public TypeNode gcd_2(int t1, int t2) {
      return this.gcd_2[this.convert(t1)][this.convert(t2)];
   }
}
