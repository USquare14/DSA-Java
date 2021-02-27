
import java.util.Arrays;
import java.util.Comparator;
import java.util.TreeSet;

/**
 * @author umangupadhyay
 */

public class ClosestPair {
    
    static class Point{
        
        double x;
        double y;
        
        public Point(double x, double y){
            this.x = x;
            this.y = y;
        }
        
        public double distance(Point p){
//            return Math.sqrt((this.x - p.x) * (this.x - p.x) + (this.y - p.y) * (this.y - p.y));
            return (this.x - p.x) * (this.x - p.x) + (this.y - p.y) * (this.y - p.y);
        }
        
        public boolean equals(Object o)
        {
            Point p = (Point)o;
            return p.x == x && p.y==y;
        }
        
        @Override
        public String toString() {
            return x + " " + y;
        }
        
    }
    
    static class VerticalComparator implements Comparator<Point>{
        @Override
        public int compare(Point p1, Point p2) {
            if(p1.y == p2.y) return Double.compare(p1.x, p2.x);
            return Double.compare(p1.y, p2.y);
        }
    }

    static class HorizontalComparator implements Comparator<Point>{
        @Override
        public int compare(Point p1, Point p2) {
            if(p1.x == p2.x) return Double.compare(p1.y, p2.y);
            return Double.compare(p1.x, p2.x);
        }
    }
    
    static double closestPair(Point[] points){
        
        Point[] closestPair = new Point[2];
        double crtMinDist = Double.POSITIVE_INFINITY;
        
        Point[] sorted = Arrays.copyOf(points, points.length);
        Arrays.sort(sorted, new HorizontalComparator());
        int leftMostCandidateIndex = 0;
        
        TreeSet<Point> candidates = new TreeSet<Point>(new VerticalComparator());
        
        for(Point curr : sorted){
            while(curr.x - sorted[leftMostCandidateIndex].x > crtMinDist) {
                candidates.remove(sorted[leftMostCandidateIndex]);
                leftMostCandidateIndex++;
            }
            Point head = new Point(curr.x , curr.y - crtMinDist);
            Point tail = new Point(curr.x , curr.y + crtMinDist);
            
            for(Point point : candidates.subSet(head, tail)){
                double dist = curr.distance(point);
                if(dist < crtMinDist){
                    crtMinDist = dist;
                    closestPair[0] = curr;
                    closestPair[1] = point;
                }
            }
            
            candidates.add(curr);
        }
        return closestPair[0].distance(closestPair[1]);
    }
    
}
