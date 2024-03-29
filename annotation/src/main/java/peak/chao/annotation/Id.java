package peak.chao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)//作用域：属性
@Retention(RetentionPolicy.CLASS)//编译期
public @interface Id {
    int value();
}
