
public class Tree {
	private Token value;
	private Tree left;
	private Tree right;
	
	public Tree(Token root, Tree l, Tree r) {
		value = root;
		left = l;
		right = r;
	}
	
	public Tree getLeft() {
		return left;
	}
	
	public Tree getRight() {
		return right;
	}
	
	public Token getValue() {
		return value;
	}
	
	public String toString() {
		String out = "";
		//Print this node
		out += value.toString();
		//Left Children
		if(left != null) {
			out += left.toString();
		}
		//Right Children
		if(right != null) {
			out += right.toString();
		}
		
		return out;
	}
}
