import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NeighbourNode extends TreeNode{

    /*A list of all the innner nodes or what i like to call neighbour nodes*/
    public  List<TreeNode> allNeighbours;
    boolean marked=false;

    public NeighbourNode()
    {
        this.indexes =  new ArrayList<String>();
        this.allNeighbours = new ArrayList<TreeNode>();
    }

    /*
    * The search operation is fairly simple and uses a recursive approach on the inner nodes (in our case NeighbourNode).
    * Each inner node is marked visited and the leaf nodes associated with the given inner node are explored.
    * */
    @Override
    void search(String index) {
        for(TreeNode node : allNeighbours)
        {
            if(!node.isMarked())
            {
               // System.out.println("Here");
                node.search(index);
                marked=true;
            }
        }
    }

    /*
     * Iterate through the whole tree and mark them visited
     * */
    @Override
    void iterateTree(FileOutputStream fOut) {
        if(constants.SHOW_VALUE) {
            int keyNum=0;
            int i=0;
            while (i < allNeighbours.size()) {
                    keyNum=0;
                    for(int j=0; j<allNeighbours.get(i).indexes.size(); j++)
                    {
                        System.out.println("NeighbourNode ["+ i +"] index : "+j+" - "+ allNeighbours.get(i).indexes.get(j));
                        keyNum=j;
                    }
                i++;
            }
        }

        else
        {
            for (TreeNode node : allNeighbours)
            {
                if(!node.isMarked())
                {
                    node.iterateTree(fOut);
                    marked=true;
                }
            }
        }


    }

    /*
    * As range searches are concerned, a similar approach to search is used such that a recursive call
    * is made on all the inner nodes and the leaf nodes for each inner node are explored.
    * */
    @Override
    void searchForGivenRange(String index1, String index2, int type) {

        for(TreeNode node : allNeighbours)
        {
            if(!node.isMarked())
            {

                node.searchForGivenRange(index1,index2,type);
                marked=true;
            }
        }

    }

    /*Return is inner node visited or not*/
    @Override
    boolean isMarked() {
        return marked;
    }


    /*
    * The key is inserted into the leaf node (ChildNode) and overflow is checked
    * If an overflow in  the inner node, which is the NeighbourNode in our case is observed,
    * this node is again split into two different nodes.
    * Now, the first node of the split will contain 50% of the values as above
    * and the smallest among the remaining ones are moved to parent node
    * such that the second node of the split contains the rest of the values.
     *
    * */
    @Override
    void insertInTree(String index, String val) {

        TreeNode childNode = getChildNode(index);

        if (BPlusTree.showStatistics)
             System.out.println("Inserting Index: "+ " | " + index + " | ");

        childNode.insertInTree(index,val);


        //If the child is overflew, i'll split the child from middle and make the neighbour node as child
        if(childNode.treeOverflow())
        {
            TreeNode nextNeighbour = childNode.splitNodeForMiddle();
            insertChildNode(nextNeighbour.getTopKey(), nextNeighbour);
        }

        if(BPlusTree.root.treeOverflow())
        {
            TreeNode nextNeighbour = splitNodeForMiddle();
            NeighbourNode currRoot = new NeighbourNode();
            currRoot.indexes.add(nextNeighbour.getTopKey());
            currRoot.allNeighbours.add(this);
            currRoot.allNeighbours.add(nextNeighbour);
            BPlusTree.root=currRoot;
        }
    }

    /*
    * Insert the child or leaf node for a given index or neighbour node
    * Use binary search to get relevant location,
    * if key present, move the node by 1
    * if ket not present, add the node at correct position
    * */
    public void insertChildNode(String index, TreeNode node)
    {
        int position = Collections.binarySearch(indexes,index);


        if(position>=0) {
            allNeighbours.set(position+1,node);
        }
        else
        {
            indexes.add(-position-1, index);
            allNeighbours.add(-position-1, node);
        }
    }
    @Override
    String getTopKey() {
        return allNeighbours.get(0).getTopKey();
    }

    /*
     * Split the node from middle
     * Create a new node and place 50% of values in it
     * Place the rest in earlier node
     * */
    @Override
    TreeNode splitNodeForMiddle() {


        int start = sizeOfKey()/2+1;
        int end = sizeOfKey();

        NeighbourNode node = new NeighbourNode();
        //copy all keys and values to neighbour node
        node.indexes.addAll(indexes.subList(start,end));
        node.allNeighbours.addAll(allNeighbours.subList(start,end+1));

        indexes.subList(start-1,end).clear();
        allNeighbours.subList(start,end+1).clear();

        return node;
    }

    //Check if tree overflowed, that is more indexes than the numOfKeys in Tree
    @Override
    boolean treeOverflow() {
        return allNeighbours.size()>BPlusTree.numOfKeys;
    }


    public TreeNode getChildNode(String index)
    {
        //Similar logic to that in ChildNode
        //Use binary search

        int value = Collections.binarySearch(indexes,index);
        int childindex=0;

        if(value>=0)
            childindex=value+1;
        else
            childindex = -value-1;

        return allNeighbours.get(childindex);
    }

}
