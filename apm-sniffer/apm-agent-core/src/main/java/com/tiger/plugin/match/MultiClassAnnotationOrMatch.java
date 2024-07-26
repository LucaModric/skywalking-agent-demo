/**
 * @Description: 满足多个注解其中一个的情况
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

public class MultiClassAnnotationOrMatch implements IndirectMatch {

    private List<String> needMatchClassAnNames;

    private MultiClassAnnotationOrMatch(String[] names)   {
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
                junction.or(named(needMatchClassAnName));
            }
        }
        return junction;
    }

    public static IndirectMatch getInstance(String... annotationNames)   {
        return new MultiClassAnnotationOrMatch(annotationNames);
    }

    @Override
    public boolean isMatch(TypeDescription typeDescription) {
        boolean isMatch = false;
        AnnotationList declaredAnnotations = typeDescription.getDeclaredAnnotations();
        for (AnnotationDescription declaredAnnotation : declaredAnnotations) {
            if (needMatchClassAnNames.contains(declaredAnnotation.getAnnotationType().getActualName())) {
                isMatch = true;
                break;
            }
        }
        return isMatch;
    }
}
