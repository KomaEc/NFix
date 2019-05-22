package com.google.inject.internal;

interface Initializable<T> {
   T get(Errors var1) throws ErrorsException;
}
