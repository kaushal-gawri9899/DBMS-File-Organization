import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class NeighbourNode extends TreeNode{

    public  List<TreeNode> allNeighbours;

    public NeighbourNode()
    {
        this.keys =  new ArrayList<String>();
        this.allNeighbours = new ArrayList<TreeNode>();
    }

    @Override
    void search(String key) {

    }

    @Override
    void iterateTree(FileOutputStream fOut) {

    }

    @Override
    void searchForGivenRange(String key1, String key2, int type) {

    }

    @Override
    boolean isMarked() {
        return false;
    }

    @Override
    void insertInTree(String key, String val) {

    }

    @Override
    String getTopKey() {
        return null;
    }

    @Override
    TreeNode splitNodeForMiddle() {
        return null;
    }

    @Override
    boolean treeOverflow() {
        return false;
    }
}
