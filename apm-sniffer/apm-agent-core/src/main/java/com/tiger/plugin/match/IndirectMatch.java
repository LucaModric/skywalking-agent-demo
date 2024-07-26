/**
 * @Description:
 * @Author: tiger
 * @CreateDate: 2024/7/24 18:53
 */
package com.tiger.plugin.match;

import net.bytebuddy.description.type.TypeDescription;

public interface IndirectMatch extends ClassMatch {
    boolean isMatch(TypeDescription typeDescription);
}
