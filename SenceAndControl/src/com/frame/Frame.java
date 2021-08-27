package com.frame;

import com.interfaces.FrameExtract;

/**
 * Description:数据帧超类，具有数据帧的共有属性
 */
public class Frame implements FrameExtract {
    public final static byte FIND = (byte) 0x01;
    public final static byte BOOT = (byte) 0x02;
    public final static byte OPEN = (byte) 0x03;
    public final static byte SET_TEMPERATURE = (byte) 0x04;
    public final static byte SET_PARAMETER = (byte) 0x05;
    public final static byte SET_BIAS = (byte) 0x06;
    public final static byte SET_DEVICE_ADDRESS = (byte) 0x07;
    public final static byte UPLOAD_STATE = (byte) 0x08;
    //public final static byte ACK = (byte) 0x09;

    public final static byte[] HEADER = {(byte) 0xFF, (byte) 0xFF};
    public final static byte[] FOOTER = {(byte) 0xFF, (byte) 0xF7};
    protected byte length;
    protected byte sequence;
    protected byte deviceAddress;
    protected byte functionID;
    protected byte[] CRC;

    public Frame(int length, int sequence, int deviceAddress, byte functionID){
        this.length = (byte) length;
        this.sequence = (byte) sequence;
        this.deviceAddress = (byte) deviceAddress;
        this.functionID = functionID;
    }

    @Override
    public int getFrameLength() {
        return length;
    }

    @Override
    public int getFrameSequence() {
        return sequence;
    }

    @Override
    public int getDeviceAddress() {
        return deviceAddress;
    }

    @Override
    public int getFunctionID() {
        return functionID;
    }

    @Override
    public byte[] getCRC() {
        return CRC;
    }

    public byte[] getBytes(){
        return null;
    }

    public static String getCRC(byte[] bytes) {
        // CRC寄存器全为1
        int CRC = 0x0000ffff;
        // 多项式校验值
        int POLYNOMIAL = 0x0000a001;
        int i, j;
        for (i = 0; i < bytes.length; i++) {
            CRC ^= ((int) bytes[i] & 0x000000ff);
            for (j = 0; j < 8; j++) {
                if ((CRC & 0x00000001) != 0) {
                    CRC >>= 1;
                    CRC ^= POLYNOMIAL;
                } else {
                    CRC >>= 1;
                }
            }
        }
        // 结果转换为16进制
        String result = Integer.toHexString(CRC).toUpperCase();
        if (result.length() != 4) {
            StringBuffer sb = new StringBuffer("0000");
            result = sb.replace(4 - result.length(), 4, result).toString();
        }
        //高位在前地位在后
        //return result.substring(2, 4) + " " + result.substring(0, 2);
        // 交换高低位，低位在前高位在后
        return result.substring(2, 4) + " " + result.substring(0, 2);
    }

    public static boolean isCorrect(byte[] frame){
        // CRC寄存器全为1
        int CRC = 0x0000ffff;
        // 多项式校验值
        int POLYNOMIAL = 0x0000a001;
        int i, j;
        for (i = 0; i < frame.length; i++) {
            CRC ^= ((int) frame[i] & 0x000000ff);
            for (j = 0; j < 8; j++) {
                if ((CRC & 0x00000001) != 0) {
                    CRC >>= 1;
                    CRC ^= POLYNOMIAL;
                } else {
                    CRC >>= 1;
                }
            }
        }
        if(CRC == 0) {
            return true;
        } else {
            return false;
        }
    }
}
