
import java.io.FileOutputStream;
import java.util.List;

/*
* Abstract Class for Code Re-usability and to provide data encapsulation and abstraction
* */
public abstract class TreeNode {

    List<String> indexes;

    int sizeOfKey()
    {
        return indexes.size();
    }

    abstract void search(String index);
    abstract void iterateTree(FileOutputStream fOut);
    abstract void searchForGivenRange(String index1, String index2, int type);
    abstract boolean isMarked();
    abstract void insertInTree(String index, String val);
    abstract String getTopKey();
    abstract TreeNode splitNodeForMiddle();
    abstract boolean treeOverflow();
}
