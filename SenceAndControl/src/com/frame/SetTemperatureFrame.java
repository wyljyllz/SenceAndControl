package com.frame;

import com.interfaces.DataExtract;
import com.utils.FormatConverter;

/**
 * Description:设置温度帧，功能号为4，长度为11字节
 */
public class SetTemperatureFrame extends Frame implements DataExtract {
    private byte data;

    public SetTemperatureFrame(int sequence, int deviceAddress, byte data){
        super(11, sequence, deviceAddress, Frame.SET_TEMPERATURE);
        this.data = data;
        CRC = FormatConverter.hexStringToBytes(Frame.getCRC(FormatConverter.hexStringToBytes(
                FormatConverter.byteToHexStr(length) + FormatConverter.byteToHexStr(this.sequence) +
                FormatConverter.byteToHexStr(this.deviceAddress) + FormatConverter.byteToHexStr(functionID) +
                FormatConverter.byteToHexStr(data))));
    }

    @Override
    public byte[] getBytes(){
        return getData_Bytes();
    }

    @Override
    public String getData_Hex() {
        return FormatConverter.byteArrayToHexStr(Frame.HEADER) + FormatConverter.byteToHexStr(length) +
                FormatConverter.byteToHexStr(sequence) + FormatConverter.byteToHexStr(deviceAddress) +
                FormatConverter.byteToHexStr(functionID) + FormatConverter.byteToHexStr(data) +
                FormatConverter.byteArrayToHexStr(CRC) + FormatConverter.byteArrayToHexStr(Frame.FOOTER);
    }

    @Override
    public byte[] getData_Bytes() {
        return FormatConverter.hexStringToBytes(getData_Hex());
    }
}
