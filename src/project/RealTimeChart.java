package project;

import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler.LegendPosition;
import org.knowm.xchart.style.markers.SeriesMarkers;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;

public class RealTimeChart {
	XYChart chart;
	SwingWrapper<XYChart> sw ;
	
	public RealTimeChart(double[][] initdata) {
		chart = QuickChart.getChart("Wave Packet", "Position", "", "wavePacket", initdata[0], initdata[1]);
		chart.getStyler().setYAxisMax(1.5);
		
		XYSeries potential = chart.addSeries("potential", initdata[0], initdata[2]);
		potential.setMarker(SeriesMarkers.CIRCLE);
		
		chart.getStyler().setLegendPosition(LegendPosition.InsideSW);
		sw = new SwingWrapper<XYChart>(chart);
		sw.displayChart();
	}
	
	public void updateChart(double[][] data){
		chart.updateXYSeries("wavePacket", data[0], data[1], null);
		chart.updateXYSeries("wavePacket", data[0], data[1], null);
		sw.repaintChart();
	}
	
}
