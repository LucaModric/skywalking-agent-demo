/**
 * @Description:
 * @Author: tiger
 * @CreateDate: 2024/7/24 23:27
 */
package com.tiger;

import com.tiger.plugin.match.ClassMatch;
import com.tiger.plugin.match.IndirectMatch;
import com.tiger.plugin.match.NamedMatch;
import net.bytebuddy.description.NamedElement;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.bytebuddy.matcher.ElementMatchers.isStatic;
import static net.bytebuddy.matcher.ElementMatchers.not;

public class PluginFinder {

    private Map<String, List<AbstractClassEnhancePluginDefine>> namedMatchs = new HashMap<>();
    private List<AbstractClassEnhancePluginDefine> indirtectMatchs = new ArrayList<>();

    public PluginFinder(List<AbstractClassEnhancePluginDefine> lists) {
        if (lists == null || lists.size() == 0) {
            throw new IllegalArgumentException("参数不合法");
        }
        for (AbstractClassEnhancePluginDefine item : lists) {
            ClassMatch classMatch = item.enHanceClass();
            if (classMatch == null) {
                continue;
            }
            if (classMatch instanceof NamedMatch) {
                NamedMatch namedMatch = (NamedMatch) classMatch;

                List<AbstractClassEnhancePluginDefine> abstractClassEnhancePluginDefines = namedMatchs.computeIfAbsent(namedMatch.getClassName(), k -> new ArrayList<AbstractClassEnhancePluginDefine>());
                abstractClassEnhancePluginDefines.add(item);
            } else {
                indirtectMatchs.add(item);
            }
        }
    }

    public ElementMatcher<? super TypeDescription> buildMatch() {
        ElementMatcher.Junction<? super TypeDescription> junction = new ElementMatcher.Junction.AbstractBase<NamedElement>() {
            @Override
            public boolean matches(NamedElement target) {
                return namedMatchs.containsKey(target.getActualName());
            }
        };
        junction = junction.and(not(isStatic()));

        for (AbstractClassEnhancePluginDefine item : indirtectMatchs) {
            ClassMatch classMatch = item.enHanceClass();
            if (classMatch == null) {
                continue;
            }
            if (classMatch instanceof IndirectMatch) {
                junction = junction.or(classMatch.buildJunction());
            }
        }
        return junction;
    }

    public List<AbstractClassEnhancePluginDefine> find(TypeDescription typeDescription) {
        List<AbstractClassEnhancePluginDefine> list = new ArrayList<>();
        if (namedMatchs.containsKey(typeDescription.getTypeName())) {
            list.addAll(namedMatchs.get(typeDescription.getTypeName()));
        }
        for (AbstractClassEnhancePluginDefine item : indirtectMatchs) {
            ClassMatch classMatch = item.enHanceClass();
            if (classMatch instanceof IndirectMatch) {
                IndirectMatch indirectMatch = (IndirectMatch) classMatch;
                if (indirectMatch.isMatch(typeDescription)) {
                    list.add(item);
                }
            }
        }
        return list;
    }


}
