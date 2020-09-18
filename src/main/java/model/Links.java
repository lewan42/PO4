package model;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import java.util.ArrayList;
import static model.Utils.links;

public class Links {
    private Circle start;
    private Circle end;
    private Line link;
    private Table ts;
    private Table te;
    private double hs;
    private double he;
    private Attr as;
    private Attr ae;


    public Links(Circle start, Circle end, Table ts, Table te, double hs, double he, Attr as, Attr ae) {
        this.as = as;
        this.ae = ae;
        this.ts = ts;
        this.te = te;
        this.start = start;
        this.end = end;
        link = new Line(start.getCenterX(), start.getCenterY(), end.getCenterX(), end.getCenterY());
        this.hs = hs;
        this.he = he;


        link.setStrokeWidth(3);
    }



    public void setStart(Circle start) {
        this.start = start;
    }

    public void setEnd(Circle end) {
        this.end = end;
    }

    public void setLink(Line link) {
        this.link = link;
    }

    public Circle getStart() {
        return start;
    }

    public Circle getEnd() {
        return end;
    }

    public Line getLink() {
        return link;
    }

    public Table getTs() {
        return ts;
    }

    public Table getTe() {
        return te;
    }

    public static ArrayList<Links> getLinks() {
        return links;
    }

    public double getHs() {
        return hs;
    }

    public double getHe() {
        return he;
    }

    public Attr getAs() {
        return as;
    }

    public Attr getAe() {
        return ae;
    }

    public void setHs(double hs) {
        this.hs = hs;
    }

    public void setHe(double he) {
        this.he = he;
    }

    @Override
    public String toString() {
        return "Links{" +
                "start=" + start +
                ", end=" + end +
                ", link=" + link +
                '}';
    }
}
