package corg.vfix.pg.jimple;

public class OperationType {
   public static final int INIT_1 = 11;
   public static final int INIT_2 = 12;
   public static final int INIT_3 = 13;
   public static final int SKIP_1 = 21;
   public static final int SKIP_2 = 22;
   public static final int SKIP_3 = 23;
   public static final int NONE = 0;

   public static String typeToString(int type) {
      switch(type) {
      case 11:
         return "INIT_1";
      case 12:
         return "INIT_2";
      case 13:
         return "INIT_3";
      case 14:
      case 15:
      case 16:
      case 17:
      case 18:
      case 19:
      case 20:
      default:
         return "NONE";
      case 21:
         return "SKIP_1";
      case 22:
         return "SKIP_2";
      case 23:
         return "SKIP_3";
      }
   }
}
