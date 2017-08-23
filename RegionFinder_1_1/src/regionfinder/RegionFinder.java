/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package regionfinder;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author admin
 */
public class RegionFinder {

    //List of all markers
    private static List<Marker> markers = new ArrayList<>();
    //List of all regions
    private static List<Region> regions = new ArrayList<>();
    //List of list of all markers arranged by chromosome
    private static List<List<Marker>> mbc = new ArrayList<>();
    //List of list of all markers arranged by chromosome
    private static List<List<Region>> rbc = new ArrayList<>();

    private static final String DELIMITER = "\\s";

    public static class Marker implements Comparator<Marker> {

        int position;
        String name;
        int chr;
        double p;
        int region;
        int isSignificant;
        int hasNeighbor;
        int sumSigHasNeighbor;
        boolean upstreamLimit;
        boolean downstreamLimit;
        boolean suggestive;

        public boolean isSuggestive() {
            return suggestive;
        }

        public void setSuggestive(boolean suggestive) {
            this.suggestive = suggestive;
        }

        public boolean isUpstreamLimit() {
            return upstreamLimit;
        }

        public void setUpstreamLimit(boolean upstreamLimit) {
            this.upstreamLimit = upstreamLimit;
        }

        public boolean isDownstreamLimit() {
            return downstreamLimit;
        }

        public void setDownstreamLimit(boolean downstreamLimit) {
            this.downstreamLimit = downstreamLimit;
        }

        public int getSumSigHasNeighbor() {
            return sumSigHasNeighbor;
        }

        public void setSumSigHasNeighbor(int sumSigHasNeighbor) {
            this.sumSigHasNeighbor = sumSigHasNeighbor;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getChr() {
            return chr;
        }

        public void setChr(int chr) {
            this.chr = chr;
        }

        public double getP() {
            return p;
        }

        public void setP(double p) {
            this.p = p;
        }

        public int getRegion() {
            return region;
        }

        public void setRegion(int region) {
            this.region = region;
        }

        public int getIsSignificant() {
            return isSignificant;
        }

        public void setIsSignificant(int isSignificant) {
            this.isSignificant = isSignificant;
        }

        public int getHasNeighbor() {
            return hasNeighbor;
        }

        public void setHasNeighbor(int hasNeighbor) {
            this.hasNeighbor = hasNeighbor;
        }

        @Override
        public int compare(Marker o1, Marker o2) {
            return Integer.compare(o1.getPosition(), o2.getPosition());
        }

        public double compareDouble(Marker o1, Marker o2) {
            return Double.compare(o1.getP(), o2.getP());
        }

    }

    public static class Region {

        int regionNumber;
        int chr;
        int regionStart;
        int regionStop;
        int numSigMarkers;
        int numSugMarkers;
        int numTotalMarkers;
        int sizeOfRegion;
        String minMarkerName;
        int minMarkerPosition;
        double minP;

        public double getMinP() {
            return minP;
        }

        public void setMinP(double minP) {
            this.minP = minP;
        }

        public int getRegionNumber() {
            return regionNumber;
        }

        public void setRegionNumber(int regionNumber) {
            this.regionNumber = regionNumber;
        }

        public int getChr() {
            return chr;
        }

        public void setChr(int chr) {
            this.chr = chr;
        }

        public int getRegionStart() {
            return regionStart;
        }

        public void setRegionStart(int regionStart) {
            this.regionStart = regionStart;
        }

        public int getRegionStop() {
            return regionStop;
        }

        public void setRegionStop(int regionStop) {
            this.regionStop = regionStop;
        }

        public int getNumSigMarkers() {
            return numSigMarkers;
        }

        public void setNumSigMarkers(int numSigMarkers) {
            this.numSigMarkers = numSigMarkers;
        }

        public int getNumSugMarkers() {
            return numSugMarkers;
        }

        public void setNumSugMarkers(int numSugMarkers) {
            this.numSugMarkers = numSugMarkers;
        }

        public int getNumTotalMarkers() {
            return numTotalMarkers;
        }

        public void setNumTotalMarkers(int numTotalMarkers) {
            this.numTotalMarkers = numTotalMarkers;
        }

        public int getSizeOfRegion() {
            return sizeOfRegion;
        }

        public void setSizeOfRegion(int sizeOfRegion) {
            this.sizeOfRegion = sizeOfRegion;
        }

        public String getMinMarkerName() {
            return minMarkerName;
        }

        public void setMinMarkerName(String minMarkerName) {
            this.minMarkerName = minMarkerName;
        }

        public int getMinMarkerPosition() {
            return minMarkerPosition;
        }

        public void setMinMarkerPosition(int minMarkerPosition) {
            this.minMarkerPosition = minMarkerPosition;
        }

        public int compare(Region o1, Region o2) {
            return Integer.compare(o1.getRegionStart(), o2.getRegionStart());
        }

    }

    private static void readFile(String textfile) throws FileNotFoundException {

        Scanner scanner;

        scanner = new Scanner(
                new BufferedReader(
                        new FileReader(textfile)));

        String currentLine;

        String[] currentTokens;

//        Ignores first line of text where labels go
        scanner.nextLine();

        while (scanner.hasNextLine()) {

            currentLine = scanner.nextLine();

            currentTokens = currentLine.split(DELIMITER);

            Marker currentMarker = new Marker();

            currentMarker.setName(currentTokens[0]);

            currentMarker.setChr(Integer.parseInt(currentTokens[1]));

            currentMarker.setPosition(Integer.parseInt(currentTokens[2]));

            if (currentTokens[3].equalsIgnoreCase("#N/A")
                    || currentTokens[3].equalsIgnoreCase("NA")) {
                currentMarker.setP(1.00);
            } else {
                currentMarker.setP(Double.parseDouble(currentTokens[3]));
            }

            markers.add(currentMarker);

        }

        scanner.close();

    }

    private static void sortMarkersByChromosome() {

        for (int x = 1; x <= 23; x++) {

            List<Marker> lm = new ArrayList<>();

            for (Marker m : markers) {

                if (m.getChr() == x) {

                    lm.add(m);
                }

            }

            mbc.add(lm);

        }
    }

    private static void sortRegionsByChromosome() {

        for (int x = 1; x <= 23; x++) {

            

            for (Region r : regions) {

                if (r.getChr() == x) {
                    
                    List<Region> lr = new ArrayList<>();

                    lr.add(r);
                    rbc.add(lr);
                }
                
            }
            
            

        }
    }

    private static void writeFile() throws FileNotFoundException {

        String format = "%-7s%-15s%-8s%-16s%-15s%-16s%-14s%-8s%-8s%-7s%-13s";

        PrintWriter pw = new PrintWriter("output.txt");

        pw.println("Region\t"
                + "MarkerName\t" + "Chr\t" + "Position \t"
                + "P-value\t" + "RegionStart\t" + "RegionStop\t"
                + "NumSigMarkers\t" + "NumSuggestiveMarkers\t"
                + "NumTotalMarkers" + "SizeOfRegion");

        for (List<Region> l : rbc) {

            for (Region r : l) {

                pw.println(String.format(format, r.getRegionNumber(),
                        r.getMinMarkerName(),
                        r.getChr(),
                        r.getMinMarkerPosition(),
                        r.getMinP(),
                        r.getRegionStart(),
                        r.getRegionStop(),
                        r.getNumSigMarkers(),
                        r.getNumSugMarkers(),
                        r.getNumTotalMarkers(),
                        r.getSizeOfRegion()
                ));
                pw.flush();

            }
            
        }
        
        pw.close();
    }

    private static void characterizeMarkers(double index , double suggestive, int space) {

        for (List<Marker> l : mbc) {

            List<Marker> sugs = new ArrayList<>();

            for (Marker m : l) {

                if (m.getP() < suggestive) {
                    m.setSuggestive(true);
                    sugs.add(m);
                }

            }

            sugs.sort((o1, o2) -> Integer.compare(o1.getPosition(), o2.getPosition()));

            for (Marker m : sugs) {

                if (m.getP() < index) {
                    m.setIsSignificant(1);
                }

            }
            for (Marker m : sugs) {

                if (m.getIsSignificant() == 1) {
                    for (Marker s : sugs) {
                        if (Math.abs(m.getPosition() - s.getPosition()) <= space) {
                            m.setHasNeighbor(1);
                            s.setHasNeighbor(1);

                        }
                    }
                }
            }
            
            for (Marker m : sugs) {

                if (m.getHasNeighbor() == 1) {
                    for (Marker s : sugs) {
                        if (Math.abs(m.getPosition() - s.getPosition()) <= space) {
                            
                            s.setHasNeighbor(1);

                        }
                    }
                }
            }

            for (Marker m : sugs) {

                m.setSumSigHasNeighbor(m.getHasNeighbor() + m.getIsSignificant());

            }

            for (int x = 1; x < sugs.size() - 1; x++) {

                if ((sugs.get(x - 1).getSumSigHasNeighbor() == 0)
                        && (sugs.get(x).getSumSigHasNeighbor() != 0)
                        && (sugs.get(x + 1).getSumSigHasNeighbor() == 0)) {

                    sugs.get(x).setUpstreamLimit(true);
                    sugs.get(x).setDownstreamLimit(true);
                }

                if ((sugs.get(x - 1).getSumSigHasNeighbor() == 0)
                        && (sugs.get(x).getSumSigHasNeighbor() != 0)
                        && (sugs.get(x + 1).getSumSigHasNeighbor() != 0)) {

                    sugs.get(x).setUpstreamLimit(true);

                }

                if ((sugs.get(x - 1).getSumSigHasNeighbor() != 0)
                        && (sugs.get(x).getSumSigHasNeighbor() != 0)
                        && (sugs.get(x + 1).getSumSigHasNeighbor() == 0)) {

                    sugs.get(x).setDownstreamLimit(true);

                }

                if ((x - 1 == 0)
                        && (sugs.get(x - 1).getSumSigHasNeighbor() != 0)
                        && (sugs.get(x).getSumSigHasNeighbor() != 0)) {

                    sugs.get(x - 1).setUpstreamLimit(true);

                }

                if ((sugs.get(x).getSumSigHasNeighbor() != 0)
                        && (sugs.get(x + 1).getSumSigHasNeighbor() != 0)
                        && (x + 2 == sugs.size())) {

                    sugs.get(x + 1).setDownstreamLimit(true);

                }

            }

        }

    }

//   Adds characterized regions to region list
    private static void characterizeRegions() {

        for (List<Marker> l : mbc) {

            List<Marker> sugs = new ArrayList<>();

            for (Marker m : l) {

                if (m.isSuggestive() == true) {

                    sugs.add(m);

                }

            }

            for (Marker m : sugs) {

                for (Marker n : sugs) {

                    if (m.isUpstreamLimit() && n.isDownstreamLimit()
                            && (n.getPosition() > m.getPosition())) {
                            
                            
                        
                        

                        Region r = new Region();

                        r.setRegionStart(m.getPosition());
                        r.setRegionStop(n.getPosition());
                        r.setChr(m.getChr());

                        int totalMarkers = 0;

                        for (Marker o : l) {
                            if (o.getPosition() >= r.getRegionStart()
                                    && o.getPosition() <= r.getRegionStop()) {
                                totalMarkers += 1;
                            }
                            r.setNumTotalMarkers(totalMarkers);
                        }

                        int sugMarkers = 0;

                        int sigMarkers = 0;

                        for (Marker p : sugs) {

                            if (p.getPosition() >= r.getRegionStart()
                                    && p.getPosition() <= r.getRegionStop()) {
                                sugMarkers += 1;
                                if (p.getIsSignificant() == 1) {
                                    sigMarkers += 1;

                                }
                            }
                        }
                        r.setNumSigMarkers(sigMarkers);
                        r.setNumSugMarkers(sugMarkers);
                        r.setSizeOfRegion((r.getRegionStop() - r.getRegionStart()) + 1);

                        List<Marker> markersInRegion = new ArrayList<>();

                        for (Marker o : sugs) {

                            if (o.getPosition() >= r.getRegionStart()
                                    && o.getPosition() <= r.getRegionStop()) {
                                markersInRegion.add(o);
                            }

                        }
                        markersInRegion.sort((o1, o2) -> Double.compare(o1.getP(), o2.getP()));
                        r.setMinP(markersInRegion.get(0).getP());
                        r.setMinMarkerPosition(markersInRegion.get(0).getPosition());
                        r.setMinMarkerName(markersInRegion.get(0).getName());

                        regions.add(r);
                        
                        for (Marker q : sugs ){
                            if ( (q.getPosition() > r.getRegionStart()) &&
                                    (q.getPosition() < r.getRegionStop()) &&
                                    (q.getSumSigHasNeighbor() == 0)) {
                                
                                regions.remove(r);
                                
                            }
                                  
                        }
                            

                    }
                }

            }

        }

    }
    
    public static void addRegionNumber(){
        
        int regionNum = 0;
        
        for (List<Region> l : rbc){
            l.sort((o1, o2) -> Integer.compare(o1.getRegionStart(), o2.getRegionStart()));
            

            for (int x = 0; x <= l.size() - 1; x++) {

                regionNum += 1;

                l.get(x).setRegionNumber(regionNum);

            }
            
        }

    }


    public static void main(String[] args) {
        try {
            readFile("input.txt");

        } catch (FileNotFoundException ex) {
            System.out.println("File not found. Please check file name and try again");
            System.out.println(ex.toString());
            return;
        }
        
        
        sortMarkersByChromosome();
        
        characterizeMarkers(
                0.00001,
                0.0001,
               500000
        
        );




        characterizeRegions();

        sortRegionsByChromosome();
        
       
        
        addRegionNumber();
        
        
        try {
            writeFile();
        } catch (FileNotFoundException ex) {
            System.out.println("File not found. Please check file name and try again");
            System.out.println(ex.toString());
            return;
        }


    }

}
