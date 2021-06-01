import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChildNode extends TreeNode{

    List<String> nodeVal;

    ChildNode nextNode;
    boolean marked  =false;
    public ChildNode()
    {
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

            if(keyOfNode.toUpperCase().contains(key.toUpperCase()))
            {
                System.out.println("Value Found in B+ Tree"+"["+key+"] : "+ keyOfNode+" : "+ valOfNode);
            }
			if(valOfNode.toUpperCase().contains(key.toUpperCase()))
			{
				System.out.println("Value Found in B+ Tree"+"["+key+"] : "+ keyOfNode+" : "+ valOfNode);
			}
            i++;
        }
    }

    @Override
    public void iterateTree(FileOutputStream fOut) {

            marked=true;

            //Change it to tree size from constants clas
            byte[] currentRecord = new byte[constants.TREE_SIZE];

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
                   System.out.println("B+ tree File" + constants.TREE_FILE_NAME + "Not Found");
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

                    if(type==constants.RANGE_SEARCH_STD)
                    {
                        toSearch = valOfNode;
                    }

                    else if(type==constants.RANGE_SEARCH_ID)
                    {
						toSearch = valOfNode.substring(constants.ID_OFFSET, constants.ID_SIZE);
                    }

                    if(toSearch.compareTo(key1)>=0 && toSearch.compareTo(key2)<=0)
                    {
                        System.out.println("Result For Given Range"+"["+key1+"]" + "----" + "["+key2+"] :-  " + keyOfNode + " : " + valOfNode );
                    }

                    //Also check if key entered is a substring of the last-string
                    //Above if won't consider that

                    if(toSearch.contains(key2))
                    {
                        System.out.println("Result For Given Range"+"["+key1+"] " + "----" + "["+key2+"] :-  " + keyOfNode + " : " + valOfNode );
                    }

                    i++;
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
               if (BPlusTree.showStatistics)
                     System.out.println("Setting Key and Value at node: " + nodeVal.size() + " | " + key + " | ");

               nodeVal.set(index,val);

            }

            else
            {
                if (BPlusTree.showStatistics)
                    System.out.println("Inserting key and value at node: "+ nodeVal.size()+ " | " + key + " | ");
                keys.add(index,key);
                nodeVal.add(index,val);
            }

            if(BPlusTree.root.treeOverflow())
            {

                if(BPlusTree.showStatistics)
                    System.out.println("Overflow for node: "+ nodeVal.size() + " with index " + "| " + key + " |");
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

		if(BPlusTree.showStatistics)
            System.out.println("Splitting current tree node");

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
