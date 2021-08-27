package com.utils;

/**
 * Description:将字节流转换为十六进制字符串
 */
public class FormatConverter {
    private static String[] binaryArray = {"0000","0001","0010","0011",
                                            "0100","0101","0110","0111",
                                            "1000","1001","1010","1011",
                                            "1100","1101","1110","1111"};
    private static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String byteArrayToHexStr(byte[] byteArray) {
        if (byteArray == null){
            return null;
        }
        //char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[byteArray.length * 2];
        String result = "";
        for (int j = 0; j < byteArray.length; j++) {
            int v = byteArray[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
            result += hexChars[j * 2];
            result += hexChars[j * 2 + 1];
            result += " ";
        }
        //System.out.printf("bytes转十六进制字符结果：%s\n", result);
        return result;//new String(hexChars);
    }

    public static String byteToHexStr(byte b){
        //char[] hexArray = "0123456789ABCDEF".toCharArray();
        String result = "";
        int v = b & 0xFF;
        result += hexArray[v >>> 4];
        result += hexArray[v & 0x0F];
        return result;
    }

    public static byte[] hexStringToBytes(String str)
    {
        str = str.replaceAll("\\s+", "");
        //System.out.printf("转换之前十六进制字符串：%s\n", str);
        if (str == null) {
            return null;
        }
        if (str.length() == 0) {
            return new byte[0];
        }
        byte[] byteArray = new byte[str.length() / 2];
        for (int i = 0; i < byteArray.length; i++){
            String subStr = str.substring(2 * i, 2 * i + 2);
            byteArray[i] = ((byte)Integer.parseInt(subStr, 16));
        }
        //System.out.printf("转换之后的字节流再转十六进制字符串：%s\n", byteArrayToHexStr(byteArray));
        return byteArray;
    }
    

    public static String byteArrayToString(int num,byte[] buffer)
    {
        System.out.println(buffer);
        int a;
        String msg="";
        for (int i=0;i<num;i++) {
            if (buffer[i] < 0) {
                a = 256 + buffer[i];
            } else {
                a = buffer[i];
            }
            if(a<=15) {
                msg+="0";
            }
            msg+=intToHex(a);
        }
        System.out.println(msg);
        return msg;
    }
    public static String intToHex(int n) {
        StringBuffer s = new StringBuffer();
        String a;
        char []b = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        if(n==0) {
            a="0";
        } else {
            while(n != 0){
                s = s.append(b[n%16]);
                n = n/16;
            }
            a = s.reverse().toString();
        }
        return a;
    }
    public static int HexToInt(String str) {
    	int x = Integer.parseInt(str,16);
    	return x;
    }

    /**
     * @param start 字节数组切片开始，包括
     * @param end 字节数组切片结束，不包括
     * @return 得到的字节数组切片结果
     */
    public static byte[] slice(byte[] initValue, int start, int end){
        byte[] result = new byte[end - start];
        for(int i = 0; i < end-start; i++){
            result[i] = initValue[start+i];
        }
        return result;
    }

    public static byte[] hexStringToAsciiBytes(String hex){
        String hexChars = hex.replaceAll("\\s+","");
        byte[] result = new byte[hexChars.length()];
        String hexString = "0123456789ABCDEF";
        int i = 0;
        for(char str:hexChars.toCharArray()){

            int ascii = str;
            result[i++] = (byte) ascii;
            System.out.println(result[i-1]);
        }
        return result;
    }
}
