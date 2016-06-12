package memorySimulator;

import java.util.*;

/*
 * 1 = memory allocate
 * 		1- First fit
 * 		2- Best fit
 * 		3- Worst Fit
 * 2 = memory deallocate
 * 3 = Defragmentation
 */

class Node {
	public int size;
	public int address;
	public boolean free;
	Node(int size, int address, boolean free) {
		this.size = size;
		this.address = address;
		this.free = free;
	}
}

public class Simulator {
	public static int n;
	public static List<Node> memory;
	public static int allocate(int m, int type) {
		if (type == 1) {
			for (int i = 0; i < memory.size(); ++i) {
				// uses the first free memory that fits m.
				if (memory.get(i).size >= m && memory.get(i).free) {
					int newAddress = memory.get(i).address + m;
					
					Node temp = new Node(memory.get(i).size - m, newAddress, true);
					if (temp.size != 0) memory.add(i + 1, temp); //to add the extra free memory
					
					memory.get(i).size = m;
					memory.get(i).free = false;
					return memory.get(i).address;
				}
			}
		} else if (type == 2) {
			int ind = -1;
			int diff = Integer.MAX_VALUE; // best fit = minimum difference in size.
			
			for (int i = 0; i < memory.size(); ++i)
				if (memory.get(i).size >= m && memory.get(i).size - m < diff && memory.get(i).free) {
					//find the best free memory to that fits m.
					diff = memory.get(i).size - m;
					ind = i;
				}
			
			if (ind == -1)
				return ind;
			
			Node temp = new Node(memory.get(ind).size - m, memory.get(ind).address + m, true);
			if (temp.size != 0) memory.add(ind + 1, temp); // to add the extra free memory
			
			memory.get(ind).size = m;
			memory.get(ind).free = false;
			return memory.get(ind).address;
		} else if (type == 3) {
			int ind = -1;
			int maxi = Integer.MIN_VALUE;
			
			for (int i = 0; i < memory.size(); ++i) {
				// to find the biggest free memory.
				if (memory.get(i).size >= m && memory.get(i).size > maxi && memory.get(i).free) {
					maxi = memory.get(i).size;
					ind = i;
				}
			}
			
			if (ind == -1)
				return ind;
			
			Node temp = new Node(memory.get(ind).size - m, memory.get(ind).address + m, true);
			if (temp.size != 0) memory.add(ind + 1, temp); // to add the extra free memory
			
			memory.get(ind).size = m;
			memory.get(ind).free = false;
			return memory.get(ind).address;
		}
		
		return -1;
	}
	public static boolean Deallocate(int s) {
		for (int i = 0; i < memory.size(); ++i)
			if (memory.get(i).address == s && !memory.get(i).free) {
				// if a memory with address equal to s and the memory is not free make it free.
				memory.get(i).free = true;
				return true;
			}
		
		return false;
	}
	public static void Defragment() {
		int curaddress = 0;
		Node free = new Node(0, 0, true);
		
		for (int i = 0; i < memory.size(); ++i) {
			if (memory.get(i).free) {
				// if the memory is free then add its size to the free memory and remove it from the memory.
				free.size += memory.get(i).size;
				
				memory.remove(i);
				--i;
			} else {
				// if the memory is not free then update its address and get the next address.
				memory.get(i).address = curaddress;
				curaddress += memory.get(i).size;
			}
		}
		
		// sets the address of the free memory.
		free.address = curaddress;
		
		// adds the free memory at the end of the memory.
		memory.add(free);
	}
	public static void main(String[] args) {
		Scanner scn = new Scanner(System.in);
		
		
		n = scn.nextInt();
		memory = new ArrayList<Node>();
		
		int sum = 0;	// it is the address for the next memory.
		for (int i = 0; i < n; ++i) {
			int x;
			x = scn.nextInt();	// to get the size of each memory block.
			
			memory.add(new Node(x, sum, true));
			
			sum += x;	// the address of this memory = address of previous memory + size of previous memory.
		}
		
		
		// Input output related stuff -_-
		int type = 5;
		while (true) {
			type = scn.nextInt();
			if (type == 1) {
				int m = scn.nextInt();	// get the process needed memory.
				type = scn.nextInt();	// get what type of allocation you want to do.
				
				System.out.println(allocate(m, type));
			} else if (type == 2) {
				int address = scn.nextInt();	// get the address of the memory you want to free.
				System.out.println(Deallocate(address));
			} else if (type == 3) {
				Defragment();
				
				// prints the elements in the memory {address, size, state}.
				for (int i = 0; i < memory.size(); ++i)
					System.out.println(memory.get(i).address + " " + memory.get(i).size + " " + memory.get(i).free);
			} else if(type == 4) {
				// just for printing to see the memory {address, size, state}.
				for (int i = 0; i < memory.size(); ++i)
					System.out.println(memory.get(i).address + " " + memory.get(i).size + " " + memory.get(i).free);
			} else {
				break;
			}
		}
		
		scn.close();
	}

}
