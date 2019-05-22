package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.extended;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.reflection.AbstractAttributedCharacterIteratorAttributeConverter;
import java.awt.font.TextAttribute;

public class TextAttributeConverter extends AbstractAttributedCharacterIteratorAttributeConverter {
   public TextAttributeConverter() {
      super(TextAttribute.class);
   }
}
