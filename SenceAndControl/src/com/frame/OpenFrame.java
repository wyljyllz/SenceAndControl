package com.frame;

import com.interfaces.DataExtract;
import com.utils.FormatConverter;

/**
 * Description:开锁帧，功能号为3，长度为12字节
 */
public class OpenFrame extends Frame implements DataExtract {
    private byte[] data;

    public OpenFrame(int sequence, int deviceAddress, byte[] data){
        super(12, sequence, deviceAddress, Frame.OPEN);
        this.data = data;
        CRC = FormatConverter.hexStringToBytes(Frame.getCRC(FormatConverter.hexStringToBytes(
                FormatConverter.byteToHexStr(length) + FormatConverter.byteToHexStr(this.sequence) +
                FormatConverter.byteToHexStr(this.deviceAddress) + FormatConverter.byteToHexStr(functionID) +
                FormatConverter.byteArrayToHexStr(data))));
    }

    @Override
    public byte[] getBytes(){
        return getData_Bytes();
    }

    @Override
    public String getData_Hex() {
        System.out.println("帧头："+FormatConverter.byteArrayToHexStr(Frame.HEADER)+
                "帧长："+FormatConverter.byteToHexStr(length)+
                "帧号："+FormatConverter.byteToHexStr(sequence)+
                "设备地址："+FormatConverter.byteToHexStr(deviceAddress)+
                "功能号："+FormatConverter.byteToHexStr(functionID)+
                "数据："+FormatConverter.byteArrayToHexStr(data)+
                "CRC："+FormatConverter.byteArrayToHexStr(CRC)+
                "帧尾："+FormatConverter.byteArrayToHexStr(Frame.FOOTER));
        return FormatConverter.byteArrayToHexStr(Frame.HEADER) + FormatConverter.byteToHexStr(length) +
                FormatConverter.byteToHexStr(sequence) + FormatConverter.byteToHexStr(deviceAddress) +
                FormatConverter.byteToHexStr(functionID) + FormatConverter.byteArrayToHexStr(data) +
                FormatConverter.byteArrayToHexStr(CRC) + FormatConverter.byteArrayToHexStr(Frame.FOOTER);
    }

    @Override
    public byte[] getData_Bytes() {
        return FormatConverter.hexStringToBytes(getData_Hex());
    }
}
