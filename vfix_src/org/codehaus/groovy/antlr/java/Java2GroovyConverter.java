package org.codehaus.groovy.antlr.java;

import org.codehaus.groovy.antlr.GroovySourceAST;
import org.codehaus.groovy.antlr.treewalker.VisitorAdapter;

public class Java2GroovyConverter extends VisitorAdapter {
   private String[] tokenNames;
   private int[] typeMapping;

   public Java2GroovyConverter(String[] tokenNames) {
      this.tokenNames = tokenNames;
      this.typeMapping = new int[400];
      this.typeMapping[39] = 38;
      this.typeMapping[1] = 1;
      this.typeMapping[3] = 3;
      this.typeMapping[4] = 4;
      this.typeMapping[5] = 5;
      this.typeMapping[6] = 6;
      this.typeMapping[7] = 7;
      this.typeMapping[8] = 8;
      this.typeMapping[9] = 9;
      this.typeMapping[10] = 10;
      this.typeMapping[11] = 11;
      this.typeMapping[12] = 12;
      this.typeMapping[13] = 13;
      this.typeMapping[14] = 14;
      this.typeMapping[15] = 15;
      this.typeMapping[16] = 16;
      this.typeMapping[17] = 17;
      this.typeMapping[18] = 18;
      this.typeMapping[19] = 19;
      this.typeMapping[20] = 20;
      this.typeMapping[21] = 21;
      this.typeMapping[22] = 22;
      this.typeMapping[23] = 23;
      this.typeMapping[24] = 24;
      this.typeMapping[25] = 25;
      this.typeMapping[26] = 26;
      this.typeMapping[27] = 27;
      this.typeMapping[28] = 56;
      this.typeMapping[29] = 28;
      this.typeMapping[30] = 29;
      this.typeMapping[31] = 30;
      this.typeMapping[32] = 31;
      this.typeMapping[33] = 32;
      this.typeMapping[34] = 33;
      this.typeMapping[35] = 34;
      this.typeMapping[36] = 35;
      this.typeMapping[37] = 36;
      this.typeMapping[38] = 37;
      this.typeMapping[39] = 38;
      this.typeMapping[40] = 42;
      this.typeMapping[41] = 43;
      this.typeMapping[42] = 44;
      this.typeMapping[43] = 46;
      this.typeMapping[44] = 59;
      this.typeMapping[45] = 60;
      this.typeMapping[46] = 61;
      this.typeMapping[47] = 62;
      this.typeMapping[48] = 63;
      this.typeMapping[49] = 64;
      this.typeMapping[50] = 65;
      this.typeMapping[51] = 66;
      this.typeMapping[52] = 67;
      this.typeMapping[53] = 68;
      this.typeMapping[54] = 69;
      this.typeMapping[55] = 70;
      this.typeMapping[56] = 71;
      this.typeMapping[57] = 72;
      this.typeMapping[58] = 73;
      this.typeMapping[59] = 74;
      this.typeMapping[60] = 75;
      this.typeMapping[61] = 78;
      this.typeMapping[62] = 124;
      this.typeMapping[63] = 79;
      this.typeMapping[64] = 80;
      this.typeMapping[65] = 82;
      this.typeMapping[66] = 83;
      this.typeMapping[67] = 84;
      this.typeMapping[68] = 87;
      this.typeMapping[69] = 93;
      this.typeMapping[70] = 94;
      this.typeMapping[71] = 95;
      this.typeMapping[72] = 86;
      this.typeMapping[73] = 96;
      this.typeMapping[74] = 97;
      this.typeMapping[75] = 98;
      this.typeMapping[76] = 99;
      this.typeMapping[77] = 100;
      this.typeMapping[78] = 101;
      this.typeMapping[79] = 102;
      this.typeMapping[80] = 103;
      this.typeMapping[81] = 104;
      this.typeMapping[82] = 105;
      this.typeMapping[83] = 106;
      this.typeMapping[84] = 107;
      this.typeMapping[85] = 108;
      this.typeMapping[86] = 109;
      this.typeMapping[87] = 111;
      this.typeMapping[88] = 112;
      this.typeMapping[89] = 113;
      this.typeMapping[90] = 114;
      this.typeMapping[91] = 115;
      this.typeMapping[92] = 116;
      this.typeMapping[93] = 117;
      this.typeMapping[94] = 118;
      this.typeMapping[95] = 92;
      this.typeMapping[96] = 88;
      this.typeMapping[97] = 119;
      this.typeMapping[98] = 120;
      this.typeMapping[99] = 122;
      this.typeMapping[100] = 123;
      this.typeMapping[101] = 89;
      this.typeMapping[102] = 90;
      this.typeMapping[103] = 91;
      this.typeMapping[104] = 121;
      this.typeMapping[105] = 125;
      this.typeMapping[106] = 127;
      this.typeMapping[107] = 128;
      this.typeMapping[108] = 126;
      this.typeMapping[109] = 129;
      this.typeMapping[110] = 131;
      this.typeMapping[111] = 132;
      this.typeMapping[112] = 133;
      this.typeMapping[113] = 134;
      this.typeMapping[114] = 134;
      this.typeMapping[115] = 139;
      this.typeMapping[116] = 140;
      this.typeMapping[117] = 138;
      this.typeMapping[118] = 135;
      this.typeMapping[119] = 141;
      this.typeMapping[120] = 142;
      this.typeMapping[121] = 136;
      this.typeMapping[122] = 145;
      this.typeMapping[123] = 146;
      this.typeMapping[124] = 147;
      this.typeMapping[125] = 148;
      this.typeMapping[126] = 157;
      this.typeMapping[127] = 158;
      this.typeMapping[128] = 159;
      this.typeMapping[129] = 160;
      this.typeMapping[130] = 161;
      this.typeMapping[131] = 162;
      this.typeMapping[132] = 163;
      this.typeMapping[133] = 164;
      this.typeMapping[134] = 165;
      this.typeMapping[135] = 166;
      this.typeMapping[136] = 167;
      this.typeMapping[137] = 170;
      this.typeMapping[138] = 171;
      this.typeMapping[139] = 172;
      this.typeMapping[140] = 173;
      this.typeMapping[141] = 176;
      this.typeMapping[142] = 177;
      this.typeMapping[143] = 181;
      this.typeMapping[144] = 182;
      this.typeMapping[145] = 153;
      this.typeMapping[146] = 183;
      this.typeMapping[147] = 143;
      this.typeMapping[148] = 144;
      this.typeMapping[149] = 187;
      this.typeMapping[150] = 188;
      this.typeMapping[151] = 186;
      this.typeMapping[152] = 189;
      this.typeMapping[153] = 191;
      this.typeMapping[154] = 192;
      this.typeMapping[155] = 156;
      this.typeMapping[156] = 152;
      this.typeMapping[157] = 155;
      this.typeMapping[158] = 154;
      this.typeMapping[159] = 195;
      this.typeMapping[160] = 85;
      this.typeMapping[161] = 85;
      this.typeMapping[162] = 196;
      this.typeMapping[163] = 197;
      this.typeMapping[164] = 198;
      this.typeMapping[165] = 203;
      this.typeMapping[166] = 205;
      this.typeMapping[167] = 206;
      this.typeMapping[168] = 211;
      this.typeMapping[169] = 213;
      this.typeMapping[170] = 214;
      this.typeMapping[171] = 217;
      this.typeMapping[172] = 218;
   }

   public void visitDefault(GroovySourceAST t, int visit) {
      if (visit == 1) {
         t.setType(this.typeMapping[t.getType()]);
         if (t.getType() == 85) {
            String text = t.getText();
            if (this.isSingleQuoted(text)) {
               t.setText(text.substring(1, text.length() - 1));
            } else if (this.isDoubleQuoted(text)) {
               t.setText(text.substring(1, text.length() - 1));
            }
         }
      }

   }

   private boolean isSingleQuoted(String text) {
      return text != null && text.length() > 2 && text.charAt(0) == '\'' && text.charAt(text.length() - 1) == '\'';
   }

   private boolean isDoubleQuoted(String text) {
      return text != null && text.length() > 2 && text.charAt(0) == '"' && text.charAt(text.length() - 1) == '"';
   }
}
