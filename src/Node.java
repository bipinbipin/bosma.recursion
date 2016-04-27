import java.util.ArrayList;

public class Node extends Tree{
	
	//Left side
	public Tree left;
	//Right Side
	public Tree right;
	//Probability of getting
	//public float prob;
	//Was this inverted - tells to use < or [
	public boolean invert;
	
	//public String engWord;
	//public String gerWord;
	
	public Node(Tree left, Tree right, float prob, boolean invert) {
		this.left = left;
		this.right = right;
		this.prob = prob;
		this.invert = invert;
	}
	
	public String toString() {
		if(invert) {
			return "< " + this.left.toString() + "," + this.right.toString() + " >";
		}
		else
			return "[ " + this.left.toString() + "," + this.right.toString() + " ]";
	}
	
	public ArrayList<String> getAlignments() {
		ArrayList<String> var;
		var = this.left.getAlignments();
		var.addAll(this.right.getAlignments());
		
		
		return var;
	}
	
	public boolean isNode() {
		return true;
	}
	
	public boolean isLeaf() {
		return false;
	}

}
