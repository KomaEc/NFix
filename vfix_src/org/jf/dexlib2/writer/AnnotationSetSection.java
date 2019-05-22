package org.jf.dexlib2.writer;

import java.util.Collection;
import javax.annotation.Nonnull;
import org.jf.dexlib2.iface.Annotation;

public interface AnnotationSetSection<AnnotationKey extends Annotation, AnnotationSetKey> extends NullableOffsetSection<AnnotationSetKey> {
   @Nonnull
   Collection<? extends AnnotationKey> getAnnotations(@Nonnull AnnotationSetKey var1);
}
