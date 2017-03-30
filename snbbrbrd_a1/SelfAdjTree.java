import edu.gwu.util.*;
import edu.gwu.algtest.*;
import java.lang.*;
import java.util.*;

public class SelfAdjTree implements TreeSearchAlgorithm {

	public static TreeNode root, tempRoot, tempNode, newNode, minimum, parent, grandParent;

	public int count;
	public Object saveVal;


	public String getName() {
  		return "\n snbbrbrd's implementation of SelfAdjtree";
    }

    public void setPropertyExtractor(int algID, PropertyExtractor prop){

    }

	public TreeNode getRoot() {
		return null;
	}

	//Function to insert key
	public Object insert(Comparable key, Object value) {

		tempRoot = root;
		parent = null;

		//While tempRoot is not null
		while (tempRoot != null) {

			//parent equals tempRoot
			parent = tempRoot;

			//Transverse left child
			if (key.compareTo(parent.key) < 0)
				//temproot becomes right child
				tempRoot = tempRoot.right;

			//else transverse right child
			else
				//else temproot becomes left child
				tempRoot = tempRoot.left;
		}

		//Make new node to insert
		newNode = new TreeNode(key, value, null, null, parent);

		//If parent null
		if (parent == null)
			//Root becomes newNode
			root = newNode;
		//else transverse left child
		else if (key.compareTo(parent.key) < 0)
			parent.right = newNode;
		//else transverse right child
		else
			parent.left = newNode;

		Splay(newNode);
		count++;

		return newNode.value;
	}

	//Rotate left
	public void makeLeftChildParent(TreeNode child, TreeNode parent) {

		if ((child == null) || (parent == null) || (parent.left != child) || (child.parent != parent))
			throw new RuntimeException("BAD");
	
		if (parent.parent != null) {

			if (parent == parent.parent.left)
				parent.parent.left = child;
			else
				parent.parent.right = child;
		}

		if (child.right != null)
			child.right.parent = parent;

		child.parent = parent.parent;
		parent.parent = child;

		parent.left = child.right;
		child.right = parent;
	}

	//Rotate right
	public void makeRightChildParent(TreeNode child, TreeNode parent) {

		if ((child == null) || (parent == null) || (parent.right != child) || (child.parent != parent))
			throw new RuntimeException("BAD");
	
		if (parent.parent != null) {

			if (parent == parent.parent.left)
				parent.parent.left = child;
			else
				parent.parent.right = child;
		}

		if (child.left != null)
			child.left.parent = parent;

		child.parent = parent.parent;
		parent.parent = child;

		parent.right = child.left;
		child.left = parent;
	}

	public void Splay(TreeNode node) {

		while (node.parent != null) {

			parent = node.parent;
			grandParent = parent.parent;

			if (grandParent == null) {

				if (node == parent.left)
					makeLeftChildParent(node, parent);
				else
					makeRightChildParent(node, parent);
			} else {

				if (node == parent.left) {
					if (parent == grandParent.left) {
						makeLeftChildParent(parent, grandParent);
						makeLeftChildParent(node, parent);
					} else {
						makeLeftChildParent(node, node.parent);
						makeRightChildParent(node, node.parent);
					}
				} else {
					if (parent == grandParent.left) {
						makeRightChildParent(node, node.parent);
						makeLeftChildParent(node, node.parent);
					} else {
						makeRightChildParent(parent, grandParent);
						makeRightChildParent(node, parent);
					}
				}
			}
		}

		root = node;
	}

	//Method to search for corresponding key in subtree
	public ComparableKeyValuePair search(Comparable key) {

		tempRoot = root;

		Splay(root);

		while (tempRoot != null) {

			if (key.compareTo(tempRoot.key) < 0)
				tempRoot = tempRoot.right;
			else if (key.compareTo(tempRoot.key) > 0)
				tempRoot = tempRoot.left;
			else
				return tempRoot;
		}

		return null;
	}

	//Method to search for maximum key TreeNode
	public ComparableKeyValuePair maximum() {

		//Call recursive mimimum search, and return the corresponding maximum node's KeyValuePair
		return maximum(root);
	}

	//Recursively search through subtree left children for maximum key
	private ComparableKeyValuePair maximum(TreeNode node) {

		//If node.left is a leaf (Most maximum TreeNode), return node
		if (node.left == null)
			return node;

		//Else recursievely send node.left child
		else
			return maximum(node.left);


	}


	//Method to search for minimum key TreeNode
	public ComparableKeyValuePair minimum() {

		//Call recursive minimum search, and return the corresponding minimum node's KeyValuePair
		return minimum(root);
	}

	//Recursively search through subtree right children for minimum key
	private ComparableKeyValuePair minimum(TreeNode node) {

		//If node.right is a leaf (Most minimum TreeNode), return
		if (node.right == null)
			return node;

		//Else recursievely send node.right child
		else
			return minimum(node.right);
	}

	
	//public delete, finds the value of deleting node and stores it, then returns
	public Object delete(Comparable key) {
		//Save value of deleted node
		saveVal = search(key).value;


		delete(root, key);
		//System.out.println("Delete returning: " + tempNode + " but saveVal = " + saveVal);
		
		return saveVal;
	}


	
	private void delete(TreeNode node, Comparable key) {

		if (node == null)
			return;

		Splay(node);

		if ((node.left != null) && (node.right != null)) {

			minimum = node.left;

			while (minimum.right != null)
				minimum = minimum.right;

			minimum.right = node.right;
			node.right.parent = minimum;
			node.left.parent = null;
			root = node.left;

		} else if (node.right != null) {
			node.right.parent = null;
			root = node.right;
		} else if (node.left != null) {
			node.left.parent = null;
			root =node.left;
		} else {
			root = null;
		}

		node.parent = null;
		node.left = null;
		node.right = null;
		node = null;
		count--;

		return;
	}
	
	

	public Comparable successor(Comparable key) {
		
		//Find the node in question
		newNode = (TreeNode) search(key);

		//If null
		if (newNode == null)
			return null;

		//If it has right child, sucessor is in there so return minimum on right child
		if (newNode.right != null)
			return minimum(newNode.right).key;

		//Save newNode's parent
		parent = newNode.parent;
		//Also save newNode
		tempNode = newNode;

		//Until tempNode is not parents right child
		while (parent != null && tempNode == parent.right) {
			//Move tempNode up tree
			tempNode = parent;
			//Move parent up tree
			parent = parent.parent;
		}

		//return parent.key
		return parent.key;
	}
	

	public Comparable predecessor(Comparable key) {
		
		//Find node in question
		newNode = (TreeNode) search(key);

		//If null
		if (newNode == null)
			return null;

		//If it has left child, predecessor is in there so return maximum on left child
		if (newNode.left != null)
			return maximum(newNode.left).key;

		//Save newNode's parent 
		parent = newNode.parent;
		//Also save newNode
		tempNode = newNode;

		//Until tempNode is not parents left child
		while (parent != null && tempNode == parent.left) {
			//Move tempNode up tree
			tempNode = parent;
			//Move parent up tree
			parent = parent.parent;
		}

		return parent.key;
	}

	public void initialize(int maxSize) {
		root = null;
		count = 0;

	}

	// Calls private method getCurrentSize with root
	public int getCurrentSize() {
		return count;
	}

	//Creates a key enumerator
	public Enumeration getKeys() {
		//calls KeyEnumerator
		KeyEnumerator keys = new KeyEnumerator();
		//Populates the key enumerator with root
		keys.populate(root);
		//return keys
		return keys;
	}

	//Creates a value enumerator
	public Enumeration getValues() {
		//calls ValEnumerator
		ValEnumerator values = new ValEnumerator();
		//Populates the value enumerator with root
		values.populate(root);
		//return values
		return values;
	}


	public void inOrder(TreeNode node) {

		if (node != null) {
			inOrder(node.left);
			System.out.print(" [" + node.key + ", " + node.value + "] ");
			inOrder(node.right);
		}
	}

	public static void main(String args[]) {

		SelfAdjTree SAT = new SelfAdjTree();

		SAT.initialize(50);

		System.out.println("Inserting: " + SAT.insert(50, 1));
		System.out.println("Inserting: " + SAT.insert(25, 2));
		System.out.println("Inserting: " + SAT.insert(75, 3));
		System.out.println("Inserting: " + SAT.insert(40, 4));
		System.out.println("Inserting: " + SAT.insert(90, 5));
		System.out.println("Inserting: " + SAT.insert(60, 6));
		System.out.println("Inserting: " + SAT.insert(10, 7));

		SAT.inOrder(root);
		System.out.println();

		System.out.println("40 successor: " + SAT.successor(40));
		System.out.println("40 predecessor: " + SAT.predecessor(40));

		
		System.out.println("Maximum: " + SAT.maximum());
		System.out.println("Minimum: " + SAT.minimum());
		System.out.println("Searching for 75: " + SAT.search(75));
		System.out.println("Searching for 100: " + SAT.search(100));

		System.out.println("deleting 90: " + SAT.delete(90));
		System.out.println("Searching for 90: " + SAT.search(90));
		System.out.println("Maximum: " + SAT.maximum());

		SAT.inOrder(root);
		System.out.println();

		Enumeration enumVal = SAT.getValues();
		System.out.println("Value Enumerator: ");

		while (enumVal.hasMoreElements()) {
			System.out.print(enumVal.nextElement() + ", ");
		}
		System.out.println();

		Enumeration enumKey = SAT.getKeys();
		System.out.println("Key Enumerator: ");

		while (enumKey.hasMoreElements()) {
			System.out.print(enumKey.nextElement() + ", ");
		}
		System.out.println();

		System.out.println("Total Nodes: " + SAT.getCurrentSize());
		

	}

}

//Class value enumerator
class ValEnumerator implements Enumeration {

	//Create an arraylist of Objects to hold values
	ArrayList<Object> values = new ArrayList<Object>();
	int index = 0;

	//Def enumerator hasMoreElements()
	public boolean hasMoreElements() {
		if (index < values.size())
			return true;
		else
			return false;
	}

	//Def enumerator nextElement()
	public Object nextElement() {
		Object o = values.get(index);
		index++;
		return o;
	}

	//Def enumerator getEnumeration()
	public Enumeration getEnumeration() {
		index = 0;
		return this;
	}

	//Populates the values arraylist similair to inOrder recursive print
	public void populate(TreeNode node) {

		if (node != null) {
			
			populate(node.left);
			
			values.add(node.value);

			populate(node.right);
		}
		
	}
}

//Class key unumerator
class KeyEnumerator implements Enumeration {

	//Create an arraylist of Comparable for keys
	ArrayList<Comparable> keys = new ArrayList<Comparable>();
	int index = 0, keyIndex = 0;

	//Def enumerator hasMoreElements()
	public boolean hasMoreElements() {
		if (index < keys.size())
			return true;
		else
			return false;
	}

	//Def enumerator nextElement()
	public Comparable nextElement() {
		Comparable c = keys.get(index);
		index++;
		return c;
	}

	//Def enumerator getEnumeration()
	public Enumeration getEnumeration() {
		index = 0;
		return this;
	}

	//Populates the key arraylist similair to inOrder recursive print
	public void populate(TreeNode node) {

		if (node != null) {

			populate(node.left);

			keys.add(node.key);

			populate(node.right);
		}
		
	}
}