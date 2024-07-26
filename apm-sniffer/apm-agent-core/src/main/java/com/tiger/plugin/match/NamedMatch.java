/**
 * @Description: 类名直接匹配
 * @Author: tiger
 * @CreateDate: 2024/7/24 18:54
 */
package com.tiger.plugin.match;

import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

public class NamedMatch implements ClassMatch {

    private String className;

    @Override
    public ElementMatcher.Junction<? super TypeDescription> buildJunction() {
        return ElementMatchers.named(className);
    }

    private NamedMatch(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public static NamedMatch getInstance(String className) {
        return new NamedMatch(className);
    }
}
