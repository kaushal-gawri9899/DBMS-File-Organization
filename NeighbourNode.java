import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NeighbourNode extends TreeNode{

    public  List<TreeNode> allNeighbours;
    boolean marked=false;

    public NeighbourNode()
    {
        this.keys =  new ArrayList<String>();
        this.allNeighbours = new ArrayList<TreeNode>();
    }

    @Override
    void search(String key) {

        //Traverse through all the neighbour nodes and search for key

        for(TreeNode node : allNeighbours)
        {
            if(!node.isMarked())
            {
               // System.out.println("Here");
                node.search(key);
                marked=true;
            }
        }
    }

    @Override
    void iterateTree(FileOutputStream fOut) {
        if(constants.SHOW_VALUE) {
            int keyNum=0;
            int i=0;
            while (i < allNeighbours.size()) {
                    keyNum=0;
                    for(int j=0; j<allNeighbours.get(i).keys.size(); j++)
                    {
                        System.out.println("NeighbourNode ["+ i +"] key : "+j+" - "+ allNeighbours.get(i).keys.get(j));
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

    @Override
    void searchForGivenRange(String key1, String key2, int type) {

        for(TreeNode node : allNeighbours)
        {
            if(!node.isMarked())
            {

                node.searchForGivenRange(key1,key2,type);
                marked=true;
            }
        }

    }

    @Override
    boolean isMarked() {
        return marked;
    }

    @Override
    void insertInTree(String key, String val) {

        TreeNode childNode = getChildNode(key);

        if (BPlusTree.showStatistics)
             System.out.println("Inserting key: "+ " | " + key + " | ");

        childNode.insertInTree(key,val);


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
            currRoot.keys.add(nextNeighbour.getTopKey());
            currRoot.allNeighbours.add(this);
            currRoot.allNeighbours.add(nextNeighbour);
            BPlusTree.root=currRoot;
        }
    }

    public void insertChildNode(String key, TreeNode node)
    {
        int position = Collections.binarySearch(keys,key);


        if(position>=0) {
            allNeighbours.set(position+1,node);
        }
        else
        {
            keys.add(-position-1, key);
            allNeighbours.add(-position-1, node);
        }
    }
    @Override
    String getTopKey() {
        return allNeighbours.get(0).getTopKey();
    }

    @Override
    TreeNode splitNodeForMiddle() {


        int start = sizeOfKey()/2+1;
        int end = sizeOfKey();

        NeighbourNode node = new NeighbourNode();
        //copy all keys and values to neighbour node
        node.keys.addAll(keys.subList(start,end));
        node.allNeighbours.addAll(allNeighbours.subList(start,end+1));

        keys.subList(start-1,end).clear();
        allNeighbours.subList(start,end+1).clear();

        return node;
    }

    @Override
    boolean treeOverflow() {
        return allNeighbours.size()>BPlusTree.numOfKeys;
    }


    public TreeNode getChildNode(String key)
    {
        //Similar logic to that in ChildNode
        //Use binary search

        int value = Collections.binarySearch(keys,key);
        int index=0;

        if(value>=0)
            index=value+1;
        else
            index = -value-1;

        return allNeighbours.get(index);
    }


    public TreeNode getFirstNeighbour(String key)
    {
        int value = Collections.binarySearch(keys, key);
        int index=0;

        if(value>=0)
            index=value+1;
        else
            index = -value-1;

        if(index>0)
            return allNeighbours.get(index-1);

        return null;
    }


    public TreeNode getSecondNeighbour(String key)
    {
        int value = Collections.binarySearch(keys,key);
        int index=0;

        if(value>=0)
            index = value+1;
        else
            index = -value-1;

        if(index<sizeOfKey())
            return allNeighbours.get(index+1);

        return null;
    }

}
