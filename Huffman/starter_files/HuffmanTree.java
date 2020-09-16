import java.util.*;

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
		if(humanMessage.equals(""))
			return "";
		if(chars.size() == 1){
			encoding.put(chars.get(0),"0");
		}
		else{
			generateCode(encoding,root,"");
		}
		printTree(encoding);
		StringBuilder encodedMessage = new StringBuilder();
		for(int i = 0;i<humanMessage.length();i++){
			String tempChar = humanMessage.substring(i,i+1);
			String code = encoding.get(tempChar);
			encodedMessage.append(code);
		}
		return encodedMessage.toString();
	}
	
	public String decode(String encodedMessage) {

		HashMap<String,String> encoding = new HashMap<>();

		if(encodedMessage.equals(""))
			return "";

		if(chars.size() == 1){
			encoding.put(chars.get(0),"0");
		}
		else
			generateCode(encoding,root,"");
		printTree(encoding);
		String remainingMessage = encodedMessage;
		StringBuilder decodedMessage = new StringBuilder();
		int i = 0;
		while(remainingMessage.length() >0) {
			String temp = remainingMessage.substring(0,i);
			if(encoding.containsValue(temp)){
				String humanCharacter = getCharacter(encoding,temp);
				decodedMessage.append(humanCharacter);
				remainingMessage = remainingMessage.substring(i);
				i = 0;
			}
			else{
				i++;
			}
		}
        return decodedMessage.toString();
	}


	private void printTree(HashMap<String,String> encoding){
		System.out.println(Arrays.asList(encoding));
	}

	private String getCharacter(HashMap<String,String> encoding,String binary){
		for (String aChar : chars) {
			if (encoding.get(aChar).equals(binary))
				return aChar;
		}
		return null;
	}

	private static void generateCode(HashMap<String,String> code, TreeNode node,String prefix){
		if(node == null)
			return;
		if(node.getValue() == null){
			generateCode(code,node.getLeftChild(),prefix + "0");
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
