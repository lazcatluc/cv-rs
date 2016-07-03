//package com.zee.cv.services;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import com.thoughtworks.qdox.JavaProjectBuilder;
//import com.thoughtworks.qdox.model.JavaClass;
//import com.thoughtworks.qdox.model.JavaField;
//import com.thoughtworks.qdox.model.JavaMethod;
//import com.thoughtworks.qdox.model.JavaType;
//import com.thoughtworks.qdox.parser.ParseException;
//import com.zee.cv.model.LinkData;
//import com.zee.cv.model.NodeData;
//import com.zee.cv.model.NodeData.Method;
//import com.zee.cv.model.NodeData.Method.Parameter;
//import com.zee.cv.model.NodeData.Property;
//import com.zee.cv.model.Reply;
//
//@Component
//public class QDoxApiService {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(QDoxApiService.class);
//
//    private static final String appPath = System.getProperty("user.dir") + File.separator;
//
//    @Value("${data.path}")
//    private String repoPath;
//
//    public Reply getClassesInfo(String cloneRepoPath) {
//        List<NodeData> nodeList = new ArrayList<>();
//
//        JavaProjectBuilder builder = new JavaProjectBuilder();
//
//        // Add a sourcefolder;
//        try {
//            builder.addSourceTree(new File(cloneRepoPath));
//        } catch (ParseException pe) {
//        }
//        int i = 1;
//        List<JavaClass> classes = (List<JavaClass>) builder.getClasses();
//        for (JavaClass qDoxjavaClass : classes) {
//
//            // class fields
//            List<Property> properties = new ArrayList<>();
//            List<JavaField> qDoxFields = qDoxjavaClass.getFields();
//            for (JavaField qdoxJavaField : qDoxFields) {
//                String visibility = qdoxJavaField.isPublic() ? "public"
//                        : qdoxJavaField.isProtected() ? "protected" : qdoxJavaField.isPrivate() ? "private" : "";
//                Property property = new Property(qdoxJavaField.getName(), qdoxJavaField.getType().toString(),
//                        visibility);
//                properties.add(property);
//            }
//
//            // class methods
//            List<Method> methods = new ArrayList<>();
//            List<JavaMethod> qDoxMethods = qDoxjavaClass.getMethods();
//            for (JavaMethod qDoxMethod : qDoxMethods) {
//                String visibility = qDoxMethod.isPublic() ? "public"
//                        : qDoxMethod.isProtected() ? "protected" : qDoxMethod.isPrivate() ? "private" : "";
//                Method method = new Method(qDoxMethod.getName(), new Parameter[0], visibility);
//                methods.add(method);
//            }
//
//            NodeData node = new NodeData(i++, qDoxjavaClass.getFullyQualifiedName(),
//                    properties.toArray(new Property[0]), methods.toArray(new Method[0]));
//            nodeList.add(node);
//        }
//        List<LinkData> links = new ArrayList<>();
//
//        for (NodeData node : nodeList) {
//
//            JavaClass classByName = builder.getClassByName(node.name);
//
//            JavaClass superClass = classByName.getSuperJavaClass();
//            for (NodeData superNode : nodeList) {
//                if (superNode.name.equals(superClass.getFullyQualifiedName())) {
//                    LinkData link = new LinkData(node.key, superNode.key, "generalization");
//                    links.add(link);
//                }
//            }
//
//        }
//        return new Reply(nodeList.toArray(new NodeData[0]), links.toArray(new LinkData[0]));
//    }
//
//}
