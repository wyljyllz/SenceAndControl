package com.frame;

import com.interfaces.DataExtract;
import com.utils.FormatConverter;

/**
 * Description:设置参数帧，功能号为5，长度为28
 */
public class SetParameterFrame extends Frame implements DataExtract {
    public byte[] data;

    public SetParameterFrame(int sequence, int deviceAddress, byte[] data){
        super(28, sequence, deviceAddress, Frame.SET_PARAMETER);
        this.data = data;
        System.out.println("@@@@@@@@@SetParameterFrame");
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
