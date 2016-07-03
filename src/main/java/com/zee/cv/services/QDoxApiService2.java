package com.zee.cv.services;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaField;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaPackage;
import com.thoughtworks.qdox.model.JavaSource;
import com.thoughtworks.qdox.model.Type;
//import com.thoughtworks.qdox.model.JavaType;
import com.thoughtworks.qdox.parser.ParseException;
import com.zee.cv.model.LinkData;
import com.zee.cv.model.LinkDataArray;
import com.zee.cv.model.NodeData;
import com.zee.cv.model.NodeData.Method;
import com.zee.cv.model.NodeData.Method.Parameter;
import com.zee.cv.model.NodeData.Property;
import com.zee.cv.model.NodeDataArray;
import com.zee.cv.model.Reply;
import com.zee.cv.model.ReplyPackageAssociation;

@Component
public class QDoxApiService2 {

    private static final Logger LOGGER = LoggerFactory.getLogger(QDoxApiService2.class);

    private static final String appPath = System.getProperty("user.dir") + File.separator;

    @Value("${data.path}")
    private String repoPath;

    public Reply getClassesInfoHierarchy(String cloneRepoPath) {
        List<NodeData> nodeList = new ArrayList<>();

        JavaDocBuilder builder = new JavaDocBuilder();

        // Add a sourcefolder;
        try {
            builder.addSourceTree(new File(cloneRepoPath));
        } catch (ParseException pe) {
        }
        int i = 1;
        List<JavaClass> classes = Arrays.asList(builder.getClasses());
        for (JavaClass qDoxjavaClass : classes) {

            // class fields
            List<Property> properties = new ArrayList<>();
            List<JavaField> qDoxFields = Arrays.asList(qDoxjavaClass.getFields());
            for (JavaField qdoxJavaField : qDoxFields) {
                String visibility = qdoxJavaField.isPublic() ? "public"
                        : qdoxJavaField.isProtected() ? "protected" : qdoxJavaField.isPrivate() ? "private" : "";
                Property property = new Property(qdoxJavaField.getName(), qdoxJavaField.getType().toString(),
                        visibility);
                properties.add(property);
            }

            // class methods
            List<Method> methods = new ArrayList<>();
            List<JavaMethod> qDoxMethods = Arrays.asList(qDoxjavaClass.getMethods());
            for (JavaMethod qDoxMethod : qDoxMethods) {
                String visibility = qDoxMethod.isPublic() ? "public"
                        : qDoxMethod.isProtected() ? "protected" : qDoxMethod.isPrivate() ? "private" : "";
                Method method = new Method(qDoxMethod.getName(), new Parameter[0], visibility);
                methods.add(method);
            }

            NodeData node = new NodeData(i++, qDoxjavaClass.getFullyQualifiedName(),
                    properties.toArray(new Property[0]), methods.toArray(new Method[0]));
            nodeList.add(node);
        }
        List<LinkData> links = new ArrayList<>();
        // hierarchy
        for (NodeData node : nodeList) {

            JavaClass classByName = builder.getClassByName(node.name);

            JavaClass superClass = classByName.getSuperJavaClass();
            if (superClass != null)
                for (NodeData superNode : nodeList) {
                    if (superNode.name.equals(superClass.getFullyQualifiedName())) {
                        LinkData link = new LinkData(node.key, superNode.key, "generalization");
                        links.add(link);
                    }
                }

        }
        return new Reply(nodeList.toArray(new NodeData[0]), links.toArray(new LinkData[0]));
    }
    public Reply getClassesInfoAggregation(String cloneRepoPath) {
        List<NodeData> nodeList = new ArrayList<>();

        JavaDocBuilder builder = new JavaDocBuilder();

        // Add a sourcefolder;
        try {
            builder.addSourceTree(new File(cloneRepoPath));
        } catch (ParseException pe) {
        }
        int i = 1;
        List<JavaClass> classes = Arrays.asList(builder.getClasses());
        for (JavaClass qDoxjavaClass : classes) {

            // class fields
            List<Property> properties = new ArrayList<>();
            List<JavaField> qDoxFields = Arrays.asList(qDoxjavaClass.getFields());
            for (JavaField qdoxJavaField : qDoxFields) {
                String visibility = qdoxJavaField.isPublic() ? "public"
                        : qdoxJavaField.isProtected() ? "protected" : qdoxJavaField.isPrivate() ? "private" : "";
                Property property = new Property(qdoxJavaField.getName(), qdoxJavaField.getType().toString(),
                        visibility);
                properties.add(property);
            }

            // class methods
            List<Method> methods = new ArrayList<>();
            List<JavaMethod> qDoxMethods = Arrays.asList(qDoxjavaClass.getMethods());
            for (JavaMethod qDoxMethod : qDoxMethods) {
                String visibility = qDoxMethod.isPublic() ? "public"
                        : qDoxMethod.isProtected() ? "protected" : qDoxMethod.isPrivate() ? "private" : "";
                Method method = new Method(qDoxMethod.getName(), new Parameter[0], visibility);
                methods.add(method);
            }

            NodeData node = new NodeData(i++, qDoxjavaClass.getFullyQualifiedName(),
                    properties.toArray(new Property[0]), methods.toArray(new Method[0]));
            nodeList.add(node);
        }
        List<LinkData> links = new ArrayList<>();

        // aggregation
        for (JavaClass qDoxjavaClass : classes) {
            // class fields
            List<JavaField> qDoxFields = Arrays.asList(qDoxjavaClass.getFields());
            for (JavaField qdoxJavaField : qDoxFields) {
                Type type = qdoxJavaField.getType();
                String fullQualifiedNameClassName = type.getFullQualifiedName();

                for (NodeData fieldNode : nodeList) {
                    if (fieldNode.name.equals(fullQualifiedNameClassName)) {
                        long to = 0;
                        for (NodeData node : nodeList) {

                            if (node.name.equals(qDoxjavaClass.getFullyQualifiedName())) {
                                to = node.key;
                            }
                        }
                        LinkData link = new LinkData(fieldNode.key, to, "aggregation");
                        links.add(link);
                    }
                }
            }
        }
        return new Reply(nodeList.toArray(new NodeData[0]), links.toArray(new LinkData[0]));
    }

    public ReplyPackageAssociation getPackageAssociations(String cloneRepoPath) {
        List<NodeDataArray> nodeList = new ArrayList<>();

        JavaDocBuilder builder = new JavaDocBuilder();

        // Add a sourcefolder;
        try {
            builder.addSourceTree(new File(cloneRepoPath));
        } catch (ParseException pe) {
        }
        int i = 1;
        List<String> strPackageList = new ArrayList<String>();
        JavaPackage[] packages = builder.getPackages();
        for (JavaPackage javaPackage : packages) {
            strPackageList.add(javaPackage.getName());

            NodeDataArray node = new NodeDataArray(i++, javaPackage.getName());
            nodeList.add(node);
        }

        List<LinkDataArray> links = new ArrayList<>();
        for (JavaPackage javaPackage : packages) {
            System.out.println(javaPackage.getName());
            JavaClass[] classes = javaPackage.getClasses();

            List<String> importPakcages = new ArrayList<>();
            for (JavaClass javaClass : classes) {
                // System.out.println(javaClass.getFullyQualifiedName());
                JavaSource source = javaClass.getSource();
                String[] imports = source.getImports();
                for (String string : imports) {
                    String str = string.substring(0, string.lastIndexOf('.'));

                    if (strPackageList.contains(str))
                        if (!importPakcages.contains(str)) {
                            importPakcages.add(str);
                            System.out.println("\t" + str);

                            long from = 0;
                            long to = 0;
                            String text = "";
                            for (NodeDataArray nodedataArray : nodeList) {
                                if (nodedataArray.text.equals(javaPackage.getName()))
                                    from = nodedataArray.key;
                                if (nodedataArray.text.equals(str))
                                    to = nodedataArray.key;
                            }
                            LinkDataArray link = new LinkDataArray(from, to, text);
                            links.add(link);
                        }
                }
            }
        }

        return new ReplyPackageAssociation(nodeList.toArray(new NodeDataArray[0]), links.toArray(new LinkDataArray[0]));
    }
}
