package com.xiaoye.myutils.utils;

public class StringToHex {

    public static String convertStringToHex(String str){

        char[] chars = str.toCharArray();

        StringBuffer hex = new StringBuffer();
        for(int i = 0; i < chars.length; i++){
            hex.append(Integer.toHexString((int)chars[i]));
        }

        return hex.toString();
    }

    public static String convertHexToString(String hex){

        StringBuilder sb = new StringBuilder();
        StringBuilder temp = new StringBuilder();

        //49204c6f7665204a617661 split into two characters 49, 20, 4c...
        for( int i=0; i<hex.length()-1; i+=2 ){

            //grab the hex in pairs
            String output = hex.substring(i, (i + 2));
            //convert hex to decimal
            int decimal = Integer.parseInt(output, 16);
            //convert the decimal to character
            sb.append((char)decimal);

            temp.append(decimal);
        }

        return sb.toString();
    }

    public static int CalcCRC16(byte[] pArray,int length)
    {
        int wCRC = 0xFFFF;
        int CRC_Count = length;
        int i;
        int num = 0;
        while (CRC_Count > 0)
        {
            CRC_Count--;
            wCRC = wCRC ^ (0xFF & pArray[num++]);
            for (i = 0; i < 8; i++)
            {
                if ((wCRC & 0x0001) == 1)
                {
                    wCRC = wCRC >> 1 ^ 0xA001;
                }
                else
                {
                    wCRC = wCRC >> 1;
                }
            }
        }
        return wCRC;
    }
}
