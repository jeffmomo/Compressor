
import java.io.*;


/**
 *
 * @author Lanqin Yuan
 * 1196194 
 * @author Jeff Mo
 * 1196144
 */
public class BitPacker implements IPacker
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
        try
        {
            // write out last byte
            byteOutput.write(outByte);
        } catch(Exception e){};
    }
    
    // dictonary size determins how many bits to leave for the phrase number
    public void packBytes(int phraseNumber, byte byteSeq)
    {
        // get number of bits and bytes need for phrase number
        int bitsNeeded = BytesUtil.getBitsNeeded(dictionarySize);

        // shifting carryover bits
        int co = leftoverBits << (bitsNeeded);
        // shifting phrase number into correct position
        int p = phraseNumber;//(bitsNeeded);
        

        
        // pack both character and phrase number with leftover
        int packedBytes = co | p ;
        
        // finding how many bits do not form whole bytes
        int remainder = (leftoverLength + bitsNeeded) % 8;
            
        // write leftover and phrase number. Only whole bytes to out stream
        byte[] outBytes = BytesUtil.intToBytes((packedBytes) >>> remainder, BytesUtil.getBytesNeeded(leftoverLength + bitsNeeded - remainder));
       
        try
        {
            byteOutput.write(outBytes);
        } catch (IOException ex){ ex.printStackTrace(); }
        
        // remove sign in byte sequence
        int b = (int)byteSeq & 0xFF;
        packedBytes = (((packedBytes) << (32 - remainder )) >>> (32-remainder -8)) | b ;
        
        // write leftover bits from phrase number plus the byte sequence.
        outBytes = BytesUtil.intToBytes((packedBytes) >>> remainder, 1);
        try
        {
            byteOutput.write(outBytes);
        } catch (IOException ex){ ex.printStackTrace(); }
        
        
        // set leftover buffer
        if( remainder != 0)
        {
            leftoverBits = ((packedBytes) << (32 - remainder )) >>> (32-remainder);
            
        }
        else
        {
            leftoverBits = 0;
        }
        leftoverLength = remainder;
        
        dictionarySize++;
    }
    
}
