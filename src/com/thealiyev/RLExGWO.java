package com.thealiyev;

import java.util.ArrayList;
import java.util.Random;

public class RLExGWO {
    private static Random random = null;

    public static void main(String[] args) {
        RLExGWO rlExGWO = new RLExGWO();
        rlExGWO.RLExGWO();
    }

    private void RLExGWO() {
        System.out.println("Reinforcement Learning based Expanded Gray Wolf Optimization Starts...");
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
        ArrayList<Double> sigmas;
        double sum, averageA;
        System.out.println("Expanded Gray Wolf Initialization Starts...");
        double a;
        double r1, r2;
        ArrayList<Double> A;
        ArrayList<Double> C;
        ArrayList<Double> D;
        ArrayList<Double> X;
        double x;
        int population = 30, dimension = 30;
        double min = -100.0, max = 100.0;
        int iteration = 500;

        ArrayList<ArrayList<Double>> optimizationMatrix = createOptimizationMatrix(population, dimension, min, max);
        ArrayList<Double> fitnessValues = findFitnessValues(optimizationMatrix);
        ArrayList<Double> sortedFitnessValues = sortFitnessValues(fitnessValues);

        System.out.println("Alpha's Fitness Value at the Initialization: " + sortedFitnessValues.get(0));
        System.out.println("Alpha's Values at the Initialization: " + optimizationMatrix.get(fitnessValues.indexOf(sortedFitnessValues.get(0))));

        System.out.println("Expanded Gray Wolf Iteration Starts...");
        for (int stCounter = 0; stCounter < iteration; stCounter = stCounter + 1) {
            a = 2.0 - 2.0 * stCounter / iteration;
            for (int ndCounter = 0; ndCounter < optimizationMatrix.size(); ndCounter = ndCounter + 1) {
                for (int rdCounter = 0; rdCounter < optimizationMatrix.get(ndCounter).size(); rdCounter = rdCounter + 1) {
                    A = new ArrayList<>();
                    C = new ArrayList<>();
                    D = new ArrayList<>();
                    X = new ArrayList<>();
                    sigmas = new ArrayList<>();

                    x = optimizationMatrix.get(ndCounter).get(rdCounter);
                    if (x < min) {
                        x = min;
                    } else if (x > max) {
                        x = max;
                    }

                    r1 = random.nextDouble();
                    r2 = random.nextDouble();
                    A.add(2 * a * r1 - a);
                    C.add(2 * r2);
                    D.add(C.get(0) * optimizationMatrix.get(fitnessValues.indexOf(sortedFitnessValues.get(0))).get(rdCounter) - x);
                    if (D.get(0) < 0) {
                        D.set(0, -1 * D.get(0));
                    }
                    X.add(optimizationMatrix.get(fitnessValues.indexOf(sortedFitnessValues.get(0))).get(rdCounter) - A.get(0) * D.get(0));

                    r1 = random.nextDouble();
                    r2 = random.nextDouble();
                    A.add(2 * a * r1 - a);
                    C.add(2 * r2);
                    D.add(C.get(1) * optimizationMatrix.get(fitnessValues.indexOf(sortedFitnessValues.get(1))).get(rdCounter) - x);
                    if (D.get(1) < 0) {
                        D.set(1, -1 * D.get(1));
                    }
                    X.add(optimizationMatrix.get(fitnessValues.indexOf(sortedFitnessValues.get(1))).get(rdCounter) - A.get(1) * D.get(1));

                    r1 = random.nextDouble();
                    r2 = random.nextDouble();
                    A.add(2 * a * r1 - a);
                    C.add(2 * r2);
                    D.add(C.get(2) * optimizationMatrix.get(fitnessValues.indexOf(sortedFitnessValues.get(2))).get(rdCounter) - x);
                    if (D.get(2) < 0) {
                        D.set(2, -1 * D.get(2));
                    }
                    X.add(optimizationMatrix.get(fitnessValues.indexOf(sortedFitnessValues.get(2))).get(rdCounter) - A.get(2) * D.get(2));

                    if (ndCounter > 2) {
                        for (int fourthCounter = 0; fourthCounter < ndCounter; fourthCounter = fourthCounter + 1) {
                            r1 = random.nextDouble();
                            r2 = random.nextDouble();
                            A.add(2 * a * r1 - a);
                            C.add(2 * r2);
                            D.add(C.get(fourthCounter) * optimizationMatrix.get(ndCounter - 1).get(rdCounter) - x);
                            if (D.get(fourthCounter) < 0) {
                                D.set(fourthCounter, -1 * D.get(fourthCounter));
                            }
                            X.add(optimizationMatrix.get(ndCounter - 1).get(rdCounter) - A.get(fourthCounter) * D.get(fourthCounter));
                        }
                    }

                    sum = 0;
                    for (double counter = 0; counter < ndCounter + 1; counter = counter + 1) {
                        sigmas.add(counter + 1);
                        sum = sum + (counter + 1);
                    }
                    for (int counter = 0; counter < sigmas.size(); counter = counter + 1) {
                        sigmas.set(counter, sigmas.get(counter) / sum);
                    }

                    averageA = 0;
                    for (int counter = 0; counter < A.size(); counter = counter + 1) {
                        averageA = averageA + A.get(counter);
                    }
                    averageA = averageA / A.size();

                    if (averageA > 1) {
                        //State = exploration, work on 1st row of Q Table
                        if (QTable.get(0).get(0) > QTable.get(0).get(1)) {
                            //Action = exploration
                            QValue = QTable.get(0).get(0);
                            x = 0;
                            for (int fifthCounter = 0; fifthCounter < X.size(); fifthCounter = fifthCounter + 1) {
                                x = x + X.get(fifthCounter);
                            }
                            x = x / X.size();
                            if (x < optimizationMatrix.get(ndCounter).get(rdCounter)) {
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
                            x = 0;
                            for (int fifthCounter = 0; fifthCounter < X.size(); fifthCounter = fifthCounter + 1) {
                                x = x + X.get(fifthCounter) * A.get(fifthCounter);
                            }
                            x = x / X.size();
                            if (x < optimizationMatrix.get(ndCounter).get(rdCounter)) {
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
                            x = 0;
                            for (int fifthCounter = 0; fifthCounter < X.size(); fifthCounter = fifthCounter + 1) {
                                x = x + X.get(fifthCounter);
                            }
                            x = x / X.size();
                            if (x < optimizationMatrix.get(ndCounter).get(rdCounter)) {
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
                            x = 0;
                            for (int fifthCounter = 0; fifthCounter < X.size(); fifthCounter = fifthCounter + 1) {
                                x = x + X.get(fifthCounter) * A.get(fifthCounter);
                            }
                            x = x / X.size();
                            if (x < optimizationMatrix.get(ndCounter).get(rdCounter)) {
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

                    optimizationMatrix.get(ndCounter).set(rdCounter, x);
                }
            }
            fitnessValues = findFitnessValues(optimizationMatrix);
            sortedFitnessValues = sortFitnessValues(fitnessValues);
            System.out.println(sortedFitnessValues.get(0));
        }

        System.out.println("Expanded Gray Wolf Iteration Ends...");
        System.out.println("Alpha's Values at the End: " + optimizationMatrix.get(fitnessValues.indexOf(sortedFitnessValues.get(0))));
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

    private ArrayList<Double> sortFitnessValues(ArrayList<Double> fitnessValues) {
        ArrayList<Double> duplicatedFitnessValues = new ArrayList<>(fitnessValues);
        ArrayList<Double> sortedFitnessValues = new ArrayList<>();

        double min;
        for (int stCounter = 0; stCounter < fitnessValues.size(); stCounter = stCounter + 1) {
            min = duplicatedFitnessValues.get(0);
            for (int ndCounter = 0; ndCounter < duplicatedFitnessValues.size(); ndCounter = ndCounter + 1) {
                if (duplicatedFitnessValues.get(ndCounter) < min) {
                    min = duplicatedFitnessValues.get(ndCounter);
                }
            }
            sortedFitnessValues.add(min);
            duplicatedFitnessValues.remove(new Double(min));
        }

        return sortedFitnessValues;
    }
}