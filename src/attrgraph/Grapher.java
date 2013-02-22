/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package attrgraph;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.Graph;
import java.util.HashMap;
/*import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.stmt.ExpressionStmt;
import japa.parser.ast.stmt.Statement;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;*/
import java.util.HashSet;


/**
 *
 * @author Atakan Aral
 */
public class Grapher {
    /*private CompilationUnit cu;
    private FileInputStream in;
    
    public void parse(File f) throws FileNotFoundException, ParseException, IOException{
        in = new FileInputStream(f.getAbsolutePath());
        
        try {
            cu = JavaParser.parse(in);
            parseCallGraph(cu);
        } finally {
            in.close();
        }
    }*/

    public static Graph<String, Integer> getCallGraph(HashSet<String> methods, HashSet<String> calls){
        Graph<String, Integer> g = new DirectedSparseGraph<String, Integer>();
        String tmp1,tmp2;
        for(String m : methods){
            //g.addVertex(""+(m.substring(m.lastIndexOf('/')+1)).hashCode());
            g.addVertex(m);
        }
        
        int id =0;
        
        for(String c : calls){
            tmp1=c.split("->")[0];
            tmp2=c.split("->")[1];
            if(!methods.contains(tmp1) || !methods.contains(tmp2)) continue;
            g.addEdge(id++, tmp1, tmp2);
        }
        
        return g;
    }
    
    public static Graph<String, Integer> getAccessGraph(HashSet<String> methods, HashSet<String> attributes, HashSet<String> accesses){
        Graph<String, Integer> g = new SparseGraph<String, Integer>();
        
        for(String m : methods){
            g.addVertex(m);
        }
        
        int id = 0;
        
        HashMap<String, String> connections;
        connections = new HashMap<>();
        
        for(String access : accesses){
            String method = access.split("\\|")[0];
            String attr = access.split("\\|")[1];
            
            if(!attributes.contains(attr)) continue;
            if(!methods.contains(method)) continue;
            
            if(!connections.containsKey(attr)){
                connections.put(attr, method);
            }
            else{
                String value = connections.get(attr);
                connections.put(attr, value+"|"+method);
            }
        }
        
        for(String attr : connections.keySet()){
            String methodString = connections.get(attr);
            String[] methodArray = methodString.split("\\|");
            for(String m1:methodArray){
                for(String m2:methodArray){
                    if(m1.equals(m2)) continue;
                    g.addEdge(id++, m1, m2);
                }
            }
        }
        
        return g;
    }
    
    
    /*private void parseCallGraph(CompilationUnit cu) {
        List<TypeDeclaration> types = cu.getTypes();
        
        if(types==null) {
            return;
        }
            
        for (TypeDeclaration type : types) {
            List<BodyDeclaration> members = type.getMembers();
            
            if(members==null) {
                continue;
            }
            
            for (BodyDeclaration member : members) {
                if (member instanceof MethodDeclaration) {
                    MethodDeclaration method = (MethodDeclaration) member;
                    
                    System.out.print(cu.getPackage().getName()+".");
                    System.out.print(type.getName()+".");
                    System.out.print(method.getName()+"(");
                    
                    List<Parameter> parameters = method.getParameters();
                    if(parameters!=null){
                        String parameterList = parameters.toString().trim().replaceAll(", ",",");
                        System.out.print(parameterList.substring(1,parameterList.length()-1));
                    }
                    
                    System.out.println("):" + method.getType().toString());
                    
                    List<Statement> statements = method.getBody().getStmts();
                    if(statements==null) {
                        continue;
                    }
                    for (Statement statement : statements) {
                        if(statement instanceof ExpressionStmt){
                            ExpressionStmt expression = (ExpressionStmt)statement;
                            if(expression.getExpression() instanceof MethodCallExpr){
                                MethodCallExpr methodCall = (MethodCallExpr)expression.getExpression();
                                System.out.println(methodCall.getScope().toString()+"."+methodCall.getName());
                            }
                        }
                    }
                }
            }
        }
    }*/
}
