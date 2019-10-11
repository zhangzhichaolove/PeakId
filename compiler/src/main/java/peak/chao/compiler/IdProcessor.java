package peak.chao.compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

import peak.chao.annotation.Id;

/**
 * 注解处理器核心类
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes({"peak.chao.annotation.Id"})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class IdProcessor extends AbstractProcessor {

    private Filer filer;
    private Messager messager;
    private Elements elementUtils;

    /**
     * 指出注解处理器
     *
     * @return 支持处理哪种注解
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new HashSet<>();
        set.add(Id.class.getCanonicalName());
        return set;
    }

    /**
     * @return 指定当前注解器使用的Jdk版本
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }


    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
        messager = processingEnvironment.getMessager();
        elementUtils = processingEnvironment.getElementUtils();
        messager.printMessage(Diagnostic.Kind.NOTE, "IdProcessor >> init");
    }

    /**
     * 注解处理
     *
     * @param set
     * @param roundEnvironment
     * @return 处理是否完成
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
//        try {
//            //创建指定类
//            JavaFileObject javaFileObject = filer.createSourceFile("peak.chao.peakid.MainActivityXXTest");
//            Writer writer = javaFileObject.openWriter();
//            writer.write("package peak.chao.peakid;\n" +
//                    "\n" +
//                    "import peak.chao.id.BindId;\n" +
//                    "\n" +
//                    "public class MainActivityXXTest implements BindId<MainActivity> {\n" +
//                    "    @Override\n" +
//                    "    public void bind(MainActivity target) {\n" +
//                    "        target.tv = target.findViewById(R.id.tv);\n" +
//                    "    }\n" +
//                    "\n" +
//                    "    @Override\n" +
//                    "    public void unBind() {\n" +
//                    "\n" +
//                    "    }\n" +
//                    "}\n");
//            writer.flush();
//            writer.close();
//            return true;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(Id.class);
        for (Element e : elements) {
            Element enclosingElement = e.getEnclosingElement();
            messager.printMessage(Diagnostic.Kind.NOTE, "IdProcessor >> init" + e.getSimpleName().toString() + " in " + enclosingElement.getSimpleName().toString());

            //构建bind方法
            MethodSpec bind = MethodSpec.methodBuilder("bind")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(void.class)
                    .addParameter(ClassName.bestGuess(enclosingElement.toString()), "target")
                    .addStatement("target.$N=target.findViewById($L)", e.getSimpleName().toString(), e.getAnnotation(Id.class).value())
                    .build();

            //构建unBind方法
            MethodSpec unBind = MethodSpec.methodBuilder("unBind")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(void.class)
                    .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!")
                    .build();

            //构建带范型的实现类
            ParameterizedTypeName superName = ParameterizedTypeName.get(ClassName.bestGuess("peak.chao.id.BindId"), ClassName.bestGuess(enclosingElement.getSimpleName().toString()));

            //构建类
            TypeSpec viewBind = TypeSpec.classBuilder(enclosingElement.getSimpleName().toString() + "$ViewBind")
                    .addModifiers(Modifier.PUBLIC)
                    .addSuperinterface(superName)
                    .addMethod(bind)
                    .addMethod(unBind)
                    .build();

            String packageName = elementUtils.getPackageOf(enclosingElement).getQualifiedName().toString();

            //创建类文件
            JavaFile javaFile = JavaFile.builder(packageName, viewBind)
                    .build();

            try {
                javaFile.writeTo(filer);
                return true;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }
}
