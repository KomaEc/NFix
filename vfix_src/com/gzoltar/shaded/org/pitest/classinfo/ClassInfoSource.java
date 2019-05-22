package com.gzoltar.shaded.org.pitest.classinfo;

import com.gzoltar.shaded.org.pitest.functional.Option;

public interface ClassInfoSource {
   Option<ClassInfo> fetchClass(ClassName var1);
}
