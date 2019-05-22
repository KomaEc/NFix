package soot.tagkit;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Unit;
import soot.UnitBox;
import soot.baf.Baf;
import soot.options.Options;

public class CodeAttribute extends JasminAttribute {
   private static final Logger logger = LoggerFactory.getLogger(CodeAttribute.class);
   protected List<Unit> mUnits;
   protected List<Tag> mTags;
   private byte[] value;
   private String name = "CodeAtribute";

   public CodeAttribute() {
   }

   public CodeAttribute(String name) {
      this.name = name;
   }

   public CodeAttribute(String name, List<Unit> units, List<Tag> tags) {
      this.name = name;
      this.mUnits = units;
      this.mTags = tags;
   }

   public String toString() {
      return this.name;
   }

   public String getName() {
      return this.name;
   }

   public void setValue(byte[] v) {
      this.value = v;
   }

   public byte[] getValue() throws AttributeValueException {
      if (this.value == null) {
         throw new AttributeValueException();
      } else {
         return this.value;
      }
   }

   public String getJasminValue(Map<Unit, String> instToLabel) {
      StringBuffer buf = new StringBuffer();
      if (this.mTags.size() != this.mUnits.size()) {
         throw new RuntimeException("Sizes must match!");
      } else {
         Iterator<Tag> tagIt = this.mTags.iterator();
         Iterator unitIt = this.mUnits.iterator();

         while(tagIt.hasNext()) {
            Unit unit = (Unit)unitIt.next();
            Tag tag = (Tag)tagIt.next();
            buf.append("%" + (String)instToLabel.get(unit) + "%" + new String(Base64.encode(tag.getValue())));
         }

         return buf.toString();
      }
   }

   public List<UnitBox> getUnitBoxes() {
      List<UnitBox> unitBoxes = new ArrayList(this.mUnits.size());
      Iterator it = this.mUnits.iterator();

      while(it.hasNext()) {
         unitBoxes.add(Baf.v().newInstBox((Unit)it.next()));
      }

      return unitBoxes;
   }

   public byte[] decode(String attr, Hashtable<String, Integer> labelToPc) {
      if (Options.v().verbose()) {
         logger.debug("[] JasminAttribute decode...");
      }

      List<byte[]> attributeHunks = new LinkedList();
      int attributeSize = 0;
      StringTokenizer st = new StringTokenizer(attr, "%");
      boolean isLabel = false;
      if (attr.startsWith("%")) {
         isLabel = true;
      }

      int tablesize;
      for(tablesize = 0; st.hasMoreTokens(); isLabel = !isLabel) {
         String token = st.nextToken();
         if (isLabel) {
            Integer pc = (Integer)labelToPc.get(token);
            if (pc == null) {
               throw new RuntimeException("PC is null, the token is " + token);
            }

            int pcvalue = pc;
            if (pcvalue > 65535) {
               throw new RuntimeException("PC great than 65535, the token is " + token + " : " + pcvalue);
            }

            byte[] pcArray = new byte[]{(byte)(pcvalue >> 8 & 255), (byte)(pcvalue & 255)};
            attributeHunks.add(pcArray);
            attributeSize += 2;
            ++tablesize;
         } else {
            byte[] hunk = Base64.decode(token.toCharArray());
            attributeSize += hunk.length;
            attributeHunks.add(hunk);
         }
      }

      attributeSize += 2;
      byte[] attributeValue = new byte[attributeSize];
      attributeValue[0] = (byte)(tablesize >> 8 & 255);
      attributeValue[1] = (byte)(tablesize & 255);
      int index = 2;
      Iterator it = attributeHunks.iterator();

      while(it.hasNext()) {
         byte[] hunk = (byte[])it.next();
         byte[] var13 = hunk;
         int var14 = hunk.length;

         for(int var15 = 0; var15 < var14; ++var15) {
            byte element = var13[var15];
            attributeValue[index++] = element;
         }
      }

      if (index != attributeSize) {
         throw new RuntimeException("Index does not euqal to attrubute size :" + index + " -- " + attributeSize);
      } else {
         if (Options.v().verbose()) {
            logger.debug("[] Jasmin.decode finished...");
         }

         return attributeValue;
      }
   }
}
