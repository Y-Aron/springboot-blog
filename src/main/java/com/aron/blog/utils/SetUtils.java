package com.aron.blog.utils;

import java.util.Set;

/**
 * @author: Y-Aron
 * @create: 2018-10-12 16:30
 **/
public class SetUtils {

    public static Object first(Set set){
        if (set != null && set.size() > 0) {
            return set.iterator().next();
        }
        return null;
    }
}
