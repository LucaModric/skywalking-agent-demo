/**
 * @Description:
 * @Author: tiger
 * @CreateDate: 2024/7/24 18:30
 */
package com.tiger.plugin.match;

import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

public interface ClassMatch {

    ElementMatcher.Junction<? super TypeDescription> buildJunction();
}
