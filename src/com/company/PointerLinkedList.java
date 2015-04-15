package com.company;

import java.util.LinkedList;

/**
 * Created by Jeff on 7/04/2015.
 */
public class PointerLinkedList<T>
{
	private PointerLinkedList<T> _next;
	private PointerLinkedList<T> _below;
	private int _sequenceNumber = -1;
	private T _value;

	public PointerLinkedList(int seqNum, T item)
	{
		_sequenceNumber = seqNum;
		_value = item;
	}
	public PointerLinkedList()
	{
		_sequenceNumber = 0;
	}

	public PointerLinkedList add(int seqNum, T item)
	{
		if(_value == null)
		{
			_sequenceNumber = seqNum;
			_value = item;
			return this;
		}

		PointerLinkedList nextItem = this;
		while(nextItem._next != null)
			nextItem = nextItem._next;

		nextItem._next = new PointerLinkedList<T>(seqNum, item);
		return nextItem._next;
	}

	public PointerLinkedList find(T item)
	{

		PointerLinkedList nextItem = this;
		while(nextItem != null)
		{
			if(nextItem._value == item)
			{
				swap(this, nextItem);
				return this;
			}
			nextItem = nextItem._next;
		}


		return null;
	}


	public PointerLinkedList addBelow(PointerLinkedList<T> list)
	{
		return (_below = list);
	}

	public PointerLinkedList<T> getBelow()
	{
		return _below;
	}

	public int getSequenceNumber()
	{
		return _sequenceNumber;
	}

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
