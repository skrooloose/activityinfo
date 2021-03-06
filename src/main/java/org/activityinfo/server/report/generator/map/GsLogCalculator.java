package org.activityinfo.server.report.generator.map;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.util.List;

import org.activityinfo.server.report.generator.map.cluster.Cluster;

public class GsLogCalculator implements RadiiCalculator {

    private double minRadius;
    private double maxRadius;

    public GsLogCalculator(double minRadius, double maxRadius) {
        this.minRadius = minRadius;
        this.maxRadius = maxRadius;
    }

    @Override
    public void calculate(List<Cluster> clusters) {

        // FIRST PASS:
        // calculate min and max of the markers
        // calculate the weightedCentroid of the clusters

        double[] values = new double[clusters.size()];
        double minValue = Double.MAX_VALUE;
        double maxValue = Double.MIN_VALUE;

        for (int i = 0; i != clusters.size(); ++i) {

            values[i] = clusters.get(i).sumValues();

            if (values[i] > maxValue) {
                maxValue = values[i];
            }
            if (values[i] < minValue) {
                minValue = values[i];
            }
        }

        // / SECOND PASS: calculate symbol size

        double logMin = Math.log(minValue);
        double logMax = Math.log(maxValue);
        double logRange = logMax - logMin;
        double symbolRange = maxRadius - minRadius;

        for (int i = 0; i != clusters.size(); ++i) {

            double logValue = Math.log(values[i]);

            double p = ((logValue - logMin) / logRange);
            if (Double.isNaN(p)) {
                p = 0.0;
            }

            clusters.get(i)
                .setRadius(Math.round(minRadius + (symbolRange * p)));
        }
    }
}
