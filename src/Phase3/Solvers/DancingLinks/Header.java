package Phase3.Solvers.DancingLinks;
/**
 * A specialized class representing a header cell in the Dancing Links matrix for the DX algorithm.
 */
public class Header extends Cell {

    public int name;
    public int size;

    /**
     * Constructs a new header cell with the specified name.
     *
     * @param name The name or identifier of the header cell.
     */
    public Header(int name) {
        super(null);
        size = 0;
        this.name = name;
        C = this;
    }

}
