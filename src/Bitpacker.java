
import java.io.*;


/**
 *
 * @author Lanqin Yuan
 * 1196194
 */
public class BitPacker
{
    private OutputStream byteOutput;
    private int leftoverBits;
    private int leftoverLength;
    private int dictionarySize = 0;
    
  

    public BitPacker(OutputStream outputStream) 
    {
        byteOutput = outputStream;
        leftoverBits = 0;
        leftoverLength = 0;
    }
    
    public void finalisePackedBits()
    {
        
        // shift leftover bit to add padding for last byte
        int l = leftoverBits << (8 - leftoverLength);
        byte[] outByte = BytesUtil.intToBytes(l,1);
        System.out.println("derp ");
        BytesUtil.printIntBits(l);
        try
        {
        // write out last byte
        byteOutput.write(outByte);
        } catch(Exception e){};
    }
    
    // dictonary size determins how many bits to leave for the phrase number
    public void packBytes(int phraseNumber, byte character)
    {
        System.out.println("dictionary size: "+dictionarySize);
        // get number of bits and bytes need for phrase number
        int bitsNeeded = BytesUtil.getBitsNeeded(dictionarySize);
        
        // shifting phrase number into correct position
        int p = phraseNumber << 8;//(bitsNeeded);
        // remove sign in byte sequence
        int c = (((int)character) << (32-8)) >>> (32-8);
        // shifting carryover bits
        int co = leftoverBits << (8 + bitsNeeded);
        
        // debuging
        System.out.println("co " + co);
        BytesUtil.printIntBits(co);
        System.out.println("p " + p);
        BytesUtil.printIntBits(p);
        System.out.println("c " + c);
        BytesUtil.printIntBits(c);
        
        // pack both character and phrase number with leftover
        int packedBytes = co | p | c;
        System.out.println("concat");
        BytesUtil.printIntBits(packedBytes);
        
        // finding how many bits do not form whole bytes
        int remainder = (leftoverLength + bitsNeeded)% 8;
        System.out.println("remainder " + remainder);
        
        // write out whole bytes to out stream
        byte[] outBytes = BytesUtil.intToBytes((packedBytes) >>> remainder, BytesUtil.getBytesNeeded(leftoverLength + bitsNeeded + 8 - remainder));
        
        System.out.println("out bytes");
        BytesUtil.printBytes(outBytes);
        try
        {
            byteOutput.write(outBytes);
        } catch (IOException ex){ ex.printStackTrace(); }
        
        // set leftover buffer
        if( remainder != 0)
        {
            leftoverBits = (((int)packedBytes) << (32 - remainder )) >>> (32-remainder);
        }
        else
        {
            leftoverBits = 0;
        }
        leftoverLength = remainder;
        System.out.println("leftover");
        BytesUtil.printIntBits(leftoverBits);
        dictionarySize++;
    }
    
}
