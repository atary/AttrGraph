/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package attrgraph;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.*;
import japa.parser.ast.expr.*;
import japa.parser.ast.stmt.*;
import japa.parser.ast.visitor.VoidVisitorAdapter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author itü
 */
public class Parser {
    private CompilationUnit cu;
    private FileInputStream in;
    private HashSet<String> classes;
    private HashSet<String> methods;
    private HashSet<String> attributes;
    private HashSet<String> parameters;
    private HashSet<String> calls;
    private HashMap<String, String> methodClasses;
    private HashSet<String> conflicts;
    private int conflictCount;
    private HashSet<String> accesses;
    private int i;
     
    public Parser() {
        classes=new HashSet<String>();
        methods=new HashSet<String>();
        attributes=new HashSet<String>();
        parameters=new HashSet<String>();
        calls=new HashSet<String>();
        methodClasses=new HashMap<String, String>();
        conflicts=new HashSet<String>();
        accesses=new HashSet<String>();
        conflictCount=0;
        i=0;
    }
            
    public void parse(File f) throws FileNotFoundException, ParseException, IOException{
        in = new FileInputStream(f.getAbsolutePath());
        String curDir=f.getParent().substring(f.getParent().lastIndexOf('\\')+1);
        
        try {
            // parse the file
            cu = JavaParser.parse(in);
        } finally {
            in.close();
        }
        
        new ClassVisitor().visit(cu, curDir);
    }
    
    public HashSet<String> getMethods() {
        HashSet<String> temp = new HashSet<>();
        
        for(String m : methods){
            temp.add(m.split("\\|")[0]+"."+m.split("\\|")[1]);
        }
        return temp;
    }

    public HashSet<String> getCalls() {
        HashSet<String> temp = new HashSet<>();
        for(String call : calls){
            String caller = call.split("\\|")[0];
            String callee = call.split("\\|")[1];
            String className = getMethodClass(callee);
            if(!className.equals("EXTERNAL")){
                temp.add(caller + "->" + className + "." + callee);
            }
        }
        return temp;
    }
    
    public HashSet<String> getAccesses() {
        HashSet<String> temp = new HashSet<>();
        for(String access : accesses){
            String method = access.split("\\|")[0];
            String attr = access.split("\\|")[1];
            if(!method.contains(".")){
                method = getMethodClass(method) + "." + method;
            }            
            temp.add(method + "|" + attr);
            System.out.println(method + "|" + attr);
        }
        return temp;
    }
    
    public HashSet<String> getAttributes() {
        HashSet<String> temp = new HashSet<>();
        for(String attribute : attributes){
            String pack = attribute.split("\\|")[0];
            String decl = attribute.split("\\|")[1];
            String name = decl.split(" ")[1];
            temp.add(pack + "." + name);
        }
        return temp;
    }
    
    public void printConflicts(){
        System.out.print(conflictCount);
        System.out.println("/"+calls.size());
    }

    public void printClassNames() {
        System.out.println(classes.toString());
    }

    public void printMethodNames() {
        System.out.println(methods.toString());
    }
    
    public void printAttributeNames(){ 
        System.out.println(attributes.toString());
    }
    
    public void printParameterNames(){ 
        System.out.println(parameters.toString());
    }
    
    public void printMethodCalls(){
        for(String call : calls){
            if(call.split("\\|")[1].contains(".")){
                System.out.println(call);
            }
        }
    }
    
    public void printMethodClasses(){
        System.out.println(methodClasses.toString());
    }

    void saveClassNames(String appName,DBOperations dbop) throws SQLException {
        String[] np;
        for(String s:classes){
            np=s.split("\\.");
            dbop.insertClass(appName,np);
        }
        System.out.println("Classes inserted");
    }

    void saveMethodNames(DBOperations dbop) throws SQLException {
        String[] np;
        long id;
        for(String s:methods){
            np=s.split("\\|");
            id=dbop.insertMethod(np[0].split("\\."),np[1]);
            dbop.insertParameter(id,np);
        }
        System.out.println("Methods inserted");
    }

    void saveAttributeNames(DBOperations dbop) throws SQLException {
        String[] np;
        for(String s:attributes){
            np=s.split("\\|");
            dbop.insertAttribute(np);
        }
        System.out.println("Attributes inserted");
    }

    void saveMethodCallNames(DBOperations dbop) throws SQLException {
        String[] np;
        long id;
        for(String s:parameters){
            np=s.split("\\|");
            id=dbop.insertMethodCall(np[0].split("\\."),np[1],np[2]);
            dbop.insertMethodCallParameter(id,np);
        }
        System.out.println("Method Calls inserted");
    }
    
    private class ClassVisitor extends VoidVisitorAdapter {

        @Override
        public void visit(ClassOrInterfaceDeclaration n, Object arg) {
            String classNamewithPack= ((String) arg).trim() + "." + n.getName().trim();
            classes.add(classNamewithPack);
            new FieldVisitor().visit(n,classNamewithPack);
            new MethodVisitor().visit(n,classNamewithPack);
            new ConstructorVisitor().visit(n,classNamewithPack);
        }
    }
    
    private class ConstructorVisitor extends VoidVisitorAdapter {
        @Override
        public void visit(ConstructorDeclaration n, Object arg) {
            String s=n.getName();
            
            if(n.getParameters()!=null){
                s+="("+n.getParameters().size()+")";
                /*for(Parameter p:n.getParameters())
                    s+=p.toString()+"|";*/
            }
            else{
                s+="(0)";
            }
        
            methods.add(((String) arg).trim()+"|"+ s.trim());
            
            String prev = methodClasses.put(s, ((String) arg).trim());
            if(prev!=null && !prev.equals(((String) arg).trim())){
                conflicts.add(s);
            }
            
            new MethodFieldVisitor().visit(n,((String) arg).trim()+"|"+n.getName());
            new MethodCallVisitor().visit(n, ((String) arg).trim()+"."+s);
            new AttributeAccessVisitor().visit(n, ((String) arg).trim()+"."+s);
        }
    }

    private class MethodVisitor extends VoidVisitorAdapter {

        @Override
        public void visit(MethodDeclaration n, Object arg) {
            String s=n.getName();
            
            if(n.getParameters()!=null){
                s+="("+n.getParameters().size()+")";
                /*for(Parameter p:n.getParameters())
                    s+=p.toString()+"|";*/
            }
            else{
                s+="(0)";
            }
        
            methods.add(((String) arg).trim()+"|"+ s.trim());
            
            String prev = methodClasses.put(s, ((String) arg).trim());
            if(prev!=null && !prev.equals(((String) arg).trim())){
                //conflictCount++;
                conflicts.add(s);
                //System.out.println("WARNING: Method "+n.getName()+" exists in both "+prev+" and "+((String) arg).trim()+" classes.");
            }
            
            new MethodFieldVisitor().visit(n,((String) arg).trim()+"|"+n.getName());
            new MethodCallVisitor().visit(n, ((String) arg).trim()+"."+s);
            new AttributeAccessVisitor().visit(n, ((String) arg).trim()+"."+s);
        }
    }
    
    private class FieldVisitor extends VoidVisitorAdapter {

        @Override
        public void visit(FieldDeclaration n, Object arg) {
            String s="";
            
            if(n.getVariables()!=null){
                for(VariableDeclarator v:n.getVariables()){
                    if(v.toString().contains("="))
                        s+=n.getType()+" "+v.toString().substring(0, v.toString().indexOf('='));
                    else s+=n.getType()+" "+v.toString();
                }
            }
        
            attributes.add(((String) arg).trim()+"|"+ s.trim());
            
        }
    }    
    
    
    private class MethodFieldVisitor extends VoidVisitorAdapter {

        @Override
        public void visit(MethodCallExpr n, Object arg) {
            String s="";
             
            if(n.getArgs()!=null)
                for(Expression t:n.getArgs())
                    parameters.add(((String) arg)+"|"+n.getName()+"|"+t.toString());
        }
    }
    
    private class MethodCallVisitor extends VoidVisitorAdapter {

        @Override
        public void visit(MethodCallExpr n, Object arg) {
            int numArgs = 0;
            if(n.getArgs()!=null){
                numArgs = n.getArgs().size();
                
                for(Expression argExpr : n.getArgs()){
                    String e = argExpr.toString();
                    e=e.replace("this.", "");
                    e=e.split("\\[")[0];
                    if(!e.contains("\\.")){
                        String temp = (String) arg;
                        accesses.add(n.getName()+"|"+temp.substring(0, temp.lastIndexOf('.'))+"."+e);
                    }
                }
            }
            calls.add(((String) arg)+"|"+n.getName()+"("+numArgs+")");
        }
    }
    
    private class AttributeAccessVisitor extends VoidVisitorAdapter {

        @Override
        public void visit(AssignExpr n, Object arg) {
            String e = n.getTarget().toString();
            e=e.replace("this.", "");
            e=e.split("\\[")[0];
            if(!e.contains("\\.")){
                String temp = (String) arg;
                //accesses.add(temp+"|"+temp.substring(0, temp.lastIndexOf('.'))+"."+e);
            }
        }
    }
    
    private String getMethodClass(String mn){
        if(conflicts.contains(mn)){
            conflictCount++;
            return "EXTERNAL";
        }
        String classWithPackage = methodClasses.get(mn);
        return classWithPackage == null ? "EXTERNAL" : classWithPackage;
    }
        
}
