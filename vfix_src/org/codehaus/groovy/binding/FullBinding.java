package org.codehaus.groovy.binding;

import groovy.lang.Closure;

public interface FullBinding extends BindingUpdatable {
   SourceBinding getSourceBinding();

   TargetBinding getTargetBinding();

   void setSourceBinding(SourceBinding var1);

   void setTargetBinding(TargetBinding var1);

   void setValidator(Closure var1);

   Closure getValidator();

   void setConverter(Closure var1);

   Closure getConverter();

   void setReverseConverter(Closure var1);

   Closure getReverseConverter();
}
