/**
 * @Description: 多个类名or连接的情况
 * @Author: tiger
 * @CreateDate: 2024/7/24 18:55
 */
package com.tiger.plugin.match;

import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import java.util.Arrays;
import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.named;

public class MultiNamedMatch implements IndirectMatch {

    private List<String> needMatchClassNames;

    private MultiNamedMatch(String[] names) {
        if (names == null || names.length == 0) {
            throw new IllegalArgumentException("参数不合法");
        }

        this.needMatchClassNames = Arrays.asList(names);
    }

    @Override
    public ElementMatcher.Junction<? super TypeDescription> buildJunction() {
        ElementMatcher.Junction<? super TypeDescription> junction = null;
        for (String needMatchClassName : needMatchClassNames) {
            if (junction == null) {
                junction = named(needMatchClassName);
            } else {
                junction = junction.or(named(needMatchClassName));
            }
        }
        return junction;

    }

    public static IndirectMatch getInstance(String... args)   {
        return new MultiNamedMatch(args);
    }


    @Override
    public boolean isMatch(TypeDescription typeDescription) {
        return needMatchClassNames.contains(typeDescription.getTypeName());
    }
}
