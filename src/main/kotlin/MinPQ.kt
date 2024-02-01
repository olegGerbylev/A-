import java.util.Stack
import kotlin.math.min

class MinPQ<T : Comparable<T>>: Iterable<Path> {
    private val elements = mutableListOf<T>()

    fun isEmpty(): Boolean = elements.isEmpty()

    fun size(): Int = elements.size

    fun enqueue(element: T) {
        elements.add(element)
        bubbleUp(elements.size - 1)
    }

    fun dequeue(): T? {
        if (isEmpty()) {
            return null
        }

        swap(0, elements.size - 1)
        val removed = elements.removeAt(elements.size - 1)
        bubbleDown(0)
        return removed
    }

    private fun bubbleUp(index: Int) {
        var currentIndex = index
        var parentIndex = (currentIndex - 1) / 2

        while (currentIndex > 0 && elements[currentIndex] < elements[parentIndex]) {
            swap(currentIndex, parentIndex)
            currentIndex = parentIndex
            parentIndex = (currentIndex - 1) / 2
        }
    }

    private fun bubbleDown(index: Int) {
        var currentIndex = index
        var leftChildIndex = 2 * currentIndex + 1
        var rightChildIndex = 2 * currentIndex + 2
        var smallestChildIndex = currentIndex

        while (leftChildIndex < elements.size) {
            if (elements[leftChildIndex] < elements[smallestChildIndex]) {
                smallestChildIndex = leftChildIndex
            }

            if (rightChildIndex < elements.size && elements[rightChildIndex] < elements[smallestChildIndex]) {
                smallestChildIndex = rightChildIndex
            }

            if (smallestChildIndex == currentIndex) {
                break
            }

            swap(currentIndex, smallestChildIndex)
            currentIndex = smallestChildIndex
            leftChildIndex = 2 * currentIndex + 1
            rightChildIndex = 2 * currentIndex + 2
        }
    }

    private fun swap(i: Int, j: Int) {
        val temp = elements[i]
        elements[i] = elements[j]
        elements[j] = temp
    }

    override fun iterator(): Iterator<Path> {
        val stack = Stack<Path>()
        for (element in elements){
            if(element is Path){
                stack.push(element)
            }
        }
        return stack.iterator()
    }
}

fun main() {
    val priorityQueue = MinPQ<Int>()

    priorityQueue.enqueue(3)
    priorityQueue.enqueue(1)
    priorityQueue.enqueue(4)
    priorityQueue.enqueue(1)

    while (!priorityQueue.isEmpty()) {
        println(priorityQueue.dequeue())
    }
    val minPQ = MinPQ<Path>()
    minPQ.enqueue(Path(0,0,7,3))
    minPQ.enqueue(Path(0,0,10,0))
    minPQ.enqueue(Path(0,0,8,2))
    minPQ.enqueue(Path(0,0,9,1))
    minPQ.enqueue(Path(1,0,9,1))
    minPQ.enqueue(Path(2,0,8,2))
    minPQ.enqueue(Path(3,0,7,3))
    while (!minPQ.isEmpty()) {
        println(minPQ.dequeue())
    }
}
