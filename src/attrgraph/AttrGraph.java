/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package attrgraph;

import edu.uci.ics.jung.io.GraphIOException;
import japa.parser.ParseException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author itü
 */
public class AttrGraph {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, ParseException, IOException, SQLException, ClassNotFoundException, GraphIOException {
       
        Parser classParser=new Parser();
        //DBOperations dbop = new DBOperations();
        
        String url;
        //url = "/depot/Work/Academic/Students/PhD/Atakan Aral/javaParser";
        //url = "C:/Users/itü/Desktop/Dropbox/Dropbox/AtakanAral/JavaParser/javaParser";
        //url="/home/atakan/javaParser";
        //url = "C:/javaParser";
        //url = "C:/Users/Atakan/Desktop/eclipse workspace/Structure101Test/src/test";
        
        //url = "C:/Users/Atakan Aral/Documents/NetBeansProjects/HighRandIndexSample";
        url = "C:/Users/Atakan Aral/Documents/NetBeansProjects/LowRandIndexSample";
        
        for(File f : FileUtils.listFiles(new File(url), new String[]{"java"}, true)){
            classParser.parse(f);
        }
        
        //classParser.printClassNames();
        //classParser.printMethodNames();
        //classParser.printAttributeNames();
        //classParser.printParameterNames();
        //classParser.printMethodCalls();
        //classParser.printMethodClasses();

        //Visualizer.visualizeGraph(Grapher.getCallGraph(classParser.getMethods(), classParser.getCalls()));
        
        Visualizer.visualizeGraph(Grapher.getAccessGraph(classParser.getMethods(), classParser.getAttributes(), classParser.getAccesses()));
        /*
        Clusterer c = new Clusterer();
        System.out.println("Call Graph\n\n");
        c.cluster(Grapher.getCallGraph(classParser.getMethods(), classParser.getCalls()));

        System.out.println("\n\nAccess Graph\n\n");
        c.cluster(Grapher.getAccessGraph(classParser.getMethods(), classParser.getAttributes(), classParser.getAccesses()));
        */
        //GraphIO.writeGraphML(Grapher.getCallGraph(classParser.getMethods(), classParser.getCalls()),"/depot/Work/Academic/Students/PhD/Atakan Aral/NetworkX/parser.graphml");
        //Visualizer.visualizeCallGraph(GraphIO.readGraphML("/depot/Work/Academic/Students/PhD/Atakan Aral/NetworkX/parser.graphml"));
        
        //classParser.printConflicts();
        
        //dbop.connectDB();
        //dbop.createAppsDB();
        //dbop.insertApp("javaParser");
        //dbop.createClassesDB();
        //classParser.saveClassNames("javaParser",dbop);
        //dbop.createMethodsDB();
        //dbop.createParametersDB();
        //classParser.saveMethodNames(dbop);
        //dbop.createAttributesDB();
        //classParser.saveAttributeNames(dbop);
        //dbop.createMethodCallsDB();
        //dbop.createMethodCallParametersDB();
        //classParser.saveMethodCallNames(dbop);
        
        //dbop.closeCon();
    }
    

        
}
