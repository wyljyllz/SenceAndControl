package com.frame;

public class FrameFactory {
    public static Frame produceFrame(byte[] frame){
        byte functionID = frame[5];
        switch (functionID){
            case Frame.UPLOAD_STATE:
                return new UploadFrameReceived(frame[3], frame);
            default:
                return new ACK(frame);
        }
    }
}
