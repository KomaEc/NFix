package com.google.inject.internal;

import com.google.inject.spi.InjectionPoint;

interface SingleMemberInjector {
   void inject(Errors var1, InternalContext var2, Object var3);

   InjectionPoint getInjectionPoint();
}
