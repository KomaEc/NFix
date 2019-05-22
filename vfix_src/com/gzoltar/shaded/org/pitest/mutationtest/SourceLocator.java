package com.gzoltar.shaded.org.pitest.mutationtest;

import com.gzoltar.shaded.org.pitest.functional.Option;
import java.io.Reader;
import java.util.Collection;

public interface SourceLocator {
   Option<Reader> locate(Collection<String> var1, String var2);
}
