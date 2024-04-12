package com.github.alexwith.humap.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is to know if a repository method is on the form
 * {@code CompletableFuture<List<T>>}, because we can not find the generic
 * type of the completable future at runtime. Hopefully a solution to this
 * can be found in the future
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FindAll {}
