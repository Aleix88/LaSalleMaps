package Utils.AVLTree;

public class AVLTree<E> {

    private Node<E> root;

    public AVLTree() {
        root = null;
    }

    /*
     * Bàsicament es com un getter de la altura excepte que si li passes un null et retorna 0. No te massa sentit en aquest contexte, pero
     * es important perque s'utilitza en la funció recursiva de insertar, on es possible que un node no tingui fill esquerra/dret i per tant l'altura d'aquests
     * ultims sigui 0. (0 --> No hi ha node, 1 --> Node "fulla"/ "extrem"
     */
    private int altura(Node<E> node) {
        if (node == null)
            return 0;
        return node.getAltura(); //La idea es que a cada insertar vaig fent un update de les altures de cada node, de manera que el node sempre tindrà la correcte
    }

    /*Funció que serveix bàsicament per definir l'altura del node. Compara l'altura del fill esquerre i la del fill dret, retornant la mès gran
    * Seguidament se li suma 1 per definir la altura del node(Altura node es la altura mès gran de les dos branques +1)
    * Pot semblar raro que es fagi utilitzant les altures dels nodes de sota, pero la funció de insertar es recursiva, i les noves altures es reconstrueixen
    * de baix a dalt*/
    private int maxim(int altura1, int altura2) {
        if (altura1 > altura2) return altura1;
        return altura2;
    }

    /*Retorna el balanç del node. 0,1 estàn be, 2+ cal reconfigurar*/
    private int getBalanceFactor(Node<E> node) {
        if (node == null) //Aquesta comprovació està per la mateixa rao que la comprovació de altura
            return 0; //Balanceig de un node que no existeix es 0
        /*
         *Com que sempre es tindrà la altura actualitzada, es molt facil trobar el factor de balanç. Altura node esquerra - altura node dret
         */
        return altura(node.getLeftchild()) - altura(node.getRightchild());
    }

    /* Aquesta serà la funció que mostrarem de cara a l'exterior. Es simplement perque no calgui que l'usuari entengui com funciona la recursiva, perque sinó
    * hauria de cridarla sempre d'una manera.*/
    public void insertarNode(NodeData<E> ne) {
        root = insertar(root, ne);
    }

    /* Probablement hauria estat menys costós fer la funció iterativa, pero hem remarcat que el cost de inserir no ens importa, i la recursiva permet, si es planteja
    be, que la codificació sigui mès facil
    Mencionar que s'ha hagut de passar a lowercase, perque tot i que es igual quin tipus d'ordre sigui mentres la funció de comparació doni sempre
    el mateix resultat per un mateix input, debuggar l'abre seria molt dificil. D'aquesta manera es odre alfabetic
    El insertar l'he vist fet iteratiu, pero la vertitat esque es bastant confus.
    No miro si els valors son repetits. Bàsicament perque de tota la vida sempre es busca abans d'afegir.
     */
    private Node<E> insertar(Node<E> node, NodeData<E> nodeData) { //Funcio que crido recursivament.

        if (node == null) {
            return (new Node<E>(nodeData)); //Nomès es dona 1 cop amb el nou node
        }

        // NodeData <= alfabeticament que node b, entres
        if (nodeData.getOrder().compareTo(node.getNodeData().getOrder()) <= 0) { //<= es per si els strings fossin iguals, pero no hauria de ser
            node.setLeftchild(insertar(node.getLeftchild(), nodeData));
        } else {
            /* No posarem mai duplicats en aquesta estructura, perque abans de inserir es comprova que existeixi / es confia en que en els fitxers importats
            no hi ha repeticions
            NodeData > alfabeticament que node b
             */
            node.setRightchild(insertar(node.getRightchild(), nodeData));  //Si no es esquerra es dret
        }
        /* Aquí nomès vindrem quan hagim "obert" totes les crides recursives. Es lo que hem mencionat abans sobre l'altura. La reconstruim desde sota utilitzant
        * el fill esquerra i dret (Que poden no existir, per aixó if(null) return 0)*/
        node.setAltura(1 + maxim(altura(node.getLeftchild()), altura(node.getRightchild())));
        /* Al mateix temps que reconstruim l'altura cap amunt, també nem chechejant el balanç. Es com quan ho feiem a classe, desde el punt de insersió anavem pujant
         * i mirant on es generava el desbalanceig (de baix a dalt) */
        int factorEquil = getBalanceFactor(node);

         /* Al llarg que pujem l'arbre es poden donar 4 casos, R, LR, L, RL. Com sabem quin es cada un ? Mitjançant 2 coses. La primera es si el balanç es positiu
         o negatiu. Aixó pemetra dir si es L o R. Positiu R, negatiu L. Per tant, aixó separa en R LR i L RL. La segona cosa es dona comparant el node que has introduit
         amb el fill esquerra i el dret, de manera que saps el node que has introduit l'has posat a l'esquerra d'on hi ha el desbalanceig o a la dreta d'on hi ha
         el desbalanceig. Per exemple, imaginat que has aplicat el primer mètode i ara dubtes entre R i LR. LR serà si has introduit el node a la dreta del fill esquerra,
         i R si ho has fet a l'esquerra. Sabent aixó, apliques la rotació adequada, que sempre es la mateixa. La gracia de aixó es que si el desbalanceig es produeix molt
         amunt i la introduccio del nou node s'ha fet molt abaix, aquest mètode segueix funcionat per com esta format l'AVL
         */

         // (0 - 2 < -1 (leftchild(0) - rightchild(2))) + (nou node esquerra rightchild o dreta rightchild ? --> dreta rightchild)
        if (factorEquil < -1 && nodeData.getOrder().compareTo(node.getRightchild().getNodeData().getOrder()) > 0) {
            return R(node); //RR
            //El contrari del cas anterior
        }else if (factorEquil > 1 && nodeData.getOrder().compareTo(node.getLeftchild().getNodeData().getOrder()) <= 0) {
            return L(node); //LL
            //L perque factor equilibri > 1, R perque agafant el fill esquerra es dona que el node es mès gran que aquest
        }else if (factorEquil > 1 && nodeData.getOrder().compareTo(node.getLeftchild().getNodeData().getOrder()) > 0){
            node.setLeftchild(R(node.getLeftchild())); //Primer L, utilitzant el node esquerra d'on hi ha el desbalanceig. Despres L
            return L(node); //LR
            //R perque factor equilibri <-1, L perque agafant el fill dret, node introduit < node dret
        }else if (factorEquil < -1 && nodeData.getOrder().compareTo(node.getRightchild().getNodeData().getOrder()) <= 0) {
            node.setRightchild(L(node.getRightchild())); //Primer R, utilitzant el node dret d'on hi ha el desbalanceig. Despres R
            return R(node); //RL
        } //O un o l'altre

        return node; /*node.setLeftchild(insertar(node.getLeftchild(), nodeData)); ->Si no ha canviat, retorno el mateix node esquerra ( o dret )*/
    }


    private Node<E> R(Node<E> root) {
        Node<E> right = root.getRightchild();
        root.setRightchild(right.getLeftchild());
        right.setLeftchild(root);
        /* La antiga arrel ha caigut. No puc simplement sumar un o treure un, perque el desbalanceig no sempre sera 2-0 o similar, pot ser
        perfectament 3-1, 4-2, i aixó me la liaria.*/
        root.setAltura(maxim(altura(root.getLeftchild()), altura(root.getRightchild())) + 1);
        right.setAltura(maxim(altura(right.getLeftchild()), altura(right.getRightchild())) + 1); //La nova arrel ha pujat
        //El fill esquerra del que abans era el fill dret canvia de banda, pero l'altura es la mateixa. Perque? Perque lo que te per sota s'ho emporta
        // i en el fons nomès canvia el pare, que no afecta en la altura del fill.

        return right; //La nova arrel, enganchem la nova estructura amb l'abre de dalt.
    }

    private Node<E> L(Node<E> root) {
        Node<E> left = root.getLeftchild();

        //root no es realment root de l'abre, sinó la root del desbalanceig
        //No cal posar re a null per sort
        root.setLeftchild(left.getRightchild());
        left.setRightchild(root);

        //Lo mismo que amb R
        root.setAltura(maxim(altura(root.getLeftchild()), altura(root.getRightchild())) + 1);
        left.setAltura(maxim(altura(left.getLeftchild()), altura(left.getRightchild())) + 1);

        return left; //La nova arrel, enganchem la nova estructura amb l'abre de dalt.
    }
    /*
     * La de busqueda, que es el que ens interessa, la fem iterativa:
     * https://stackoverflow.com/questions/15346774/are-recursive-methods-always-better-than-iterative-methods-in-java
     * Veiem que es una mica mes rapida (O(log n) igual pero), i gasta menys memoria. Lo bo es que tampoc costa molt de llegir d'aquesta manera. Bastant simple.
     */
    public E search(String s) {
        Node<E> aux = root;
        while (aux != null && !s.equals(aux.getNodeData().getOrder())) {
            //Si la s es mes petita que aux, node esquerre. Sinó, node dret.
            /*El lowercase el fa una mica mes lent, es una pena, potser el trec quan funcioni be.
            / Una mica mès lent pero es un bucle que es fa a cada iteracio..-.-....Realment el cost de O(log n) sembla que no pero s'en va a la merda*/
            if (s.compareTo(aux.getNodeData().getOrder()) <= 0) {
                aux = aux.getLeftchild();
            } else {
                aux = aux.getRightchild();
            }
        }
        if (aux == null) {
            return null; //No l'ha trobat
        }
        return aux.getNodeData().getData();
    }
    public void clear(){
        root = null;
    }
}
