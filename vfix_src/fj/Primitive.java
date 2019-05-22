package fj;

public final class Primitive {
   public static final F<Boolean, Byte> Boolean_Byte = new F<Boolean, Byte>() {
      public Byte f(Boolean b) {
         return (byte)(b ? 1 : 0);
      }
   };
   public static final F<Boolean, Character> Boolean_Character = new F<Boolean, Character>() {
      public Character f(Boolean b) {
         return (char)(b ? 1 : 0);
      }
   };
   public static final F<Boolean, Double> Boolean_Double = new F<Boolean, Double>() {
      public Double f(Boolean b) {
         return b ? 1.0D : 0.0D;
      }
   };
   public static final F<Boolean, Float> Boolean_Float = new F<Boolean, Float>() {
      public Float f(Boolean b) {
         return b ? 1.0F : 0.0F;
      }
   };
   public static final F<Boolean, Integer> Boolean_Integer = new F<Boolean, Integer>() {
      public Integer f(Boolean b) {
         return b ? 1 : 0;
      }
   };
   public static final F<Boolean, Long> Boolean_Long = new F<Boolean, Long>() {
      public Long f(Boolean b) {
         return b ? 1L : 0L;
      }
   };
   public static final F<Boolean, Short> Boolean_Short = new F<Boolean, Short>() {
      public Short f(Boolean b) {
         return (short)(b ? 1 : 0);
      }
   };
   public static final F<Byte, Boolean> Byte_Boolean = new F<Byte, Boolean>() {
      public Boolean f(Byte b) {
         return b != 0;
      }
   };
   public static final F<Byte, Character> Byte_Character = new F<Byte, Character>() {
      public Character f(Byte b) {
         return (char)b;
      }
   };
   public static final F<Byte, Double> Byte_Double = new F<Byte, Double>() {
      public Double f(Byte b) {
         return (double)b;
      }
   };
   public static final F<Byte, Float> Byte_Float = new F<Byte, Float>() {
      public Float f(Byte b) {
         return (float)b;
      }
   };
   public static final F<Byte, Integer> Byte_Integer = new F<Byte, Integer>() {
      public Integer f(Byte b) {
         return Integer.valueOf(b);
      }
   };
   public static final F<Byte, Long> Byte_Long = new F<Byte, Long>() {
      public Long f(Byte b) {
         return (long)b;
      }
   };
   public static final F<Byte, Short> Byte_Short = new F<Byte, Short>() {
      public Short f(Byte b) {
         return (short)b;
      }
   };
   public static final F<Character, Boolean> Character_Boolean = new F<Character, Boolean>() {
      public Boolean f(Character c) {
         return c != 0;
      }
   };
   public static final F<Character, Byte> Character_Byte = new F<Character, Byte>() {
      public Byte f(Character c) {
         return (byte)c;
      }
   };
   public static final F<Character, Double> Character_Double = new F<Character, Double>() {
      public Double f(Character c) {
         return (double)c;
      }
   };
   public static final F<Character, Float> Character_Float = new F<Character, Float>() {
      public Float f(Character c) {
         return (float)c;
      }
   };
   public static final F<Character, Integer> Character_Integer = new F<Character, Integer>() {
      public Integer f(Character c) {
         return Integer.valueOf(c);
      }
   };
   public static final F<Character, Long> Character_Long = new F<Character, Long>() {
      public Long f(Character c) {
         return (long)c;
      }
   };
   public static final F<Character, Short> Character_Short = new F<Character, Short>() {
      public Short f(Character c) {
         return (short)c;
      }
   };
   public static final F<Double, Boolean> Double_Boolean = new F<Double, Boolean>() {
      public Boolean f(Double d) {
         return d != 0.0D;
      }
   };
   public static final F<Double, Byte> Double_Byte = new F<Double, Byte>() {
      public Byte f(Double d) {
         return (byte)((int)d);
      }
   };
   public static final F<Double, Character> Double_Character = new F<Double, Character>() {
      public Character f(Double d) {
         return (char)((int)d);
      }
   };
   public static final F<Double, Float> Double_Float = new F<Double, Float>() {
      public Float f(Double d) {
         return (float)d;
      }
   };
   public static final F<Double, Integer> Double_Integer = new F<Double, Integer>() {
      public Integer f(Double d) {
         return (int)d;
      }
   };
   public static final F<Double, Long> Double_Long = new F<Double, Long>() {
      public Long f(Double d) {
         return (long)d;
      }
   };
   public static final F<Double, Short> Double_Short = new F<Double, Short>() {
      public Short f(Double d) {
         return (short)((int)d);
      }
   };
   public static final F<Float, Boolean> Float_Boolean = new F<Float, Boolean>() {
      public Boolean f(Float f) {
         return f != 0.0F;
      }
   };
   public static final F<Float, Byte> Float_Byte = new F<Float, Byte>() {
      public Byte f(Float f) {
         return (byte)((int)f);
      }
   };
   public static final F<Float, Character> Float_Character = new F<Float, Character>() {
      public Character f(Float f) {
         return (char)((int)f);
      }
   };
   public static final F<Float, Double> Float_Double = new F<Float, Double>() {
      public Double f(Float f) {
         return (double)f;
      }
   };
   public static final F<Float, Integer> Float_Integer = new F<Float, Integer>() {
      public Integer f(Float f) {
         return (int)f;
      }
   };
   public static final F<Float, Long> Float_Long = new F<Float, Long>() {
      public Long f(Float f) {
         return (long)f;
      }
   };
   public static final F<Float, Short> Float_Short = new F<Float, Short>() {
      public Short f(Float f) {
         return (short)((int)f);
      }
   };
   public static final F<Integer, Boolean> Integer_Boolean = new F<Integer, Boolean>() {
      public Boolean f(Integer i) {
         return i != 0;
      }
   };
   public static final F<Integer, Byte> Integer_Byte = new F<Integer, Byte>() {
      public Byte f(Integer i) {
         return (byte)i;
      }
   };
   public static final F<Integer, Character> Integer_Character = new F<Integer, Character>() {
      public Character f(Integer i) {
         return (char)i;
      }
   };
   public static final F<Integer, Double> Integer_Double = new F<Integer, Double>() {
      public Double f(Integer i) {
         return (double)i;
      }
   };
   public static final F<Integer, Float> Integer_Float = new F<Integer, Float>() {
      public Float f(Integer i) {
         return (float)i;
      }
   };
   public static final F<Integer, Long> Integer_Long = new F<Integer, Long>() {
      public Long f(Integer i) {
         return (long)i;
      }
   };
   public static final F<Integer, Short> Integer_Short = new F<Integer, Short>() {
      public Short f(Integer i) {
         return (short)i;
      }
   };
   public static final F<Long, Boolean> Long_Boolean = new F<Long, Boolean>() {
      public Boolean f(Long l) {
         return l != 0L;
      }
   };
   public static final F<Long, Byte> Long_Byte = new F<Long, Byte>() {
      public Byte f(Long l) {
         return (byte)((int)l);
      }
   };
   public static final F<Long, Character> Long_Character = new F<Long, Character>() {
      public Character f(Long l) {
         return (char)((int)l);
      }
   };
   public static final F<Long, Double> Long_Double = new F<Long, Double>() {
      public Double f(Long l) {
         return (double)l;
      }
   };
   public static final F<Long, Float> Long_Float = new F<Long, Float>() {
      public Float f(Long l) {
         return (float)l;
      }
   };
   public static final F<Long, Integer> Long_Integer = new F<Long, Integer>() {
      public Integer f(Long l) {
         return (int)l;
      }
   };
   public static final F<Long, Short> Long_Short = new F<Long, Short>() {
      public Short f(Long l) {
         return (short)((int)l);
      }
   };
   public static final F<Short, Boolean> Short_Boolean = new F<Short, Boolean>() {
      public Boolean f(Short s) {
         return s != 0;
      }
   };
   public static final F<Short, Byte> Short_Byte = new F<Short, Byte>() {
      public Byte f(Short s) {
         return (byte)s;
      }
   };
   public static final F<Short, Character> Short_Character = new F<Short, Character>() {
      public Character f(Short s) {
         return (char)s;
      }
   };
   public static final F<Short, Double> Short_Double = new F<Short, Double>() {
      public Double f(Short s) {
         return (double)s;
      }
   };
   public static final F<Short, Float> Short_Float = new F<Short, Float>() {
      public Float f(Short s) {
         return (float)s;
      }
   };
   public static final F<Short, Integer> Short_Integer = new F<Short, Integer>() {
      public Integer f(Short s) {
         return Integer.valueOf(s);
      }
   };
   public static final F<Short, Long> Short_Long = new F<Short, Long>() {
      public Long f(Short s) {
         return (long)s;
      }
   };

   private Primitive() {
      throw new UnsupportedOperationException();
   }
}
