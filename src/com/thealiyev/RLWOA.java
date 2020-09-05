package com.thealiyev;

import java.util.ArrayList;
import java.util.Random;

public class RLWOA {
    private static Random random = null;

    public static void main(String[] args) {
        RLWOA rlwoa = new RLWOA();
        rlwoa.RLWOA();
    }

    private void RLWOA() {
        System.out.println("Reinforcement Learning based Whale Optimization Starts...");
        random = new Random();
        System.out.println("Reinforcement Learning Initialization Starts...");
        ArrayList<ArrayList<Double>> QTable = new ArrayList<>();
        ArrayList<Double> vector = new ArrayList<>();

        vector.add(0.0);
        vector.add(0.0);
        QTable.add(vector);
        vector = new ArrayList<>();

        vector.add(0.0);
        vector.add(0.0);
        QTable.add(vector);

        double QValue, MaxQValue, reward;
        double alpha = 0.9, gamma = 0.8;
        System.out.println("Whale Optimization Initialization Starts...");
        double a1, a2, r1, r2, A, C;
        boolean p;
        int randIndividual;
        double Xalpha, Dalpha, Xrand, Drand;
        double D;
        double b = 1, l;
        double X;
        int population = 30, dimension = 100;
        double min = -100.0, max = 100.0;
        int iteration = 500;
        double sigma1 = 0.6;

        ArrayList<ArrayList<Double>> optimizationMatrix = createOptimizationMatrix(population, dimension, min, max);
        ArrayList<Double> fitnessValues = findFitnessValues(optimizationMatrix);
        ArrayList<Double> sortedFitnessValues = sortFitnessValue(fitnessValues);

        System.out.println("Alpha's Fitness Value at the Initialization: " + sortedFitnessValues.get(0));
        System.out.println("Alpha's Values at the Initialization: " + optimizationMatrix.get(fitnessValues.indexOf(sortedFitnessValues.get(0))));

        System.out.println("Reinforcement Learning Whale Optimization Iteration Starts...");
        for (int stCounter = 0; stCounter < iteration; stCounter = stCounter + 1) {
            a1 = 2.0 - 2.0 * stCounter / iteration;
            for (int ndCounter = 0; ndCounter < optimizationMatrix.size(); ndCounter = ndCounter + 1) {
                for (int rdCounter = 0; rdCounter < optimizationMatrix.get(ndCounter).size(); rdCounter = rdCounter + 1) {
                    X = optimizationMatrix.get(ndCounter).get(rdCounter);
                    if (X < min) {
                        X = min;
                    } else if (X > max) {
                        X = max;
                    }

                    r1 = random.nextDouble();
                    A = 2 * a1 * r1 - a1;
                    r2 = random.nextDouble();
                    C = 2 * r2;

                    p = random.nextBoolean();
                    if (p) {
                        if (Math.abs(A) < 1) {
                            Xalpha = optimizationMatrix.get(fitnessValues.indexOf(sortedFitnessValues.get(0))).get(rdCounter);
                            Dalpha = C * Xalpha - X;
                            if (Dalpha < 0) {
                                Dalpha = Dalpha * -1;
                            }

                            //State = exploitation, work on 2nd row of Q Table
                            if (QTable.get(1).get(0) > QTable.get(1).get(1)) {
                                //Action = exploration
                                QValue = QTable.get(1).get(0);
                                X = Xalpha - A * Dalpha;
                                if (X < optimizationMatrix.get(ndCounter).get(rdCounter)) {
                                    reward = 1.0;
                                } else {
                                    reward = -1.0;
                                }
                                //Finds Q max
                                MaxQValue = QTable.get(0).get(0);
                                if (QTable.get(0).get(1) > MaxQValue) {
                                    MaxQValue = QTable.get(0).get(1);
                                }
                                //Calculate Q and update Q Table
                                QValue = QValue + alpha * (reward + gamma * MaxQValue - QValue);
                                QTable.get(1).set(0, QValue);
                            } else {
                                //Action = exploitation
                                QValue = QTable.get(1).get(1);
                                X = sigma1 * Xalpha - A * Dalpha;
                                if (X < optimizationMatrix.get(ndCounter).get(rdCounter)) {
                                    reward = 1.0;
                                } else {
                                    reward = -1.0;
                                }
                                //Finds Q max
                                MaxQValue = QTable.get(1).get(0);
                                if (QTable.get(1).get(1) > MaxQValue) {
                                    MaxQValue = QTable.get(1).get(1);
                                }
                                //Calculate Q and update Q Table
                                QValue = QValue + alpha * (reward + gamma * MaxQValue - QValue);
                                QTable.get(1).set(1, QValue);
                            }
                        } else if (Math.abs(A) >= 1) {
                            randIndividual = random.nextInt(sortedFitnessValues.size());
                            Xrand = optimizationMatrix.get(randIndividual).get(rdCounter);
                            Drand = C * Xrand - X;
                            if (Drand < 0) {
                                Drand = Drand * -1;
                            }

                            //State = exploration, work on 1st row of Q Table
                            if (QTable.get(0).get(0) > QTable.get(0).get(1)) {
                                //Action = exploration
                                QValue = QTable.get(0).get(0);
                                X = Xrand - A * Drand;

                                if (X < optimizationMatrix.get(ndCounter).get(rdCounter)) {
                                    reward = 1.0;
                                } else {
                                    reward = -1.0;
                                }
                                //Finds Q max
                                MaxQValue = QTable.get(0).get(0);
                                if (QTable.get(0).get(1) > MaxQValue) {
                                    MaxQValue = QTable.get(0).get(1);
                                }
                                //Calculate Q and update Q Table
                                QValue = QValue + alpha * (reward + gamma * MaxQValue - QValue);
                                QTable.get(0).set(0, QValue);
                            } else {
                                //Action = exploitation
                                QValue = QTable.get(0).get(0);
                                X = sigma1 * Xrand - A * Drand;

                                if (X < optimizationMatrix.get(ndCounter).get(rdCounter)) {
                                    reward = 1.0;
                                } else {
                                    reward = -1.0;
                                }
                                //Finds Q max
                                MaxQValue = QTable.get(0).get(0);
                                if (QTable.get(0).get(1) > MaxQValue) {
                                    MaxQValue = QTable.get(0).get(1);
                                }
                                //Calculate Q and update Q Table
                                QValue = QValue + alpha * (reward + gamma * MaxQValue - QValue);
                                QTable.get(0).set(0, QValue);
                            }
                        }
                    } else {
                        Xalpha = optimizationMatrix.get(fitnessValues.indexOf(sortedFitnessValues.get(0))).get(rdCounter);
                        D = Xalpha - X;
                        if (D < 0) {
                            D = D * -1;
                        }
                        a2 = -1.0 + stCounter * ((-1.0) / iteration);
                        l = (a2 - 1.0) * random.nextDouble() + 1.0;

                        if (A > 1) {
                            //State = exploration, work on 1st row of Q Table
                            if (QTable.get(0).get(0) > QTable.get(0).get(1)) {
                                //Action = exploration
                                QValue = QTable.get(0).get(0);
                                X = D * Math.pow(Math.E, (b * l)) * Math.cos(2 * Math.PI * l) + Xalpha;
                                if (X < optimizationMatrix.get(ndCounter).get(rdCounter)) {
                                    reward = 1.0;
                                } else {
                                    reward = -1.0;
                                }
                                //Finds Q max
                                MaxQValue = QTable.get(0).get(0);
                                if (QTable.get(0).get(1) > MaxQValue) {
                                    MaxQValue = QTable.get(0).get(1);
                                }
                                //Calculate Q and update Q Table
                                QValue = QValue + alpha * (reward + gamma * MaxQValue - QValue);
                                QTable.get(0).set(0, QValue);
                            } else {
                                //Action = exploitation
                                QValue = QTable.get(0).get(1);
                                X = sigma1 * D * Math.pow(Math.E, (b * l)) * Math.cos(2 * Math.PI * l) + Xalpha;
                                if (X < optimizationMatrix.get(ndCounter).get(rdCounter)) {
                                    reward = 1.0;
                                } else {
                                    reward = -1.0;
                                }
                                //Finds Q max
                                MaxQValue = QTable.get(1).get(0);
                                if (QTable.get(1).get(1) > MaxQValue) {
                                    MaxQValue = QTable.get(1).get(1);
                                }
                                //Calculate Q and update Q Table
                                QValue = QValue + alpha * (reward + gamma * MaxQValue - QValue);
                                QTable.get(0).set(1, QValue);
                            }
                        } else {
                            //State = exploitation, work on 2nd row of Q Table
                            if (QTable.get(1).get(0) > QTable.get(1).get(1)) {
                                //Action = exploration
                                QValue = QTable.get(1).get(0);
                                X = D * Math.pow(Math.E, (b * l)) * Math.cos(2 * Math.PI * l) + Xalpha;
                                if (X < optimizationMatrix.get(ndCounter).get(rdCounter)) {
                                    reward = 1.0;
                                } else {
                                    reward = -1.0;
                                }
                                //Finds Q max
                                MaxQValue = QTable.get(0).get(0);
                                if (QTable.get(0).get(1) > MaxQValue) {
                                    MaxQValue = QTable.get(0).get(1);
                                }
                                //Calculate Q and update Q Table
                                QValue = QValue + alpha * (reward + gamma * MaxQValue - QValue);
                                QTable.get(1).set(0, QValue);
                            } else {
                                //Action = exploitation
                                QValue = QTable.get(1).get(1);
                                X = sigma1 * D * Math.pow(Math.E, (b * l)) * Math.cos(2 * Math.PI * l) + Xalpha;
                                if (X < optimizationMatrix.get(ndCounter).get(rdCounter)) {
                                    reward = 1.0;
                                } else {
                                    reward = -1.0;
                                }
                                //Finds Q max
                                MaxQValue = QTable.get(1).get(0);
                                if (QTable.get(1).get(1) > MaxQValue) {
                                    MaxQValue = QTable.get(1).get(1);
                                }
                                //Calculate Q and update Q Table
                                QValue = QValue + alpha * (reward + gamma * MaxQValue - QValue);
                                QTable.get(1).set(1, QValue);
                            }
                        }
                    }

                    optimizationMatrix.get(ndCounter).set(rdCounter, X);
                }
            }
            fitnessValues = findFitnessValues(optimizationMatrix);
            sortedFitnessValues = sortFitnessValue(fitnessValues);
            //if you want to print iteration number, Alpha's Fitness Value and Alpha's Values out, please remove comment tags of code below:
            /*System.out.println("Alpha's Fitness Value at the " + (stCounter + 1) + "th iteration: " + trio.get(0) + " ");
            System.out.println("Alpha's Values: at the " + (stCounter + 1) + "th iteration: " + optimizationMatrix.get(fitnessValues.indexOf(trio.get(0))));*/
            //System.out.println("Alpha's Fitness Value at the End: " + trio.get(0));
            System.out.println(sortedFitnessValues.get(0));
        }

        System.out.println("Reinforcement Learning Whale Optimization Iteration Ends...");
        //System.out.println("Alpha's Values at the End: " + optimizationMatrix.get(fitnessValues.indexOf(trio.get(0))));
    }

    private ArrayList<ArrayList<Double>> createOptimizationMatrix(int population, int dimension, double min,
                                                                  double max) {
        random = new Random();
        double X;

        ArrayList<ArrayList<Double>> optimizationMatrix = new ArrayList<>();
        ArrayList<Double> optimizationVector = new ArrayList<>();

        for (int stCounter = 0; stCounter < population; stCounter = stCounter + 1) {
            for (int ndCounter = 0; ndCounter < dimension; ndCounter = ndCounter + 1) {
                X = min + (max - min) * random.nextDouble();
                optimizationVector.add(X);
            }
            optimizationMatrix.add(optimizationVector);
            optimizationVector = new ArrayList<>();
        }

        return optimizationMatrix;
    }

    private ArrayList<Double> findFitnessValues(ArrayList<ArrayList<Double>> optimizationMatrix) {
        ArrayList<Double> fitnessValues = new ArrayList<>();

        double sphere = 0.0;
        for (int stCounter = 0; stCounter < optimizationMatrix.size(); stCounter = stCounter + 1) {
            for (int ndCounter = 0; ndCounter < optimizationMatrix.get(stCounter).size(); ndCounter = ndCounter + 1) {
                sphere = sphere + optimizationMatrix.get(stCounter).get(ndCounter) * optimizationMatrix.get(stCounter).get(ndCounter);
            }
            fitnessValues.add(sphere);
            sphere = 0.0;
        }

        return fitnessValues;
    }

    private ArrayList<Double> sortFitnessValue(ArrayList<Double> fitnessValues) {
        ArrayList<Double> duplicatedFitnessValues = new ArrayList<>(fitnessValues);
        ArrayList<Double> trio = new ArrayList<>();

        double min;
        for (int stCounter = 0; stCounter < fitnessValues.size(); stCounter = stCounter + 1) {
            min = duplicatedFitnessValues.get(0);
            for (int ndCounter = 0; ndCounter < duplicatedFitnessValues.size(); ndCounter = ndCounter + 1) {
                if (duplicatedFitnessValues.get(ndCounter) < min) {
                    min = duplicatedFitnessValues.get(ndCounter);
                }
            }
            trio.add(min);
            duplicatedFitnessValues.remove(new Double(min));
        }

        return trio;
    }
}