import edu.gwu.util.*;
import edu.gwu.algtest.*;
import java.lang.*;
import java.util.*;

public class BinarySearchTree implements TreeSearchAlgorithm {

	//TreeNode Fields
	public static TreeNode root, tempNode, newNode, current, parent;
	public Object saveVal;
	public int maxSize, count;

	//My name
	public String getName() {
  		return "\n snbbrbrd's implementation of BinarySearchTree";
    }

    //I have no idea what this does
    public void setPropertyExtractor(int algID, PropertyExtractor prop){

    }

    //Got root?
	public TreeNode getRoot() {
		//Well now you do!
		return root;
	}

	//Method to insert a node (Go Figure!!)
	public Object insert(Comparable key, Object value){

		//Create that newNode!
		newNode = new TreeNode(key, value, null, null, null);

		//If root is null, make new root!
		if(root == null) {
			root = newNode;
			//append count
			count++;
			//Return root Value! Awesome!
			return newNode.value;
		}

		//Save a pointer (C joke haha) to the root
		current = root;

		//Set a parent node to null
		parent = null;
		
		//While we are currently transversing the tree
		while(true) {

			//Parent becomes the current node
			parent = current;

			//If key is less than current key (Go left subtree)
			if(key.compareTo(current.key) < 0) {		
				//Change current to current left child		
				current = current.left;

				//If current (Now left child) is null
				if(current == null) {
					//Parent left child equals newNode
					parent.left = newNode;
					//newNode parent equals parent
					newNode.parent = parent;
					//Increment count
					count++;
					//return newNode value
					return newNode.value;
				}

			//Else, key is greater than current key (Go right subtree)
			} else { 
				//Change current to current right child
				current = current.right;

				//If current (Now right child) is null
				if(current == null) {
					//Parent right child equals newNode
					parent.right = newNode;
					//newNode parent equals parent
					newNode.parent = parent;
					//Increment count
					count++;
					//return newNode value
					return newNode.value;
				}
			}
		}
	}

	//Method to search for corresponding key in subtree
	public ComparableKeyValuePair search(Comparable key) {
		
		//Check if root has key
		if (key.compareTo(root.key) == 0) 
			return root;

		//Else recursively search for key
		else 
			//Return search key
			return search(root, key); 
	}

	//Recursively search through subtree for corresponding key, return keyValuePair if found
	private ComparableKeyValuePair search(TreeNode node, Comparable key) {
		
		if (node == null)
			return null;
		
		//If current node equals key
		if (key.compareTo(node.key) == 0)
			//Return current node
			return node;

		//Check if key is less than node key
		else if (key.compareTo(node.key) < 0) {
			//Return search in left child
			return search(node.left, key);
		}
		//Else check if key is greater than node key
		else if (key.compareTo(node.key) > 0) {
			//Return search in rigth child
			return search(node.right, key);
		}
		else
			return null;

	}

	//Method to search for minimum key TreeNode
	public ComparableKeyValuePair minimum() {

		//Call recursive mimimum search, and return the corresponding minimum node's KeyValuePair
		return minimum(root);
	}

	//Recursively search through subtree left children for minimum key
	private ComparableKeyValuePair minimum(TreeNode node) {

		//If node.left is a leaf (Most minimum TreeNode), return node
		if (node.left == null)
			return node;

		//Else recursievely send node.left child
		else
			return minimum(node.left);
	}


	//Method to search for maximum key TreeNode
	public ComparableKeyValuePair maximum() {

		//Call recursive maximum search, and return the corresponding maximum node's KeyValuePair
		return maximum(root);
	}

	//Recursively search through subtree right children for maximum key
	private ComparableKeyValuePair maximum(TreeNode node) {

		//If node.right is a leaf (Most maximum TreeNode), return
		if (node.right == null)
			return node;

		//Else recursievely send node.right child
		else
			return maximum(node.right);
	}

	//public delete, finds the value of deleting node and stores it, then returns
	public Object delete(Comparable key) {
		//Save value of deleted node
		saveVal = search(key).value;

		delete(root, key);
		
		return saveVal;
	}

	
	//Method to.. delete a node you guessed it!
	private TreeNode delete(TreeNode node, Comparable key) {

		//System.out.println("Trying to delete key: " + key + " searching node: " + node);


		//if node to be deleted is null
		if (node == null) {
			//return null
			return null;
		}

		//save the compare value for quick reference
		int cmp = key.compareTo(node.key);

		//if we found the node to delete
		if (cmp == 0) {
			//CASE 1: Node to be removed has no children (isLeaf)
			if (node.left == null && node.right == null) {

				//System.out.println("deleting: " + node + " with value: " + node.value + " and key: " + key);
				
				//If node.parent not null
				if (node.parent != null) {
					//System.out.println("---->" + key + " node: " + node + " | node.parent: " + node.parent + " | node.parent.left: " + node.parent.left + " | node.parent.right" + node.parent.right);

					//Check if parent left child is not null
					if (node.parent.left != null) {
						//If parent left child is node to delete, set to null
						if ( node.parent.left.key.compareTo(key) == 0 ) {
							//System.out.println("setting node.parent.left = null");
							node.parent.left = null;
						}
					}
					//Else check if parent right child is not null
					else if (node.parent.right != null) {
						//If parent right child is node to delete, set to null
						if ( node.parent.right.key.compareTo(key) == 0 ) {
							//System.out.println("setting node.parent.right = null"); 
							node.parent.right = null;
						}
					}
				}
				
				//Set node to null
				node = null;

				//decrement count
				count--;

				return null;
			}
			//Go left
			if (node.right == null) {
				//System.out.println("Returning (left) " + node.left);
				return node.left;
			}

			//Go right
			if (node.left == null) {
				//System.out.println("Returning (right) " + node.right);
				return node.right;
			}
			//System.out.println("Node has two children: " + node + " node.left: " + node.left + " | node.right: " + node.right);
			
			//CASE2: If node to be deleted has two children
			newNode = (TreeNode) minimum(node.right);
			//Copy key/value from tempNode to node
			node.key = newNode.key;
			node.value = newNode.value;

			//decrement count
			count--;

			//Now delete newNode from Node's right subtree and return
			node.right = delete(node.right, newNode.key);
			
			return node;


		//Else transverse left subtree
		} else if (cmp < 0) {
			//Send node.left
			//System.out.println("Returning (cmp<0) " + node.left);
			node.left = delete(node.left, key);
			return node;
		//Else transverse right subtree
		} else {
			//Send node.right
			//System.out.println("Returning (cmp>0) " + node.right);
			node.right = delete(node.right, key);
			return node;
		}
		
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

	//Initialize method sets root to null, and sets max size
	public void initialize(int maxSize) {
		root = null;
		tempNode = null;
		count = 0;
	}

	// Calls private method getCurrentSize with root
	public int getCurrentSize() {
		return getCurrentSize(root);
	}

	// returns current root tree size
	private int getCurrentSize(TreeNode node) {
		if (node == null) 
			return 0;
		else 
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

	//Print inOrder
	public void inOrder(TreeNode node) {

		if(node!=null){
			inOrder(node.left);
			System.out.print(" [" + node.key + ", " + node.value + "] ");
			inOrder(node.right);
		}
	
	}

	public static void main (String args[]) {

		BinarySearchTree BST = new BinarySearchTree();

		BST.initialize(10);

		
		System.out.println("Inserting: " + BST.insert(50, 1));
		System.out.println("Inserting: " + BST.insert(25, 2));
		System.out.println("Inserting: " + BST.insert(75, 3));
		System.out.println("Inserting: " + BST.insert(40, 4));
		System.out.println("Inserting: " + BST.insert(90, 5));
		System.out.println("Inserting: " + BST.insert(60, 6));
		System.out.println("Inserting: " + BST.insert(10, 7));

		System.out.println("Total Nodes: " + BST.getCurrentSize());

		BST.inOrder(root);
		System.out.println();

		System.out.println("40 successor: " + BST.successor(40));
		System.out.println("40 predecessor: " + BST.predecessor(40));

		
		System.out.println("Maximum: " + BST.maximum());
		System.out.println("Minimum: " + BST.minimum());
		System.out.println("Searching for 75: " + BST.search(75));
		System.out.println("Searching for 100: " + BST.search(100));

		System.out.println("--> deleting 10: " + BST.delete(10));
		BST.inOrder(root);
		System.out.println();

		System.out.println("Total Nodes: " + BST.getCurrentSize());

		System.out.println("--> deleting 25: " + BST.delete(25));
		BST.inOrder(root);
		System.out.println();

		System.out.println("Total Nodes: " + BST.getCurrentSize());

		System.out.println("--> deleting 75: " + BST.delete(75));
		BST.inOrder(root);
		System.out.println();

		System.out.println("Total Nodes: " + BST.getCurrentSize());

		System.out.println("Searching for 10: " + BST.search(10));
		System.out.println("Searching for 25: " + BST.search(25));
		System.out.println("Searching for 75: " + BST.search(75));

		System.out.println("Maximum: " + BST.maximum());

		BST.inOrder(root);
		System.out.println();

		Enumeration enumVal = BST.getValues();
		System.out.println("Value Enumerator: ");

		while (enumVal.hasMoreElements()) {
			System.out.print(enumVal.nextElement() + ", ");
		}
		System.out.println();

		Enumeration enumKey = BST.getKeys();
		System.out.println("Key Enumerator: ");

		while (enumKey.hasMoreElements()) {
			System.out.print(enumKey.nextElement() + ", ");
		}
		System.out.println();

		System.out.println("Total Nodes: " + BST.getCurrentSize());
		

		//BST.inOrder(root);
		
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
