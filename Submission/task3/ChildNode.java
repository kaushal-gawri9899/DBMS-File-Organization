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
        indexes = new ArrayList<String>();
        nodeVal = new ArrayList<String>();
    }

    /*
    * A simple iteration to all the leaf nodes (ChildNode) is used,
    * similar to a linear scan to search for the given index in the tree.
    * If the search parameters match the key, as String.contains() is used,
    * even if the search key is a substring of the key value in the B+ Tree,
    * the results are outputted on the console. */
    @Override
    public void search(String key) {

        //Mark current node visited
        marked=true;
        int i=0;

        while(i<indexes.size())
        {

            String indexOfNode = indexes.get(i);
            String valOfNode = nodeVal.get(i);
            if(indexOfNode.toUpperCase().contains(key.toUpperCase()))
            {
                System.out.println("Value Found in B+ Tree"+"["+key+"] : "+ indexOfNode+" : "+ valOfNode);
            }
			if(valOfNode.toUpperCase().contains(key.toUpperCase()))
			{
				System.out.println("Value Found in B+ Tree"+"["+key+"] : "+ indexOfNode+" : "+ valOfNode);
			}
            i++;
        }
    }

    /*
    * Iterate through the whole tree and write out on the file name specified in constants class
    * */
    @Override
    public void iterateTree(FileOutputStream fOut) {

            marked=true;

            //Change it to tree size from constants clas
            byte[] currentRecord = new byte[constants.TREE_SIZE];

            int i=0;

            while(i<indexes.size())
            {
                String keyOfNode = indexes.get(i);
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

    /*
    * All the leaf nodes for each inner node are explored.
    * The key or index of leaf node is then matched with key1 and key2 provided in the query.
    * String.compareTo() is used, such that if a value greater than or equal to 0 is returned for key1,
    * it means that the value the leaf node has is greater than the key1 and thus should lie in the range.
    * Also, if a value less than or equal to 0 is returned for key2,
    * it means that the  value of the leaf node is less than key2 and thus should also lie in the range.
    * Another condition is checked if key2 is the substring of the value for given leaf node as this requirement is not met by using String.compareTo().
     * */
    @Override
    public void searchForGivenRange(String index1, String index2, int type) {
                marked=true;

                int i=0;

                while(i<indexes.size())
                {
                    String keyOfNode = indexes.get(i);
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

                    if(toSearch.compareTo(index1)>=0 && toSearch.compareTo(index2)<=0)
                    {
                        System.out.println("Result For Given Range"+"["+index1+"]" + "----" + "["+index2+"] :-  " + keyOfNode + " : " + valOfNode );
                    }

                    //Also check if key entered is a substring of the last-string
                    //Above if won't consider that

                    if(toSearch.contains(index2))
                    {
                        System.out.println("Result For Given Range"+"["+index1+"] " + "----" + "["+index2+"] :-  " + keyOfNode + " : " + valOfNode );
                    }

                    i++;
                }

    }

    /*
    * Return true if current node marked else return false
    * */
    @Override
    public boolean isMarked() {
        return marked;
    }

    /*
    * Use binary search to check if index already present
    * If yes, set the current value
    * If not, insert the value for leaf node and index at inner node level
    * If tree overflows, split the node from middle such that 50% of the values are placed in new node
    * The second node created after split will contain the remaining values.
    * The next step is to copy the smallest key value from the second node consisting of other half of elements to the parent node
     * */
    @Override
    public void insertInTree(String index, String val) {


            //I would use pre built binarySearch method provided by Collections framework with O(logn) time in worst case
            int value = Collections.binarySearch(indexes,index);
            int leafIndex  = 0;
            if(value>=0)
                leafIndex=value;
            else
                leafIndex = -value-1;

            if(value>=0)
            {
                //Already there
               if (BPlusTree.showStatistics)
                     System.out.println("Setting index and Value at node: " + nodeVal.size() + " | " + index + " | ");

               nodeVal.set(leafIndex,val);

            }

            else
            {
                if (BPlusTree.showStatistics)
                    System.out.println("Inserting index and value at node: "+ nodeVal.size()+ " | " + index + " | ");
                indexes.add(leafIndex,index);
                nodeVal.add(leafIndex,val);
            }

            if(BPlusTree.root.treeOverflow())
            {

                if(BPlusTree.showStatistics)
                    System.out.println("Overflow for node: "+ nodeVal.size() + " with index " + "| " + index + " |");
                //To handle overflow, you need to splt the node
                TreeNode nextChild = splitNodeForMiddle();
                NeighbourNode currRoot = new NeighbourNode();
                currRoot.indexes.add(nextChild.getTopKey());
                currRoot.allNeighbours.add(this);
                currRoot.allNeighbours.add(nextChild);

                //Update the root
                BPlusTree.root = currRoot;

            }
    }

    @Override
    String getTopKey() {
        return indexes.get(0);
    }

    /*
    * Split the node from middle
    * Create a new node and place 50% of values in it
    * Place the rest in earlier node
    * The next step is to copy the smallest key value from the second node consisting of other half of elements to the parent node
    * */
    @Override
    public TreeNode splitNodeForMiddle() {

		if(BPlusTree.showStatistics)
            System.out.println("Splitting current tree node");

        /*after creating a new leaf node, insert keys and values from first half and set the next to neighbour node*/

        ChildNode neighbour = new ChildNode();
        int totalNodes = sizeOfKey();
        int start = (totalNodes+1)/2;
        int end = totalNodes;

        neighbour.indexes.addAll(indexes.subList(start,end));
        neighbour.nodeVal.addAll(nodeVal.subList(start,end));

        indexes.subList(start,end).clear();
        nodeVal.subList(start,end).clear();

        //Update neighbour as the next of current node and update next of neighbour as earlier nextNode
        //This is so that a node could be inserted in between two nodes with correct references(pointers)
        neighbour.nextNode=nextNode;
        nextNode=neighbour;

        return neighbour;

    }

    /*
    * Check if tree overflowed
    * This is when the list containing values for inner node has more elements than number of index in BPlusTree
    * */
    @Override
    public boolean treeOverflow() {
        return nodeVal.size() > BPlusTree.numOfKeys - 1;
    }
}
