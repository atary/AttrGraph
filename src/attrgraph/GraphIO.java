/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package attrgraph;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.io.GraphIOException;
import edu.uci.ics.jung.io.GraphMLWriter;
import edu.uci.ics.jung.io.graphml.*;
import java.io.*;
import java.util.HashSet;
import org.apache.commons.collections15.Transformer;

/**
 *
 * @author ovatman
 */
public class GraphIO {
    public static void writeGraphML(Graph<String, Integer> g,String filename) throws IOException{
        GraphMLWriter<String, Integer> graphWriter = new GraphMLWriter<String, Integer> ();

        PrintWriter out = new PrintWriter(
                      new BufferedWriter(
                          new FileWriter(filename)));

        graphWriter.save(g, out);
    }
    
     public static Graph<String, Integer> readGraphML(String filename) throws IOException, GraphIOException{
             BufferedReader fileReader = new BufferedReader(new FileReader(filename));
             
        /* Create the Graph Transformer */
        Transformer<GraphMetadata, Graph<String, Integer>>
        graphTransformer = new Transformer<GraphMetadata,
                                Graph<String, Integer>>() {

        public Graph<String, Integer>
            transform(GraphMetadata metadata) {
                if (metadata.getEdgeDefault().equals(
                metadata.getEdgeDefault().DIRECTED)) {
                    return new
                    DirectedSparseGraph<String, Integer>();
                } else {
                    return new
                    UndirectedSparseGraph<String, Integer>();
                }
            }
        };
        
        /* Create the Vertex Transformer */
        Transformer<NodeMetadata, String> vertexTransformer
        = new Transformer<NodeMetadata, String>() {
            public String transform(NodeMetadata metadata) {
                String v=new String();
                return v;
            }
        };

        /* Create the Edge Transformer */
        Transformer<EdgeMetadata, Integer> edgeTransformer =
        new Transformer<EdgeMetadata, Integer>() {
            public Integer transform(EdgeMetadata metadata) {
                Integer e=new Integer(0);
                return e;
            }
        };
        
        /* Create the Hyperedge Transformer */
        Transformer<HyperEdgeMetadata, Integer> hyperEdgeTransformer
        = new Transformer<HyperEdgeMetadata, Integer>() {
            public Integer transform(HyperEdgeMetadata metadata) {
                Integer e = new Integer(0);
                return e;
            }
        };
        
        /* Create the graphMLReader2 */
        GraphMLReader2<Graph<String, Integer>, String, Integer>
        graphReader = new
        GraphMLReader2<Graph<String, Integer>, String, Integer>
            (fileReader, graphTransformer, vertexTransformer,
            edgeTransformer, hyperEdgeTransformer);
        
         return graphReader.readGraph();

         
    }   


}
