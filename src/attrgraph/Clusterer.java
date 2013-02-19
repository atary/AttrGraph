/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package attrgraph;

import edu.uci.ics.jung.algorithms.cluster.EdgeBetweennessClusterer;
import edu.uci.ics.jung.algorithms.cluster.WeakComponentClusterer;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.UndirectedGraph;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author itü
 */
public class Clusterer {
    private HashMap<Integer,String> vertexDict;
    private TreeMap<Integer,Integer> clustersIndexed;
    
    public Clusterer(){
        vertexDict = new HashMap<Integer,String>();
        clustersIndexed = new TreeMap<Integer,Integer>();
    }
    
    public void cluster(Graph<String,Integer> g){
        hashVertex(g);
        
        WeakComponentClusterer<String,Integer> clusterer = new WeakComponentClusterer<String,Integer>();
        
        Set<Set<String>> clusters = clusterer.transform(g);
        
        int i=0;
        
        for(Set<String> ss:clusters){
            for(String s : ss)
                clustersIndexed.put(s.hashCode(), i);
            i++;
        }
        
        System.out.print("c(");
        for(Map.Entry<Integer,Integer> e:clustersIndexed.entrySet())
            System.out.print(e.getValue()+",");
        
        
        vertexDict.clear();
        clustersIndexed.clear();
        
    }
    
    private void hashVertex(Graph<String,Integer> g){
        int hash=0;
        for(String s:g.getVertices()){
            hash=s.hashCode();
            vertexDict.put(hash,s);
        }
        
    }
}
