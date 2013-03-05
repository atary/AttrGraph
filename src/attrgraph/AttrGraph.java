/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package attrgraph;

import edu.uci.ics.jung.io.GraphIOException;
import japa.parser.ParseException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
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
        url = "C:/javaParser";
        String projectName = url.replace(url.substring(0, url.lastIndexOf("/")+1), "");
        //url = "C:/Users/Atakan/Desktop/eclipse workspace/Structure101Test/src/test";
        
        //url = "C:/Users/Atakan Aral/Documents/NetBeansProjects/HighRandIndexSample";
        //url = "C:/Users/Atakan Aral/Documents/NetBeansProjects/LowRandIndexSample";
        //url = "C:/Users/Æ/Desktop/Dropbox/Tolga Ovatman/JavaParser/javaParser";
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
        
        //Visualizer.visualizeGraph(Grapher.getAccessGraph(classParser.getMethods(), classParser.getAttributes(), classParser.getAccesses()));

        //Visualizer.visualizeGraph(Grapher.getCoopMethodsGraph(classParser.getMethods(), classParser.getCalls()));
        //Visualizer.visualizeGraph(Grapher.getMethodLayoutGraph(classParser.getMethods()));
        //Visualizer.visualizeGraph(Grapher.getCoopAttrsGraph(classParser.getAttributes(),classParser.getCalls(),classParser.getAccesses()));
        //Visualizer.visualizeGraph(Grapher.getAttrLayoutGraph(classParser.getAttributes()));
        
        //System.out.println(classParser.getInternalCalls().size());
        //System.out.println(classParser.getCalls().size());
        
        Clusterer c = new Clusterer();
        
        //System.out.println("Coop Methods Graph\n\na<-");
        System.out.print("a<-");
        c.cluster(Grapher.getCoopMethodsGraph(classParser.getMethods(), classParser.getCalls()));

        //System.out.println("\n\nMethod Call Graph\n\nb<-");
        System.out.print("\nb<-");
        c.cluster(Grapher.getCallGraph(classParser.getMethods(), classParser.getCalls()));
        
        //System.out.println("\n\nInternal Method Call Graph\n\nc<-");
        System.out.print("\nc<-");
        c.cluster(Grapher.getCallGraph(classParser.getMethods(), classParser.getInternalCalls()));

        //System.out.println("\n\nMethod Layout Graph\n\nd<-");
        System.out.print("\nd<-");
        c.cluster(Grapher.getMethodLayoutGraph(classParser.getMethods()));
        System.out.println("\nadjustedRandIndex(a,b)");
        System.out.println("adjustedRandIndex(a,c)");
        System.out.println("adjustedRandIndex(a,d)");
        System.out.println("adjustedRandIndex(b,c)");
        System.out.println("adjustedRandIndex(b,d)");
        System.out.println("adjustedRandIndex(c,d)");
        System.out.println("\n\n");
        
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
