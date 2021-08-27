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
	
	
    static CommPortIdentifier portId; //�Դ��ڽ��й�������õ���
    static Enumeration<?> portList; // ��Ч�����ϵĶ˿ڵ�ö��
    InputStream inputStream; // �Ӵ�������������
    UploadFrameReceived uploadFrameReceived;
    static OutputStream outputStream;// �򴮿��������
    static SerialPort serialPort; //����RS-232����ͨ�Ŷ˿ڵĵײ�ӿڣ��������˴���ͨ���������С���ܼ�������ֱ�ӶԴ��ڽ��ж���д�����ù���
    //private BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>();
    //����������������ϴ�״̬����
    private BlockingQueue<Frame> uploadQueue;
    //����������������·������Win�еĶ������й���
    private BlockingQueue<Frame> downQueue;
    private BlockingQueue<Frame> downBufferQueue = new LinkedBlockingQueue<Frame>();
    //���������������ack����
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
     * ��дSerialPort EventListent �ķ���,���������˿����Ƿ���������
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
            case SerialPortEvent.DATA_AVAILABLE:// ���п�������ʱ��ȡ����
                byte[] readBuffer = new byte[64];
          
                try {
                    int numBytes = -1;
                    while (inputStream.available() > 0) {
                        numBytes = inputStream.read(readBuffer);
                       
                        System.out.printf("���տ��ư巢�����ֽ�����%d\n", numBytes);
                       
                       
                        if (numBytes == 44) {
                        	 readbuff=readBuffer;
                             FormatConverter FOR=new  FormatConverter();
                             String strn= FOR.byteArrayToString(44,readBuffer);
                             System.out.println("strn=  "+strn);
                             UploadFrameReceived upl= new UploadFrameReceived(44, readBuffer);

                             /*���õ�ǰ�¶�*/
                             System.out.println("getTemperature_gather  "+upl.getTemperature_gather());                                     
                        
                             double nowTe=upl.getTemperature_gather();
                             String strT = String.valueOf(nowTe);             
                             win.textField1.setText(strT);
                             
                             /*�����趨�¶�*/
                             
                             System.out.println("getTemperature_set  "+upl.getTemperature_set());                                     
                             double setTemp=upl.getTemperature_set();
                             
                             String strTgather = String.valueOf(setTemp);             
                             win.textField2.setText(strTgather);
                             /*���ù���״̬*/
                             System.out.println("getMachine_state  "+upl.getMachine_state());                      
                             if(upl.getMachine_state()==0)
                             	 stringMachinestate="ֹͣ";
                             else
                             	if(upl.getMachine_state()==1)
                             		stringMachinestate="Ԥ����";
                             	else
                                 	if(upl.getMachine_state()==2)
                                 		stringMachinestate="����";
                                 	else
                                 		stringMachinestate="����";                              		
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
     * ͨ�������COM2���ڣ����ü������Լ���صĲ���
     * @return ����true��ʾ�˿ڴ򿪳ɹ�������false��ʾ�˿ڴ�ʧ��
     */
    public Boolean startComPort() {
        // ͨ������ͨ�Ź������õ�ǰ�����ϵĴ����б�
        portList = CommPortIdentifier.getPortIdentifiers();
        while (portList.hasMoreElements()) {
            // ��ȡ��Ӧ���ڶ���
            portId = (CommPortIdentifier) portList.nextElement();
            // �ж϶˿������Ƿ�Ϊ����
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                // �ж����COM2���ڴ��ڣ��ʹ򿪸ô���
                if (portId.getName().equals("COM2")) {
                    try {
                        //�򿪲����ô�����ΪCOM_2��ͨ���ӳ�Ϊ2��
                        serialPort = (SerialPort) portId.open("COM_2", 2000);
                    } catch (PortInUseException e) {
                        e.printStackTrace();
                        return false;
                    }
                    try {
                        //�õ����ڵ���
                        inputStream = serialPort.getInputStream();
                        outputStream = serialPort.getOutputStream();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    }
                    try {
                        //��������������¼�
                        serialPort.addEventListener(this);
                    } catch (TooManyListenersException e) {
                        e.printStackTrace();
                        return false;
                    }
                    //���ü���Ϊ������ʱ֪ͨ
                    serialPort.notifyOnDataAvailable(true);
                    try {
                        // ���ô��ڲ�������Ϊ(������,����λ,ֹͣλ,��ż����)
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
                                    else{ System.out.printf("����%d֡����ʧ�ܣ�", downBuff.getFunctionID()); }
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
                                    else{ System.out.printf("����%d֡����ʧ�ܣ�", downBuff.getFunctionID()); }
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
         /*�����¶ȷ�Χ*/
        
         double nowTemp=upl.getTemperature_gather();
         double setTemp=upl.getTemperature_set();      
         int setP=upl.getBias();
         System.out.println("nowTemp=*/"+nowTemp);
         System.out.println("setTemp=*/"+setTemp);
         System.out.println("setP=*/"+setP);

         if(nowTemp-setTemp>setP)
         {
         	
         	win.getDownQueue().add(new BootFrame(win.sequence++,win.deviceAddress,FormatConverter.hexStringToBytes("01")[0]));
             System.out.println("����ѹ�������¶��½�*/");
         }
         else 
         	if(setTemp-nowTemp>setP)
         	{ 
         	 win.getDownQueue().add(new BootFrame(win.sequence++,win.deviceAddress,FormatConverter.hexStringToBytes("00")[0]));
         	System.out.println("�ر�ѹ�������¶�����");
         	}
         
                       
          /*���ó���״̬*/
          System.out.println("getLock_state="+upl.getLock_state());
          
          
          /*�����¶�����*/

         
         
          
        
          
  	
    }
    
    public static void main(String[] args) {
        Serial_comm COM = new Serial_comm();
       
        //��������
        Boolean success = COM.startComPort();
        COM.setUploadQueue(win.getUploadQueue());
        COM.setDownQueue(win.getDownQueue());
       // win.Top(). textField3.setText("stringMachinestate");
        
        //�����ɹ��ͼ���������Ϣ
        if (success) {
            COM.start();
        }
        else {
            System.out.println("����δ�����ɹ���");
        }
    }
}
