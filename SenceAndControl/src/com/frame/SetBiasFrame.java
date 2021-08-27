package com.frame;

import com.interfaces.DataExtract;
import com.utils.FormatConverter;

/**
 * Description:�����¶ȿ���ƫ��֡�����ܺ�Ϊ6������Ϊ11�ֽ�
 */
public class SetBiasFrame extends Frame implements DataExtract {
    private byte data;

    public SetBiasFrame(int sequence, int deviceAddress, byte data){
        super(11, sequence, deviceAddress, Frame.SET_BIAS);
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
