package com.interfaces;

/**
 * Description:��ȡ֡�еĸ����ֶ�
 */
public interface FrameExtract {
    public int getFrameLength();
    public int getFrameSequence();
    public int getDeviceAddress();
    public int getFunctionID();
    public byte[] getCRC();
}
