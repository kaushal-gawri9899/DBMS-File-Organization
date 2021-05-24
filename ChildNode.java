import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class ChildNode extends TreeNode{

    List<String> nodeVal;

    ChildNode nextNode;
    boolean marked  =false;
    public ChildNode()
    {
//        BPlusTree.keys =
        keys = new ArrayList<String>();
        nodeVal = new ArrayList<String>();
    }

    @Override
    public void search(String key) {

        //Mark current node
        marked=true;
        int i=0;

        while(i<keys.size())
        {

            String keyOfNode = keys.get(i);
            String valOfNode = nodeVal.get(i);

            if(keyOfNode.toUpperCase().contains(key.toUpperCase()) || valOfNode.toUpperCase().contains(key.toUpperCase()))
            {
                System.out.println("Value Found in B+ Tree"+"["+key+"] : "+ keyOfNode+" : "+ valOfNode);
            }
            i++;
        }
    }

    @Override
    public void iterateTree(FileOutputStream fOut) {
            marked=true;

            //Change it to record size from dbimpl
            byte[] currentRecord = new byte[100];

            int i=0;

            while(i<keys.size())
            {
                String keyOfNode = keys.get(i);
                String valOfNode = nodeVal.get(i);

                String toReturn = keyOfNode + "," + valOfNode + ",";

                currentRecord = toReturn.getBytes();

                try{
                    fOut.write(currentRecord);
                }
                catch(FileNotFoundException ex){
                   System.out.println("B+ tree File Not Found");
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
                i++;
            }
    }

    @Override
    public void searchForGivenRange(String key1, String key2, int type) {
                marked=true;

                int i=0;

                while(i<keys.size())
                {
                    String keyOfNode = keys.get(i);
                    String valOfNode = nodeVal.get(i);

                    String toSearch="";

                    if(type==constants.RANGE_SEARCH_DATE)
                    {
                        toSearch = keyOfNode.substring(constants.DATE_OFFSET, constants.DATE_SIZE);

                    }

                    else if(type==constants.RANGE_SEARCH_ID)
                    {
                        toSearch = keyOfNode.substring(constants.ID_OFFSET, constants.ID_SIZE);

                    }


                    if(toSearch.compareTo(key1)>=0 && toSearch.compareTo(key2)<=0)
                    {
                        System.out.println("Result For Given Range"+"["+key1+"] : " + "----" + "["+key2+"] :-  " + keyOfNode + " : " + valOfNode );
                    }

                    //Also check if key entered is a substring of the laststring
                    //Above if won't consider that

                    if(toSearch.contains(key2))
                    {
                        System.out.println("Result For Given Range"+"["+key1+"] : " + "----" + "["+key2+"] :-  " + keyOfNode + " : " + valOfNode );
                    }
                }

    }

    @Override
    public boolean isMarked() {
        return marked;
    }

    @Override
    public void insertInTree(String key, String val) {


            //I would use pre built binarySearch method provided by Collections framework with O(logn) time in worst case

            int value = Collections.binarySearch(keys,key);
            int index  = 0;
            if(value>=0)
                index=value;
            else
                index = -value-1;

            if(value>=0)
            {
                //Already there
                //Add a check for user output
                //if(constants.DEBUG_MODE_INSERT)
                System.out.println("Setting key and value at node: "+nodeVal.size()+ " | " + key + " | " + value + "|");

                nodeVal.set(index,val);

            }

            else
            {
                //if(constants.DEBUG_MODE_INSERT)
                System.out.println("Inserting key and value at node: "+nodeVal.size()+ " | " + key + " | " + value + "|");
                keys.add(index,key);
                nodeVal.add(index,val);
            }

            if(BPlusTree.root.treeOverflow())
            {
                //if(constants.DEBUG_MODE_INSERT)
                System.out.println("Overflow lead at node: "+nodeVal.size()+ " | " + key + " | " + value + "|");

                //To handle overflow, you need to splt the node
                TreeNode nextChild = splitNodeForMiddle();
                NeighbourNode currRoot = new NeighbourNode();
                currRoot.keys.add(nextChild.getTopKey());
                currRoot.allNeighbours.add(this);
                currRoot.allNeighbours.add(nextChild);

                //Update the root
                BPlusTree.root = currRoot;

            }
    }

    @Override
    String getTopKey() {
        return keys.get(0);
    }

    @Override
    public TreeNode splitNodeForMiddle() {
        //if(constants.DEBUG_MODE_INSERT)
        System.out.println("Splitting Tree Node");

        /*after creating a new leaf node, insert keys and values from first half and set the next to neighbour node*/

        ChildNode neighbour = new ChildNode();
        int totalNodes = sizeOfKey();
        int start = (totalNodes+1)/2;
        int end = totalNodes;

        neighbour.keys.addAll(keys.subList(start,end));
        neighbour.nodeVal.addAll(nodeVal.subList(start,end));

        keys.subList(start,end).clear();
        nodeVal.subList(start,end).clear();

        //Update neighbour as the next of current node and update next of neighbour as earlier nextNode
        //This is so that a node could be inserted in between two nodes with correct references(pointers)
        neighbour.nextNode=nextNode;
        nextNode=neighbour;

        return neighbour;



    }

    @Override
    public boolean treeOverflow() {
        return nodeVal.size() > BPlusTree.numOfKeys - 1;
    }
}
