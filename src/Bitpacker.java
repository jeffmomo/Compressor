import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author Lanqin Yuan
 * 1196194
 */
public class Bitpacker 
{
    private ByteArrayOutputStream byteOutput;
    private int leftoverBits;
    private int leftoverLength;
    
    public static void main(String[] args) 
    {
        // testing
       /* int dictionarySize = Integer.parseInt(args[0]);
        int parseNumber = Integer.parseInt(args[1]);
        
        
        printIntBits(parseNumber);
        System.out.println("" + getBitsNeeded(dictionarySize));
        System.out.println("" + getBytesNeeded(getBitsNeeded(dictionarySize)));
        byte[] buf = BytesUtil.intToBytes(parseNumber, getBytesNeeded(parseNumber));
        
        //BytesUtil.printBytes(buf);
        System.out.println("" + BytesUtil.bytesToInt(buf, 0, buf.length));
        */
        int dictionarySize = 0;
        Bitpacker b = new Bitpacker();
        b.packBytes(dictionarySize++, 0, (byte) 126);
        b.packBytes(dictionarySize++, 1, (byte) 13);
        b.packBytes(dictionarySize++, 2, (byte) 7);
        b.packBytes(dictionarySize++, 0, (byte) 14);
        b.packBytes(dictionarySize++, 0, (byte) 124);
        b.packBytes(dictionarySize++, 3, (byte) 126);
        b.packBytes(dictionarySize++, 0, (byte) 13);
        b.packBytes(dictionarySize++, 1, (byte) 243);
        b.packBytes(dictionarySize++, 2, (byte) 124);
        b.packBytes(dictionarySize++, 0, (byte) 124);
        
        /*
        b.leftoverBits = 1;
        b.leftoverLength = 1;*/
        //b.packBytes(dictionarySize, parseNumber, (byte)245);
        
        
        
        
        System.out.println("final output");
        byte[] finalBytes = b.returnPackedBits();
        BytesUtil.printBytes(finalBytes);
        
        try 
        {            
            FileOutputStream f = new FileOutputStream("f");
            f.write(finalBytes);
            f.close();
        } catch (Exception e){};
    }

    public Bitpacker() 
    {
        byteOutput = new ByteArrayOutputStream();
        leftoverBits = 0;
        leftoverLength = 0;
    }
    
    public byte[] returnPackedBits()
    {
        
        // shift and write leftover bits into stream
        int l = leftoverBits << (8 - leftoverLength);
        byte[] outBytes = BytesUtil.intToBytes(l,1);
        System.out.println("derp ");
        BytesUtil.printIntBits(l);
        byteOutput.write(outBytes, 0, 1);
        
        // retrun whole output as byte array
        return byteOutput.toByteArray();
    }
    
    // dictonary size determins how many bits to leave for the parse number
    public void packBytes(int dictionarySize,int parseNumber, byte character)
    {
        System.out.println("dictionary size: "+dictionarySize);
        // get number of bits and bytes need for phrase number
        int bitsNeeded = BytesUtil.getBitsNeeded(dictionarySize);
        
        // shifting parse number into correct position
        int p = parseNumber << 8;//(bitsNeeded);
        // remove sign in character
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
        
        // pack both character and parse number with leftover
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
    }
    
}
