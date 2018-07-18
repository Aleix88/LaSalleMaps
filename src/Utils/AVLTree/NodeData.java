package Utils.AVLTree;

public class NodeData<E> {

    private String order;
    private E data;


    public NodeData(String order, E data) {
        this.order = order;
        this.data = data;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public E getData() {
        return data;
    }

    public void setData(E data) {
        this.data = data;
    }
}

