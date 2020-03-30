
/*
 * COMP 141- Programming Languages
 * Name: Ian Higa
 * Project Phase: 2.2
 */
public class Tree {
	private Token value;
	private Tree left;
	private Tree right;
	private Tree middle;
	
	public Tree(Token root, Tree l, Tree r) {
		value = root;
		left = l;
		right = r;
		middle = null;
	}
	
	public Tree(Token root, Tree l, Tree m, Tree r) {
		value = root;
		left = l;
		right = r;
		middle = m;
	}
	
	public Tree getLeft() {
		return left;
	}
	
	public Tree getRight() {
		return right;
	}
	
	public Tree gfetMid() {
		return middle;
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
		//Middle Children
		if(middle != null) {
			out += middle.toString();
		}
		//Right Children
		if(right != null) {
			out += right.toString();
		}
		
		return out;
	}
}
