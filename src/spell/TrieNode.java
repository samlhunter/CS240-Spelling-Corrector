package spell;

public class TrieNode implements INode {
    int value;
    INode [] children;

    public TrieNode(){
        this.value=0;
        this.children= new INode[26];
    }
    @Override
    public int getValue() {return value;}

    @Override
    public void incrementValue() {value++;}

    @Override
    public INode[] getChildren() {return children;}

    public void addChild(int index) {
        this.children[index]=new TrieNode();
    }
}
