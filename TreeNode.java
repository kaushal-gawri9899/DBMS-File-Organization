
import java.io.FileOutputStream;
import java.util.List;

public abstract class TreeNode {

    List<String> keys;

    int sizeOfKey()
    {
        return keys.size();
    }

    abstract void search(String key);
    abstract void iterateTree(FileOutputStream fOut);
    abstract void searchForGivenRange(String key1, String key2, int type);
    abstract boolean isMarked();
    abstract void insertInTree(String key, String val);
    abstract String getTopKey();
    abstract TreeNode splitNodeForMiddle();
    abstract boolean treeOverflow();
}
