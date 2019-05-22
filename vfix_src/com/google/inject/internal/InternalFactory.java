package com.google.inject.internal;

import com.google.inject.spi.Dependency;

interface InternalFactory<T> {
   T get(Errors var1, InternalContext var2, Dependency<?> var3, boolean var4) throws ErrorsException;
}
