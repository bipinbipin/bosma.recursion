import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ITG {
	
	static HashMap<String, Float>  probs;
	//Probs
	static float AB = -1;
	static float BA = -2;
	static float wEps = -20;
	static float epsW = -21;
	
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		String engText = "en.txt";
		String gerText = "de.txt";
		String dict = "dict.txt";
		probs = new HashMap<String, Float>();
		ArrayList<String> engSentences = new ArrayList<String>();
		ArrayList<String> gerSentences = new ArrayList<String>();
		getProbs(dict);
		getSentences(engText, engSentences);
		getSentences(gerText, gerSentences);

		align(engSentences.get(95), gerSentences.get(95));

	}
	
	public static void getSentences(String file, ArrayList<String> sentences) throws FileNotFoundException, IOException {
		
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
		    String line;
		    String[] str;
		    while ((line = br.readLine()) != null) {
		    	str = line.split("\\n");
		    	sentences.add(str[0]);
		    }		    
		}
	}
	
	public static void getProbs(String file) throws FileNotFoundException, IOException {
		
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
		    String line;
		    String[] str;
		    while ((line = br.readLine()) != null) {
		    	str = line.split("\\t", 3);
		    	Float fl = new Float(str[2]).floatValue();
		    	
		    	probs.put(str[0] + "," + str[1], fl);
		    }
		}
	}
	
	public static Tree alignProbs(String[] engSentence, int engStart, int engEnd, String[] gerSentence, int gerStart, int gerEnd, int depth) {
		String engWord = "";
		String gerWord = "";
		//System.out.println("depth " + depth);
		
		//word/epsilon
		if(engStart == engEnd) {
			
			for(int i = gerStart; i < gerEnd; i++) {
				gerWord = gerWord.concat(gerSentence[i] + " ");
			}
			//System.out.println("German Word " +  gerWord);

			Leaf lef = new Leaf("epsilon", gerWord, epsW*gerWord.length());

			return lef;
		}
		//epsilon/word
		else if(gerStart == gerEnd) {
			
			for(int i = engStart; i < engEnd; i++) {
				engWord = engWord.concat(engSentence[i] + " ");
			}
			//System.out.println("English word " + engWord);

			return new Leaf(engWord, "epsilon", wEps);
		}
		else if((engEnd - engStart == 1) && (gerEnd - gerStart == 1)) {
			engWord = engWord.concat(engSentence[0]);
			gerWord = gerWord.concat(gerSentence[0]);

			return new Leaf(engWord, gerWord, wordAlignProb(engWord, gerWord));
		}
		//Recursive Part
		else {
			ArrayList<Tree> trees = new ArrayList<Tree>();
			for(int i = engStart; i < engEnd; i++ ) {
				for(int k = gerStart; k < gerEnd; k++) {
					
					if(i == engStart && k == gerStart) {
						//Do nothing
					}
					else {
						//Aligned
						Tree left = alignProbs(engSentence, engStart, i, gerSentence, gerStart, k, depth+1);
						Tree right = alignProbs(engSentence, i, engEnd, gerSentence, k, gerEnd, depth+1);
						Tree tree = new Node(left, right, left.prob + right.prob + AB, false);
						
						System.out.println("Is node " + left.isNode());
						System.out.println("Is leaf " + left.isLeaf());
						
						if(left.isLeaf()) {
							System.out.println("left w " + left.engWord);
							System.out.println("gerW " + left.gerWord);
						}

						//System.out.println("R prob " +right.prob);
						System.out.println("L prob " +left.prob);
						//System.out.println("Tree prob " +tree.prob);
						trees.add(tree);
						
						//Inverted
						Tree leftInv = alignProbs(engSentence, engStart, i, gerSentence, k, gerEnd, depth+1);
						Tree rightInv = alignProbs(engSentence, i, engEnd, gerSentence, gerStart, k, depth+1);
						Tree treeInv = new Node(leftInv, rightInv, leftInv.prob+rightInv.prob+BA, true);
						trees.add(treeInv);
					}
				}
			}
			return getMaxTree(trees);
		}
	}
	
	public static void align(String engSentence, String gerSentence) {
		
		System.out.println(engSentence + " " + gerSentence);
		
		String[] eng = engSentence.split("\\s");
		String[] ger = gerSentence.split("\\s");
		Tree tree = alignProbs(eng, 0, eng.length, ger, 0, ger.length, 0);
		
		System.out.println(tree.toString());
		ArrayList<String> alignment = tree.getAlignments();
		
		printAlignment(alignment);
	}
	
	public static void printAlignment(ArrayList<String> alignment) {
		System.out.println("Printing Alignment");
		for(int i = 0; i < alignment.size(); i++) {
			//May need to be checked
			System.out.println(alignment.get(i));
		}
	}
	

	public static Tree getMaxTree(ArrayList<Tree> trees) {
		Tree maxTree = trees.get(0);
		float maxProb = maxTree.prob;
		
		

		for(int i = 0; i < trees.size(); i++) {
			System.out.println(trees.get(i).prob);
			if(trees.get(i).prob > maxProb) {

				maxTree = trees.get(i);
				maxProb = maxTree.prob;
			}
		}
		return maxTree;
	}
	
	public static float wordAlignProb(String eW, String gW) {
		Float prob = probs.get(eW + "," + gW);
		if(prob == null) {
			return -999;
		}
		else {
			return prob;
		}
	}

}
