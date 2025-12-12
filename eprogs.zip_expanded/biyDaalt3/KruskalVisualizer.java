package biyDaalt3;

import dataStructures.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Scanner;

public class KruskalVisualizer extends JFrame {
    private AdjacencyWGraph graph;
    private WeightedEdge[] mstEdges;
    private Point[] nodePositions;
    private int numVertices;

    public KruskalVisualizer() {
        setTitle("Kruskal's Algorithm");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //create graph
        Scanner scanner = new Scanner(System.in);
        System.out.println("numVertices count: ");
        numVertices = Integer.parseInt(scanner.nextLine().trim());
        graph = new AdjacencyWGraph(numVertices);
        //add edges
        while(true) {
            int v1,v2,weight;
            try {
                System.out.println("v1: ");
                v1 = Integer.parseInt(scanner.nextLine().trim());
                System.out.println("v2: ");
                v2 = Integer.parseInt(scanner.nextLine().trim());
                System.out.println("weight: ");
                weight = Integer.parseInt(scanner.nextLine().trim());
                graph.putEdge(new WeightedEdge(v1, v2, weight));
            } catch(Exception e) {
                System.out.println("found error, ");
                break;
            }
        }
        scanner.close();

        //run Kruskal
        mstEdges = new WeightedEdge[numVertices - 1];
        boolean success = graph.kruskal(mstEdges);
        System.out.println("success: "+success);
        if (!success) {
            System.out.println("Graph is not connected");
        } else {
            System.out.println("Minimum Spanning Tree Edges:");
            int totalWeight = 0;
            for (WeightedEdge edge : mstEdges) {
                if (edge != null) {
                    System.out.println(edge);
                    totalWeight += edge.getWeight();
                }
            }
            System.out.println("minimum spanning tree weight: " + totalWeight);
        }

        //set node positions in a circle
        nodePositions = new Point[numVertices + 1];
        int centerX = 400;
        int centerY = 300;
        int radius = 150;
        for (int i = 1; i <= numVertices; i++) {
            double angle = 2 * Math.PI * (i - 1) / numVertices;
            int x = centerX + (int) (radius * Math.cos(angle));
            int y = centerY + (int) (radius * Math.sin(angle));
            nodePositions[i] = new Point(x, y);
        }

        add(new GraphPanel());
    }

    private class GraphPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            //draw all edges in gray
            g2d.setColor(Color.GRAY);
            g2d.setStroke(new BasicStroke(2));
            for (int i = 1; i <= numVertices; i++) {
                for (int j = i + 1; j <= numVertices; j++) {
                    if (graph.existsEdge(i, j)) {
                        drawEdge(g2d, i, j);
                    }
                }
            }
            //draw MST edges in red
            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(4));
            for (WeightedEdge edge : mstEdges) {
                if (edge != null) {
                    drawEdge(g2d, edge.getVertex1(), edge.getVertex2());
                }
            }
            //draw nodes
            g2d.setColor(Color.BLUE);
            for (int i = 1; i <= numVertices; i++) {
                Point p = nodePositions[i];
                g2d.fillOval(p.x - 15, p.y - 15, 30, 30);
                g2d.setColor(Color.WHITE);
                g2d.drawString(String.valueOf(i), p.x - 5, p.y + 5);
                g2d.setColor(Color.BLUE);
            }
        }
        private void drawEdge(Graphics2D g2d, int v1, int v2) {
            Point p1 = nodePositions[v1];
            Point p2 = nodePositions[v2];
            g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new KruskalVisualizer().setVisible(true);
        });
    }
}