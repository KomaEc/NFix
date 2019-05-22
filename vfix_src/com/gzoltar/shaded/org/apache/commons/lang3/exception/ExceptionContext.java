package com.gzoltar.shaded.org.apache.commons.lang3.exception;

import com.gzoltar.shaded.org.apache.commons.lang3.tuple.Pair;
import java.util.List;
import java.util.Set;

public interface ExceptionContext {
   ExceptionContext addContextValue(String var1, Object var2);

   ExceptionContext setContextValue(String var1, Object var2);

   List<Object> getContextValues(String var1);

   Object getFirstContextValue(String var1);

   Set<String> getContextLabels();

   List<Pair<String, Object>> getContextEntries();

   String getFormattedExceptionMessage(String var1);
}
