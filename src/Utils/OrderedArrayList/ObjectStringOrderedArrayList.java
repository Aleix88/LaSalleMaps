package Utils.OrderedArrayList;

public class ObjectStringOrderedArrayList {

    private Object object;
    private String order;

    public ObjectStringOrderedArrayList() {
    }

    public ObjectStringOrderedArrayList(Object object, String order) {
        this.object = object;
        this.order = order;
    }

    public Object getObject() {
        return object;
    }

    public String getOrder() {
        return order;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
