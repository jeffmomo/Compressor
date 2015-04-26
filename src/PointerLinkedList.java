/**
 *
 * @author Lanqin Yuan
 * 1196194 
 * @author Jeff Mo
 * 1196144
 */


/**
 * A move-to-front list implementation with multiple layers which stores a sequence number and a value
 */
public class PointerLinkedList<T>// implements ICharFinder<T>
{
	private PointerLinkedList<T> _next;
	private PointerLinkedList<T> _below;
	private int _sequenceNumber = -1;
	private T _value;

	// Constructs list with existing params
	public PointerLinkedList(int seqNum, T item)
	{
		_sequenceNumber = seqNum;
		_value = item;
	}
	public PointerLinkedList()
	{
		_sequenceNumber = 0;
	}

	// Adds to the list
	public PointerLinkedList<T> add(int seqNum, T item)
	{
		// Supplements the current node if current node is not complete.
		if(_value == null)
		{
			_sequenceNumber = seqNum;
			_value = item;
			return this;
		}

		// Otherwise adds to the end of the last node and returns the newly constructed node.
		PointerLinkedList<T> nextItem = this;
		while(nextItem._next != null)
			nextItem = nextItem._next;

		nextItem._next = new PointerLinkedList<T>(seqNum, item);
		return nextItem._next;
	}

	// Finds the first occurance of item in the current layer. Performs move-to-front procedures.
	public PointerLinkedList find(T item)
	{

		PointerLinkedList nextItem = this;
		// Looks for value matching the argument. If found then swaps it to front and returns it
		while(nextItem != null)
		{
			if(nextItem._value == item)
			{
				swap(this, nextItem);
				return this;
			}
			nextItem = nextItem._next;
		}

		// Returns null if not found
		return null;
	}


	// Adds a list as a layer below the current node.
	public PointerLinkedList<T> addBelow(PointerLinkedList<T> list)
	{
		return (_below = list);
	}

	// Gets the list below the current node
	public PointerLinkedList<T> getBelow()
	{
		return _below;
	}

	// Gets the sequence number of the current node
	public int getSequenceNumber()
	{
		return _sequenceNumber;
	}

	// Performs swapping of values. Used for MTF procedures
	private void swap(PointerLinkedList<T> l1, PointerLinkedList<T> l2)
	{
		PointerLinkedList<T> below = l1._below;
		int sequenceNumber = l1._sequenceNumber;
		T value = l1._value;

		l1._below = l2._below;
		l1._sequenceNumber = l2._sequenceNumber;
		l1._value = l2._value;

		l2._below = below;
		l2._sequenceNumber = sequenceNumber;
		l2._value = value;
	}

}
