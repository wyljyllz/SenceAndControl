package com.frame;

import com.interfaces.DataExtract;
import com.utils.FormatConverter;

/**
 * Description:����֡�����ܺ�Ϊ3������Ϊ12�ֽ�
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
        System.out.println("֡ͷ��"+FormatConverter.byteArrayToHexStr(Frame.HEADER)+
                "֡����"+FormatConverter.byteToHexStr(length)+
                "֡�ţ�"+FormatConverter.byteToHexStr(sequence)+
                "�豸��ַ��"+FormatConverter.byteToHexStr(deviceAddress)+
                "���ܺţ�"+FormatConverter.byteToHexStr(functionID)+
                "���ݣ�"+FormatConverter.byteArrayToHexStr(data)+
                "CRC��"+FormatConverter.byteArrayToHexStr(CRC)+
                "֡β��"+FormatConverter.byteArrayToHexStr(Frame.FOOTER));
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
