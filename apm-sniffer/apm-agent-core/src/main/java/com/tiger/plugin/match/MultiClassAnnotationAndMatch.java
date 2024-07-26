/**
 * @Description: 满足多个注解的情况
 * @Author: tiger
 * @CreateDate: 2024/7/24 23:06
 */
package com.tiger.plugin.match;

import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.annotation.AnnotationList;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import java.util.Arrays;
import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.isAnnotatedWith;
import static net.bytebuddy.matcher.ElementMatchers.named;

public class MultiClassAnnotationAndMatch implements IndirectMatch {

    private List<String> needMatchClassAnNames;

    private MultiClassAnnotationAndMatch(String[] names)   {
        if (names == null || names.length == 0) {
            throw new IllegalArgumentException("参数不合法");
        }

        this.needMatchClassAnNames = Arrays.asList(names);
    }


    @Override
    public ElementMatcher.Junction<? super TypeDescription> buildJunction() {
        ElementMatcher.Junction<? super TypeDescription> junction = null;
        for (String needMatchClassAnName : needMatchClassAnNames) {
            if (junction == null) {
                junction = isAnnotatedWith(named(needMatchClassAnName));
            } else {
                junction.and(named(needMatchClassAnName));
            }
        }
        return junction;
    }

    public static IndirectMatch getInstance(String... annotationNames)   {
        return new MultiClassAnnotationAndMatch(annotationNames);
    }

    @Override
    public boolean isMatch(TypeDescription typeDescription) {
        AnnotationList declaredAnnotations =
                typeDescription.getDeclaredAnnotations();

        for (AnnotationDescription declaredAnnotation : declaredAnnotations) {
            needMatchClassAnNames.remove(declaredAnnotation.getAnnotationType().getActualName());
        }
        return needMatchClassAnNames.isEmpty();
    }
}
