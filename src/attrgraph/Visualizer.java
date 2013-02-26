/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package attrgraph;

import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import java.awt.Dimension;
import javax.swing.JFrame;
/**
 *
 * @author Atakan
 */
public class Visualizer {
    public static void visualizeGraph(Graph<String, Integer> g){
            Layout<String, Integer> layout = new ISOMLayout<String, Integer>(g);
            layout.setSize(new Dimension(800,600)); // sets the initial size of the space
            BasicVisualizationServer<String, Integer> vv = new BasicVisualizationServer<String, Integer>(layout);
            vv.setPreferredSize(new Dimension(800,600)); //Sets the viewing area size
            vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<String>());
            vv.getRenderer().getVertexLabelRenderer().setPosition(Position.AUTO);
            JFrame frame = new JFrame("Graph with "+g.getVertexCount()+" vertices and "+g.getEdgeCount()+" edges.");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(vv);
            frame.pack();
            frame.setVisible(true);
    }
}
