package how.to.use.in.external;

import com.ericlam.mc.eldgui.controller.UIController;
import com.ericlam.mc.eldgui.demo.error.ELDGExceptionViewHandler;
import com.ericlam.mc.eldgui.demo.error.ErrorView;
import com.ericlam.mc.eldgui.exception.ExceptionViewHandler;
import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@UIController("1234")
public class HowToUse {


    public static void main(String[] args) {
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
    public void testPrimitive() {
        System.out.println(objectFieldsToMap(1234).toString());
        System.out.println(objectFieldsToMap("123414141").toString());
        System.out.println(objectFieldsToMap(new User("user1", "A", "BC", 99)).toString());
        System.out.println(objectFieldsToMap(List.of(1, 2, 3, 5)).toString());
        System.out.println(objectFieldsToMap(new Exception("1234")));
    }

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

    private static Map<String, Object> objectFieldsToMap(Object model) {
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
}
