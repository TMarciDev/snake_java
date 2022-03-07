package hu.elte.fi.progtech.snake.model;

public class GridType {

    private SegmentType segment;

    GridType() {
        segment = SegmentType.EMPTY;
    }

    public SegmentType getSegment() {
        return segment;
    }

    public void setType(SegmentType segment) {
        this.segment = segment;
    }

}
