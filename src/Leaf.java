import java.util.ArrayList;

public class Leaf extends Tree{
	
//	public String engWord;
//	public String gerWord;
//	public float prob;
	
	public Leaf(String engWord, String gerWord, float prob) {
		this.engWord = engWord;
		this.gerWord = gerWord;
		this.prob = prob;
	}
	
	public String toString() {
		return this.engWord + " / " + this.gerWord;
	}
	
	public ArrayList<String> getAlignments() {
		ArrayList<String> var = new ArrayList<String>();
		var.add(this.engWord + "," + this.gerWord);
		return var;
	}
	
	public boolean isNode() {
		return false;
	}
	
	public boolean isLeaf() {
		return true;
	}

}
