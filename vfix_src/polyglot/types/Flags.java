package polyglot.types;

import java.io.Serializable;
import polyglot.util.InternalCompilerError;

public class Flags implements Serializable {
   static final int[] print_order = new int[64];
   static int next_bit = 0;
   static final String[] flag_names = new String[64];
   static final String[] c_body_flag_names = new String[64];
   static final String[] c_head_flag_names = new String[64];
   public static final Flags NONE = new Flags(0L);
   public static final Flags PUBLIC = createFlag("public", "", "", (Flags)null);
   public static final Flags PRIVATE = createFlag("private", "", "", (Flags)null);
   public static final Flags PROTECTED = createFlag("protected", "", "", (Flags)null);
   public static final Flags STATIC = createFlag("static", "static", "", (Flags)null);
   public static final Flags FINAL = createFlag("final", "", "", (Flags)null);
   public static final Flags SYNCHRONIZED = createFlag("synchronized", "", "", (Flags)null);
   public static final Flags TRANSIENT = createFlag("transient", "", "", (Flags)null);
   public static final Flags NATIVE = createFlag("native", "", "", (Flags)null);
   public static final Flags INTERFACE = createFlag("interface", "", "", (Flags)null);
   public static final Flags ABSTRACT = createFlag("abstract", "", "", (Flags)null);
   public static final Flags VOLATILE = createFlag("volatile", "", "", (Flags)null);
   public static final Flags STRICTFP = createFlag("strictfp", "", "", (Flags)null);
   protected static final Flags ACCESS_FLAGS;
   protected long bits;

   public static Flags createFlag(String name, Flags after) {
      return createFlag(name, "", "", after);
   }

   public static Flags createFlag(String name, String cHeadName, String cBodyName, Flags after) {
      if (next_bit >= flag_names.length) {
         throw new InternalCompilerError("too many flags");
      } else if (print_order[next_bit] != 0) {
         throw new InternalCompilerError("print_order and next_bit inconsistent");
      } else if (flag_names[next_bit] != null) {
         throw new InternalCompilerError("flag_names and next_bit inconsistent");
      } else {
         int bit = next_bit++;
         flag_names[bit] = name;
         c_head_flag_names[bit] = cHeadName;
         c_body_flag_names[bit] = cBodyName;
         if (after == null) {
            print_order[bit] = bit;
         } else {
            for(int i = bit; i > 0 && (after.bits & (long)print_order[i]) == 0L; --i) {
               print_order[i] = print_order[i - 1];
               print_order[i - 1] = bit;
            }
         }

         return new Flags(1L << bit);
      }
   }

   protected Flags(long bits) {
      this.bits = bits;
   }

   public Flags set(Flags other) {
      return new Flags(this.bits | other.bits);
   }

   public Flags clear(Flags other) {
      return new Flags(this.bits & ~other.bits);
   }

   public Flags retain(Flags other) {
      return new Flags(this.bits & other.bits);
   }

   public boolean intersects(Flags other) {
      return (this.bits & other.bits) != 0L;
   }

   public boolean contains(Flags other) {
      return (this.bits & other.bits) == other.bits;
   }

   public Flags Public() {
      return this.set(PUBLIC);
   }

   public Flags clearPublic() {
      return this.clear(PUBLIC);
   }

   public boolean isPublic() {
      return this.contains(PUBLIC);
   }

   public Flags Private() {
      return this.set(PRIVATE);
   }

   public Flags clearPrivate() {
      return this.clear(PRIVATE);
   }

   public boolean isPrivate() {
      return this.contains(PRIVATE);
   }

   public Flags Protected() {
      return this.set(PROTECTED);
   }

   public Flags clearProtected() {
      return this.clear(PROTECTED);
   }

   public boolean isProtected() {
      return this.contains(PROTECTED);
   }

   public Flags Package() {
      return this.clear(ACCESS_FLAGS);
   }

   public boolean isPackage() {
      return !this.intersects(ACCESS_FLAGS);
   }

   public Flags Static() {
      return this.set(STATIC);
   }

   public Flags clearStatic() {
      return this.clear(STATIC);
   }

   public boolean isStatic() {
      return this.contains(STATIC);
   }

   public Flags Final() {
      return this.set(FINAL);
   }

   public Flags clearFinal() {
      return this.clear(FINAL);
   }

   public boolean isFinal() {
      return this.contains(FINAL);
   }

   public Flags Synchronized() {
      return this.set(SYNCHRONIZED);
   }

   public Flags clearSynchronized() {
      return this.clear(SYNCHRONIZED);
   }

   public boolean isSynchronized() {
      return this.contains(SYNCHRONIZED);
   }

   public Flags Transient() {
      return this.set(TRANSIENT);
   }

   public Flags clearTransient() {
      return this.clear(TRANSIENT);
   }

   public boolean isTransient() {
      return this.contains(TRANSIENT);
   }

   public Flags Native() {
      return this.set(NATIVE);
   }

   public Flags clearNative() {
      return this.clear(NATIVE);
   }

   public boolean isNative() {
      return this.contains(NATIVE);
   }

   public Flags Interface() {
      return this.set(INTERFACE);
   }

   public Flags clearInterface() {
      return this.clear(INTERFACE);
   }

   public boolean isInterface() {
      return this.contains(INTERFACE);
   }

   public Flags Abstract() {
      return this.set(ABSTRACT);
   }

   public Flags clearAbstract() {
      return this.clear(ABSTRACT);
   }

   public boolean isAbstract() {
      return this.contains(ABSTRACT);
   }

   public Flags Volatile() {
      return this.set(VOLATILE);
   }

   public Flags clearVolatile() {
      return this.clear(VOLATILE);
   }

   public boolean isVolatile() {
      return this.contains(VOLATILE);
   }

   public Flags StrictFP() {
      return this.set(STRICTFP);
   }

   public Flags clearStrictFP() {
      return this.clear(STRICTFP);
   }

   public boolean isStrictFP() {
      return this.contains(STRICTFP);
   }

   public boolean moreRestrictiveThan(Flags f) {
      if (!this.isPrivate() || !f.isProtected() && !f.isPackage() && !f.isPublic()) {
         if (this.isPackage() && (f.isProtected() || f.isPublic())) {
            return true;
         } else {
            return this.isProtected() && f.isPublic();
         }
      } else {
         return true;
      }
   }

   public String toString() {
      return this.translate().trim();
   }

   public String translate() {
      StringBuffer sb = new StringBuffer();

      for(int i = 0; i < next_bit; ++i) {
         int bit = print_order[i];
         if ((this.bits & 1L << bit) != 0L) {
            sb.append(flag_names[bit]);
            sb.append(" ");
         }
      }

      return sb.toString();
   }

   public String translateCBody() {
      StringBuffer sb = new StringBuffer();

      for(int i = 0; i < next_bit; ++i) {
         int bit = print_order[i];
         if ((this.bits & 1L << bit) != 0L) {
            sb.append(c_body_flag_names[bit]);
            sb.append(" ");
         }
      }

      return sb.toString();
   }

   public String translateCHead() {
      StringBuffer sb = new StringBuffer();

      for(int i = 0; i < next_bit; ++i) {
         int bit = print_order[i];
         if ((this.bits & 1L << bit) != 0L) {
            sb.append(c_head_flag_names[bit]);
            sb.append(" ");
         }
      }

      return sb.toString();
   }

   public int hashCode() {
      return (int)(this.bits >> 32 | this.bits) * 37;
   }

   public boolean equals(Object o) {
      return o instanceof Flags && this.bits == ((Flags)o).bits;
   }

   static {
      ACCESS_FLAGS = PUBLIC.set(PRIVATE).set(PROTECTED);
   }
}
