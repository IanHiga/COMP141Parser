
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
}
