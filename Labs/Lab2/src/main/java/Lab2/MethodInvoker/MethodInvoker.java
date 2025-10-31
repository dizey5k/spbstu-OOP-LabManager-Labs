package Lab2.MethodInvoker;

import Lab2.Repeat.Repeat;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.Supplier;

public class MethodInvoker {

    private static final Map<Class<?>, Supplier<Object>> DEFAULTS = Map.ofEntries(
            Map.entry(int.class, () -> 42),
            Map.entry(Integer.class, () -> 42),
            Map.entry(boolean.class, () -> true),
            Map.entry(Boolean.class, () -> true),
            Map.entry(double.class, () -> 3.14),
            Map.entry(Double.class, () -> 3.14),
            Map.entry(String.class, () -> "default_value"),
            Map.entry(char.class, () -> 'X'),
            Map.entry(Character.class, () -> 'X'),
            Map.entry(long.class, () -> 100L),
            Map.entry(Long.class, () -> 100L),
            Map.entry(float.class, () -> 2.71f),
            Map.entry(Float.class, () -> 2.71f),
            Map.entry(byte.class, () -> (byte) 10),
            Map.entry(Byte.class, () -> (byte) 10),
            Map.entry(short.class, () -> (short) 20),
            Map.entry(Short.class, () -> (short) 20)
    );

    public void invokeAnnotatedMethods(Object target) {
        System.out.println("=== Finding annotated methods in class " +
                target.getClass().getSimpleName() + " ===");

        java.util.Arrays.stream(target.getClass().getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(Repeat.class))
                .filter(this::isProtectedOrPrivate)
                .forEach(m -> invokeMethod(target, m, m.getAnnotation(Repeat.class).value()));
    }

    private boolean isProtectedOrPrivate(Method m) {
        int mod = m.getModifiers();
        return java.lang.reflect.Modifier.isProtected(mod) || java.lang.reflect.Modifier.isPrivate(mod);
    }

    private void invokeMethod(Object target, Method method, int times) {
        System.out.printf("\n--- Method: %s ---\nModifier: %s\nCalls: %d\nParams: %s\n",
                method.getName(), getModifier(method), times,
                java.util.Arrays.toString(method.getParameterTypes()));

        method.setAccessible(true);
        Object[] params = java.util.Arrays.stream(method.getParameterTypes())
                .map(t -> DEFAULTS.getOrDefault(t, () -> null).get())
                .toArray();

        for (int i = 1; i <= times; i++) {
            try {
                System.out.printf("Call %d: ", i);
                Object result = method.invoke(target, params);
                if (method.getReturnType() != void.class) {
                    System.out.println("Returns: " + result);
                }
            } catch (Exception e) {
                System.out.println("Error: " + getExceptionMessage(e));
            }
        }
    }

    private String getModifier(Method m) {
        return switch (m.getModifiers() & java.lang.reflect.Modifier.methodModifiers()) {
            case java.lang.reflect.Modifier.PRIVATE -> "private";
            case java.lang.reflect.Modifier.PROTECTED -> "protected";
            case java.lang.reflect.Modifier.PUBLIC -> "public";
            default -> "package-private";
        };
    }

    private String getExceptionMessage(Exception e) {
        if (e instanceof java.lang.reflect.InvocationTargetException ite) {
            return ite.getTargetException().getMessage();
        }
        return e.getMessage();
    }
}