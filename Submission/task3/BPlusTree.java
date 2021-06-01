import java.io.FileOutputStream;

public class BPlusTree {

	//Maximum number of keys or index that BPlusTree can have
    public static int numOfKeys;

    //The root leaf node of the tree
    public static TreeNode root;

    //Display console statements or not
    public static boolean showStatistics;

    public BPlusTree()
    {
        numOfKeys = 1024;
        root = new ChildNode();
        showStatistics = false;
    }

    /*
    * Set's the Debug Mode for output statements on and off
    * */
    public void setStatistics(boolean value)
    {
        this.showStatistics = false;
    }

    /*
    * The insert method is responsible to add a index and value in tree
    * This method invokes the method responsible to add node to tree
    * As in BPlus Tree the values (or data) is held by leaf nodes,
    * thus method invoked is for the leaf node(in my case Child Node)
    *
    * @params : String index, String value
    * */
    public void insertIntoTree(String index, String value)
    {
        root.insertInTree(index, value);
    }

    /*
    * The search method is responsible to search for a respective index in the tree
    * The method matches the index using String.contains() for every substring
    *
    * @params : String index
    * */
    public void search(String index)
    {
        root.search(index);
    }

    /*
    * This method is responsible for iterating through the whole tree
    * and exploring leaf nodes (or ChildNode)
    *
    * @params : FileOutputStream fOut
    * */
    public void iterateTree(FileOutputStream fOut)
    {
        root.iterateTree(fOut);
    }

    /*
    * This method is responsible to search for a given range of index in the tree
    * This method uses both String.compareTo() and String.contains() to retrieve search results
    *
    * @params : String index1, String index2, int type
    * */
    public void searchForGivenRange(String index1, String index2, int type)
    {
        root.searchForGivenRange(index1, index2, type);
    }


}
