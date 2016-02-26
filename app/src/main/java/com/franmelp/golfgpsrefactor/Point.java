package com.franmelp.golfgpsrefactor;

/**
 * Created by francescomelpignano on 26/02/16.
 */
public class Point
{
    public Point(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public double x;
    public double y;

    @Override
    public String toString()
    {
        return String.format("(%.2f,%.2f)", x, y);
    }
}
