package mingzuozhibi.coreserver.commons.util;

public abstract class SpecificationUtils {

    public static Class<?> getFieldClass(Class<?> type, String name) {
        try {
            return type.getDeclaredField(name).getType();
        } catch (NoSuchFieldException e) {
            Class<?> superclass = type.getSuperclass();
            if (superclass != Object.class) {
                return getFieldClass(superclass, name);
            }
            throw new IllegalArgumentException("找不到" + name + "属性");
        }
    }

}
