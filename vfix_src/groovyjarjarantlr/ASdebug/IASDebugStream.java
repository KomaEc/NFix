package groovyjarjarantlr.ASdebug;

import groovyjarjarantlr.Token;

public interface IASDebugStream {
   String getEntireText();

   TokenOffsetInfo getOffsetInfo(Token var1);
}
