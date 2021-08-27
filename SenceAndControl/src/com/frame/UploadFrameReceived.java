package com.frame;

import com.interfaces.DataExtract;
import com.utils.FormatConverter;

/**
 * Description:收到的上传数据帧，功能号为8，长度为44，包括控制板的各种信息，数据部分要进行解析处理
 */
public class UploadFrameReceived extends Frame implements DataExtract {
    private byte[] data;
    //终端控制板参数
    private byte[] deviceCode = new byte[5];/*设备编码*/
    //private byte reserved3 = (byte)0x00;
    private byte upload_interval;
    private byte boot_delay;
    //private byte[] reserved6 = {(byte)0x00, (byte)0x00};
    private byte temperature_set = (byte) 0x04;/*设置温度*/
    private byte bias;
    //private byte[] reserved9 = {(byte)0xFF, (byte)0xFF};
    //private byte[] reserved10 = {(byte)0xFF, (byte)0xFF};
    //private byte reserved11 = (byte)0x00;
    //终端控制板状态
    private byte system_state;
    //private byte reserved3 = (byte)0x00;
    private byte machine_state;
    private byte temperature_gather;/*采集温度*/
    //private byte[] reserved7 = {(byte)0xFF, (byte)0xFF};
    private byte lock_state1 ;/*抽屉*/
    private byte lock_state2;
    //private byte[] reserved9 = {(byte)0xFF, (byte)0xFF};

    public UploadFrameReceived(int sequence, byte[] data){
        super(44, 0, 0, Frame.UPLOAD_STATE);
        this.data = data;
        //终端控制板参数
        deviceCode = FormatConverter.slice(data, 6, 11);/*设备编码*/
        deviceAddress = data[11];/*设备地址*/
        upload_interval = data[13];/*上传时间间隔*/
        boot_delay = data[14];/*压缩机启动延时*/
        temperature_set = data[17];/*设定温度*/
        bias = data[18];/*温度偏差控制*/
        //终端控制板状态
        system_state = data[23];/*系统状态*/
        machine_state = data[31];/*压缩机状态*/
        temperature_gather = data[33];/*采集温度*/
  //      lock_state = FormatConverter.slice(data, 19,21);
        lock_state1=data[38];
        lock_state2=data[37];
        
    }

    public byte[] getDeviceCode() {
        return deviceCode;
    }

    public byte getUpload_interval() {
        return upload_interval;
    }

    public byte getBoot_delay() {
        return boot_delay;
    }

   
    public double getTemperature_set() {
    	String str=FormatConverter.byteToHexStr( temperature_set);
    	int i1= Integer.parseInt(String.valueOf(str.charAt(0)),16);
        int i2=Integer.parseInt(String.valueOf(str.charAt(1)),16);
        int ii1[];
         ii1=new int [8];
        for(int n=0;n<4;n++)
        {
     	   ii1[n]=i1%2;
            i1=i1/2;
        }
        for(int n=4;n<8;n++)
        {
     	   ii1[n]=i2%2;
            i2=i2/2;
        }
        double x=ii1[5]*1+ii1[6]*2+ii1[7]*4+ii1[0]*8+ii1[1]*16+ii1[2]*32;
        if(ii1[4]==1)
     	   x=x+0.5;
       
          if(ii1[3]==1)
         	 x=x*(-1);
        
     	return x;
    	
    	
    	
    }
    public double getTemperature_gather() {/*采集温度*/
    	
       String str=FormatConverter.byteToHexStr(temperature_gather);
       int i1= Integer.parseInt(String.valueOf(str.charAt(0)),16);
       int i2=Integer.parseInt(String.valueOf(str.charAt(1)),16);
       int ii1[];
      
        ii1=new int [8];
       for(int n=0;n<4;n++)
       {
    	   ii1[n]=i1%2;
           i1=i1/2;
       }
       for(int n=4;n<8;n++)
       {
    	   ii1[n]=i2%2;
           i2=i2/2;
       }
       double x=ii1[5]*1+ii1[6]*2+ii1[7]*4+ii1[0]*8+ii1[1]*16+ii1[2]*32;
       if(ii1[4]==1)
    	   x=x+0.5;
      
    	 
         if(ii1[3]==1)
        	x=x*(-1);
       
    	return x;
    }   
    public int getMachine_state() {/*机器状态*/
    	String str=FormatConverter.byteToHexStr(machine_state);
    	 int i=Integer.parseInt(str,16);
    	return i;
    }
    public String getLock_state() {/*设备锁*/
    	String str1=FormatConverter.byteToHexStr(lock_state1);
    	String str2=FormatConverter.byteToHexStr(lock_state2);
    	String str=str1+str2;
    	
        return str;
    }
    public int getBias() {
    	String str=FormatConverter.byteToHexStr(bias);
    	int a=Integer.parseInt(str,16);
    	  	
        return a;
    }

    public byte getSystem_state() {
        return system_state;
    }

   

   
  

    public boolean isCorrect(){
        return Frame.isCorrect(FormatConverter.hexStringToBytes(FormatConverter.byteToHexStr(length) +
                FormatConverter.byteToHexStr(sequence) + FormatConverter.byteToHexStr(deviceAddress) +
                FormatConverter.byteToHexStr(functionID) + FormatConverter.byteArrayToHexStr(data) +
                FormatConverter.byteArrayToHexStr(CRC)));
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
