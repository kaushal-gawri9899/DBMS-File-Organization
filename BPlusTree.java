import java.io.FileOutputStream;

public class BPlusTree {

    public static int numOfKeys;
    public static TreeNode root;
    public static boolean showStatistics;
    public BPlusTree()
    {
        numOfKeys = 1024;
        root = new ChildNode();
        showStatistics = false;
    }

    public void setStatistics(boolean value)
    {
        this.showStatistics = false;
    }

    public void insertIntoTree(String key, String value)
    {
        root.insertInTree(key, value);
    }

    public void search(String key)
    {
        root.search(key);
    }

    public void iterateTree(FileOutputStream fOut)
    {
        root.iterateTree(fOut);
    }

    public void searchForGivenRange(String key1, String key2, int type)
    {
        root.searchForGivenRange(key1, key2, type);
    }


}
