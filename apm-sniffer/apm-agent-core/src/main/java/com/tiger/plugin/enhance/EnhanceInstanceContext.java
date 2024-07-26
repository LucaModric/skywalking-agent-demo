/**
 * @Description:
 * @Author: tiger
 * @CreateDate: 2024/7/25 20:01
 */
package com.tiger.plugin.enhance;

public class EnhanceInstanceContext {

    /**
     * 是否增强了
     */
    private boolean isEnhanced = false;

    /**
     * 是否新增了属性CONTEXT_ATTR_NAME
     */
    private boolean objectExtended = false;

    public boolean isEnhanced() {
        return isEnhanced;
    }

    public void initializationStageCompleted() {
        isEnhanced = true;
    }

    public boolean isObjectExtended() {
        return objectExtended;
    }

    public void objectExtendedCompleted() {
        objectExtended = true;
    }

}
