public class Header extends Cell {

    public int name;
    public int size;

    public Header(int name) {
        super(null);
        size = 0;
        this.name = name;
        C = this;
    }

}
