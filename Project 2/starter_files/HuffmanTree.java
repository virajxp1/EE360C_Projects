import java.util.HashMap;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class HuffmanTree {
	TreeNode root;
	ArrayList<String> chars;
	ArrayList<Integer> frequencies;
	
	HuffmanTree() {
		root = null;
	}
	
	public void constructHuffmanTree(ArrayList<String> characters, ArrayList<Integer> freq) {
		//Characters contains each characters freq contains the frequency of each character
		//Create priority queue
		if(characters.size() == 0){
			root = null;
			return;
		}
		PriorityQueue<TreeNode> Queue = new PriorityQueue<>(characters.size(),new treeNodeComparator());
		for(int i = 0;i<characters.size();i++){
			TreeNode n = new TreeNode();
			n.setValue(characters.get(i));
			n.setFrequency(freq.get(i));
			Queue.add(n);
		}

		while(Queue.size() >1){
			TreeNode newNode = new TreeNode();
			TreeNode min1 = Queue.poll();
			TreeNode min2 = Queue.poll();
			newNode.setLeftChild(min1);
			newNode.setRightChild(min2);
			min1.setParent(newNode);
			min2.setParent(newNode);
			newNode.setFrequency(min1.getFrequency()+min2.getFrequency());
			Queue.add(newNode);
		}
		root = Queue.poll();
		chars = characters;
		frequencies = freq;
	}
	
	public String encode(String humanMessage) {
		HashMap<String,String> encoding = new HashMap<>();
		generateCode(encoding,root,"");
		String encodedMessage = "";
		for(int i = 0;i<humanMessage.length();i++){
			String tempChar = humanMessage.substring(i,i+1);
			String code = encoding.get(tempChar);
			encodedMessage+=code;
		}
		return encodedMessage;
	}
	
	public String decode(String encodedMessage) {

		HashMap<String,String> encoding = new HashMap<>();
		generateCode(encoding,root,"");

		String remainingMessage = encodedMessage;
		String decodedMessage = "";
		int i = 0;
		while(remainingMessage.length() >0) {
			String temp = remainingMessage.substring(0,i);
			if(encoding.containsValue(temp)){
				String humanCharacter = getCharacter(encoding,temp);
				decodedMessage+=humanCharacter;
				remainingMessage = remainingMessage.substring(i);
				i = 0;
			}
			else{
				i++;
			}
		}
        return decodedMessage;
	}

	private String getCharacter(HashMap<String,String> encoding,String binary){
		for(int i = 0;i<chars.size();i++){
			if(encoding.get(chars.get(i)).equals(binary))
				return chars.get(i);
		}
		return null;
	}

	private static void generateCode(HashMap<String,String> code, TreeNode node,String prefix){
		if(node == null)
			return;
		if(node.getLeftChild() == null && node.getRightChild() == null){
			code.put(node.getValue(),prefix+"0");
			return;
		}
		if(node.getValue() == null){
			generateCode(code,node.getLeftChild(),prefix+"0");
			generateCode(code,node.getRightChild(),prefix + "1");
		}
		else{
			code.put(node.getValue(),prefix);
		}
	}

}

class treeNodeComparator implements Comparator<TreeNode>{

	/**
	 * Compares its two arguments for order.  Returns a negative integer,
	 * zero, or a positive integer as the first argument is less than, equal
	 * to, or greater than the second.
	 * @param o1 the first object to be compared.
	 * @param o2 the second object to be compared.
	 * @return a negative integer, zero, or a positive integer as the
	 * first argument is less than, equal to, or greater than the
	 * second.
	 * @throws NullPointerException if an argument is null and this
	 *                              comparator does not permit null arguments
	 * @throws ClassCastException   if the arguments' types prevent them from
	 *                              being compared by this comparator.
	 */
	@Override
	public int compare(TreeNode o1, TreeNode o2) {
		return Integer.compare(o1.getFrequency(), o2.getFrequency());
	}
}
