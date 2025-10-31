package Lab2;

import Lab2.AnnotadedClass.AnnotatedClass;
import Lab2.MethodInvoker.MethodInvoker;
import Lab2.Repeat.Repeat;
import LabManager.LabInfo;
import LabManager.RunnableLab;

import java.lang.reflect.Method;
import java.util.Scanner;

public class Lab2 implements RunnableLab {
    private final Scanner scanner;
    private final LabInfo labInfo;

    public Lab2() {
        this.scanner = new Scanner(System.in);
        this.labInfo = new LabInfo(2, "annotation and reflection",
                "usage of annotation and reflection for methods class");
    }

    @Override
    public void run() {
        System.out.println("=== Lab 2: Annotation and Reflection ===");
        System.out.println("Demonstration of call annotated methods with reflection");

        boolean running = true;

        while (running) {
            showMenu();
            int choice = getChoice();

            switch (choice) {
                case 1:
                    demonstrateBasicInvocation();
                    break;
                case 2:
                    demonstrateWithCustomObject();
                    break;
                case 3:
                    showReflectionInfo();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Incorrect choice");
            }
        }
    }

    @Override
    public LabInfo getLabInfo() {
        return labInfo;
    }

    private void showMenu() {
        System.out.println("\n--- Lab 2 menu ---");
        System.out.println("1. Demonstrate call of methods");
        System.out.println("2. Create object and call methods");
        System.out.println("3. Show info about reflection");
        System.out.println("0. Back to main menu");
        System.out.print("Choose action: ");
    }

    private int getChoice() {
        try {
            return scanner.nextInt();
        } catch (Exception e) {
            scanner.nextLine();
            return -1;
        }
    }

    /**
     * Demo basic call of annotated class
     */
    private void demonstrateBasicInvocation() {
        System.out.println("\n=== Demo of call of annotated methods ===");

        AnnotatedClass annotatedObject = new AnnotatedClass();
        MethodInvoker invoker = new MethodInvoker();

        invoker.invokeAnnotatedMethods(annotatedObject);

        System.out.println("\n=== Demo ended ===");
    }

    /**
     * Demo with user obj
     */
    private void demonstrateWithCustomObject() {
        System.out.println("\n=== Create user obj ===");
        scanner.nextLine();

        System.out.print("Type name for obj: ");
        String objectName = scanner.nextLine();

        Object customObject = createCustomObject(objectName);
        MethodInvoker invoker = new MethodInvoker();

        invoker.invokeAnnotatedMethods(customObject);
    }

    /**
     * Create user obj with annotated methods
     */
    private Object createCustomObject(final String name) {
        return new Object() {
            @Repeat(2)
            private void customPrivateMethod(String msg) {
                System.out.println("[" + name + "] private method: " + msg);
            }

            @Repeat(3)
            protected void customProtectedMethod(int x) {
                System.out.println("[" + name + "] protected with num method: " + x);
            }

            @Repeat(1)
            private String customMethodWithReturn() {
                return "[" + name + "] with return value method";
            }
        };
    }

    /**
     * Shows info about reflection
     */
    private void showReflectionInfo() {
        System.out.println("\n=== Info about reflection ===");

        Class<AnnotatedClass> clazz = AnnotatedClass.class;

        System.out.println("Class: " + clazz.getName());
        System.out.println("Simple name: " + clazz.getSimpleName());
        System.out.println("Modifiers: " + java.lang.reflect.Modifier.toString(clazz.getModifiers()));

        System.out.println("\n--- All class methods ---");
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            System.out.println("Method: " + method.getName());
            System.out.println("  Modifier: " + java.lang.reflect.Modifier.toString(method.getModifiers()));
            System.out.println("  Return type: " + method.getReturnType().getSimpleName());
            System.out.println("  Parameters: " + java.util.Arrays.toString(method.getParameterTypes()));

            Repeat annotation = method.getAnnotation(Repeat.class);
            if (annotation != null) {
                System.out.println("  Annotation @Repeat: " + annotation.value() + " calls");
            }
            System.out.println();
        }
    }
}