package no.hvl.dat100ptc.oppgave5;

import javax.swing.JOptionPane;

import easygraphics.EasyGraphics;
import no.hvl.dat100ptc.TODO;
import no.hvl.dat100ptc.oppgave1.GPSPoint;
import no.hvl.dat100ptc.oppgave3.GPSUtils;
import no.hvl.dat100ptc.oppgave4.GPSComputer;

public class ShowRoute extends EasyGraphics {

	private static int MARGIN = 50;
	private static int MAPXSIZE = 500;
	private static int MAPYSIZE = 500;

	private GPSPoint[] gpspoints;
	private GPSComputer gpscomputer;
	
	public ShowRoute() {

		String filename = JOptionPane.showInputDialog("GPS data filnavn: ");
		gpscomputer = new GPSComputer(filename);

		gpspoints = gpscomputer.getGPSPoints();

	}

	public static void main(String[] args) {
		launch(args);
	}

	public void run() {

		makeWindow("Route", MAPXSIZE + 2 * MARGIN, MAPYSIZE + 2 * MARGIN);

		showRouteMap(MARGIN + MAPYSIZE);
		
		showStatistics();
	}

	// antall x-pixels per lengdegrad
	public double xstep() {

		double maxlon = GPSUtils.findMax(GPSUtils.getLongitudes(gpspoints));
		double minlon = GPSUtils.findMin(GPSUtils.getLongitudes(gpspoints));

		double xstep = MAPXSIZE / (Math.abs(maxlon - minlon)); 

		return xstep;
	}

	// antall y-pixels per breddegrad
	public double ystep() {
		
		double maxlat = GPSUtils.findMax(GPSUtils.getLatitudes(gpspoints));
		double minlat = GPSUtils.findMin(GPSUtils.getLatitudes(gpspoints));
		
		double ystep = MAPXSIZE / (Math.abs(maxlat - minlat));
		
		return ystep;
	}

	public void showRouteMap(int ybase) {
		
		double xzero = GPSUtils.findMin(GPSUtils.getLongitudes(gpspoints));
		double xste = this.xstep();
		double yzero = GPSUtils.findMin(GPSUtils.getLatitudes(gpspoints));
		double yste = this.ystep();
		
		setColor(0,255,0);
		int x = MARGIN + (int)((gpspoints[0].getLongitude() - xzero) * xste);
		int y = ybase - (int)((gpspoints[0].getLatitude() - yzero) * yste);
		
		for (int i = 1; i < gpspoints.length; i++) {
			drawCircle(x,y,2);
			int newx = MARGIN + (int)((gpspoints[i].getLongitude() - xzero) * xste);
			int newy = ybase - (int)((gpspoints[i].getLatitude() - yzero) * yste);
			drawLine(x,y,newx,newy);
			x=newx;
			y=newy;
		}
		
	}
	
	private static double WEIGHT = 80.0;
	
	public void showStatistics() {

		int TEXTDISTANCE = 20;

		setColor(0,0,0);
		setFont("Courier",12);
		
		drawString("Total Time     :" + GPSUtils.formatTime(gpscomputer.totalTime()),TEXTDISTANCE,TEXTDISTANCE);
		drawString("Total distance :" + GPSUtils.formatDouble(gpscomputer.totalDistance()) + " km",TEXTDISTANCE,TEXTDISTANCE+13);
		drawString("Total elevation:" + GPSUtils.formatDouble(gpscomputer.totalElevation()) + " m",TEXTDISTANCE,TEXTDISTANCE+13*2);
		drawString("Max speed      :" + GPSUtils.formatDouble(gpscomputer.maxSpeed()) + " km/t",TEXTDISTANCE,TEXTDISTANCE+13*3);
		drawString("Average speed  :" + GPSUtils.formatDouble(gpscomputer.averageSpeed()) + " km/t",TEXTDISTANCE,TEXTDISTANCE+13*4);
		drawString("Energy         :" + GPSUtils.formatDouble(gpscomputer.totalKcal(WEIGHT)) + " kcal",TEXTDISTANCE,TEXTDISTANCE+13*5);

	}
}
