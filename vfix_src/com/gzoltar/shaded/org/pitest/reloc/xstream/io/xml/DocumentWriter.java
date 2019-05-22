package com.gzoltar.shaded.org.pitest.reloc.xstream.io.xml;

import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamWriter;
import java.util.List;

public interface DocumentWriter extends HierarchicalStreamWriter {
   List getTopLevelNodes();
}
