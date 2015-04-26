/**
 *
 * @author Lanqin Yuan
 * 1196194 
 * @author Jeff Mo
 * 1196144
 */
public interface IPacker
{

	public void finalisePackedBits();

	public void packBytes(int phraseNumber, byte byteSeq);

}
