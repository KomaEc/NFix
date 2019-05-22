package com.google.inject.internal;

interface ContextualCallable<T> {
   T call(InternalContext var1) throws ErrorsException;
}
