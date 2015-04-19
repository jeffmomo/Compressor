package com.company;

/**
 * Created by Jeff on 7/04/2015.
 */
public interface ICharFinder<T>
{

	//public ICharFinder find(T toFind);
	public ICharFinder<T> add(int seqNum, T item);
	public ICharFinder<T> addBelow(ICharFinder<T> list);

	public ICharFinder<T> getBelow();
	public int getSequenceNumber();

}
