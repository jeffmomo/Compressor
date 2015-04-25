/**
 * Created by Jeff on 24/04/2015.
 */
public interface IPacker
{

	public void finalisePackedBits();

	public void packBytes(int phraseNumber, byte byteSeq);

}
