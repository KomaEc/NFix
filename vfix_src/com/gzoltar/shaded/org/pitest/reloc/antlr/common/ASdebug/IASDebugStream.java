package com.gzoltar.shaded.org.pitest.reloc.antlr.common.ASdebug;

import com.gzoltar.shaded.org.pitest.reloc.antlr.common.Token;

public interface IASDebugStream {
   String getEntireText();

   TokenOffsetInfo getOffsetInfo(Token var1);
}
