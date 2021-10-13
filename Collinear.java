package com.company;

import java.awt.geom.Point2D;
import java.util.*;
import java.util.stream.Collectors;

/*
* This method is used to test and compare the runtime of different methods of finding a triplet of
* collinear points.
*
* */
public class Collinear {

    private static final int MIN = -10000;
    private static final int MAX = 10000;
    private static final int NUM_POINTS = 1000;

    /*
    * This is a helper method that will determine if three points are collinear or not by determing if the
    * slopes and intercepts are the same.
    * */
    public boolean isCollinear(Point2D a, Point2D b, Point2D c) {
        if ((a.getX() == b.getX() && b.getX() == c.getX()) || (a.getY() == b.getY() && a.getY() == c.getY())) {
            return true;
        }
        double slopeAB = (b.getY() - a.getY())/ (b.getX() - a.getX()) + 0.0;
        double interceptAB = b.getY() - slopeAB * b.getX() + 0.0;
        double slopeBC = (c.getY() - b.getY())/ (c.getX() - b.getX()) + 0.0;
        double interceptBC = c.getY() - slopeBC * c.getX() + 0.0;
        return Double.compare(slopeAB, slopeBC) == 0 && Double.compare(interceptAB, interceptBC) == 0;
    }


    /*
    * This method will iterate through each pair of points to get all three triplets possible. Then it will pass each
    * point into a helper method that will check if it is collinear.
    * */
    public void BruteForce(List<Point2D> coords) {
        for (int x = 0; x < coords.size(); x++) {
            for (int y = 0; y < coords.size(); y++) {
                for (int z = 0; z < coords.size(); z++) {
                    if (! coords.get(x).equals(coords.get(y)) && !coords.get(z).equals(coords.get(y)) && !coords.get(x).equals(coords.get(z))) {
                        if (isCollinear(coords.get(x), coords.get(y), coords.get(z))) {
                            System.out.println("YES Collinear");
                            System.out.println(coords.get(x) + " and " + coords.get(y) + " and " + coords.get(z));
                            return;
                        }
                    }
                }
            }
        }
        System.out.println("No Collinear");
    }

    /*
    * This method is essentially the brute force method but will use a shortcut of avoiding repeats since the order of
    * the triplets doesn't matter.
     */
    public void LessBruteForce(List<Point2D> coords) {
        for (int x = 0; x < coords.size(); x++) {
            for (int y = x + 1; y < coords.size(); y++) {
                for (int z = x + 2; z < coords.size(); z++) {
                    if (! coords.get(x).equals(coords.get(y)) && !coords.get(z).equals(coords.get(y)) && !coords.get(x).equals(coords.get(z))) {
                        if (isCollinear(coords.get(x), coords.get(y), coords.get(z))) {
                            System.out.println("YES Collinear");
                            System.out.println(coords.get(x) + " and " + coords.get(y) + " and " + coords.get(z));
                            return;
                        }
                    }
                }
            }
        }
        System.out.println("No Collinear");
    }

    /*
    * This method take all pairs of points and calculates the slope and intercept between it. Afterwards, it will
    * sort by slope, intercept. Then by iterating through and see if there are three same slopes and intercepts.
    * */
    public void solveUsingSorting(List<Point2D> coords) {
        List<List<Double>> allSlopeIntercepts = new ArrayList<>();
        for (int i = 0; i < coords.size(); i++) {
            for (int j = i + 1; j < coords.size(); j++) {
                double slopeIJ = (coords.get(i).getY() - coords.get(j).getY())/ (coords.get(i).getX() - coords.get(j).getX());
                double interceptIJ = 0;
                if (Double.POSITIVE_INFINITY == slopeIJ || Double.NEGATIVE_INFINITY == slopeIJ) {
                    interceptIJ = coords.get(i).getX();
                } else {
                    interceptIJ = coords.get(i).getY() - slopeIJ * coords.get(i).getX();
                }
                List<Double> slopeIntercept = new ArrayList<>();
                slopeIntercept.add(slopeIJ + 0.0);
                slopeIntercept.add(interceptIJ + 0.0);
                allSlopeIntercepts.add(slopeIntercept);
            }
        }
        //recursive sort?? quicksort
        allSlopeIntercepts = allSlopeIntercepts.stream().sorted((o1, o2) -> {
            if (o1.get(0) > o2.get(0) ||
                    (o1.get(0).equals(o2.get(0)) && o1.get(1) > o2.get(1))) {
                return -1;
            } else if (o1.get(0) < o2.get(0) ||
                    (o1.get(0).equals(o2.get(0)) && o1.get(1) < o2.get(1))) {
                return 1;
            }
            return 0;
        }).collect(Collectors.toList());
        Double prev_slope = Double.POSITIVE_INFINITY;
        Double prev_intercept = Double.POSITIVE_INFINITY;
        int counter = 0;
        for (int x = 0; x < allSlopeIntercepts.size(); x++) {
            boolean sameSlopeAndIntercept = Double.compare(allSlopeIntercepts.get(x).get(0), prev_slope) == 0 && Double.compare(allSlopeIntercepts.get(x).get(1), prev_intercept) == 0;
            if (sameSlopeAndIntercept) {
                counter ++;
                if (counter == 2){
                    System.out.println("YES Collinear");
                    System.out.println("Slopes are " + allSlopeIntercepts.get(x).get(0) + " " + prev_slope);
                    System.out.println("Intercepts are " + allSlopeIntercepts.get(x).get(1) + " " + prev_intercept);
                    return;
                }
            } else{
                counter = 0;
                prev_slope = allSlopeIntercepts.get(x).get(0);
                prev_intercept = allSlopeIntercepts.get(x).get(1);
            }
        }
        System.out.println("No Collinear");
    }


    /*
    * This method will calculate the slope and intercept for each pair and insert into a binary tree. If there is a
    * duplicate while inserting into a tree, then we will output that there is collinearity.
    * */
    public void usingBinarySearchTree(List<Point2D> coords) {
        BinarySearchTree bst = new BinarySearchTree();
        for (int i = 0; i < coords.size(); i++) {
            for (int j = i + 1; j < coords.size(); j++) {
                double slopeIJ = (coords.get(i).getY() - coords.get(j).getY())/ (coords.get(i).getX() - coords.get(j).getX());
                double interceptIJ = 0;
                if (Double.POSITIVE_INFINITY == slopeIJ || Double.NEGATIVE_INFINITY == slopeIJ) {
                    interceptIJ = coords.get(i).getX();
                } else {
                    interceptIJ = coords.get(i).getY() - slopeIJ * coords.get(i).getX();
                }
                if(bst.insert(slopeIJ + 0.0, interceptIJ + 0.0)){
                    return;
                }
            }
        }
        System.out.println("No Collinear");
    }

    /*
    * This method will calculate the slope and intercept for each pair and insert into a hashset. If there is a
     * duplicate while inserting into a set, then we will output that there is collinearity.
     *
     * Due to hashing, this method works better for more uniform distribution
     * */
    //Non-uniform distribution
    public void usingHashing(List<Point2D> coords) {
        Set<List<Double>> setSP = new HashSet<>();
        for (int i = 0; i < coords.size(); i++) {
            for (int j = i + 1; j < coords.size(); j++) {
                double slopeIJ = (coords.get(i).getY() - coords.get(j).getY())/ (coords.get(i).getX() - coords.get(j).getX());
                double interceptIJ = 0;
                if (Double.POSITIVE_INFINITY == slopeIJ || Double.NEGATIVE_INFINITY == slopeIJ) {
                    interceptIJ = coords.get(i).getX();
                } else {
                    interceptIJ = coords.get(i).getY() - slopeIJ * coords.get(i).getX();
                }
                List<Double> curSP = Arrays.asList(slopeIJ, interceptIJ);
                if (setSP.contains(curSP)) {
                    System.out.println("YES Collinear");
                    return;
                }
                setSP.add(curSP);
            }
        }
        System.out.println("No Collinear");
    }

    /*
    * Instead of calcualting all possible slopes/intecepts for every combination of points, this method will iterate
    * through a single point and every other point, calculate all that slope/intercept and sort on it. Instead of sorting an
    * aggregate list (n^2), this will sort the n points list.
    * */
    public void usingDifferentSorting(List<Point2D> coords) {
        for (int i = 0; i < coords.size(); i++) {
            List<List<Double>> curSlopeIntercepts = new ArrayList<>();
            for (int j = i + 1; j < coords.size(); j++) {
                double slopeIJ = (coords.get(i).getY() - coords.get(j).getY())/ (coords.get(i).getX() - coords.get(j).getX());
                double interceptIJ = 0;
                if (Double.POSITIVE_INFINITY == slopeIJ || Double.NEGATIVE_INFINITY == slopeIJ) {
                    interceptIJ = coords.get(i).getX();
                } else {
                    interceptIJ = coords.get(i).getY() - slopeIJ * coords.get(i).getX();
                }
                List<Double> slopeIntercept = new ArrayList<>();
                slopeIntercept.add(slopeIJ + 0.0);
                slopeIntercept.add(interceptIJ + 0.0);
                curSlopeIntercepts.add(slopeIntercept);
            }
            curSlopeIntercepts = curSlopeIntercepts.stream().sorted((o1, o2) -> {
                if (o1.get(0) > o2.get(0) ||
                        (o1.get(0).equals(o2.get(0)) && o1.get(1) > o2.get(1))) {
                    return -1;
                } else if (o1.get(0) < o2.get(0) ||
                        (o1.get(0).equals(o2.get(0)) && o1.get(1) < o2.get(1))) {
                    return 1;
                }
                return 0;
            }).collect(Collectors.toList());
            Double prev_slope = Double.POSITIVE_INFINITY;
            Double prev_intercept = Double.POSITIVE_INFINITY;
            for (int x = 0; x < curSlopeIntercepts.size(); x++) {
                if (Double.compare(curSlopeIntercepts.get(x).get(0), prev_slope) == 0 && Double.compare(curSlopeIntercepts.get(x).get(1), prev_intercept) == 0) {
                    System.out.println("YES Collinear");
                    System.out.println("Slopes are " + curSlopeIntercepts.get(x).get(0) + " " + prev_slope);
                    System.out.println("Intercepts are " + curSlopeIntercepts.get(x).get(1) + " " + prev_intercept);
                    return;
                } else {
                    prev_slope = curSlopeIntercepts.get(x).get(0);
                    prev_intercept = curSlopeIntercepts.get(x).get(1);
                }
            }
        }
        System.out.println("No Collinear");
    }

    public static void main(String[] args) {
        List<Point2D> coords = new ArrayList<>();

        for (int x = 0; x< NUM_POINTS; x++){
            Point2D curPoint = new Point2D.Double(Math.floor(Math.random() * (MAX - MIN + 1) + MIN), Math.floor(Math.random() * (MAX - MIN + 1) + MIN));
            coords.add(curPoint);
        }

        Collinear collinear = new Collinear();

        long startTime = System.currentTimeMillis();
        collinear.BruteForce(coords);
        long duration = System.currentTimeMillis() - startTime;
        System.out.println("It took brute force " + duration + " ms");

        startTime = System.currentTimeMillis();
        collinear.LessBruteForce(coords);
        duration = System.currentTimeMillis() - startTime;
        System.out.println("It took less brute force " + duration + " ms");

        startTime = System.currentTimeMillis();
        collinear.solveUsingSorting(coords);
        duration = System.currentTimeMillis() - startTime;
        System.out.println("It took sorting approach " + duration + " ms");

        startTime = System.currentTimeMillis();
        collinear.usingBinarySearchTree(coords);
        duration = System.currentTimeMillis() - startTime;
        System.out.println("It took Binary Tree approach " +duration + " ms");

        startTime = System.currentTimeMillis();
        collinear.usingHashing(coords);
        duration = System.currentTimeMillis() - startTime;
        System.out.println("It took Hashing approach " +duration + " ms");

        startTime = System.currentTimeMillis();
        collinear.usingDifferentSorting(coords);
        duration = System.currentTimeMillis() - startTime;
        System.out.println("It took alternate sorting approach " +duration + " ms");
    }
}
