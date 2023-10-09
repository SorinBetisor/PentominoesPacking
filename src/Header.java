public class Header extends Cell {

    public String name;
    public int size;

    public Header(String name) {
        super(null);
        size = 0;
        this.name = name;
        C = this;
    }

}
