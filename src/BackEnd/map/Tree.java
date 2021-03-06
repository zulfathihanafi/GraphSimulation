package BackEnd.map;

import java.util.ArrayList;
import java.util.List;

public class Tree {
    private static MapGraph G;
    private static int C;
    private static List<List<Integer>> tree;


    public static void run(MapGraph G, int C) {
        /*
    PSEUDOCODE :
        - Find all possible combination for the number ID , example : 1 2 3
        will become  1, 2, 3, 1 2, 1 3 , 1 2 3

        - Find all permutation :
        1,2,3,1 2, 2 1, 1 3, 3 1....etc


    */
        long start = System.nanoTime();
        generator(G,C);
        long end = System.nanoTime();
        System.out.println("Execution time: " + (double) (end - start) * Math.pow(10, -6) + "ms\n");
    }

    public static void generator(MapGraph G, int C){

        Tree.G = G;
        Tree.C = C;
        System.out.println("---Generating tree---\n");
        tree = new ArrayList<>();


        int[] vertexID = new int[G.size()-1];
        for(int i=1;i<G.size();i++){
            vertexID[i-1] = i;  // All ID in the tree
        }

        //this will compute the possible combination
        Integer[][] answer ;
        for(int i=1;i<=G.size();i++){
            answer = nCrArray(vertexID,i);
            for (Integer[] integers : answer) {
                tree.addAll(permute(integers)); // the generated arrays of combination will undergoes permutation process
            }
        }
        //permutation
        tree = removeDuplicates(tree); //remove any duplicate list, and add 0 to the first and last node
        System.out.println("\nPossible path : ");
        tree.forEach(System.out::println);
    }

    public static List<List<Integer>> getTree() {
        return tree;
    }

    //permutation of all integer(ID)
    public static List<List<Integer>> permute(Integer[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        Permutation(0, nums, result);
        return result;
    }
    private static void Permutation(int i, Integer[] nums, List<List<Integer>> result) {
        if (i == nums.length - 1) {
            List<Integer> list = new ArrayList<>();
            int currentCapacity = 0;
            // will store the ID
            for (int ID : nums) {
                currentCapacity += G.getVertex(ID).capacity;
                if (currentCapacity <= C) {
                    list.add(ID);
                } else {
                    currentCapacity -= G.getVertex(ID).capacity;
                }

            }
            result.add(list);

        } else {
            for (int j = i, l = nums.length; j < l; j++) {
                int temp = nums[j];
                nums[j] = nums[i];
                nums[i] = temp;
                Permutation(i + 1, nums, result);
                temp = nums[j];
                nums[j] = nums[i];
                nums[i] = temp;
            }
        }
    }
    public static List<List<Integer>> removeDuplicates(List<List<Integer>> list) {

        // Create a new ArrayList
        List<List<Integer>> newList = new ArrayList<>();

        // Traverse through the first list
        for (List<Integer> element : list) {
            element.add(0,0);
            element.add(0); // add 0 at first and last node, to set it as a route/path
            // If this element is not present in newList
            // then add it
            if (!newList.contains(element)) {

                newList.add(element);
            }
        }

        // return the new list
        return newList;
    }

    //combination
    public static long factorial(int num) {
        long fact = 1;
        for (int i = 1; i <= num; i++)
            fact = fact*i;
        return fact;
    }
    // Get the nCr
    public static int nCr(int n, int r) {

        return (int) (factorial(n)/(factorial(n-r)*factorial(r)));
    }
    // Get the combinations of the array
    public static Integer[][] nCrArray(int[] array, int r) {
        int n = array.length;
        // Choose r from the length of the array
        int ncr = nCr(n,r); //problem here
        Integer[][] result = new Integer[ncr][r];
        int result_index = 0;
        // BASE CASE
        if (r == 1) {
            // Each value in the array is a result
           for (; result_index < array.length; result_index++)
                result[result_index][0] = array[result_index];
        }
        // All other cases
        else {
            // Iterate through each of the starting values
            for (int i = 0; i < array.length-r+1; i++) {
                // Create the sub-array
                int[] recursivearray = new int[n-i-1];
                // Copy into the sub-array all values after the active value
                System.arraycopy(array, i+1, recursivearray, 0, n-i-1);
                // Calculate the sub-combinations (recurse)
                Integer[][] subarrays = nCrArray(recursivearray, r-1);
                // Create the results for this active value
                for (Integer[] subarray : subarrays) {
                    // Augment the active value and sub-combinations
                    result[result_index][0] = array[i];
                    System.arraycopy(subarray, 0,
                            result[result_index], 1,
                            subarray.length);
                    result_index++;
                }
            }
        }
        return result;
    }
}
