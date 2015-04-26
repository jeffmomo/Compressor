/**
 *
 * @author Lanqin Yuan
 * 1196194 
 * @author Jeff Mo
 * 1196144
 */
public interface ICharFinder<T>
{

	//public ICharFinder find(T toFind);
	public ICharFinder<T> add(int seqNum, T item);
	public ICharFinder<T> addBelow(ICharFinder<T> list);

	public ICharFinder<T> getBelow();
	public int getSequenceNumber();

}
