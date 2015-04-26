/**
 *
 * Big endian byte and int conversion
 *
 * @author Lanqin Yuan
 * 1196194 
 * @author Jeff Mo
 * 1196144
 */
public class BytesUtil 
{
    
    public static byte[] intToBytes(int integer, int bufferSize)
    {
        byte[] buffer = new byte[bufferSize];
        for(int i = 0;i<bufferSize;i++)
        {
            buffer[i] = (byte) (integer >>> 8*(bufferSize - 1 -i));
        }
        return buffer;
    }
    
    
    public static int bytesToInt(byte[] buffer,int offset,int length)
    {
        int returnInt = 0;
        
        for(int i = length-1;i>=0;i--)
        {
            returnInt += (int)((buffer[i+offset] & 0xFF )<< ((length- 1 - i) * 8));
        }
        return returnInt;
    }
    
    public static void printBytes(byte[] buffer)
    {
        for (byte b : buffer) 
        {
            System.out.print(Integer.toBinaryString(b & 255 | 256).substring(1) + " ");
        }
        System.out.print("\n");
    }
    // prints int bits
    public static void printIntBits(int num)
    {
         int mask = 1 << 31;
         for(int i=1; i<=32; i++) 
         {
            if( (mask & num) != 0 )
                System.out.print(1);
            else
                System.out.print(0);

            if( (i % 4) == 0 )
                System.out.print(" ");

            mask = mask >>> 1;
        }
         System.out.println();
    }
    // returns number of bits needed to store bits
    public static int getBitsNeeded(int num)
    {
        int bitsNeeded = 0;
        while(num >= 1)
        {
            num = num >> 1;
            bitsNeeded++;
        }
        return bitsNeeded;
    }
    // returns number of bytes needed to store bits
    public static int getBytesNeeded(int bits)
    {
        return (bits + 1) / 8;
    }
    
    
}
