package com.gzoltar.shaded.org.objectweb.asm.util;

import com.gzoltar.shaded.org.objectweb.asm.Label;
import java.util.Map;

public interface Textifiable {
   void textify(StringBuffer var1, Map<Label, String> var2);
}
