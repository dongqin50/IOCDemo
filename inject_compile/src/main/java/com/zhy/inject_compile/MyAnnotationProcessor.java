package com.zhy.inject_compile;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;
import com.zhy.inject_annotation.CBindEventEnum;
import com.zhy.inject_annotation.annotation.CBindEvent;
import com.zhy.inject_annotation.annotation.CBindView;
import com.zhy.inject_annotation.annotation.CSetContentView;
import com.zhy.inject_annotation.model.ContextBindingModel;
import com.zhy.inject_annotation.model.EventListener;
import com.zhy.inject_annotation.model.FieldBindingModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;


@AutoService(Processor.class)
public class MyAnnotationProcessor extends AbstractProcessor {

    private Elements elementUtils;

    private Filer filer;

    private Map<TypeElement, ContextBindingModel> contextBindingModelMap;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        elementUtils = processingEnvironment.getElementUtils();
        filer = processingEnvironment.getFiler();
        contextBindingModelMap = new HashMap<>();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {

        Set<String> annotationTypes = new HashSet<>();

        annotationTypes.add(CBindView.class.getCanonicalName());
        annotationTypes.add(CBindEvent.class.getCanonicalName());
        annotationTypes.add(CSetContentView.class.getCanonicalName());
        return annotationTypes;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        FileUtils.print("process");
        setContentView(roundEnvironment);
        bindView(roundEnvironment);
        bindEventListener(roundEnvironment);
        for (Map.Entry<TypeElement, ContextBindingModel> entry : contextBindingModelMap.entrySet()) {
            TypeElement typeElement = entry.getKey();
            ContextBindingModel model = entry.getValue();

            String packageName = elementUtils.getPackageOf(typeElement).getQualifiedName().toString();
            String name = typeElement.getSimpleName().toString();
            ClassName className = ClassName.bestGuess(name);
            ClassName viewBinder = ClassName.get("com.zhy.inject", "ViewBinder");
            TypeSpec.Builder typeSpecBuider = TypeSpec.classBuilder(name + "$$ViewBinder")
                    .addModifiers(Modifier.PUBLIC)
                    .addTypeVariable(TypeVariableName.get("T", className)) //范型
                    .addSuperinterface(ParameterizedTypeName.get(viewBinder, className));

            String methodParam = "target";

            MethodSpec.Builder methodSpecBuilder = MethodSpec.methodBuilder("bind")
                    .addModifiers(Modifier.PUBLIC)
                    .returns(TypeName.VOID)
                    .addAnnotation(Override.class)
                    .addParameter(className, methodParam, Modifier.FINAL)
                    .addComment("bind view or setListener");

            int layoutId =  model.getLayoutId();

            if(layoutId != -1){
                methodSpecBuilder.addStatement(methodParam+".setContentView($L)",layoutId);
            }

            for (Map.Entry<Integer, FieldBindingModel> filed : model.getFieldBindingModels().entrySet()) {
                FieldBindingModel fieldBindingModel = filed.getValue();
                ClassName viewClass = ClassName.bestGuess(fieldBindingModel.getFieldType().toString());
                methodSpecBuilder.addStatement(methodParam + ".$L=($T)" + methodParam + ".findViewById($L)",
                        fieldBindingModel.getFieldName(), viewClass, fieldBindingModel.getId());
            }

            for(EventListener listener: model.getEventListeners()){
                int[] ids = listener.getIds();
                if(ids == null || ids.length <= 0){
                    break;
                }
               for(int id: ids){
                   FieldBindingModel field = model.getFieldBindingModels().get(id);
                   String type = listener.getListenerType();
                   int lastPointIndex = type.lastIndexOf(".");
                   String listenerClassName = type.substring(lastPointIndex+1);
                   String listenerPackage  = type.substring(0,lastPointIndex);

                   ClassName typeClassName = ClassName.get(listenerPackage,listenerClassName);

                   MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(listener.getListenerMethod())
                           .addModifiers(Modifier.PUBLIC)
                           .addAnnotation(Override.class)
                           .returns(listener.getReturnType() == null ? TypeName.VOID:ClassName.get(listener.getReturnType()));
//                  List<Parameter> parameters = listener.getParamters();
                  int i = 1;
                  if(listener.getParamters().size() > 0){
                      StringBuffer statement = new StringBuffer(methodParam + ".$N(");
                      Object[] obj = new Object[listener.getParamters().size()+1];
                      obj[0]=listener.getMethodName();
                      for(String p : listener.getParamters()){
                          ClassName parameterType = ClassName.bestGuess(p);
                          ParameterSpec parameterSpec = ParameterSpec.builder(parameterType,"var"+i ).build();
                          methodBuilder.addParameter(parameterSpec);
                          statement.append("$N");
                          obj[i] = "var"+i;
                          i++;
                      }
                      statement.append(")");
                      methodBuilder.addStatement(statement.toString(),obj);
                  }




                   TypeSpec typeSpec = TypeSpec.anonymousClassBuilder("")
                           .addSuperinterface(typeClassName)
                           .addMethod(methodBuilder.build())
                           .build();
                                                                     
                   if(null != field){
                       methodSpecBuilder.addStatement(
                               methodParam+ ".$N.$N($L)",field.getFieldName(),listener.getSetterListener(),typeSpec.toString());
                   }else{
                       methodSpecBuilder.addStatement("($N.findViewById($L)).$N($L)",methodParam,id,listener.getSetterListener(),
                               typeSpec.toString());
                   }





//                   TypeSpec.anonymousClassBuilder("")
//                           .addSuperinterface(typeClassName)
//                           .addMethod(
//                                   MethodSpec.methodBuilder(listener.getListenerMethod())
//                                           .addAnnotation(Override.class)
//                                           .addStatement("")
//                                           .build());
//                   methodSpecBuilder.addStatement("$L.$L(new $T)",
//                          fieldName, listener.getSetterListener(),""
//
//                           );


               }


            }




            typeSpecBuider.addMethod(methodSpecBuilder.build());
            try {
                JavaFile.builder(packageName,typeSpecBuider.build())
                        .addFileComment("test ....")
                        .build().writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    private void setContentView(RoundEnvironment roundEnvironment) {
        for (Element element : roundEnvironment.getElementsAnnotatedWith(CSetContentView.class)) {

            TypeElement typeElement = (TypeElement) element;
            ContextBindingModel model = contextBindingModelMap.get(typeElement);
            int layoutId = element.getAnnotation(CSetContentView.class).value();
            if (model == null) {
                model = new ContextBindingModel(layoutId);
                contextBindingModelMap.put(typeElement, model);
            }
        }
    }

    private void bindView(RoundEnvironment roundEnvironment) {

        for (Element element : roundEnvironment.getElementsAnnotatedWith(CBindView.class)) {
            TypeElement typeElement = (TypeElement) element.getEnclosingElement();
            ContextBindingModel model = contextBindingModelMap.get(typeElement);
            if (model == null) {
                model = new ContextBindingModel(-1);
                contextBindingModelMap.put(typeElement, model);
            }
            Map<Integer, FieldBindingModel> fieldBindingModelList = model.getFieldBindingModels();
            if (fieldBindingModelList == null) {
                fieldBindingModelList = new HashMap<>();
                model.setFieldBindingModels(fieldBindingModelList);
            }

            int id = element.getAnnotation(CBindView.class).value();
            String name = element.getSimpleName().toString();
            TypeMirror typeMirror = element.asType();

            FieldBindingModel fieldBindingModel = new FieldBindingModel(name, typeMirror, id);
            fieldBindingModelList.put(id, fieldBindingModel);
        }

    }

    private void bindEventListener(RoundEnvironment roundEnvironment) {

        for (Element element : roundEnvironment.getElementsAnnotatedWith(CBindEvent.class)) {
            TypeElement typeElement = (TypeElement) element.getEnclosingElement();
            ContextBindingModel model = contextBindingModelMap.get(typeElement);
            if (model == null) {
                model = new ContextBindingModel(-1);
                contextBindingModelMap.put(typeElement, model);
            }
            List<EventListener> listenerList = model.getEventListeners();
            if (listenerList == null) {
                listenerList = new ArrayList<>();
                model.setEventListeners(listenerList);
            }
            CBindEvent annotation = element.getAnnotation(CBindEvent.class);
            int[] ids = annotation.value();
            CBindEventEnum bindEventEnum = annotation.listenerType();
            EventListener listener = CBindEventEnum.analysisCBindEventEnum(bindEventEnum);
            listener.setIds(ids);
            listener.setMethodName(element.getSimpleName().toString());
            listenerList.add(listener);
        }
    }


}
