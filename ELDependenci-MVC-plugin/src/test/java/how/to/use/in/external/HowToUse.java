package how.to.use.in.external;

import com.ericlam.mc.eldgui.controller.UIController;
import com.ericlam.mc.eldgui.demo.ELDGExceptionViewHandler;
import com.ericlam.mc.eldgui.demo.error.ErrorView;
import com.ericlam.mc.eldgui.exception.ExceptionViewHandler;
import com.ericlam.mc.eldgui.view.BukkitView;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@UIController("1234")
public class HowToUse {


    public static void main(String[] args) {
        Object o = Math.random() > 0.5 ? new ArrayList<String>() : new Object();
        System.out.println(o instanceof Collection<?>);
    }

    @Test
    public void testGson() {
        Gson gson = new Gson();
        byte[] json = gson.toJson("123131").getBytes(StandardCharsets.UTF_8);
        System.out.println(Arrays.toString(json));
        Object o = gson.fromJson(new String(json), Object.class);
        System.out.println(o);
        System.out.println(o.getClass());
    }

    @Test
    public void testJackson() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        byte[] bb = mapper.writeValueAsBytes(UUID.randomUUID());
        System.out.println(Arrays.toString(bb));
        Object o = mapper.readValue(bb, Object.class);
        System.out.println(o instanceof UUID);
        System.out.println(o.getClass());
    }

    @Test
    public void testJacksonConvert() {
        ObjectMapper mapper = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
        Map<String, Object> map = Map.of(
                "username", "username1",
                "firstName", "first",
                "lastName", "last",
                "age", 12,
                "address.line1", "line1",
                "address.line2", "line2"
        );
        Map<String, Object> result = nester(map);
        System.out.println(result);
        User user = mapper.convertValue(result, User.class);
        System.out.println(user);
    }

    private static Map<String, Object> nester(Map<String, Object> map) {
        HashMap<String, Object> result = new HashMap<>();
        var nested = new HashMap<String, Map<String, Object>>();
        map.forEach((k, v) -> {
            final int indexOfDot = k.indexOf('.');
            if (indexOfDot == -1) {
                result.put(k, v);
            } else {
                String[] paths = k.split("\\.");
                var remain = String.join("", Arrays.copyOfRange(paths, 1, paths.length));
                nested.putIfAbsent(paths[0], new HashMap<>());
                nested.get(paths[0]).put(remain, v);
            }
        });
        nested.forEach((key, mapp) -> result.put(key, nester(mapp)));
        return result;
    }

    private static Map<String, Object> generateNestedMap(String path, Object value) {
        final int indexOfDot = path.indexOf('.');
        return indexOfDot == -1 ? Collections.singletonMap(path, value)
                : Collections.singletonMap(path.split("\\.")[0],
                generateNestedMap(path.substring(indexOfDot + 1), value));
    }

    @Test
    public void testFlatMap() {
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

    public BukkitView<?, ?> testView(String a, Integer b) {
        return null;
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
    private static class A {
    }

    private static class B extends A {
    }

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
    public static Map<String, Object> objectToMap(Object model) {
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
