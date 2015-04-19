/**
 *
 * Big endian byte and int conversion
 * @author Lanqin Yuan
 * 1196194
 */
public class BytesUtil {
    
    public static byte[] intToBytes(int integer, int bufferSize)
    {
        byte[] buffer = new byte[bufferSize];
        for(int i = 0;i<bufferSize;i++)
        {
            buffer[i] = (byte) (integer >> 8*(bufferSize - 1 -i));
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
}
