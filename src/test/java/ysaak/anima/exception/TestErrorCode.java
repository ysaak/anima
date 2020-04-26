package ysaak.anima.exception;

import org.junit.Assert;
import org.junit.Test;
import org.reflections.Reflections;
import ysaak.anima.AnimaApplication;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Set;

public class TestErrorCode {
    @Test
    public void testErrorCodeUniqueness() throws Exception {
        final Reflections reflections = new Reflections(AnimaApplication.class.getPackage().getName());
        final Set<Class<? extends ErrorCode>> errorClassSet = reflections.getSubTypesOf(ErrorCode.class);


        final HashMap<String, String> codeMap = new HashMap<>();

        for (Class<? extends ErrorCode> errorClass : errorClassSet) {

            ErrorCode[] enumValues = getEnumValues(errorClass);

            for (ErrorCode errorCode : enumValues) {
                String fullClassName = getErrorCodeFullClassName(errorCode);
                String code = errorCode.getCode();

                Assert.assertNotNull("Error code is null for " + fullClassName, errorCode.getCode());

                if (codeMap.containsKey(code)) {
                    String existingError = codeMap.get(code);
                    Assert.fail("Error code '" + code + "' is present in two errors " + existingError + " and " + fullClassName);
                }
                else {
                    codeMap.put(code, fullClassName);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static <E extends ErrorCode> E[] getEnumValues(Class<E> enumClass) throws NoSuchFieldException, IllegalAccessException {
        Field f = enumClass.getDeclaredField("$VALUES");
        f.setAccessible(true);
        Object o = f.get(null);
        return (E[]) o;
    }

    private static String getErrorCodeFullClassName(ErrorCode errorCode) {
        return errorCode.getClass().getName() + "." + ((Enum<?>) errorCode).name();
    }
}
