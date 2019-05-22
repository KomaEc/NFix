package org.jf.dexlib2.dexbacked.raw;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.dexbacked.DexReader;
import org.jf.dexlib2.dexbacked.raw.util.DexAnnotator;
import org.jf.dexlib2.util.AnnotatedBytes;

public class DebugInfoItem {
   @Nonnull
   public static SectionAnnotator makeAnnotator(@Nonnull DexAnnotator annotator, @Nonnull MapItem mapItem) {
      return new SectionAnnotator(annotator, mapItem) {
         @Nonnull
         public String getItemName() {
            return "debug_info_item";
         }

         public void annotateItem(@Nonnull AnnotatedBytes out, int itemIndex, @Nullable String itemIdentity) {
            DexReader reader = this.dexFile.readerAt(out.getCursor());
            int lineStart = reader.readBigUleb128();
            out.annotateTo(reader.getOffset(), "line_start = %d", (long)lineStart & 4294967295L);
            int parametersSize = reader.readSmallUleb128();
            out.annotateTo(reader.getOffset(), "parameters_size = %d", parametersSize);
            int codeAddress;
            int lineNumber;
            if (parametersSize > 0) {
               out.annotate(0, "parameters:");
               out.indent();

               for(codeAddress = 0; codeAddress < parametersSize; ++codeAddress) {
                  lineNumber = reader.readSmallUleb128() - 1;
                  out.annotateTo(reader.getOffset(), "%s", StringIdItem.getOptionalReferenceAnnotation(this.dexFile, lineNumber, true));
               }

               out.deindent();
            }

            out.annotate(0, "debug opcodes:");
            out.indent();
            codeAddress = 0;
            lineNumber = lineStart;

            while(true) {
               int opcode = reader.readUbyte();
               int nameIdx;
               int nameIndex;
               int typeIndex;
               switch(opcode) {
               case 0:
                  out.annotateTo(reader.getOffset(), "DBG_END_SEQUENCE");
                  out.deindent();
                  return;
               case 1:
                  out.annotateTo(reader.getOffset(), "DBG_ADVANCE_PC");
                  out.indent();
                  nameIdx = reader.readSmallUleb128();
                  codeAddress += nameIdx;
                  out.annotateTo(reader.getOffset(), "addr_diff = +0x%x: 0x%x", nameIdx, codeAddress);
                  out.deindent();
                  break;
               case 2:
                  out.annotateTo(reader.getOffset(), "DBG_ADVANCE_LINE");
                  out.indent();
                  nameIdx = reader.readSleb128();
                  lineNumber += nameIdx;
                  out.annotateTo(reader.getOffset(), "line_diff = +%d: %d", Math.abs(nameIdx), lineNumber);
                  out.deindent();
                  break;
               case 3:
                  out.annotateTo(reader.getOffset(), "DBG_START_LOCAL");
                  out.indent();
                  nameIdx = reader.readSmallUleb128();
                  out.annotateTo(reader.getOffset(), "register_num = v%d", nameIdx);
                  nameIndex = reader.readSmallUleb128() - 1;
                  out.annotateTo(reader.getOffset(), "name_idx = %s", StringIdItem.getOptionalReferenceAnnotation(this.dexFile, nameIndex, true));
                  typeIndex = reader.readSmallUleb128() - 1;
                  out.annotateTo(reader.getOffset(), "type_idx = %s", TypeIdItem.getOptionalReferenceAnnotation(this.dexFile, typeIndex));
                  out.deindent();
                  break;
               case 4:
                  out.annotateTo(reader.getOffset(), "DBG_START_LOCAL_EXTENDED");
                  out.indent();
                  nameIdx = reader.readSmallUleb128();
                  out.annotateTo(reader.getOffset(), "register_num = v%d", nameIdx);
                  nameIndex = reader.readSmallUleb128() - 1;
                  out.annotateTo(reader.getOffset(), "name_idx = %s", StringIdItem.getOptionalReferenceAnnotation(this.dexFile, nameIndex, true));
                  typeIndex = reader.readSmallUleb128() - 1;
                  out.annotateTo(reader.getOffset(), "type_idx = %s", TypeIdItem.getOptionalReferenceAnnotation(this.dexFile, typeIndex));
                  int sigIndex = reader.readSmallUleb128() - 1;
                  out.annotateTo(reader.getOffset(), "sig_idx = %s", StringIdItem.getOptionalReferenceAnnotation(this.dexFile, sigIndex, true));
                  out.deindent();
                  break;
               case 5:
                  out.annotateTo(reader.getOffset(), "DBG_END_LOCAL");
                  out.indent();
                  nameIdx = reader.readSmallUleb128();
                  out.annotateTo(reader.getOffset(), "register_num = v%d", nameIdx);
                  out.deindent();
                  break;
               case 6:
                  out.annotateTo(reader.getOffset(), "DBG_RESTART_LOCAL");
                  out.indent();
                  nameIdx = reader.readSmallUleb128();
                  out.annotateTo(reader.getOffset(), "register_num = v%d", nameIdx);
                  out.deindent();
                  break;
               case 7:
                  out.annotateTo(reader.getOffset(), "DBG_SET_PROLOGUE_END");
                  break;
               case 8:
                  out.annotateTo(reader.getOffset(), "DBG_SET_EPILOGUE_BEGIN");
                  break;
               case 9:
                  out.annotateTo(reader.getOffset(), "DBG_SET_FILE");
                  out.indent();
                  nameIdx = reader.readSmallUleb128() - 1;
                  out.annotateTo(reader.getOffset(), "name_idx = %s", StringIdItem.getOptionalReferenceAnnotation(this.dexFile, nameIdx));
                  out.deindent();
                  break;
               default:
                  nameIdx = opcode - 10;
                  nameIndex = nameIdx / 15;
                  typeIndex = nameIdx % 15 - 4;
                  codeAddress += nameIndex;
                  lineNumber += typeIndex;
                  out.annotateTo(reader.getOffset(), "address_diff = +0x%x:0x%x, line_diff = +%d:%d, ", nameIndex, codeAddress, typeIndex, lineNumber);
               }
            }
         }
      };
   }
}
