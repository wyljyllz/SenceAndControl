package com;


import java.awt.BorderLayout;
import java.io.*;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JFrame;

import org.jfree.chart.ChartPanel;

import com.chart.DataWindows;
import com.chart.chart;
import com.frame.BootFrame;
import com.frame.Frame;
import com.frame.FrameFactory;
import com.frame.UploadFrameReceived;
import com.utils.FormatConverter;
import com.window.Win;
import gnu.io.*;

public class Serial_comm extends Thread implements SerialPortEventListener {
	static String stringMachinestate;
	static Win win = new Win();
	static byte[] readbuff;
	
	
    static CommPortIdentifier portId; //对串口进行管理和设置的类
    static Enumeration<?> portList; // 有效连接上的端口的枚举
    InputStream inputStream; // 从串口来的输入流
    UploadFrameReceived uploadFrameReceived;
    static OutputStream outputStream;// 向串口输出的流
    static SerialPort serialPort; //描述RS-232串行通信端口的底层接口，它定义了串口通信所需的最小功能集。可以直接对串口进行读、写及设置工作
    //private BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>();
    //堵塞队列用来存放上传状态数据
    private BlockingQueue<Frame> uploadQueue;
    //堵塞队列用来存放下发命令，和Win中的堵塞队列共享
    private BlockingQueue<Frame> downQueue;
    private BlockingQueue<Frame> downBufferQueue = new LinkedBlockingQueue<Frame>();
    //堵塞队列用来存放ack数据
    private BlockingQueue<Frame> ackQueue = new LinkedBlockingQueue<Frame>();

    public void setUploadQueue(BlockingQueue<Frame> uploadQueue) {
        this.uploadQueue = uploadQueue;
    }

    public void setDownQueue(BlockingQueue<Frame> downQueue) {
        this.downQueue = downQueue;
    }

    public void setDownBufferQueue(BlockingQueue<Frame> downBufferQueue) {
        this.downBufferQueue = downBufferQueue;
    }

    @Override
    /**
     * 重写SerialPort EventListent 的方法,持续监听端口上是否有数据流
     */
    public void serialEvent(SerialPortEvent event) {
        switch (event.getEventType()) {
            case SerialPortEvent.BI:
            case SerialPortEvent.OE:
            case SerialPortEvent.FE:
            case SerialPortEvent.PE:
            case SerialPortEvent.CD:
            case SerialPortEvent.CTS:
            case SerialPortEvent.DSR:
            case SerialPortEvent.RI: 
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY: break;
            case SerialPortEvent.DATA_AVAILABLE:// 当有可用数据时读取数据
                byte[] readBuffer = new byte[64];
          
                try {
                    int numBytes = -1;
                    while (inputStream.available() > 0) {
                        numBytes = inputStream.read(readBuffer);
                       
                        System.out.printf("接收控制板发来的字节数：%d\n", numBytes);
                       
                       
                        if (numBytes == 44) {
                        	 readbuff=readBuffer;
                             FormatConverter FOR=new  FormatConverter();
                             String strn= FOR.byteArrayToString(44,readBuffer);
                             System.out.println("strn=  "+strn);
                             UploadFrameReceived upl= new UploadFrameReceived(44, readBuffer);

                             /*设置当前温度*/
                             System.out.println("getTemperature_gather  "+upl.getTemperature_gather());                                     
                        
                             double nowTe=upl.getTemperature_gather();
                             String strT = String.valueOf(nowTe);             
                             win.textField1.setText(strT);
                             
                             /*设置设定温度*/
                             
                             System.out.println("getTemperature_set  "+upl.getTemperature_set());                                     
                             double setTemp=upl.getTemperature_set();
                             
                             String strTgather = String.valueOf(setTemp);             
                             win.textField2.setText(strTgather);
                             /*设置工作状态*/
                             System.out.println("getMachine_state  "+upl.getMachine_state());                      
                             if(upl.getMachine_state()==0)
                             	 stringMachinestate="停止";
                             else
                             	if(upl.getMachine_state()==1)
                             		stringMachinestate="预启动";
                             	else
                                 	if(upl.getMachine_state()==2)
                                 		stringMachinestate="运行";
                                 	else
                                 		stringMachinestate="故障";                              		
                              System.out.println("stringMachinestate="+stringMachinestate);
                              win.textField3.setText(stringMachinestate);
                            
                              setTemper();
                              
                              
                        	
                        	
                        	byte byta=readBuffer[33];
                       System.out.println(readBuffer);
                       System.out.println(byta);
                       
                        	String stra=FormatConverter.byteToHexStr(byta);
                        	System.out.println(stra);
                       
                        	
                        	
                            //msgQueue.add(new Date()+":"+FormatConverter.byteArrayToString(numBytes,readBuffer));
                            //msgQueue.add(new Date()+":"+FormatConverter.byteArrayToHexStr(FormatConverter.slice(readBuffer, 0, numBytes)));
                            uploadQueue.add(FrameFactory.produceFrame(readBuffer));

                        }else {
                            ackQueue.add(FrameFactory.produceFrame(readBuffer));
                        }
                        if(numBytes != 0){readBuffer = new byte[64];}
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:break;
        }
    }
    /**
     * 通过程序打开COM2串口，设置监听器以及相关的参数
     * @return 返回true表示端口打开成功，返回false表示端口打开失败
     */
    public Boolean startComPort() {
        // 通过串口通信管理类获得当前连接上的串口列表
        portList = CommPortIdentifier.getPortIdentifiers();
        while (portList.hasMoreElements()) {
            // 获取相应串口对象
            portId = (CommPortIdentifier) portList.nextElement();
            // 判断端口类型是否为串口
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                // 判断如果COM2串口存在，就打开该串口
                if (portId.getName().equals("COM2")) {
                    try {
                        //打开并设置串口名为COM_2，通信延迟为2秒
                        serialPort = (SerialPort) portId.open("COM_2", 2000);
                    } catch (PortInUseException e) {
                        e.printStackTrace();
                        return false;
                    }
                    try {
                        //得到串口的流
                        inputStream = serialPort.getInputStream();
                        outputStream = serialPort.getOutputStream();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    }
                    try {
                        //给串口增添监听事件
                        serialPort.addEventListener(this);
                    } catch (TooManyListenersException e) {
                        e.printStackTrace();
                        return false;
                    }
                    //设置监听为有数据时通知
                    serialPort.notifyOnDataAvailable(true);
                    try {
                        // 设置串口参数依次为(波特率,数据位,停止位,奇偶检验)
                        serialPort.setSerialPortParams(38400,SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                    } catch (UnsupportedCommOperationException e) {
                        e.printStackTrace();
                        return false;
                    }
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        try {
            while (true) {
                if (downQueue.size() > 0) {
                    Frame command = downQueue.remove();
                    downBufferQueue.add(command);
                    outputStream.write(command.getBytes());
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            boolean isACK = false;
                            Frame downBuff = downBufferQueue.peek();
                            if(ackQueue.size() > 0){
                                Frame ack = ackQueue.remove();
                                if(ack.getFunctionID() == downBuff.getFunctionID()){
                                    isACK = true;
                                    downBufferQueue.remove();
                                }
                                else {
                                    try {
                                        outputStream.write(downBuff.getBytes());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }else{
                                try {
                                    downBuff = downBufferQueue.remove();
                                    outputStream.write(downBuff.getBytes());
                                    sleep(500);
                                    Frame ack = ackQueue.peek();
                                    if(ack.getFunctionID() == downBuff.getFunctionID()){ isACK = true; }
                                    else{ System.out.printf("功能%d帧发送失败！", downBuff.getFunctionID()); }
                                } catch (InterruptedException | IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            if(!isACK){
                                downBuff = downBufferQueue.remove();
                                try {
                                    outputStream.write(downBuff.getBytes());
                                    sleep(500);
                                    Frame ack = ackQueue.peek();
                                    if(ack.getFunctionID() == downBuff.getFunctionID()){ isACK = true; }
                                    else{ System.out.printf("功能%d帧发送失败！", downBuff.getFunctionID()); }
                                } catch (InterruptedException | IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    }, 1000);
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public static void setTemper() {
	
        UploadFrameReceived upl= new UploadFrameReceived(44, readbuff);
         /*控制温度范围*/
        
         double nowTemp=upl.getTemperature_gather();
         double setTemp=upl.getTemperature_set();      
         int setP=upl.getBias();
         System.out.println("nowTemp=*/"+nowTemp);
         System.out.println("setTemp=*/"+setTemp);
         System.out.println("setP=*/"+setP);

         if(nowTemp-setTemp>setP)
         {
         	
         	win.getDownQueue().add(new BootFrame(win.sequence++,win.deviceAddress,FormatConverter.hexStringToBytes("01")[0]));
             System.out.println("启动压缩机，温度下降*/");
         }
         else 
         	if(setTemp-nowTemp>setP)
         	{ 
         	 win.getDownQueue().add(new BootFrame(win.sequence++,win.deviceAddress,FormatConverter.hexStringToBytes("00")[0]));
         	System.out.println("关闭压缩机，温度上升");
         	}
         
                       
          /*设置抽屉状态*/
          System.out.println("getLock_state="+upl.getLock_state());
          
          
          /*设置温度曲线*/

         
         
          
        
          
  	
    }
    
    public static void main(String[] args) {
        Serial_comm COM = new Serial_comm();
       
        //启动串口
        Boolean success = COM.startComPort();
        COM.setUploadQueue(win.getUploadQueue());
        COM.setDownQueue(win.getDownQueue());
       // win.Top(). textField3.setText("stringMachinestate");
        
        //启动成功就监听串口消息
        if (success) {
            COM.start();
        }
        else {
            System.out.println("串口未启动成功！");
        }
    }
}
