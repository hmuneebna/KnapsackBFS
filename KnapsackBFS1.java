//Husam Muneeb
//CS 3310
//Final Project
//May 8,2020
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.PriorityQueue;
import java.util.Scanner;

//Class for Node structure

class Node implements Comparable<Node>
{
	int level; //level of node
	int bound; //bound of node
	int profit; //profit of node
	float weight; //weight of node

	@Override
	//Comparison
	public int compareTo(Node o) 
	{
		int cmp = o.profit - profit;
		if (cmp == 0) 
		{
			return (int) (weight - o.weight);
		}
		return cmp;
	}
	
	@Override
	//output toString()
	public String toString() {
		return "( " + profit + "," + weight + ","  + bound + ")";
	}
};

class Item {
	float weight;
	int profit_item;
	
	public Item(float weight, int profit_item) {
		this.weight = weight;
		this.profit_item = profit_item;
	}
}


public class KnapsackBFS1 {
	
//Comparing the inputted items with their values
	public static boolean cmp(Item a, Item b) {
		double r1 = (double) a.profit_item / a.weight;
		double r2 = (double) b.profit_item / b.weight;
		return r1 > r2;
	}
	
	//This method returns bound of profit in subtree rooted with u
	public static int bound(Node u, int n, int W, Item arr[]) {
		//if weight is more than capacity, return 0 as bound
		if (u.weight >= W) 
		{
			return 0;
		}
		//initialize bound on profit by current profit
		int profit_bound = u.profit;
		
		//start from index 1 to current index
		int j = u.level + 1;
		float totweight = u.weight;
		//Check index condition and Knapsack Capacity
		while ((j < n) && (totweight + arr[j].weight <= W)) {
			totweight += arr[j].weight;
			profit_bound += arr[j].profit_item;
			j++;
		}
		//if k is not n, include last item partially for profit
		if (j < n) 
		{
			profit_bound += (W - totweight) * arr[j].profit_item / arr[j].weight;

		}
		return profit_bound;
	}
	
	//Knapsack method, returns max profit with capacity W

	public static int knapsack(int W, Item arr[], int n) {
		PriorityQueue<Node> PQ = new PriorityQueue<>(); //Initialize PQ to 0
		Node u = new Node(); //Initialize new nodes. 
		Node v = new Node();

		u.level = -1; //starting node
		PQ.add(u);
		//Keep saving maxprofit, compute profit of all children
		int maxprofit = 0; 
		while (!PQ.isEmpty()) {
			
			//print the PQ 
			System.out.print("Priority Queue: ");
			for(Node l : PQ) {
				System.out.print(l + " " );
			}
			System.out.println();
			
			//Pop the element 
			u = PQ.remove();
			
			//print the visited node
			System.out.printf("Visited Node %d,  profit = %d bound = %d, maxprofit = %d\n", 
					u.level, u.profit, u.bound, maxprofit);
			
			//If its starting node, assign it to level 0
			if (u.level == -1) 
			{
				v.level = 0;
			}
			// if theres nothing on the next level continue
			if (u.level == n - 1) 
			{
				continue;
			}
			//compute profit of child nodes 
			v.level = u.level + 1;
			//Take current levels item add current
			//levels weight and profit_item to node u's
			//weight and profit_item
			v.weight = u.weight + arr[v.level].weight;
			v.profit = u.profit + arr[v.level].profit_item;
			
			if (v.weight <= W && v.profit > maxprofit) {
				maxprofit = v.profit;
			}
			
			v.bound = bound(v, n, W, arr);
			if (v.bound > maxprofit) {
				PQ.add(v);
			}
			
		}
		//return max profit
		return maxprofit;

	}

	public static void main(String args[]) throws IOException {
			int[] a = new int[3];

			Scanner kb=new Scanner(System.in); //scanner 
			System.out.println("Enter capacity: "); //capacity of knapsack 
			int W =kb.nextInt();
			
			try {
			
			Scanner sc=new Scanner(new File("input.txt")); //input file 
			String[] parts = sc.nextLine().split(" "); //read from input file 
			
			int[] profits = new int[parts.length]; //length of each line, first line is profits 
			for (int i = 0; i < parts.length; i++) 
			{
				profits[i] = Integer.parseInt(parts[i]);
			}

			parts = sc.nextLine().split(" "); //add the integers in input file to weights, from second line
			int[] weights = new int[parts.length];
			for (int i = 0; i < parts.length; i++) {
				weights[i] = Integer.parseInt(parts[i]);
			}
			
			//If the length is equal, then print the knapsack values
			if(weights.length == profits.length) 
			{
				Item[] items = new Item[weights.length];
				for (int i = 0 ; i < weights.length ; i++) {
					items[i] = new Item(weights[i], profits[i]);
				}
				System.out.println(KnapsackBFS1.knapsack(W, items, items.length));
			}
			else {
				System.out.println("Invalid input file");
			}
			}catch(IOException e)
			{
				System.out.println("File not found");
			}
		}
	}
