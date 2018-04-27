package ticketing;

public class Seat {
    private Integer floor;
    private Integer row;
    private Integer seat;

    public Seat(Integer floor, Integer row, Integer seat) {
        this.floor = floor;
        this.row = row;
        this.seat = seat;
    }

    public Integer getFloor() {
        return floor;
    }

    public Integer getRow() {
        return row;
    }

    public Integer getSeat() {
        return seat;
    }
}
