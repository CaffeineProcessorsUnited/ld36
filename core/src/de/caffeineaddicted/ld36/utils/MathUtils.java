package de.caffeineaddicted.ld36.utils;

import com.badlogic.gdx.math.Vector2;
import de.caffeineaddicted.ld36.actors.Entity;

import java.util.Random;

public class MathUtils {
    public static float clamp(float val, float min, float max) {
        if (val < min)
            return min;
        else if (val > max)
            return max;
        return val;
    }

    public static boolean intersectRect(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        return !(
                x3 > x2
                        || x4 < x1
                        || y3 > y2
                        || y4 < y1
        );
    }

    /**
     * Check if point 1 is inside rect spanned by 2 & 3
     */
    public static boolean pointInRect(float x1, float y1, float x2, float y2, float x3, float y3) {
        return x1 > x2 && x1 < x3 && y1 > y2 && y1 < y3;
    }

    public static boolean intersectCircleRect(float x1, float y1, float r, float x2, float y2, float x3, float y3) {
        float closestX = clamp(x1, x2, x3);
        float closestY = clamp(y1, y2, y3);

        float distX = x1 - closestX;
        float distY = y1 - closestY;
        float dist = distX * distX + distY * distY;
        return dist <= (r * r);
    }

    public static double angleToPoint(float x1, float y1, float x2, float y2) {
        double angle = Math.toDegrees(Math.atan2(x2 - x1, y2 - y1));
        return (angle < 0) ? angle + 360 : angle;
    }

    public static float distanceP2P(Vector2 p1, Vector2 p2) {
        return distanceP2P(p1.x, p1.y, p2.x, p2.y);
    }

    public static float distanceP2P(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    public static float distanceP2P(Entity e1, Entity e2) {
        return MathUtils.distanceP2P(e1.getCenterPoint().x, e1.getCenterPoint().y, e2.getCenterPoint().x, e2.getCenterPoint().y);
    }

    public static int selectedSlice(float angle, int numSlices) {
        return (int) (angle / (360.f / numSlices));
    }

    public static int random(int min, int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }
}
