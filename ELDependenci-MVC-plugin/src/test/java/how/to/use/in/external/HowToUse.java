package how.to.use.in.external;

import com.ericlam.mc.eldgui.controller.UIController;
import com.ericlam.mc.eldgui.demo.error.ELDGExceptionViewHandler;
import com.ericlam.mc.eldgui.demo.error.ErrorView;
import com.ericlam.mc.eldgui.exception.ExceptionViewHandler;
import com.ericlam.mc.eldgui.view.BukkitView;
import com.google.gson.Gson;
import com.sun.tools.javac.Main;
import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@UIController("1234")
public class HowToUse {


    public static void main(String[] args) {
        System.out.println(
                objectFieldsToMap(
                        new Student(
                                "studen1",
                                "Chan",
                                "Tai Man",
                                19,
                                "1234544",
                                5)
                ));
    }

    @Test
    public void testAnnotation() {
        UIController realAnnotation = HowToUse.class.getAnnotation(UIController.class);
        System.out.println(realAnnotation.annotationType());
        System.out.println(realAnnotation.value());
        UIController fakeAnnotation = new UIController() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return UIController.class;
            }

            @Override
            public String value() {
                return "1234";
            }
        };
        System.out.println(fakeAnnotation.annotationType());
        System.out.println(fakeAnnotation.value());

        Assert.assertEquals(fakeAnnotation.annotationType(), realAnnotation.annotationType());
        Assert.assertEquals(fakeAnnotation.value(), realAnnotation.value());
    }


    @Test
    public void testPrimitive(){
        var student = new Student(
                "studen1",
                "Chan",
                "Tai Man",
                19,
                "1234544",
                5);
        System.out.println(objectFieldsToMap(student));
        System.out.println(objectToMap(student));
    }

    public BukkitView<?, ?> testView(String a, Integer b){ return null; }

    @Test
    public void testClassCommonPackage() {
        System.out.println(ELDGExceptionViewHandler.class.getClassLoader().getName());
        System.out.println(ExceptionViewHandler.class.getClassLoader().getName());
        System.out.println(UserController.class.getClassLoader().getName());
        System.out.println(ELDGExceptionViewHandler.class.getProtectionDomain().getCodeSource().hashCode());
        System.out.println(ErrorView.class.getProtectionDomain().getCodeSource().hashCode());
        System.out.println(ExceptionViewHandler.class.getProtectionDomain().getCodeSource().hashCode());
        System.out.println(UserController.class.getProtectionDomain().getCodeSource().hashCode());
    }

    @Test
    public void testClassInherit() {
        System.out.println(B.class.isAnnotationPresent(UIController.class));
    }

    @UIController("1234")
    private static class A {}
    private static class B extends A {}

    public static Map<String, Object> objectFieldsToMap(Object model) {
        return Arrays.stream(model.getClass().getFields()).collect(Collectors.toMap(Field::getName, f -> {
            try {
                f.setAccessible(true);
                return f.get(model);
            } catch (Exception e) {
                e.printStackTrace();
                return "[Error: " + e.getClass().getSimpleName() + "]";
            }
        }));
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> objectToMap(Object model){
        Gson gson = new Gson();
        return gson.fromJson(gson.toJson(model), Map.class);
    }

    public static class Student extends User {

        public String studentId;
        public int studyLevel;

        public Student(
                String username,
                String firstName,
                String lastName,
                int age,
                String studentId,
                int studyLevel
        ) {
            super(username, firstName, lastName, age);
            this.studentId = studentId;
            this.studyLevel = studyLevel;
        }
    }
}