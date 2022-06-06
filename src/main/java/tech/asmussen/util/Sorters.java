package tech.asmussen.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * A utility class for sorting a list of numbers.
 */
public class Sorters {
	
	/**
	 * Sorts the specified array of numbers using the quick sort algorithm.
	 *
	 * @param numbers The array of numbers to sort.
	 */
	public static void quickSort(int[] numbers) {
		
		quickSort(numbers, 0, numbers.length - 1);
	}
	
	/**
	 * Sorts the specified array of numbers using the quick sort algorithm.
	 *
	 * @param numbers   The array of numbers to sort.
	 * @param lowIndex  The low index of the array.
	 * @param highIndex The high index of the array.
	 */
	private static void quickSort(int[] numbers, int lowIndex, int highIndex) {
		
		if (lowIndex >= highIndex) return;
		
		int pivotIndex = new Random().nextInt(highIndex - lowIndex) + lowIndex;
		int pivot = numbers[pivotIndex];
		
		quickSwap(numbers, pivotIndex, highIndex);
		
		int leftPointer = quickPartition(numbers, lowIndex, highIndex, pivot);
		
		quickSort(numbers, lowIndex, leftPointer - 1);
		quickSort(numbers, leftPointer + 1, highIndex);
	}
	
	/**
	 * Partitions the array around the pivot.
	 *
	 * @param array     The array to partition.
	 * @param lowIndex  The low index of the array.
	 * @param highIndex The high index of the array.
	 * @param pivot     The pivot to partition around.
	 * @return The index of the pivot.
	 */
	private static int quickPartition(int[] array, int lowIndex, int highIndex, int pivot) {
		
		int leftPointer = lowIndex;
		int rightPointer = highIndex - 1;
		
		while (leftPointer < rightPointer) {
			
			while (array[leftPointer] <= pivot && leftPointer < rightPointer) {
				
				leftPointer++;
			}
			
			while (array[rightPointer] >= pivot && leftPointer < rightPointer) {
				
				rightPointer--;
			}
			
			quickSwap(array, leftPointer, rightPointer);
		}
		
		if (array[leftPointer] > array[highIndex]) {
			
			quickSwap(array, leftPointer, highIndex);
			
		} else {
			
			leftPointer = highIndex;
		}
		
		return leftPointer;
	}
	
	/**
	 * Swaps two elements in an array.
	 *
	 * @param numbers The array of numbers.
	 * @param index1  The index of the first element.
	 * @param index2  The index of the second element.
	 */
	private static void quickSwap(int[] numbers, int index1, int index2) {
		
		int temp = numbers[index1];
		
		numbers[index1] = numbers[index2];
		numbers[index2] = temp;
	}
	
	/**
	 * Sorts the specified array of numbers using the merge sort algorithm.
	 *
	 * @param numbers The array of numbers to sort.
	 */
	public static void mergeSort(int[] numbers) {
		
		int length = numbers.length;
		
		if (length < 2) return;
		
		int middleIndex = length / 2;
		
		int[] leftHalf = new int[middleIndex];
		int[] rightHalf = new int[length - middleIndex];
		
		System.arraycopy(numbers, 0, leftHalf, 0, middleIndex);
		
		if (length - middleIndex >= 0) System.arraycopy(numbers, middleIndex, rightHalf, 0, length - middleIndex);
		
		mergeSort(leftHalf);
		mergeSort(rightHalf);
		
		// Merge halves
		merge(numbers, leftHalf, rightHalf);
	}
	
	/**
	 * Merge two halves of an array.
	 *
	 * @param numbers   The array to merge.
	 * @param leftHalf  The left half of the array.
	 * @param rightHalf The right half of the array.
	 */
	private static void merge(int[] numbers, int[] leftHalf, int[] rightHalf) {
		
		int leftLength = leftHalf.length;
		int rightLength = rightHalf.length;
		
		int i = 0, j = 0, k = 0;
		
		while (i < leftLength && j < rightLength) {
			
			if (leftHalf[i] <= rightHalf[j]) {
				
				numbers[k] = leftHalf[i];
				
				i++;
				
			} else {
				
				numbers[k] = rightHalf[j];
				
				j++;
			}
			
			k++;
		}
		
		while (i < leftLength) {
			
			numbers[k] = leftHalf[i];
			
			i++;
			k++;
		}
		
		while (j < rightLength) {
			
			numbers[k] = rightHalf[j];
			
			j++;
			k++;
		}
	}
	
	/**
	 * Sorts the given array using the insertion sort algorithm.
	 *
	 * @param numbers The array to sort.
	 */
	public static void insertionSort(int[] numbers) {
		
		for (int i = 1; i < numbers.length; i++) {
			
			int currentValue = numbers[i];
			
			int j = i - 1;
			
			while (j >= 0 && numbers[j] > currentValue) {
				
				numbers[j + 1] = numbers[j];
				
				j--;
			}
			
			numbers[j + 1] = currentValue;
		}
	}
	
	/**
	 * Sorts the given array using the bubble sort algorithm.
	 *
	 * @param numbers The array to sort.
	 */
	public static void bubbleSort(int[] numbers) {
		
		boolean hasSwapped;
		
		do {
			
			hasSwapped = false;
			
			for (int i = 0; i < numbers.length - 1; i++) {
				
				if (numbers[i] > numbers[i + 1]) {
					
					hasSwapped = true;
					
					int temp = numbers[i];
					
					numbers[i] = numbers[i + 1];
					numbers[i + 1] = temp;
				}
			}
			
		} while (hasSwapped);
	}
	
	/**
	 * Sorts the given array using the bogo sort algorithm.
	 *
	 * @param numbers The array to sort.
	 */
	public static void bogoSort(List<Integer> numbers) {
		
		while (!isBogoSorted(numbers)) {
			
			Collections.shuffle(numbers);
		}
	}
	
	/**
	 * Checks if the list is sorted.
	 *
	 * @param numbers The list to check.
	 * @return True if the list is sorted, false otherwise.
	 */
	private static boolean isBogoSorted(List<Integer> numbers) {
		
		if (numbers == null) return true;
		
		int length = numbers.size();
		
		if (length <= 1) return true;
		
		for (int i = 0; i < length - 1; i++) {
			
			if (numbers.get(i) > numbers.get(i + 1)) return false;
		}
		
		return true;
	}
	
	/**
	 * Search a sorted array for a given value.
	 *
	 * @param numbers The sorted array.
	 * @param target  The value to search for.
	 * @return The index of the target value, or -1 if it is not found.
	 */
	public static int binarySearch(int[] numbers, int target) {
		
		return Arrays.binarySearch(numbers, target);
	}
	
	/**
	 * Get the largest number in an array.
	 *
	 * @param numbers The array of numbers.
	 * @return The largest number in the array.
	 */
	public static int getLargest(int[] numbers) {
		
		return Arrays.stream(numbers).max().isPresent() ? Arrays.stream(numbers).max().getAsInt() : 0;
	}
	
	/**
	 * Get the smallest number in an array.
	 *
	 * @param numbers The array of numbers.
	 * @return The smallest number in the array.
	 */
	public static int getSmallest(int[] numbers) {
		
		return Arrays.stream(numbers).min().isPresent() ? Arrays.stream(numbers).min().getAsInt() : 0;
	}
	
	/**
	 * Get the median of an array of numbers.
	 *
	 * @param numbers The array of numbers.
	 * @return The median of the array.
	 */
	public static int getMedian(int[] numbers) {
		
		return Arrays.stream(numbers).sorted().skip(numbers.length / 2).findFirst().isPresent() ? Arrays.stream(numbers).sorted().skip(numbers.length / 2).findFirst().getAsInt() : 0;
	}
	
	/**
	 * Get the average of the numbers in the array
	 *
	 * @param numbers The array of numbers.
	 * @return The average of the numbers in the array.
	 */
	public double getAverage(int[] numbers) {
		
		return Arrays.stream(numbers).average().isPresent() ? Arrays.stream(numbers).average().getAsDouble() : 0;
	}
}
