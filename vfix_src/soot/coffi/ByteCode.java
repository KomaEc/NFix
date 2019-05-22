package soot.coffi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ByteCode {
   private static final Logger logger = LoggerFactory.getLogger(ByteCode.class);
   public static final int NOP = 0;
   public static final int ACONST_NULL = 1;
   public static final int ICONST_M1 = 2;
   public static final int ICONST_0 = 3;
   public static final int ICONST_1 = 4;
   public static final int ICONST_2 = 5;
   public static final int ICONST_3 = 6;
   public static final int ICONST_4 = 7;
   public static final int ICONST_5 = 8;
   public static final int LCONST_0 = 9;
   public static final int LCONST_1 = 10;
   public static final int FCONST_0 = 11;
   public static final int FCONST_1 = 12;
   public static final int FCONST_2 = 13;
   public static final int DCONST_0 = 14;
   public static final int DCONST_1 = 15;
   public static final int BIPUSH = 16;
   public static final int SIPUSH = 17;
   public static final int LDC1 = 18;
   public static final int LDC2 = 19;
   public static final int LDC2W = 20;
   public static final int ILOAD = 21;
   public static final int LLOAD = 22;
   public static final int FLOAD = 23;
   public static final int DLOAD = 24;
   public static final int ALOAD = 25;
   public static final int ILOAD_0 = 26;
   public static final int ILOAD_1 = 27;
   public static final int ILOAD_2 = 28;
   public static final int ILOAD_3 = 29;
   public static final int LLOAD_0 = 30;
   public static final int LLOAD_1 = 31;
   public static final int LLOAD_2 = 32;
   public static final int LLOAD_3 = 33;
   public static final int FLOAD_0 = 34;
   public static final int FLOAD_1 = 35;
   public static final int FLOAD_2 = 36;
   public static final int FLOAD_3 = 37;
   public static final int DLOAD_0 = 38;
   public static final int DLOAD_1 = 39;
   public static final int DLOAD_2 = 40;
   public static final int DLOAD_3 = 41;
   public static final int ALOAD_0 = 42;
   public static final int ALOAD_1 = 43;
   public static final int ALOAD_2 = 44;
   public static final int ALOAD_3 = 45;
   public static final int IALOAD = 46;
   public static final int LALOAD = 47;
   public static final int FALOAD = 48;
   public static final int DALOAD = 49;
   public static final int AALOAD = 50;
   public static final int BALOAD = 51;
   public static final int CALOAD = 52;
   public static final int SALOAD = 53;
   public static final int ISTORE = 54;
   public static final int LSTORE = 55;
   public static final int FSTORE = 56;
   public static final int DSTORE = 57;
   public static final int ASTORE = 58;
   public static final int ISTORE_0 = 59;
   public static final int ISTORE_1 = 60;
   public static final int ISTORE_2 = 61;
   public static final int ISTORE_3 = 62;
   public static final int LSTORE_0 = 63;
   public static final int LSTORE_1 = 64;
   public static final int LSTORE_2 = 65;
   public static final int LSTORE_3 = 66;
   public static final int FSTORE_0 = 67;
   public static final int FSTORE_1 = 68;
   public static final int FSTORE_2 = 69;
   public static final int FSTORE_3 = 70;
   public static final int DSTORE_0 = 71;
   public static final int DSTORE_1 = 72;
   public static final int DSTORE_2 = 73;
   public static final int DSTORE_3 = 74;
   public static final int ASTORE_0 = 75;
   public static final int ASTORE_1 = 76;
   public static final int ASTORE_2 = 77;
   public static final int ASTORE_3 = 78;
   public static final int IASTORE = 79;
   public static final int LASTORE = 80;
   public static final int FASTORE = 81;
   public static final int DASTORE = 82;
   public static final int AASTORE = 83;
   public static final int BASTORE = 84;
   public static final int CASTORE = 85;
   public static final int SASTORE = 86;
   public static final int POP = 87;
   public static final int POP2 = 88;
   public static final int DUP = 89;
   public static final int DUP_X1 = 90;
   public static final int DUP_X2 = 91;
   public static final int DUP2 = 92;
   public static final int DUP2_X1 = 93;
   public static final int DUP2_X2 = 94;
   public static final int SWAP = 95;
   public static final int IADD = 96;
   public static final int LADD = 97;
   public static final int FADD = 98;
   public static final int DADD = 99;
   public static final int ISUB = 100;
   public static final int LSUB = 101;
   public static final int FSUB = 102;
   public static final int DSUB = 103;
   public static final int IMUL = 104;
   public static final int LMUL = 105;
   public static final int FMUL = 106;
   public static final int DMUL = 107;
   public static final int IDIV = 108;
   public static final int LDIV = 109;
   public static final int FDIV = 110;
   public static final int DDIV = 111;
   public static final int IREM = 112;
   public static final int LREM = 113;
   public static final int FREM = 114;
   public static final int DREM = 115;
   public static final int INEG = 116;
   public static final int LNEG = 117;
   public static final int FNEG = 118;
   public static final int DNEG = 119;
   public static final int ISHL = 120;
   public static final int LSHL = 121;
   public static final int ISHR = 122;
   public static final int LSHR = 123;
   public static final int IUSHR = 124;
   public static final int LUSHR = 125;
   public static final int IAND = 126;
   public static final int LAND = 127;
   public static final int IOR = 128;
   public static final int LOR = 129;
   public static final int IXOR = 130;
   public static final int LXOR = 131;
   public static final int IINC = 132;
   public static final int I2L = 133;
   public static final int I2F = 134;
   public static final int I2D = 135;
   public static final int L2I = 136;
   public static final int L2F = 137;
   public static final int L2D = 138;
   public static final int F2I = 139;
   public static final int F2L = 140;
   public static final int F2D = 141;
   public static final int D2I = 142;
   public static final int D2L = 143;
   public static final int D2F = 144;
   public static final int INT2BYTE = 145;
   public static final int INT2CHAR = 146;
   public static final int INT2SHORT = 147;
   public static final int LCMP = 148;
   public static final int FCMPL = 149;
   public static final int FCMPG = 150;
   public static final int DCMPL = 151;
   public static final int DCMPG = 152;
   public static final int IFEQ = 153;
   public static final int IFNE = 154;
   public static final int IFLT = 155;
   public static final int IFGE = 156;
   public static final int IFGT = 157;
   public static final int IFLE = 158;
   public static final int IF_ICMPEQ = 159;
   public static final int IF_ICMPNE = 160;
   public static final int IF_ICMPLT = 161;
   public static final int IF_ICMPGE = 162;
   public static final int IF_ICMPGT = 163;
   public static final int IF_ICMPLE = 164;
   public static final int IF_ACMPEQ = 165;
   public static final int IF_ACMPNE = 166;
   public static final int GOTO = 167;
   public static final int JSR = 168;
   public static final int RET = 169;
   public static final int TABLESWITCH = 170;
   public static final int LOOKUPSWITCH = 171;
   public static final int IRETURN = 172;
   public static final int LRETURN = 173;
   public static final int FRETURN = 174;
   public static final int DRETURN = 175;
   public static final int ARETURN = 176;
   public static final int RETURN = 177;
   public static final int GETSTATIC = 178;
   public static final int PUTSTATIC = 179;
   public static final int GETFIELD = 180;
   public static final int PUTFIELD = 181;
   public static final int INVOKEVIRTUAL = 182;
   public static final int INVOKENONVIRTUAL = 183;
   public static final int INVOKESTATIC = 184;
   public static final int INVOKEINTERFACE = 185;
   public static final int INVOKEDYNAMIC = 186;
   public static final int NEW = 187;
   public static final int NEWARRAY = 188;
   public static final int ANEWARRAY = 189;
   public static final int ARRAYLENGTH = 190;
   public static final int ATHROW = 191;
   public static final int CHECKCAST = 192;
   public static final int INSTANCEOF = 193;
   public static final int MONITORENTER = 194;
   public static final int MONITOREXIT = 195;
   public static final int WIDE = 196;
   public static final int MULTIANEWARRAY = 197;
   public static final int IFNULL = 198;
   public static final int IFNONNULL = 199;
   public static final int GOTO_W = 200;
   public static final int JSR_W = 201;
   public static final int BREAKPOINT = 202;
   public static final int RET_W = 209;
   private int icount;
   private Instruction[] instructions;

   public Instruction disassemble_bytecode(byte[] bc, int index) {
      byte b = bc[index];
      boolean isWide = false;
      int x = b & 255;
      Object i;
      switch(x) {
      case 0:
         i = new Instruction_Nop();
         break;
      case 1:
         i = new Instruction_Aconst_null();
         break;
      case 2:
         i = new Instruction_Iconst_m1();
         break;
      case 3:
         i = new Instruction_Iconst_0();
         break;
      case 4:
         i = new Instruction_Iconst_1();
         break;
      case 5:
         i = new Instruction_Iconst_2();
         break;
      case 6:
         i = new Instruction_Iconst_3();
         break;
      case 7:
         i = new Instruction_Iconst_4();
         break;
      case 8:
         i = new Instruction_Iconst_5();
         break;
      case 9:
         i = new Instruction_Lconst_0();
         break;
      case 10:
         i = new Instruction_Lconst_1();
         break;
      case 11:
         i = new Instruction_Fconst_0();
         break;
      case 12:
         i = new Instruction_Fconst_1();
         break;
      case 13:
         i = new Instruction_Fconst_2();
         break;
      case 14:
         i = new Instruction_Dconst_0();
         break;
      case 15:
         i = new Instruction_Dconst_1();
         break;
      case 16:
         i = new Instruction_Bipush();
         break;
      case 17:
         i = new Instruction_Sipush();
         break;
      case 18:
         i = new Instruction_Ldc1();
         break;
      case 19:
         i = new Instruction_Ldc2();
         break;
      case 20:
         i = new Instruction_Ldc2w();
         break;
      case 21:
         i = new Instruction_Iload();
         break;
      case 22:
         i = new Instruction_Lload();
         break;
      case 23:
         i = new Instruction_Fload();
         break;
      case 24:
         i = new Instruction_Dload();
         break;
      case 25:
         i = new Instruction_Aload();
         break;
      case 26:
         i = new Instruction_Iload_0();
         break;
      case 27:
         i = new Instruction_Iload_1();
         break;
      case 28:
         i = new Instruction_Iload_2();
         break;
      case 29:
         i = new Instruction_Iload_3();
         break;
      case 30:
         i = new Instruction_Lload_0();
         break;
      case 31:
         i = new Instruction_Lload_1();
         break;
      case 32:
         i = new Instruction_Lload_2();
         break;
      case 33:
         i = new Instruction_Lload_3();
         break;
      case 34:
         i = new Instruction_Fload_0();
         break;
      case 35:
         i = new Instruction_Fload_1();
         break;
      case 36:
         i = new Instruction_Fload_2();
         break;
      case 37:
         i = new Instruction_Fload_3();
         break;
      case 38:
         i = new Instruction_Dload_0();
         break;
      case 39:
         i = new Instruction_Dload_1();
         break;
      case 40:
         i = new Instruction_Dload_2();
         break;
      case 41:
         i = new Instruction_Dload_3();
         break;
      case 42:
         i = new Instruction_Aload_0();
         break;
      case 43:
         i = new Instruction_Aload_1();
         break;
      case 44:
         i = new Instruction_Aload_2();
         break;
      case 45:
         i = new Instruction_Aload_3();
         break;
      case 46:
         i = new Instruction_Iaload();
         break;
      case 47:
         i = new Instruction_Laload();
         break;
      case 48:
         i = new Instruction_Faload();
         break;
      case 49:
         i = new Instruction_Daload();
         break;
      case 50:
         i = new Instruction_Aaload();
         break;
      case 51:
         i = new Instruction_Baload();
         break;
      case 52:
         i = new Instruction_Caload();
         break;
      case 53:
         i = new Instruction_Saload();
         break;
      case 54:
         i = new Instruction_Istore();
         break;
      case 55:
         i = new Instruction_Lstore();
         break;
      case 56:
         i = new Instruction_Fstore();
         break;
      case 57:
         i = new Instruction_Dstore();
         break;
      case 58:
         i = new Instruction_Astore();
         break;
      case 59:
         i = new Instruction_Istore_0();
         break;
      case 60:
         i = new Instruction_Istore_1();
         break;
      case 61:
         i = new Instruction_Istore_2();
         break;
      case 62:
         i = new Instruction_Istore_3();
         break;
      case 63:
         i = new Instruction_Lstore_0();
         break;
      case 64:
         i = new Instruction_Lstore_1();
         break;
      case 65:
         i = new Instruction_Lstore_2();
         break;
      case 66:
         i = new Instruction_Lstore_3();
         break;
      case 67:
         i = new Instruction_Fstore_0();
         break;
      case 68:
         i = new Instruction_Fstore_1();
         break;
      case 69:
         i = new Instruction_Fstore_2();
         break;
      case 70:
         i = new Instruction_Fstore_3();
         break;
      case 71:
         i = new Instruction_Dstore_0();
         break;
      case 72:
         i = new Instruction_Dstore_1();
         break;
      case 73:
         i = new Instruction_Dstore_2();
         break;
      case 74:
         i = new Instruction_Dstore_3();
         break;
      case 75:
         i = new Instruction_Astore_0();
         break;
      case 76:
         i = new Instruction_Astore_1();
         break;
      case 77:
         i = new Instruction_Astore_2();
         break;
      case 78:
         i = new Instruction_Astore_3();
         break;
      case 79:
         i = new Instruction_Iastore();
         break;
      case 80:
         i = new Instruction_Lastore();
         break;
      case 81:
         i = new Instruction_Fastore();
         break;
      case 82:
         i = new Instruction_Dastore();
         break;
      case 83:
         i = new Instruction_Aastore();
         break;
      case 84:
         i = new Instruction_Bastore();
         break;
      case 85:
         i = new Instruction_Castore();
         break;
      case 86:
         i = new Instruction_Sastore();
         break;
      case 87:
         i = new Instruction_Pop();
         break;
      case 88:
         i = new Instruction_Pop2();
         break;
      case 89:
         i = new Instruction_Dup();
         break;
      case 90:
         i = new Instruction_Dup_x1();
         break;
      case 91:
         i = new Instruction_Dup_x2();
         break;
      case 92:
         i = new Instruction_Dup2();
         break;
      case 93:
         i = new Instruction_Dup2_x1();
         break;
      case 94:
         i = new Instruction_Dup2_x2();
         break;
      case 95:
         i = new Instruction_Swap();
         break;
      case 96:
         i = new Instruction_Iadd();
         break;
      case 97:
         i = new Instruction_Ladd();
         break;
      case 98:
         i = new Instruction_Fadd();
         break;
      case 99:
         i = new Instruction_Dadd();
         break;
      case 100:
         i = new Instruction_Isub();
         break;
      case 101:
         i = new Instruction_Lsub();
         break;
      case 102:
         i = new Instruction_Fsub();
         break;
      case 103:
         i = new Instruction_Dsub();
         break;
      case 104:
         i = new Instruction_Imul();
         break;
      case 105:
         i = new Instruction_Lmul();
         break;
      case 106:
         i = new Instruction_Fmul();
         break;
      case 107:
         i = new Instruction_Dmul();
         break;
      case 108:
         i = new Instruction_Idiv();
         break;
      case 109:
         i = new Instruction_Ldiv();
         break;
      case 110:
         i = new Instruction_Fdiv();
         break;
      case 111:
         i = new Instruction_Ddiv();
         break;
      case 112:
         i = new Instruction_Irem();
         break;
      case 113:
         i = new Instruction_Lrem();
         break;
      case 114:
         i = new Instruction_Frem();
         break;
      case 115:
         i = new Instruction_Drem();
         break;
      case 116:
         i = new Instruction_Ineg();
         break;
      case 117:
         i = new Instruction_Lneg();
         break;
      case 118:
         i = new Instruction_Fneg();
         break;
      case 119:
         i = new Instruction_Dneg();
         break;
      case 120:
         i = new Instruction_Ishl();
         break;
      case 121:
         i = new Instruction_Lshl();
         break;
      case 122:
         i = new Instruction_Ishr();
         break;
      case 123:
         i = new Instruction_Lshr();
         break;
      case 124:
         i = new Instruction_Iushr();
         break;
      case 125:
         i = new Instruction_Lushr();
         break;
      case 126:
         i = new Instruction_Iand();
         break;
      case 127:
         i = new Instruction_Land();
         break;
      case 128:
         i = new Instruction_Ior();
         break;
      case 129:
         i = new Instruction_Lor();
         break;
      case 130:
         i = new Instruction_Ixor();
         break;
      case 131:
         i = new Instruction_Lxor();
         break;
      case 132:
         i = new Instruction_Iinc();
         break;
      case 133:
         i = new Instruction_I2l();
         break;
      case 134:
         i = new Instruction_I2f();
         break;
      case 135:
         i = new Instruction_I2d();
         break;
      case 136:
         i = new Instruction_L2i();
         break;
      case 137:
         i = new Instruction_L2f();
         break;
      case 138:
         i = new Instruction_L2d();
         break;
      case 139:
         i = new Instruction_F2i();
         break;
      case 140:
         i = new Instruction_F2l();
         break;
      case 141:
         i = new Instruction_F2d();
         break;
      case 142:
         i = new Instruction_D2i();
         break;
      case 143:
         i = new Instruction_D2l();
         break;
      case 144:
         i = new Instruction_D2f();
         break;
      case 145:
         i = new Instruction_Int2byte();
         break;
      case 146:
         i = new Instruction_Int2char();
         break;
      case 147:
         i = new Instruction_Int2short();
         break;
      case 148:
         i = new Instruction_Lcmp();
         break;
      case 149:
         i = new Instruction_Fcmpl();
         break;
      case 150:
         i = new Instruction_Fcmpg();
         break;
      case 151:
         i = new Instruction_Dcmpl();
         break;
      case 152:
         i = new Instruction_Dcmpg();
         break;
      case 153:
         i = new Instruction_Ifeq();
         break;
      case 154:
         i = new Instruction_Ifne();
         break;
      case 155:
         i = new Instruction_Iflt();
         break;
      case 156:
         i = new Instruction_Ifge();
         break;
      case 157:
         i = new Instruction_Ifgt();
         break;
      case 158:
         i = new Instruction_Ifle();
         break;
      case 159:
         i = new Instruction_If_icmpeq();
         break;
      case 160:
         i = new Instruction_If_icmpne();
         break;
      case 161:
         i = new Instruction_If_icmplt();
         break;
      case 162:
         i = new Instruction_If_icmpge();
         break;
      case 163:
         i = new Instruction_If_icmpgt();
         break;
      case 164:
         i = new Instruction_If_icmple();
         break;
      case 165:
         i = new Instruction_If_acmpeq();
         break;
      case 166:
         i = new Instruction_If_acmpne();
         break;
      case 167:
         i = new Instruction_Goto();
         break;
      case 168:
         i = new Instruction_Jsr();
         break;
      case 169:
         i = new Instruction_Ret();
         break;
      case 170:
         i = new Instruction_Tableswitch();
         break;
      case 171:
         i = new Instruction_Lookupswitch();
         break;
      case 172:
         i = new Instruction_Ireturn();
         break;
      case 173:
         i = new Instruction_Lreturn();
         break;
      case 174:
         i = new Instruction_Freturn();
         break;
      case 175:
         i = new Instruction_Dreturn();
         break;
      case 176:
         i = new Instruction_Areturn();
         break;
      case 177:
         i = new Instruction_Return();
         break;
      case 178:
         i = new Instruction_Getstatic();
         break;
      case 179:
         i = new Instruction_Putstatic();
         break;
      case 180:
         i = new Instruction_Getfield();
         break;
      case 181:
         i = new Instruction_Putfield();
         break;
      case 182:
         i = new Instruction_Invokevirtual();
         break;
      case 183:
         i = new Instruction_Invokenonvirtual();
         break;
      case 184:
         i = new Instruction_Invokestatic();
         break;
      case 185:
         i = new Instruction_Invokeinterface();
         break;
      case 186:
         i = new Instruction_Invokedynamic();
         break;
      case 187:
         i = new Instruction_New();
         break;
      case 188:
         i = new Instruction_Newarray();
         break;
      case 189:
         i = new Instruction_Anewarray();
         break;
      case 190:
         i = new Instruction_Arraylength();
         break;
      case 191:
         i = new Instruction_Athrow();
         break;
      case 192:
         i = new Instruction_Checkcast();
         break;
      case 193:
         i = new Instruction_Instanceof();
         break;
      case 194:
         i = new Instruction_Monitorenter();
         break;
      case 195:
         i = new Instruction_Monitorexit();
         break;
      case 196:
         int nextIndex = bc[index + 1] & 255;
         switch(nextIndex) {
         case 21:
            i = new Instruction_Iload();
            break;
         case 22:
            i = new Instruction_Lload();
            break;
         case 23:
            i = new Instruction_Fload();
            break;
         case 24:
            i = new Instruction_Dload();
            break;
         case 25:
            i = new Instruction_Aload();
            break;
         case 54:
            i = new Instruction_Istore();
            break;
         case 55:
            i = new Instruction_Lstore();
            break;
         case 56:
            i = new Instruction_Fstore();
            break;
         case 57:
            i = new Instruction_Dstore();
            break;
         case 58:
            i = new Instruction_Astore();
            break;
         case 132:
            i = new Instruction_Iinc();
            break;
         case 169:
            i = new Instruction_Ret();
            break;
         default:
            throw new RuntimeException("invalid wide instruction: " + nextIndex);
         }

         ((Instruction_bytevar)i).isWide = true;
         isWide = true;
         break;
      case 197:
         i = new Instruction_Multianewarray();
         break;
      case 198:
         i = new Instruction_Ifnull();
         break;
      case 199:
         i = new Instruction_Ifnonnull();
         break;
      case 200:
         i = new Instruction_Goto_w();
         break;
      case 201:
         i = new Instruction_Jsr_w();
         break;
      case 202:
         i = new Instruction_Breakpoint();
         break;
      case 203:
      case 204:
      case 205:
      case 206:
      case 207:
      case 208:
      default:
         i = new Instruction_Unknown(b);
         break;
      case 209:
         i = new Instruction_Ret_w();
      }

      ((Instruction)i).label = index;
      if (isWide) {
         ((Instruction)i).parse(bc, index + 2);
      } else {
         ((Instruction)i).parse(bc, index + 1);
      }

      return (Instruction)i;
   }

   public void build(Instruction insts) {
      Instruction i = insts;

      for(this.icount = 0; i != null; i = i.next) {
         ++this.icount;
      }

      if (this.icount > 0) {
         this.instructions = new Instruction[this.icount];
         int k = 0;

         for(i = insts; i != null; i = i.next) {
            this.instructions[k] = i;
            ++k;
         }

         for(i = insts; i != null; i = i.next) {
            i.offsetToPointer(this);
         }
      }

   }

   public static void showCode(Instruction inst, cp_info[] constant_pool) {
      showCode(inst, 0, constant_pool);
   }

   public static void showCode(Instruction inst, int startinst, cp_info[] constant_pool) {
      Instruction j = inst;

      for(int i = startinst; j != null; j = j.next) {
         String pref;
         if (i > 999) {
            pref = "";
         } else if (i > 99) {
            pref = " ";
         } else if (i > 9) {
            pref = "  ";
         } else {
            pref = "   ";
         }

         logger.debug("" + pref + i + ": ");
         logger.debug("" + j.toString(constant_pool));
         i = j.nextOffset(i);
      }

   }

   public Instruction locateInst(int index) {
      return this.locateInstr(index, 0, this.icount);
   }

   private Instruction locateInstr(int index, int mini, int maxi) {
      int mid = (maxi - mini) / 2 + mini;
      if (mini > maxi) {
         return null;
      } else if (this.instructions[mid].label == index) {
         return this.instructions[mid];
      } else {
         return this.instructions[mid].label > index ? this.locateInstr(index, mini, mid - 1) : this.locateInstr(index, mid + 1, maxi);
      }
   }

   public static boolean isLocalStore(int bc) {
      switch(bc) {
      case 54:
      case 55:
      case 56:
      case 57:
      case 58:
      case 59:
      case 60:
      case 61:
      case 62:
      case 63:
      case 64:
      case 65:
      case 66:
      case 67:
      case 68:
      case 69:
      case 70:
      case 71:
      case 72:
      case 73:
      case 74:
      case 75:
      case 76:
      case 77:
      case 78:
         return true;
      default:
         return false;
      }
   }
}
