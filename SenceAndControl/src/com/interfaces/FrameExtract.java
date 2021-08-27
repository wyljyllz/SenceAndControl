package com.interfaces;

/**
 * Description:提取帧中的各个字段
 */
public interface FrameExtract {
    public int getFrameLength();
    public int getFrameSequence();
    public int getDeviceAddress();
    public int getFunctionID();
    public byte[] getCRC();
}
