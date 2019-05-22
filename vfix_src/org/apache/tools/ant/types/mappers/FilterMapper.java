package org.apache.tools.ant.types.mappers;

import java.io.Reader;
import java.io.StringReader;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.UnsupportedAttributeException;
import org.apache.tools.ant.filters.util.ChainReaderHelper;
import org.apache.tools.ant.types.FilterChain;
import org.apache.tools.ant.util.FileNameMapper;
import org.apache.tools.ant.util.FileUtils;

public class FilterMapper extends FilterChain implements FileNameMapper {
   public void setFrom(String from) {
      throw new UnsupportedAttributeException("filtermapper doesn't support the \"from\" attribute.", "from");
   }

   public void setTo(String to) {
      throw new UnsupportedAttributeException("filtermapper doesn't support the \"to\" attribute.", "to");
   }

   public String[] mapFileName(String sourceFileName) {
      try {
         Reader stringReader = new StringReader(sourceFileName);
         ChainReaderHelper helper = new ChainReaderHelper();
         helper.setBufferSize(8192);
         helper.setPrimaryReader(stringReader);
         helper.setProject(this.getProject());
         Vector filterChains = new Vector();
         filterChains.add(this);
         helper.setFilterChains(filterChains);
         String result = FileUtils.readFully(helper.getAssembledReader());
         return result.length() == 0 ? null : new String[]{result};
      } catch (BuildException var6) {
         throw var6;
      } catch (Exception var7) {
         throw new BuildException(var7);
      }
   }
}
