/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.generator.map;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.sigmah.server.report.generator.map.cluster.Cluster;
import org.sigmah.server.report.generator.map.cluster.genetic.BubbleFitnessFunctor;
import org.sigmah.server.report.generator.map.cluster.genetic.GeneticSolver;
import org.sigmah.server.report.generator.map.cluster.genetic.MarkerGraph;
import org.sigmah.server.report.generator.map.cluster.genetic.UpperBoundsCalculator;
import org.sigmah.shared.report.content.AiLatLng;
import org.sigmah.shared.report.content.Point;
import org.sigmah.shared.report.model.MapSymbol;
import org.sigmah.shared.report.model.PointValue;
import org.sigmah.shared.report.model.SiteData;

/*
* Real example that didn't work.
*
* @author Alex Bertram
*/
public class CoincidentPointsClusterTest extends GraphTest {


    @Test
    public void testSimpleData() throws Exception {
    	
    	List<PointValue> points = new ArrayList<PointValue>();
        points.add(new PointValue(new SiteData(), new MapSymbol(), 7.0, new Point(0, 0)));
        points.add(new PointValue(new SiteData(), new MapSymbol(), 2.0, new Point(0, 0)));
        points.add(new PointValue(new SiteData(), new MapSymbol(), 41.0, new Point(100, 100)));
        points.add(new PointValue(new SiteData(), new MapSymbol(), 9.0, new Point(0, 0)));
        points.add(new PointValue(new SiteData(), new MapSymbol(), 39.0, new Point(100, 100)));

        double originalSum = 7 + 2 + 9 + 41 + 39;

        // Now build the graph

        MarkerGraph graph = new MarkerGraph(points,
                new BubbleIntersectionCalculator(15));

        GeneticSolver solver = new GeneticSolver();

        List<Cluster> clusters = solver.solve(graph, new GsLogCalculator(5, 15),
                new BubbleFitnessFunctor(), UpperBoundsCalculator.calculate(graph, new FixedRadiiCalculator(5)));

        // check to make sure all values were included
        double sumAfterClustering = 0;
        for (Cluster cluster : clusters) {
            sumAfterClustering += cluster.sumValues();
        }

        Assert.assertEquals(originalSum, sumAfterClustering, 0.0001);

        Assert.assertEquals(2, clusters.size());

        saveClusters(graph, "clusterTest-solution", clusters);


    }


    @Test
    public void testRealData() throws Exception {

        // Define projection for the test case
        TiledMap map = new TiledMap(492, 690, new AiLatLng(2.293492496, 30.538372993), 9);

        // Read data
        BufferedReader in = new BufferedReader(new InputStreamReader(
                GraphTest.class.getResourceAsStream("/distribscolaire-points.csv")));

        double originalSum = 0;

        List<PointValue> points = new ArrayList<PointValue>();
        while (in.ready()) {

            String line = in.readLine();
            String[] columns = line.split(",");

            double lat = Double.parseDouble(columns[0]);
            double lng = Double.parseDouble(columns[1]);

            PointValue pv = new PointValue();
            pv.px = map.fromLatLngToPixel(new AiLatLng(lat, lng));
            pv.value = Double.parseDouble(columns[2]);
            pv.symbol = new MapSymbol();
            pv.site = new SiteData();

            originalSum += pv.value;

            points.add(pv);
        }

        // Now build the graph

        MarkerGraph graph = new MarkerGraph(points,
                new BubbleIntersectionCalculator(15));

        // make sure nothing was lost in the merging of coincident points
        double nodeSum = 0;
        for (MarkerGraph.Node node : graph.getNodes()) {
            nodeSum += node.getPointValue().value;
        }
        Assert.assertEquals("values after construction of graph", originalSum, nodeSum, 0.001);


        saveGraphImage("clusterTest2", graph, 15);

        GeneticSolver solver = new GeneticSolver();

        List<Cluster> clusters = solver.solve(graph, new GsLogCalculator(5, 15),
                new BubbleFitnessFunctor(), UpperBoundsCalculator.calculate(graph, new FixedRadiiCalculator(5)));

        // check to make sure all values were included
        double sumAfterClustering = 0;
        for (Cluster cluster : clusters) {
            sumAfterClustering += cluster.sumValues();
        }

        Assert.assertEquals(originalSum, sumAfterClustering, 0.001);

        Assert.assertEquals(15, clusters.size());

        saveClusters(graph, "clusterTest-solution", clusters);
    }

}
