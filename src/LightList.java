/**
 *
 * @author Lanqin Yuan
 * 1196194 
 * @author Jeff Mo
 * 1196144
 */

/**
 * Simple doubly-linked list
 */
public class LightList
{
	public byte value;
	public LightList next;
	public LightList prev;

	public LightList(byte val)
	{
		value = val;
	}


	private LightList(byte val, LightList p)
	{
		value = val;
		prev = p;
	}

	public LightList add(byte val)
	{
		next = new LightList(val, this);
		return next;
	}
}
