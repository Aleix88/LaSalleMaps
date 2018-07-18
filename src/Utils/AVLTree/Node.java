package Utils.AVLTree;



public class Node<E> {

    private NodeData<E> data;
    private int altura;
    private Node<E> leftchild;
    private Node<E> rightchild;




    public Node(NodeData<E> data) {
        this.data = data;
        this.altura = 1 ; //Altura de cada nou node introduit es sempre 1
        this.leftchild = null;
        this.rightchild = null;
    }


    public NodeData<E> getNodeData() {
        return data;
    }

    public void setData(NodeData<E> data) {
        this.data = data;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public Node<E> getLeftchild() {
        return leftchild;
    }

    public void setLeftchild(Node<E> leftchild) {
        this.leftchild = leftchild;
    }

    public Node<E> getRightchild() {
        return rightchild;
    }

    public void setRightchild(Node<E> rightchild) {
        this.rightchild = rightchild;
    }
}
