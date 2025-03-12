/*
 * Copyright (c) Diego Rigo, Sona (VR), 2024.
 */

package it.diegorigo.interfaces;

@FunctionalInterface
public interface CheckedFunction<T, R> {
    R apply(T t);
}
