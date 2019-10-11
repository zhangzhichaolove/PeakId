package peak.chao.id;

public class BindIdClass {


    public static <T> void bind(T target) {
        String className = target.getClass().getName() + "$ViewBind";
        try {
            Class<?> clazz = Class.forName(className);
            BindId bindId = (BindId) clazz.newInstance();
            bindId.bind(target);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

}
